package com.camhelp.fragment;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.camhelp.R;
import com.camhelp.activity.LoginActivity;
import com.camhelp.activity.MainActivity;
import com.camhelp.activity.SetupActivity;
import com.camhelp.common.CommonGlobal;

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
    private LinearLayout ll_base;

    TextView tv_username, tv_intro;
    LinearLayout ll_setup;
    LinearLayout  ll_exit_system, ll_personal, ll_log_out;
    Button btn_log_out;

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
        initview();
        useInit();
    }

    public void initview() {
        ll_base = (LinearLayout) getActivity().findViewById(R.id.ll_base);
        ll_base.setBackgroundColor(Color.parseColor(CommonGlobal.MYCOLOR_PRIMARY));

        tv_username = (TextView) getActivity().findViewById(R.id.tv_username);
        tv_intro = (TextView) getActivity().findViewById(R.id.tv_intro);

        ll_exit_system = (LinearLayout) getActivity().findViewById(R.id.ll_exit_system);
        btn_log_out = (Button) getActivity().findViewById(R.id.btn_log_out);
        ll_personal = (LinearLayout) getActivity().findViewById(R.id.ll_personal);
        ll_log_out = (LinearLayout) getActivity().findViewById(R.id.ll_log_out);
        ll_setup = (LinearLayout) getActivity().findViewById(R.id.ll_setup);

        ll_exit_system.setOnClickListener(this);
        btn_log_out.setOnClickListener(this);
        ll_personal.setOnClickListener(this);
        ll_log_out.setOnClickListener(this);
        ll_setup.setOnClickListener(this);
    }

    /**
     * 获取用户
     */
    public void useInit() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_setup://设置
                Intent setupIntent = new Intent(getActivity(), SetupActivity.class);
                startActivity(setupIntent);
                break;
            case R.id.ll_personal://个人信息
                break;
            case R.id.ll_exit_system://退出系统
                EXITORLOGOUT = 0;
                showchoosedialog(view, "退出系统");
                break;
            case R.id.ll_log_out://注销用户
                EXITORLOGOUT = 1;
                showchoosedialog(view, "注销");
                break;
            case R.id.btn_log_out://注销用户(隐藏)
                EXITORLOGOUT = 1;
                showchoosedialog(view, "注销");
                break;
            case R.id.no:
                choosedialog.dismiss();
                break;
            case R.id.yes:
                if (EXITORLOGOUT == 0) {
                    System.exit(0);
                } else if (EXITORLOGOUT == 1) {
                    CommonGlobal.autoLogin = false; //恢复各初始值
                    CommonGlobal.loginUserId = -1;
                    Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(loginIntent);//注销后跳转到登录界面重新登录
                    getActivity().finish();
                }
                choosedialog.dismiss();
                break;
        }
    }

    public class MyApp extends Application {
        public int theme = 0;
    }

    public void showchoosedialog(View view, String hint) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("提示").setMessage("确定" + hint).setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (EXITORLOGOUT == 0) {
                            System.exit(0);
                        } else if (EXITORLOGOUT == 1) {
                            CommonGlobal.autoLogin = false; //回复各初始值
                            CommonGlobal.loginUserId = -1;
                            Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(loginIntent);//注销后跳转到登录界面重新登录
                            getActivity().finish();
                        }
                    }
                }).setNegativeButton("取消", null);
        alert.create();
        alert.show();
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
}
