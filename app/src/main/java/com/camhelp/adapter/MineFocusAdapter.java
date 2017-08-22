package com.camhelp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.camhelp.R;
import com.camhelp.activity.ItemLookActivity;
import com.camhelp.activity.ItemLookMinePublishedActivity;
import com.camhelp.activity.LookOtherPeopleActivity;
import com.camhelp.common.CommonGlobal;
import com.camhelp.common.CommonUrls;
import com.camhelp.common.FindValueForID;
import com.camhelp.entity.UserVO;
import com.camhelp.entity.ZLMinePublishedCommonProperty;
import com.camhelp.utils.LookLargeImg;

import java.util.List;

/**
 * Created by storm on 2017-08-22.
 */

public class MineFocusAdapter extends RecyclerView.Adapter<MineFocusAdapter.ViewHolder> {
    private List<UserVO> mList;
    private Context mContext;

    public MineFocusAdapter(List<UserVO> userVOs, Context context) {
        mList = userVOs;
        mContext = context;
    }

    @Override
    public MineFocusAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_mine_focus, parent, false);
        final MineFocusAdapter.ViewHolder holder = new MineFocusAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MineFocusAdapter.ViewHolder holder, final int position) {

        final UserVO userVO = mList.get(position);
        holder.dataBinding(userVO, position, mContext);

        /*每一项的点击事件*/
        holder.queryItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLookOtherPeople = new Intent(mContext, LookOtherPeopleActivity.class);
                intentLookOtherPeople.putExtra(CommonGlobal.user_id, userVO.getUserID());//把用户id传过去
                intentLookOtherPeople.putExtra(CommonGlobal.userAvatar, userVO.getAvatar());//把用户头像传过去
                intentLookOtherPeople.putExtra(CommonGlobal.userNickname, userVO.getNickname());//把用户昵称传过去
                mContext.startActivity(intentLookOtherPeople);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View queryItemView;
        ImageView item_minefocus_iv_avator;
        TextView item_minefocus_tv_nickname,item_minefocus_tv_intro;

        public ViewHolder(View itemView) {
            super(itemView);
            queryItemView = itemView;
            item_minefocus_iv_avator = (ImageView) itemView.findViewById(R.id.item_minefocus_iv_avator);
            item_minefocus_tv_nickname = (TextView) itemView.findViewById(R.id.item_minefocus_tv_nickname);
            item_minefocus_tv_intro = (TextView) itemView.findViewById(R.id.item_minefocus_tv_intro);
        }

        public void dataBinding(final UserVO userVO, final int position, Context context) {
            Glide.with(context).load(CommonUrls.SERVER_ADDRESS_PIC+userVO.getAvatar()).into(item_minefocus_iv_avator);
            item_minefocus_tv_nickname.setText(userVO.getNickname());
            item_minefocus_tv_intro.setText(userVO.getIntro());
        }
    }
}
