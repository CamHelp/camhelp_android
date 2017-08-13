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
import com.camhelp.activity.ItemLookOtherPublishedActivity;
import com.camhelp.common.CommonGlobal;
import com.camhelp.common.CommonUrls;
import com.camhelp.common.FindValueForID;
import com.camhelp.entity.CommonProperty;
import com.camhelp.entity.ZLMinePublishedCommonProperty;
import com.camhelp.utils.LookLargeImg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by storm on 2017-08-10.
 */

public class LookOtherPeopleAdapter extends RecyclerView.Adapter<LookOtherPeopleAdapter.ViewHolder> {
    private List<ZLMinePublishedCommonProperty> mList;
    private Context mContext;
    private FindValueForID findValueForID = new FindValueForID();
    private LookLargeImg lookLargeImg = new LookLargeImg();

    List<Boolean> isLikedList = new ArrayList<Boolean>();
    List<Boolean> isCollectList = new ArrayList<Boolean>();

    public LookOtherPeopleAdapter(List<ZLMinePublishedCommonProperty> CommonPropertys, Context context) {
        mList = CommonPropertys;
        mContext = context;
    }

    @Override
    public LookOtherPeopleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_otherpeople_all, parent, false);
        final LookOtherPeopleAdapter.ViewHolder holder = new LookOtherPeopleAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(LookOtherPeopleAdapter.ViewHolder holder, final int position) {

        final ZLMinePublishedCommonProperty commonProperty = mList.get(position);
        holder.dataBinding(commonProperty, position, mContext);

        /*每一项的点击事件*/
        holder.queryItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLook = new Intent(mContext, ItemLookOtherPublishedActivity.class);
                intentLook.putExtra(CommonGlobal.commonPropertyID, commonProperty.getCommonid());
                intentLook.putExtra(CommonGlobal.commonProperty, commonProperty);
                mContext.startActivity(intentLook);
            }
        });
        /*分享*/
        holder.ll_publishfoot_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
//                intent.putExtra(Intent.EXTRA_SUBJECT, commonProperty);
                intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, commonProperty.getCommonPic1());
                intent.putExtra(Intent.EXTRA_TEXT, commonProperty.getCommonTitle());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(Intent.createChooser(intent, "分享"));
            }
        });
        /*评论*/
        holder.ll_publishfoot_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLook = new Intent(mContext, ItemLookActivity.class);
                intentLook.putExtra("commonProperty", commonProperty);
                mContext.startActivity(intentLook);
            }
        });
        /*喜欢*/
        holder.ll_publishfoot_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLikedList.get(position)) {
                    notifyItemChanged(position);//通知该位置的数据发送改变
                    isLikedList.set(position, false);
                } else {
                    notifyItemChanged(position);//通知该位置的数据发送改变
                    isLikedList.set(position, true);
                }
            }
        });
        /*收藏*/
        holder.ll_publishfoot_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCollectList.get(position)) {
                    notifyItemChanged(position);//通知该位置的数据发送改变
                    isCollectList.set(position, false);
                } else {
                    notifyItemChanged(position);//通知该位置的数据发送改变
                    isCollectList.set(position, true);
                }
            }
        });
        /*查看大图*/
        holder.item_iv_pic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imgurl = commonProperty.getCommonPic1();
                lookLargeImg.looklargeimg(imgurl, mContext);
            }
        });
        holder.item_iv_pic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imgurl = commonProperty.getCommonPic2();
                lookLargeImg.looklargeimg(imgurl, mContext);
            }
        });
        holder.item_iv_pic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imgurl = commonProperty.getCommonPic3();
                lookLargeImg.looklargeimg(imgurl, mContext);
            }
        });
        holder.item_iv_pic4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imgurl = commonProperty.getCommonPic4();
                lookLargeImg.looklargeimg(imgurl, mContext);
            }
        });

    }

    @Override
    public int getItemCount() {
        for (int i = 0; i < mList.size(); i++) {
            isLikedList.add(false);
            isCollectList.add(false);
        }
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View queryItemView;
        TextView item_tv_intro, item_foot_praisenum;//简介，类型，热度
        LinearLayout ll_publishfoot_share, ll_publishfoot_comment, ll_publishfoot_like, ll_publishfoot_collect;
        ImageView iv_like, iv_collect;
        ImageView item_iv_pic1, item_iv_pic2, item_iv_pic3, item_iv_pic4;

        public ViewHolder(View itemView) {
            super(itemView);
            queryItemView = itemView;
            item_tv_intro = (TextView) itemView.findViewById(R.id.item_tv_intro);
            item_foot_praisenum = (TextView) itemView.findViewById(R.id.item_foot_praisenum);
            ll_publishfoot_share = (LinearLayout) itemView.findViewById(R.id.ll_publishfoot_share);
            ll_publishfoot_comment = (LinearLayout) itemView.findViewById(R.id.ll_publishfoot_comment);
            ll_publishfoot_like = (LinearLayout) itemView.findViewById(R.id.ll_publishfoot_like);
            ll_publishfoot_collect = (LinearLayout) itemView.findViewById(R.id.ll_publishfoot_collect);
            iv_like = (ImageView) itemView.findViewById(R.id.iv_like);
            iv_collect = (ImageView) itemView.findViewById(R.id.iv_collect);
            item_iv_pic1 = (ImageView) itemView.findViewById(R.id.item_iv_pic1);
            item_iv_pic2 = (ImageView) itemView.findViewById(R.id.item_iv_pic2);
            item_iv_pic3 = (ImageView) itemView.findViewById(R.id.item_iv_pic3);
            item_iv_pic4 = (ImageView) itemView.findViewById(R.id.item_iv_pic4);
        }

        public void dataBinding(final ZLMinePublishedCommonProperty mCommonProperty, final int position, Context context) {
            String stitle = mCommonProperty.getCommonTitle();
            String sintro = mCommonProperty.getCommonIntro();
            String scontent = mCommonProperty.getCommonContent();
            int type = mCommonProperty.getCategoryType();
            String pic1 = mCommonProperty.getCommonPic1();
            String pic2 = mCommonProperty.getCommonPic2();
            String pic3 = mCommonProperty.getCommonPic3();
            String pic4 = mCommonProperty.getCommonPic4();

            /*设置显示文字，标题不为空显示标题，否则显示简介，否则显示详情，否则隐藏*/
            if (stitle != null && !"".equals(stitle)) {
                item_tv_intro.setText(stitle);
            } else if (sintro != null && !"".equals(sintro)) {
                item_tv_intro.setText(sintro);
            } else if (scontent != null && !"".equals(scontent)) {
                item_tv_intro.setText(scontent);
            } else {
                item_tv_intro.setVisibility(View.GONE);
            }

            item_foot_praisenum.setText(findValueForID.findCategoryType(type));//热度的位置显示类型
            item_foot_praisenum.setTextColor(mContext.getResources().getColor(R.color.colorAccent));

            if (pic1 != null) {
                Glide.with(context).load(CommonUrls.SERVER_ADDRESS_PIC+pic1).into(item_iv_pic1);
            } else {
                item_iv_pic1.setVisibility(View.GONE);
            }
            if (pic2 != null) {
                Glide.with(context).load(CommonUrls.SERVER_ADDRESS_PIC+pic2).into(item_iv_pic2);
            } else {
                item_iv_pic2.setVisibility(View.GONE);
            }
            if (pic3 != null) {
                Glide.with(context).load(CommonUrls.SERVER_ADDRESS_PIC+pic3).into(item_iv_pic3);
            } else {
                item_iv_pic3.setVisibility(View.GONE);
            }
            if (pic4 != null) {
                Glide.with(context).load(CommonUrls.SERVER_ADDRESS_PIC+pic4).into(item_iv_pic4);
            } else {
                item_iv_pic4.setVisibility(View.GONE);
            }

            if (isLikedList.get(position)) {
                iv_like.setImageResource(R.drawable.item_foot_liked);
            } else {
                iv_like.setImageResource(R.drawable.item_foot_like);
            }
            if (isCollectList.get(position)) {
                iv_collect.setImageResource(R.drawable.item_foot_collected);
            } else {
                iv_collect.setImageResource(R.drawable.item_foot_collection);
            }
        }
    }
}
