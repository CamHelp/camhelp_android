package com.camhelp.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.camhelp.R;
import com.camhelp.activity.PublishCommonPropertyActivity;
import com.camhelp.activity.PublishExperienceActivity;
import com.camhelp.activity.PublishElectricityActivity;
import com.camhelp.activity.PublishLoseActivity;
import com.camhelp.activity.PublishPickupActivity;
import com.camhelp.activity.PublishProblemActivity;
import com.camhelp.activity.PublishRepairActivity;
import com.camhelp.common.CommonGlobal;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PublishFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PublishFragment#newInstance} factory method to
 * create an instance of this fragment.
 * <p>
 * 发布fragment
 */
public class PublishFragment extends Fragment implements View.OnClickListener {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String colorPrimary,colorPrimaryBlew,colorPrimaryDark,colorAccent;

    private LinearLayout ll_base;
    private LinearLayout ll_publish_01, ll_publish_02, ll_publish_03,
            ll_publish_04, ll_publish_05, ll_publish_06;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PublishFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PublishFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PublishFragment newInstance(String param1, String param2) {
        PublishFragment fragment = new PublishFragment();
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
        return inflater.inflate(R.layout.fragment_publish, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        initcolor();
        initview();
    }


    /*获取主题色*/
    public void initcolor(){
        String defaultColorPrimary = "#"+Integer.toHexString(getResources().getColor(R.color.colorPrimary));
        String defaultColorPrimaryBlew = "#"+Integer.toHexString(getResources().getColor(R.color.colorPrimaryBlew));
        String defaultColorPrimaryDark = "#"+Integer.toHexString(getResources().getColor(R.color.colorPrimaryDark));
        String defaultColorAccent = "#"+Integer.toHexString(getResources().getColor(R.color.colorAccent));

        colorPrimary = pref.getString(CommonGlobal.colorPrimary,defaultColorPrimary);
        colorPrimaryBlew = pref.getString(CommonGlobal.colorPrimaryBlew,defaultColorPrimaryBlew);
        colorPrimaryDark = pref.getString(CommonGlobal.colorPrimaryDark,defaultColorPrimaryDark);
        colorAccent = pref.getString(CommonGlobal.colorAccent,defaultColorAccent);
    }

    public void initview() {
        ll_base = (LinearLayout) getActivity().findViewById(R.id.ll_base_publish);
        ll_base.setBackgroundColor(Color.parseColor(colorPrimary));

        ll_publish_01 = (LinearLayout) getActivity().findViewById(R.id.ll_publish_01);
        ll_publish_02 = (LinearLayout) getActivity().findViewById(R.id.ll_publish_02);
        ll_publish_03 = (LinearLayout) getActivity().findViewById(R.id.ll_publish_03);
        ll_publish_04 = (LinearLayout) getActivity().findViewById(R.id.ll_publish_04);
        ll_publish_05 = (LinearLayout) getActivity().findViewById(R.id.ll_publish_05);
        ll_publish_06 = (LinearLayout) getActivity().findViewById(R.id.ll_publish_06);

        ll_publish_01.setOnClickListener(this);
        ll_publish_02.setOnClickListener(this);
        ll_publish_03.setOnClickListener(this);
        ll_publish_04.setOnClickListener(this);
        ll_publish_05.setOnClickListener(this);
        ll_publish_06.setOnClickListener(this);

        ll_publish_06.setVisibility(View.INVISIBLE);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_publish_01:
                Intent intentPublish1 = new Intent(getActivity(), PublishCommonPropertyActivity.class);
                intentPublish1.putExtra(CommonGlobal.categoryType,CommonGlobal.categoryType_experience);
                startActivity(intentPublish1);
                break;
            case R.id.ll_publish_02:
                Intent intentPublish2 = new Intent(getActivity(), PublishCommonPropertyActivity.class);
                intentPublish2.putExtra(CommonGlobal.categoryType,CommonGlobal.categoryType_problem);
                startActivity(intentPublish2);
                break;
            case R.id.ll_publish_03:
                Intent intentPublish3 = new Intent(getActivity(), PublishCommonPropertyActivity.class);
                intentPublish3.putExtra(CommonGlobal.categoryType,CommonGlobal.categoryType_pickup);
                startActivity(intentPublish3);
                break;
            case R.id.ll_publish_04:
                Intent intentPublish4 = new Intent(getActivity(), PublishCommonPropertyActivity.class);
                intentPublish4.putExtra(CommonGlobal.categoryType,CommonGlobal.categoryType_lose);
                startActivity(intentPublish4);
                break;
            case R.id.ll_publish_05:
                Intent intentPublish5 = new Intent(getActivity(), PublishCommonPropertyActivity.class);
                intentPublish5.putExtra(CommonGlobal.categoryType,CommonGlobal.categoryType_fresh);
                startActivity(intentPublish5);
                break;
            case R.id.ll_publish_06:
                Intent publishElectricity = new Intent(getActivity(), PublishElectricityActivity.class);
                startActivity(publishElectricity);
                break;
        }
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
        initview();
    }
}
