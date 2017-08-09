package com.camhelp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.camhelp.R;
import com.camhelp.adapter.EndLessOnScrollListener;
import com.camhelp.adapter.HomeNewAndFocusAdapter;
import com.camhelp.adapter.MinePublishedAdapter;
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
    private HomeNewAndFocusAdapter homeNewAndFocusAdapter;
    private List<CommonProperty> commonPropertyList;

    EndLessOnScrollListener onScrollListener;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeNewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeNewFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_new, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initdata();
        initview();
        Toast.makeText(getActivity(), ""+commonPropertyList.size(), Toast.LENGTH_SHORT).show();

//        recycler_home_new.setLayoutManager(new FullyLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true));
//        recycler_home_new.setNestedScrollingEnabled(false);
//        homeNewAndFocusAdapter = new HomeNewAndFocusAdapter(commonPropertyList, getActivity());
//        recycler_home_new.setAdapter(homeNewAndFocusAdapter);
    }

    public void initview(){
        ll_nodata = (LinearLayout) getActivity().findViewById(R.id.ll_noanydata);
        recycler_home_new = (RecyclerView) getActivity().findViewById(R.id.recycler_home_new);
        if (commonPropertyList.size()==0){
            ll_nodata.setVisibility(View.VISIBLE);
        }

        srl_home_new = (SwipeRefreshLayout) getActivity().findViewById(R.id.srl_home_new);
        srl_home_new.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onResume();
            }
        });
        //这个是下拉刷新出现的那个圈圈要显示的颜色
        srl_home_new.setColorSchemeResources(
                R.color.red,
                R.color.yellow,
                R.color.green
        );
    }
    public void initdata(){
        commonPropertyList = DataSupport.findAll(CommonProperty.class);
    }

    public void loadmoredata(){
        int total = commonPropertyList.size();
        for (int i =0;i<total;i++){
            commonPropertyList.add(commonPropertyList.get(i));
        }
    }
    // TODO: Rename method, update argument and hook method into UI event
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

    @Override
    public void onResume() {
        super.onResume();
        if (srl_home_new != null) {
            srl_home_new.setRefreshing(true);
            commonPropertyList.clear();
            initdata();
//            homeNewAndFocusAdapter.notifyDataSetChanged();
            recycler_home_new.setLayoutManager(new FullyLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            recycler_home_new.setNestedScrollingEnabled(false);
            homeNewAndFocusAdapter = new HomeNewAndFocusAdapter(commonPropertyList, getActivity());
            recycler_home_new.setAdapter(homeNewAndFocusAdapter);
            srl_home_new.setRefreshing(false);
        }
    }
}
