package com.camhelp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.camhelp.R;
import com.camhelp.activity.ItemLookActivity;
import com.camhelp.activity.LoginActivity;
import com.camhelp.activity.LookOtherPeopleActivity;
import com.camhelp.common.CommonGlobal;
import com.camhelp.common.CommonUrls;
import com.camhelp.common.FindValueForID;
import com.camhelp.entity.CommonProperty;
import com.camhelp.entity.CommonPropertyVO;
import com.camhelp.entity.User;
import com.camhelp.utils.DateConversionUtils;
import com.camhelp.utils.L;
import com.camhelp.utils.LookLargeImg;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by storm on 2017-08-09.
 */

public class HomeNewAndFocusAdapter extends RecyclerView.Adapter<HomeNewAndFocusAdapter.ViewHolder> {
    private List<CommonPropertyVO> mList;
    private Context mContext;
    private FindValueForID findValueForID = new FindValueForID();
    private LookLargeImg lookLargeImg = new LookLargeImg();
    private User user = new User();

    private boolean isliked, iscollected;
    List<Boolean> isLikedList = new ArrayList<Boolean>();
    List<Boolean> isCollectList = new ArrayList<Boolean>();

    String shreText;
    DateConversionUtils dateConversionUtils = new DateConversionUtils();
    String url1;

    public HomeNewAndFocusAdapter(List<CommonPropertyVO> CommonPropertyVOs, Context context) {
        mList = CommonPropertyVOs;
        mContext = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_home_new_or_focus02, parent, false);
        final HomeNewAndFocusAdapter.ViewHolder holder = new HomeNewAndFocusAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final CommonPropertyVO commonPropertyVO = mList.get(position);
        holder.dataBinding(commonPropertyVO, position, mContext);

        /**
         * 图片加载
         * 通过设置tag，解决glide错乱问题!!!!
         * */
        holder.item_ll_pic.setTag(mList.get(position).getCommonPic1());//setTag
//        holder.item_iv_pic1.setTag(mList.get(position).getCommonPic1());//setTag

        String tagllPic1 = (String) holder.item_ll_pic.getTag();
//        String tagPic1 = (String) holder.item_iv_pic1.getTag();
        L.d("position:"+position+"--tagllPic1:"+tagllPic1);
//        L.d("position:"+position+"--tagPic1:"+tagPic1);
        L.d("position:"+position+"--pic1:"+commonPropertyVO.getCommonPic1());
        String pic1 = commonPropertyVO.getCommonPic1();
        String pic2 = commonPropertyVO.getCommonPic2();
        String pic3 = commonPropertyVO.getCommonPic3();
        String pic4 = commonPropertyVO.getCommonPic4();

        if (!TextUtils.equals(commonPropertyVO.getCommonPic1(), tagllPic1)) {
            holder.item_ll_pic.setVisibility(View.GONE);
        }else if (pic1==null||"".equals(pic1)){
            holder.item_ll_pic.setVisibility(View.GONE);
        }
        else {
            holder.item_ll_pic.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(CommonUrls.SERVER_ADDRESS_PIC + pic1)
                    .placeholder(R.drawable.isloading2)
                    .crossFade().into(holder.item_iv_pic1);

//            if (pic2 != null && !"".equals(pic2)) {
//                Glide.with(mContext).load(CommonUrls.SERVER_ADDRESS_PIC+pic2)
//                        .placeholder(R.drawable.isloading2).into(holder.item_iv_pic2);
//            } else {
//                holder.item_iv_pic2.setVisibility(View.GONE);
//            }
//            if (pic3 != null && !"".equals(pic3)) {
//                Glide.with(mContext).load(CommonUrls.SERVER_ADDRESS_PIC+pic3)
//                        .placeholder(R.drawable.isloading2).into(holder.item_iv_pic3);
//            } else {
//                holder.item_iv_pic3.setVisibility(View.GONE);
//            }
//            if (pic4 != null && !"".equals(pic4)) {
//                Glide.with(mContext).load(CommonUrls.SERVER_ADDRESS_PIC + pic4)
//                        .placeholder(R.drawable.isloading2)
//                        .crossFade().into(holder.item_iv_pic4);
//            } else {
//                holder.item_iv_pic4.setVisibility(View.GONE);
//            }
        }


        /*每一项的点击事件*/
        holder.queryItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLook = new Intent(mContext, ItemLookActivity.class);
                intentLook.putExtra(CommonGlobal.commonPropertyID, commonPropertyVO.getCommonid());
                intentLook.putExtra(CommonGlobal.commonProperty, commonPropertyVO);
                mContext.startActivity(intentLook);
            }
        });
        /*点击头像查看用户*/
        holder.item_top_iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLookOtherPeople = new Intent(mContext, LookOtherPeopleActivity.class);
                intentLookOtherPeople.putExtra(CommonGlobal.user_id, commonPropertyVO.getUserID());//把用户id传过去
                intentLookOtherPeople.putExtra(CommonGlobal.userAvatar, commonPropertyVO.getAvatar());//把用户头像传过去
                intentLookOtherPeople.putExtra(CommonGlobal.userNickname, commonPropertyVO.getNickname());//把用户昵称传过去
                mContext.startActivity(intentLookOtherPeople);
            }
        });
        /*分享*/
        holder.ll_publishfoot_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
//                intent.putExtra(Intent.EXTRA_SUBJECT, commonProperty);
                intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, commonPropertyVO.getCommonPic1());
                intent.putExtra(Intent.EXTRA_TEXT, shreText);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(Intent.createChooser(intent, "分享"));
            }
        });
        /*评论*/
        holder.ll_publishfoot_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLook = new Intent(mContext, ItemLookActivity.class);
                intentLook.putExtra(CommonGlobal.commonPropertyID, commonPropertyVO.getCommonid());
                intentLook.putExtra(CommonGlobal.commonProperty, commonPropertyVO);
                mContext.startActivity(intentLook);
            }
        });
        /*喜欢*/
        holder.ll_publishfoot_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLikedList.get(position)) {
//                    notifyItemChanged(position);//通知该位置的数据发送改变
                    notifyDataSetChanged();
                    isLikedList.set(position, false);
                } else {
                    notifyItemChanged(position);//通知该位置的数据发送改变
//                    notifyDataSetChanged();
                    isLikedList.set(position, true);
                }
            }
        });
        /*收藏*/
        holder.ll_publishfoot_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCollectList.get(position)) {
//                    notifyItemChanged(position);//通知该位置的数据发送改变
                    notifyDataSetChanged();
                    isCollectList.set(position, false);
                } else {
                    notifyItemChanged(position);//通知该位置的数据发送改变
//                    notifyDataSetChanged();
                    isCollectList.set(position, true);
                }
            }
        });
        /*查看大图*/
        holder.item_iv_pic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imgurl = CommonUrls.SERVER_ADDRESS_PIC + commonPropertyVO.getCommonPic1();
                lookLargeImg.looklargeimg(imgurl, mContext);
            }
        });
        holder.item_iv_pic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imgurl = CommonUrls.SERVER_ADDRESS_PIC + commonPropertyVO.getCommonPic2();
                lookLargeImg.looklargeimg(imgurl, mContext);
            }
        });
        holder.item_iv_pic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imgurl = CommonUrls.SERVER_ADDRESS_PIC + commonPropertyVO.getCommonPic3();
                lookLargeImg.looklargeimg(imgurl, mContext);
            }
        });
        holder.item_iv_pic4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imgurl = CommonUrls.SERVER_ADDRESS_PIC + commonPropertyVO.getCommonPic4();
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
        ImageView item_top_iv_avatar;//头像
        TextView item_top_tv_nickname, item_top_tv_createtime, item_top_iv_type;//昵称,发布时间,类型
        TextView item_tv_intro, item_foot_praisenum;//标题或简介或详情,热度（点赞量）
        LinearLayout ll_publishfoot_share, ll_publishfoot_comment, ll_publishfoot_like, ll_publishfoot_collect;
        ImageView iv_like, iv_collect;
        ImageView item_iv_pic1, item_iv_pic2, item_iv_pic3, item_iv_pic4;
        LinearLayout item_ll_pic;//图片显示

        public ViewHolder(View itemView) {
            super(itemView);
            queryItemView = itemView;
            item_top_iv_avatar = (ImageView) itemView.findViewById(R.id.item_top_iv_avatar);
            item_top_tv_nickname = (TextView) itemView.findViewById(R.id.item_top_tv_nickname);
            item_top_tv_createtime = (TextView) itemView.findViewById(R.id.item_top_tv_createtime);
            item_tv_intro = (TextView) itemView.findViewById(R.id.item_tv_intro);
            item_top_iv_type = (TextView) itemView.findViewById(R.id.item_top_iv_type);
            item_foot_praisenum = (TextView) itemView.findViewById(R.id.item_foot_praisenum);
            ll_publishfoot_share = (LinearLayout) itemView.findViewById(R.id.ll_publishfoot_share);
            ll_publishfoot_comment = (LinearLayout) itemView.findViewById(R.id.ll_publishfoot_comment);
            ll_publishfoot_like = (LinearLayout) itemView.findViewById(R.id.ll_publishfoot_like);
            ll_publishfoot_collect = (LinearLayout) itemView.findViewById(R.id.ll_publishfoot_collect);
            iv_like = (ImageView) itemView.findViewById(R.id.iv_like);
            iv_collect = (ImageView) itemView.findViewById(R.id.iv_collect);
            item_ll_pic = (LinearLayout) itemView.findViewById(R.id.item_ll_pic);
            item_iv_pic1 = (ImageView) itemView.findViewById(R.id.item_iv_pic1);
            item_iv_pic2 = (ImageView) itemView.findViewById(R.id.item_iv_pic2);
            item_iv_pic3 = (ImageView) itemView.findViewById(R.id.item_iv_pic3);
            item_iv_pic4 = (ImageView) itemView.findViewById(R.id.item_iv_pic4);
//            item_iv_pic1 = (ImageView) itemView.findViewById(R.id.item04_iv_pic0);
        }

        public void dataBinding(final CommonPropertyVO mCommonPropertyVO, final int position, Context context) {

            String avatar = CommonUrls.SERVER_ADDRESS_PIC + mCommonPropertyVO.getAvatar();
            Glide.with(context).load(avatar).into(item_top_iv_avatar);

            if (mCommonPropertyVO.getCreatetime() != null) {
                String sCreatetime = dateConversionUtils.sdateToString2(mCommonPropertyVO.getCreatetime().toString());
                item_top_tv_createtime.setText(sCreatetime);
            }

            String stitle = mCommonPropertyVO.getCommonTitle();
            int type = mCommonPropertyVO.getCategoryType();
            String pic1 = mCommonPropertyVO.getCommonPic1();
            String pic2 = mCommonPropertyVO.getCommonPic2();
            String pic3 = mCommonPropertyVO.getCommonPic3();
            String pic4 = mCommonPropertyVO.getCommonPic4();

            int userId = mCommonPropertyVO.getUserID();
            user.setUserID(userId);
            item_top_tv_nickname.setText(mCommonPropertyVO.getNickname());

            /*设置显示文字，标题不为空显示标题，否则隐藏*/
            if (stitle != null && !"".equals(stitle)) {
                item_tv_intro.setText(stitle);
            } else {
                item_tv_intro.setVisibility(View.GONE);
            }
            shreText = item_tv_intro.getText().toString();

            item_top_iv_type.setText(findValueForID.findCategoryType(type));
            item_foot_praisenum.setText("" + mCommonPropertyVO.getPraisenum() + "条热度");

            /*隐藏第二张图片*/
            item_iv_pic2.setVisibility(View.GONE);

            if (isLikedList.get(position)) {
                iv_like.setImageResource(R.mipmap.ic_action_liked2);
            } else {
                iv_like.setImageResource(R.drawable.ic_action_like);
            }
            if (isCollectList.get(position)) {
                iv_collect.setImageResource(R.drawable.ic_action_collected);
            } else {
                iv_collect.setImageResource(R.drawable.ic_action_collect);
            }
        }
    }
}