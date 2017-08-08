package com.camhelp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.camhelp.R;
import com.camhelp.entity.CommonProperty;

import java.util.List;

/**
 * Created by storm on 2017-08-08.
 * 我发布的内容adapter
 */

public class MinePublishedAdapter extends RecyclerView.Adapter<MinePublishedAdapter.ViewHolder> {
    private List<CommonProperty> mList;
    private Context mContext;


    public MinePublishedAdapter(List<CommonProperty> CommonPropertys, Context context) {
        mList = CommonPropertys;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_minepublished_all, parent, false);
        final MinePublishedAdapter.ViewHolder holder = new MinePublishedAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final CommonProperty commonProperty = mList.get(position);
        holder.dataBinding(commonProperty, position, mContext);

        holder.queryItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, ""+position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View queryItemView;


        public ViewHolder(View itemView) {
            super(itemView);
            queryItemView = itemView;
        }

        public void dataBinding(final CommonProperty mCommonProperty, final int position, Context context) {

        }
    }
}
