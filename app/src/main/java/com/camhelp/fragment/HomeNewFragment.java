package com.camhelp.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.CircularArray;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.camhelp.R;
import com.camhelp.adapter.EndLessOnScrollListener;
import com.camhelp.adapter.HomeNewAndFocusAdapter;
import com.camhelp.entity.CommonProperty;
import com.camhelp.utils.FullyLinearLayoutManager;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeNewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeNewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeNewFragment extends Fragment {

    private SwipeRefreshLayout srl_home_new;
    private RecyclerView recycler_home_new;
    private LinearLayout ll_nodata;

    private LinearLayoutManager mLinearLayoutManager;
    private FullyLinearLayoutManager fullyLinearLayoutManager;
    private HomeNewAndFocusAdapter homeNewAndFocusAdapter;
    private List<CommonProperty> commonPropertyList;

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
        initdata();
        initview();
        Toast.makeText(getActivity(), "" + commonPropertyList.size(), Toast.LENGTH_SHORT).show();
    }

    public void initview() {
        ll_nodata = (LinearLayout) getActivity().findViewById(R.id.ll_noanydata);
        tv_loading = (TextView) getActivity().findViewById(R.id.tv_loading);
        recycler_home_new = (RecyclerView) getActivity().findViewById(R.id.recycler_home_new);
        if (commonPropertyList.size() == 0) {
            ll_nodata.setVisibility(View.VISIBLE);
        }

        srl_home_new = (SwipeRefreshLayout) getActivity().findViewById(R.id.srl_home_new);
        srl_home_new.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FIRST = true;
                onResume();
            }
        });
        //这个是下拉刷新出现的那个圈圈要显示的颜色
        srl_home_new.setColorSchemeResources(
                R.color.red,
                R.color.yellow,
                R.color.green
        );

        /**滑动到底部自动加载更多*/
//        onScrollListener = new EndLessOnScrollListener(mLinearLayoutManager) {
//            @Override
//            public boolean onLoadMore(int currentPage) {
//                final Boolean[] result = {false};
//                    loadmoredata();
//                    onResume();
//                return result[0];
//            }
//        };
//        recycler_home_new.addOnScrollListener(onScrollListener);

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
    }

    /**
     * 加载最新数据
     */
    public void initdata() {
        commonPropertyList = DataSupport.findAll(CommonProperty.class);
    }

    /**
     * 加载更多
     */
    public void loadmoredata() {
        int total = commonPropertyList.size();
        for (int i = 0;i < total; i++) {
            commonPropertyList.add(commonPropertyList.get(i));
        }
        homeNewAndFocusAdapter.notifyDataSetChanged();
        srl_home_new.setRefreshing(false);
        homeNewAndFocusAdapter.notifyItemRemoved(homeNewAndFocusAdapter.getItemCount());
        tv_loading.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (FIRST) {
            srl_home_new.setRefreshing(true);
            commonPropertyList.clear();
            initdata();
            fullyLinearLayoutManager = new FullyLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recycler_home_new.setLayoutManager(fullyLinearLayoutManager);
            recycler_home_new.setNestedScrollingEnabled(false);
            homeNewAndFocusAdapter = new HomeNewAndFocusAdapter(commonPropertyList, getActivity());
            recycler_home_new.setAdapter(homeNewAndFocusAdapter);
            srl_home_new.setRefreshing(false);
            FIRST = false;
        } else {
            homeNewAndFocusAdapter.notifyDataSetChanged();
            srl_home_new.setRefreshing(false);
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
