package com.camhelp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.camhelp.R;
import com.camhelp.activity.LookOtherPeopleActivity;
import com.camhelp.activity.SearchResultActivity;
import com.camhelp.common.CommonGlobal;
import com.camhelp.common.CommonUrls;
import com.camhelp.entity.SearchHistory;
import com.camhelp.entity.UserVO;

import java.util.List;

/**
 * Created by storm on 2017-08-23.
 * 搜索历史adapter
 */

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder> {
    private List<SearchHistory> mList;
    private Context mContext;

    public SearchHistoryAdapter(List<SearchHistory> searchHistories, Context context) {
        mList = searchHistories;
        mContext = context;
    }

    @Override
    public SearchHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_search_history, parent, false);
        final SearchHistoryAdapter.ViewHolder holder = new SearchHistoryAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(SearchHistoryAdapter.ViewHolder holder, final int position) {

        final SearchHistory searchHistory = mList.get(position);
        holder.dataBinding(searchHistory, position, mContext);

        /*每一项的点击事件*/
        holder.queryItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSearch = new Intent(mContext,SearchResultActivity.class);
                intentSearch.putExtra(CommonGlobal.searchContent,searchHistory.getSearchContent());
                mContext.startActivity(intentSearch);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View queryItemView;
        TextView item_tv_searchHistoryContent;

        public ViewHolder(View itemView) {
            super(itemView);
            queryItemView = itemView;
            item_tv_searchHistoryContent = (TextView) itemView.findViewById(R.id.item_tv_searchHistoryContent);
        }

        public void dataBinding(final SearchHistory searchHistory, final int position, Context context) {
            item_tv_searchHistoryContent.setText(searchHistory.getSearchContent());
        }
    }
}
