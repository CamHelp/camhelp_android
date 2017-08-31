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
import com.camhelp.activity.LookOtherPeopleActivity;
import com.camhelp.common.CommonGlobal;
import com.camhelp.common.CommonUrls;
import com.camhelp.entity.Comment;
import com.camhelp.entity.Comment;
import com.camhelp.utils.DateConversionUtils;

import java.util.List;

/**
 * Created by storm on 2017-08-31.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<Comment> mList;
    private Context mContext;
    private DateConversionUtils dateConversionUtils = new DateConversionUtils();

    public CommentAdapter(List<Comment> Comments, Context context) {
        mList = Comments;
        mContext = context;
    }

    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_comment, parent, false);
        final CommentAdapter.ViewHolder holder = new CommentAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(CommentAdapter.ViewHolder holder, final int position) {

        final Comment comment = mList.get(position);
        holder.dataBinding(comment, position, mContext);

        /*每一项的点击事件*/
        holder.queryItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //查看评论者
        holder.item_commentAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLookOtherPeople = new Intent(mContext, LookOtherPeopleActivity.class);
                intentLookOtherPeople.putExtra(CommonGlobal.user_id, comment.getFromuserId());//把用户id传过去
                intentLookOtherPeople.putExtra(CommonGlobal.userAvatar, comment.getFromuseravatar());//把用户头像传过去
                intentLookOtherPeople.putExtra(CommonGlobal.userNickname, comment.getFromnickname());//把用户昵称传过去
                mContext.startActivity(intentLookOtherPeople);
            }
        });
        //查看被回复者
        holder.item_comment_toNick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //点赞
        holder.item_ll_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //回复
        holder.item_ll_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View queryItemView;
        ImageView item_commentAvatar;
        TextView item_comment_fromNick, item_comment_toNick, item_comment_createtime, item_comment_content;
        LinearLayout item_ll_like, item_ll_reply;
        TextView item_tv_praisenum;

        public ViewHolder(View itemView) {
            super(itemView);
            queryItemView = itemView;
            item_commentAvatar = (ImageView) itemView.findViewById(R.id.item_commentAvatar);
            item_comment_fromNick = (TextView) itemView.findViewById(R.id.item_comment_fromNick);
            item_comment_toNick = (TextView) itemView.findViewById(R.id.item_comment_toNick);
            item_comment_createtime = (TextView) itemView.findViewById(R.id.item_comment_createtime);
            item_comment_content = (TextView) itemView.findViewById(R.id.item_comment_content);
            item_ll_like = (LinearLayout) itemView.findViewById(R.id.item_ll_like);
            item_ll_reply = (LinearLayout) itemView.findViewById(R.id.item_ll_reply);
            item_tv_praisenum = (TextView) itemView.findViewById(R.id.item_tv_praisenum);
        }

        public void dataBinding(final Comment comment, final int position, Context context) {
            Glide.with(mContext).load(CommonUrls.SERVER_ADDRESS_PIC + comment.getFromuseravatar()).into(item_commentAvatar);
            item_comment_fromNick.setText(comment.getFromnickname() + "  ");
            if (comment.getTonickname() != null && !"".equals(comment.getTonickname())) {
                item_comment_toNick.setVisibility(View.VISIBLE);
                item_comment_toNick.setText("@" + comment.getTonickname());
            }
            item_comment_createtime.setText(dateConversionUtils.sdateToStringBirthday(comment.getCreatetime()));
            item_comment_content.setText(comment.getCommenttext());
            if (comment.getPraisenum()>0){
                item_tv_praisenum.setText("" + comment.getPraisenum());
            }
        }
    }
}
