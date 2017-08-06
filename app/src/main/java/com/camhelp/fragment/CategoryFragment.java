package com.camhelp.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.camhelp.R;
import com.camhelp.common.CommonGlobal;

import java.util.ArrayList;
import java.util.List;

/**
 * 分类fragment
 */
public class CategoryFragment extends Fragment implements View.OnClickListener {
    CategoryClubFragment tab01;
    CategoryProblemFragment tab02;
    CategoryLoseFragment tab03;
    CategoryPickFragment tab04;

    private LinearLayout ll_category_toptab;
    public ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mDatas;

    private TextView tv_01, tv_02, tv_03, tv_04;
    private LinearLayout ll_tab1, ll_tab2, ll_tab3, ll_tab4;//顶部四个选项栏

    private ImageView mTabline;
    private int mScreen1_3;

    private int mCurrentPageIndex;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
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
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initTabLine();
        initView();
    }

    private void initTabLine() {
        mTabline = (ImageView) getActivity().findViewById(R.id.id_iv_tabline);
        mTabline.setBackgroundColor(Color.parseColor(CommonGlobal.MYCOLOR_ACCENT));
        Display display = getActivity().getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        mScreen1_3 = outMetrics.widthPixels / 4;
        ViewGroup.LayoutParams lp = mTabline.getLayoutParams();
        lp.width = mScreen1_3;
        mTabline.setLayoutParams(lp);
    }

    private void initView() {
        ll_category_toptab = (LinearLayout) getActivity().findViewById(R.id.ll_category_toptab);
        ll_category_toptab.setBackgroundColor(Color.parseColor(CommonGlobal.MYCOLOR_PRIMARY_BLEW));

        mViewPager = (ViewPager) getActivity().findViewById(R.id.viewpager_category);
        tv_01 = (TextView) getActivity().findViewById(R.id.id_tv_club);
        tv_02 = (TextView) getActivity().findViewById(R.id.id_tv_problem);
        tv_03 = (TextView) getActivity().findViewById(R.id.id_tv_lose);
        tv_04 = (TextView) getActivity().findViewById(R.id.id_tv_pickup);

        ll_tab1 = (LinearLayout) getActivity().findViewById(R.id.ll_tab1);
        ll_tab2 = (LinearLayout) getActivity().findViewById(R.id.ll_tab2);
        ll_tab3 = (LinearLayout) getActivity().findViewById(R.id.ll_tab3);
        ll_tab4 = (LinearLayout) getActivity().findViewById(R.id.ll_tab4);

        ll_tab1.setOnClickListener(this);
        ll_tab2.setOnClickListener(this);
        ll_tab3.setOnClickListener(this);
        ll_tab4.setOnClickListener(this);

        mDatas = new ArrayList<Fragment>();

        tab01 = new CategoryClubFragment();
        tab02 = new CategoryProblemFragment();
        tab03 = new CategoryLoseFragment();
        tab04 = new CategoryPickFragment();

        mDatas.add(tab01);
        mDatas.add(tab02);
        mDatas.add(tab03);
        mDatas.add(tab04);

        /**
         * 解决viewpager滑动数据未保存问题
         * blog地址：http://blog.csdn.net/w372426096/article/details/49951317
         * */
        mAdapter = new FragmentPagerAdapter(this.getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = null;
                fragment = mDatas.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("id", "" + position);
                fragment.setArguments(bundle);
                return fragment;
            }

            @Override
            public int getCount() {
                return mDatas.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                Fragment fragment = (Fragment)super.instantiateItem(container, position);
                getActivity().getSupportFragmentManager().beginTransaction().show(fragment).commit();
                return fragment;
            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                Fragment fragment = mDatas.get(position);
                getActivity().getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            }
        };

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                resetTextView();
                switch (position) {
                    case 0:
                        tv_01.setTextColor(Color.parseColor("#00FF00"));
                        break;
                    case 1:
                        tv_02.setTextColor(Color.parseColor("#00FF00"));
                        break;
                    case 2:
                        tv_03.setTextColor(Color.parseColor("#00FF00"));
                        break;
                    case 3:
                        tv_04.setTextColor(Color.parseColor("#00FF00"));
                        break;
                }
                mCurrentPageIndex = position;
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPx) {
                Log.e("TAG", position + " , " + positionOffset + " , "
                        + positionOffsetPx);

                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabline
                        .getLayoutParams();

                if (mCurrentPageIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = (int) (positionOffset * mScreen1_3 + mCurrentPageIndex
                            * mScreen1_3);
                } else if (mCurrentPageIndex == 1 && position == 0)// 1->0
                {
                    lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_3 + (positionOffset - 1)
                            * mScreen1_3);
                } else if (mCurrentPageIndex == 1 && position == 1) // 1->2
                {
                    lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_3 + positionOffset
                            * mScreen1_3);
                } else if (mCurrentPageIndex == 2 && position == 1) // 2->1
                {
                    lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_3 + (positionOffset - 1)
                            * mScreen1_3);
                }else if (mCurrentPageIndex == 2 && position == 2) // 2->3
                {
                    lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_3 + positionOffset
                            * mScreen1_3);
                } else if (mCurrentPageIndex == 3 && position == 2) // 3->2
                {
                    lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_3 + (positionOffset - 1)
                            * mScreen1_3);
                }else if (mCurrentPageIndex == 3 && position == 3) // 3->3
                {
                    lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_3 + positionOffset
                            * mScreen1_3);
                }
                mTabline.setLayoutParams(lp);

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

    }

    protected void resetTextView() {
        tv_01.setTextColor(Color.WHITE);
        tv_02.setTextColor(Color.WHITE);
        tv_03.setTextColor(Color.WHITE);
        tv_04.setTextColor(Color.WHITE);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_tab1:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.ll_tab2:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.ll_tab3:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.ll_tab4:
                mViewPager.setCurrentItem(3);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        tab01.onActivityResult(requestCode, resultCode, data);
        tab02.onActivityResult(requestCode, resultCode, data);
        tab03.onActivityResult(requestCode, resultCode, data);
        tab04.onActivityResult(requestCode, resultCode, data);
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
    }
}
