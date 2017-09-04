package com.camhelp.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.camhelp.R;
import com.camhelp.activity.LoginActivity;
import com.camhelp.activity.MainActivity;
import com.camhelp.activity.WelcomeActivity;
import com.camhelp.adapter.EndLessOnScrollListener;
import com.camhelp.adapter.HomeNewAndFocusAdapter;
import com.camhelp.common.CommonGlobal;
import com.camhelp.common.CommonUrls;
import com.camhelp.entity.CommonPropertyVO;
import com.camhelp.utils.FullyLinearLayoutManager;
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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.camhelp.R.id.fl_home_top;

/**
 * Created by storm on 2017-08-12.
 * 首页fragment，只有最新列表
 */

public class HomeOnlyNewFragment extends Fragment {
    private String TAG = "HomeOnlyNewFragment";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String colorPrimary, colorPrimaryBlew, colorPrimaryDark, colorAccent;

    private SwipeRefreshLayout srl_home_new;
    private RecyclerView recycler_home_new;
    private LinearLayout ll_nodata;
    private FrameLayout fl_base_home_onlynew;


    private LinearLayoutManager mLinearLayoutManager;
    //    private FullyLinearLayoutManager fullyLinearLayoutManager;
    private HomeNewAndFocusAdapter homeNewAndFocusAdapter;
    //    private List<CommonProperty> commonPropertyList;
    private List<CommonPropertyVO> commonPropertyVOList = new ArrayList<CommonPropertyVO>();

    private Handler handler = new Handler();
    boolean isLoading;

    private FrameLayout fl_home_top;
    private TextView tv_loading;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private HomeOnlyNewFragment.OnFragmentInteractionListener mListener;

    Dialog dialogProcess;
    boolean firstComming = true;

    public HomeOnlyNewFragment() {
    }

    public static HomeOnlyNewFragment newInstance(String param1, String param2) {
        HomeOnlyNewFragment fragment = new HomeOnlyNewFragment();
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
        return inflater.inflate(R.layout.fragment_home_only_new, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        initcolor();
        initview();
//        initolddata();
        initnewdata();

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        recycler_home_new.setLayoutManager(mLinearLayoutManager);
        homeNewAndFocusAdapter = new HomeNewAndFocusAdapter(commonPropertyVOList, getActivity());
        recycler_home_new.setAdapter(homeNewAndFocusAdapter);
        srl_home_new.setRefreshing(false);
    }


    /*获取主题色*/
    public void initcolor() {
        String defaultColorPrimary = "#" + Integer.toHexString(getResources().getColor(R.color.colorPrimary));
        String defaultColorPrimaryBlew = "#" + Integer.toHexString(getResources().getColor(R.color.colorPrimaryBlew));
        String defaultColorPrimaryDark = "#" + Integer.toHexString(getResources().getColor(R.color.colorPrimaryDark));
        String defaultColorAccent = "#" + Integer.toHexString(getResources().getColor(R.color.colorAccent));

        colorPrimary = pref.getString(CommonGlobal.colorPrimary, defaultColorPrimary);
        colorPrimaryBlew = pref.getString(CommonGlobal.colorPrimaryBlew, defaultColorPrimaryBlew);
        colorPrimaryDark = pref.getString(CommonGlobal.colorPrimaryDark, defaultColorPrimaryDark);
        colorAccent = pref.getString(CommonGlobal.colorAccent, defaultColorAccent);
    }

    public void initview() {
        fl_home_top = (FrameLayout) getActivity().findViewById(R.id.fl_home_top);
        fl_base_home_onlynew = (FrameLayout) getActivity().findViewById(R.id.fl_base_home_onlynew);
        fl_home_top.setBackgroundColor(Color.parseColor(colorPrimary));
        fl_base_home_onlynew.setBackgroundColor(Color.parseColor(colorPrimary));

        ll_nodata = (LinearLayout) getActivity().findViewById(R.id.ll_noanydata);
        tv_loading = (TextView) getActivity().findViewById(R.id.tv_loading_onlynew);
        recycler_home_new = (RecyclerView) getActivity().findViewById(R.id.recycler_home_new);
        if (commonPropertyVOList.size() == 0) {
            ll_nodata.setVisibility(View.VISIBLE);
        }

        srl_home_new = (SwipeRefreshLayout) getActivity().findViewById(R.id.srl_home_new);
        srl_home_new.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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
                int lastVisiableItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
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
        if (recycler_home_new.getScrollState() == RecyclerView.SCROLL_STATE_IDLE ||
                !recycler_home_new.isComputingLayout()) {
            homeNewAndFocusAdapter.notifyDataSetChanged();
        }
        srl_home_new.setRefreshing(false);
        tv_loading.setVisibility(View.GONE);
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
                        initolddata();//把上一次的数据加载出来
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

                    Collections.reverse(commonPropertyVOList); // 倒序排列,按时间顺序

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (commonPropertyVOList.size() > 0) {
                                ll_nodata.setVisibility(View.GONE);
                                saveLocal();//把最新的保存本地
                            }
                            dialogProcess.dismiss();
                            mLinearLayoutManager = new LinearLayoutManager(getActivity());
                            recycler_home_new.setLayoutManager(mLinearLayoutManager);
                            homeNewAndFocusAdapter = new HomeNewAndFocusAdapter(commonPropertyVOList, getActivity());
                            recycler_home_new.setAdapter(homeNewAndFocusAdapter);
                            srl_home_new.setRefreshing(false);
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
}
