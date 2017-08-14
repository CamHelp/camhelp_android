package com.camhelp.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.CircularArray;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.camhelp.R;
import com.camhelp.activity.LoginActivity;
import com.camhelp.activity.MainActivity;
import com.camhelp.activity.RegisterActivity;
import com.camhelp.activity.RegisterPerfectActivity;
import com.camhelp.adapter.EndLessOnScrollListener;
import com.camhelp.adapter.HomeNewAndFocusAdapter;
import com.camhelp.common.CommonGlobal;
import com.camhelp.common.CommonUrls;
import com.camhelp.entity.CommonProperty;
import com.camhelp.entity.CommonPropertyVO;
import com.camhelp.utils.FullyLinearLayoutManager;
import com.camhelp.utils.GsonUtil;
import com.camhelp.utils.L;
import com.camhelp.utils.MyProcessDialog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 首页最新数据展示
 * 每次更新保存最新数据到本地sqlite数据库，下次进入首先加载本地数据再加载最新数据，
 * 如果加载最新数据失败，不至于空白显示
 */
public class HomeNewFragment extends Fragment {

    private SwipeRefreshLayout srl_home_new;
    private RecyclerView recycler_home_new;
    private LinearLayout ll_nodata;
    LinearLayout ll_base_home_new;

    //    private LinearLayoutManager mLinearLayoutManager;
    private FullyLinearLayoutManager fullyLinearLayoutManager;
    private HomeNewAndFocusAdapter homeNewAndFocusAdapter;
    //    private List<CommonProperty> commonPropertyList;
    private List<CommonPropertyVO> commonPropertyVOList = new ArrayList<CommonPropertyVO>();

    EndLessOnScrollListener onScrollListener;

    private Handler handler = new Handler();
    private boolean FIRST = true;
    boolean isLoading;
    boolean isUp = true;

    private TextView tv_loading;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    boolean firstComming = true;
    boolean firstOnresume = true;
    Dialog dialogProcess;
    private int positionSelected = 0;

    public HomeNewFragment() {
    }

    public static HomeNewFragment newInstance(String param1, String param2) {
        HomeNewFragment fragment = new HomeNewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_new, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initview();
        initolddata();
        initnewdata();

//        fullyLinearLayoutManager = new FullyLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        recycler_home_new.setLayoutManager(fullyLinearLayoutManager);
//        recycler_home_new.setNestedScrollingEnabled(false);
//        homeNewAndFocusAdapter = new HomeNewAndFocusAdapter(commonPropertyList, getActivity());
//        recycler_home_new.setAdapter(homeNewAndFocusAdapter);

    }

    public void initview() {
        ll_base_home_new = (LinearLayout) getActivity().findViewById(R.id.ll_base_home_new);

        ll_nodata = (LinearLayout) getActivity().findViewById(R.id.ll_noanydata);
        tv_loading = (TextView) getActivity().findViewById(R.id.tv_loading);
        recycler_home_new = (RecyclerView) getActivity().findViewById(R.id.recycler_home_new);
        if (commonPropertyVOList.size() == 0) {
            ll_nodata.setVisibility(View.VISIBLE);
        }

        srl_home_new = (SwipeRefreshLayout) getActivity().findViewById(R.id.srl_home_new);
        srl_home_new.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                CommonGlobal.homenewfragmentfirst = true;
                srl_home_new.setRefreshing(true);
                okhttpHomeNew();
            }
        });
        //这个是下拉刷新出现的那个圈圈要显示的颜色
        srl_home_new.setColorSchemeResources(
                R.color.red,
                R.color.yellow,
                R.color.green
        );

        /**滑动到底部自动加载更多*/
        recycler_home_new.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastVisiableItemPosition = fullyLinearLayoutManager.findLastVisibleItemPosition();
                if (lastVisiableItemPosition + 1 == homeNewAndFocusAdapter.getItemCount()) {
                    boolean isRefreshing = srl_home_new.isRefreshing();
                    if (isRefreshing) {
                        homeNewAndFocusAdapter.notifyItemRemoved(homeNewAndFocusAdapter.getItemCount());
                        return;
                    }
                    if (!isLoading) {
                        isLoading = true;
                        tv_loading.setVisibility(View.VISIBLE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadmoredata();
                                isLoading = false;
                            }
                        }, 2000);
                    }
                }
            }
        });

        dialogProcess = MyProcessDialog.showDialog(getActivity());
        if (firstComming) {
            dialogProcess.show();
            firstComming = false;
        }
    }

    /**
     * 加载最新数据
     */
    public void initnewdata() {
        srl_home_new.setRefreshing(true);
        okhttpHomeNew();
    }

    /**
     * 加载上一次数据
     */
    public void initolddata() {
        commonPropertyVOList = DataSupport.findAll(CommonPropertyVO.class);
    }

    /**
     * 加载更多
     */
    public void loadmoredata() {
        int total = commonPropertyVOList.size();
        for (int i = 0; i < total; i++) {
            commonPropertyVOList.add(commonPropertyVOList.get(i));
        }
        homeNewAndFocusAdapter.notifyDataSetChanged();
        srl_home_new.setRefreshing(false);
//        homeNewAndFocusAdapter.notifyItemRemoved(homeNewAndFocusAdapter.getItemCount());
        tv_loading.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        fullyLinearLayoutManager = new FullyLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycler_home_new.setLayoutManager(fullyLinearLayoutManager);
        recycler_home_new.setNestedScrollingEnabled(false);
        homeNewAndFocusAdapter = new HomeNewAndFocusAdapter(commonPropertyVOList, getActivity());
        recycler_home_new.setAdapter(homeNewAndFocusAdapter);
        srl_home_new.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
//        fullyLinearLayoutManager = new FullyLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        recycler_home_new.setLayoutManager(fullyLinearLayoutManager);
//        recycler_home_new.setNestedScrollingEnabled(false);
//        homeNewAndFocusAdapter = new HomeNewAndFocusAdapter(commonPropertyVOList, getActivity());
//        recycler_home_new.setAdapter(homeNewAndFocusAdapter);
//        srl_home_new.setRefreshing(false);
    }

    /**
     * 请求服务器数据
     */
    private void okhttpHomeNew() {
        if (firstComming) {
            dialogProcess.show();
            firstComming = false;
        }

        final String url = CommonUrls.SERVER_COMMONLIST_ALL;
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3000, TimeUnit.MILLISECONDS).build();

        FormBody body = new FormBody.Builder().build();
        Request request = new Request.Builder().url(url).get().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TAG", "onFailure" + e.toString());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "无法连接到服务器", Toast.LENGTH_SHORT).show();
                        srl_home_new.setRefreshing(false);
                        dialogProcess.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                Log.d("TAG" + "onresponse result:", result);

                Gson gson = new Gson();
                //  获得 解析者
                JsonParser parser = new JsonParser();
                //  获得 根节点元素
                JsonElement root = parser.parse(result);
                //  根据 文档判断根节点属于 什么类型的 Gson节点对象
                // 假如文档 显示 根节点 为对象类型
                // 获得 根节点 的实际 节点类型
                JsonObject element = root.getAsJsonObject();
                //  取得 节点 下 的某个节点的 value
                // 获得 name 节点的值，name 节点为基本数据节点
                JsonPrimitive codeJson = element.getAsJsonPrimitive("code");
                int code = codeJson.getAsInt();
                JsonPrimitive msgJson = element.getAsJsonPrimitive("msg");
                final String msg = msgJson.getAsString();

                if (code == 0) {
                    commonPropertyVOList.clear();//得到新数据把旧数据清空
                    final JsonArray dataJson = element.getAsJsonArray("data");
//                    commonPropertyVOList = GsonUtil.parseJsonArrayWithGson(dataJson.toString(), CommonPropertyVO.class);
                    commonPropertyVOList = gson.fromJson(dataJson, new TypeToken<List<CommonPropertyVO>>() {
                    }.getType());

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (commonPropertyVOList.size() > 0) {
                                ll_nodata.setVisibility(View.GONE);
                                saveLocal();//把最新的保存本地
                            }
//                            onResume();
                            onStart();
                            dialogProcess.dismiss();
                        }
                    });
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                            dialogProcess.dismiss();
                        }
                    });
                }
            }
        });
    }

    /*把最新的数据保存本地*/
    private void saveLocal(){
        DataSupport.deleteAll(CommonPropertyVO.class);
        for (int i= 0;i<commonPropertyVOList.size();i++){
            commonPropertyVOList.get(i).save();
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * RecyclerView 移动到当前位置，
     *
     * @param manager   设置RecyclerView对应的manager
     * @param mRecyclerView  当前的RecyclerView
     * @param n  要跳转的位置
     */
    public static void MoveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int n) {


        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(n);
        }

    }
}
