package com.camhelp.fragment;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.camhelp.R;
import com.camhelp.activity.LoginActivity;
import com.camhelp.activity.MainActivity;
import com.camhelp.activity.MineCenterActivity;
import com.camhelp.activity.MineCollectionActivity;
import com.camhelp.activity.MineFocusActivity;
import com.camhelp.activity.MinePublishedActivity;
import com.camhelp.activity.SetupActivity;
import com.camhelp.common.CommonGlobal;
import com.camhelp.entity.User;
import com.camhelp.utils.L;
import com.camhelp.utils.SharePrefUser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MineFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MineFragment#newInstance} factory method to
 * create an instance of this fragment.
 * <p>
 * 个人主页fragment
 */
public class MineFragment extends Fragment implements View.OnClickListener {
    private String TAG = "MineFragment";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String colorPrimary,colorPrimaryBlew,colorPrimaryDark,colorAccent;
    User mUser = new User();

    private LinearLayout ll_base;

    TextView tv_username, tv_intro;
    LinearLayout ll_my_published,ll_my_collection,ll_my_focus;
    LinearLayout ll_setup;
    LinearLayout ll_personal;
    private CircleImageView mine_cimg_avatar;

    private Dialog choosedialog = null;//确认框
    private int EXITORLOGOUT = -1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MineFragment newInstance(String param1, String param2) {
        MineFragment fragment = new MineFragment();
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
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userInit();
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
        mine_cimg_avatar = (CircleImageView) getActivity().findViewById(R.id.mine_cimg_avatar);
        mine_cimg_avatar.setOnClickListener(this);
        Glide.with(this).load(mUser.getAvatar())
                .error(R.drawable.avatar)
                .placeholder(R.drawable.avatar)
                .into(mine_cimg_avatar);

        ll_base = (LinearLayout) getActivity().findViewById(R.id.ll_base);
        ll_base.setBackgroundColor(Color.parseColor(colorPrimary));

        tv_username = (TextView) getActivity().findViewById(R.id.tv_username);
        tv_intro = (TextView) getActivity().findViewById(R.id.tv_intro);
//        tv_username.setText(mUser.getNickname());
//        tv_intro.setText(mUser.getIntro());

        ll_personal = (LinearLayout) getActivity().findViewById(R.id.ll_personal);
        ll_setup = (LinearLayout) getActivity().findViewById(R.id.ll_setup);
        ll_my_published = (LinearLayout) getActivity().findViewById(R.id.ll_my_published);
        ll_my_collection = (LinearLayout) getActivity().findViewById(R.id.ll_my_collection);
        ll_my_focus = (LinearLayout) getActivity().findViewById(R.id.ll_my_focus);

        ll_personal.setOnClickListener(this);
        ll_setup.setOnClickListener(this);
        ll_my_published.setOnClickListener(this);
        ll_my_collection.setOnClickListener(this);
        ll_my_focus.setOnClickListener(this);
    }

    /**
     * 获取用户
     */
    public void userInit() {
        mUser = getUser();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mine_cimg_avatar:
                Intent minecenter = new Intent(getActivity(), MineCenterActivity.class);
                startActivity(minecenter);
                break;
            case R.id.ll_personal://个人信息
                break;
            case R.id.ll_my_published://我的发布
                Intent publishedIntent = new Intent(getActivity(), MinePublishedActivity.class);
                startActivity(publishedIntent);
                break;
            case R.id.ll_my_collection://我的收藏
                Intent collectionIntent = new Intent(getActivity(), MineCollectionActivity.class);
                startActivity(collectionIntent);
                break;
            case R.id.ll_my_focus://我的关注
                Intent focusIntent = new Intent(getActivity(), MineFocusActivity.class);
                startActivity(focusIntent);
                break;
            case R.id.ll_setup://设置
                Intent setupIntent = new Intent(getActivity(), SetupActivity.class);
                startActivity(setupIntent);
                break;
        }
    }

    public User getUser() {
        String temp = pref.getString(CommonGlobal.userobj, "");
        L.d(TAG,temp);
        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(temp.getBytes(), Base64.DEFAULT));
        User user = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(bais);
            user = (User) ois.readObject();
        } catch (IOException e) {
            L.d(TAG, e.toString());
        } catch (ClassNotFoundException e1) {
            L.d(TAG, e1.toString());
        }
        return user;
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
        ll_base.setBackgroundColor(Color.parseColor(colorPrimary));
    }
}
