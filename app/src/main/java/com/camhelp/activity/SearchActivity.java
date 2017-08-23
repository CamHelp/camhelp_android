package com.camhelp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.camhelp.R;
import com.camhelp.adapter.MineFocusAdapter;
import com.camhelp.adapter.SearchHistoryAdapter;
import com.camhelp.common.CommonGlobal;
import com.camhelp.entity.CommonPropertyVO;
import com.camhelp.entity.SearchHistory;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton imgBtn_search_return,imgBtn_search;
    private EditText et_search_content;
    private LinearLayout ll_searchHistory_nodata;
    private RecyclerView recycler_searchHistory;

    private List<SearchHistory> searchHistoryList = new ArrayList<SearchHistory>();
    private LinearLayoutManager mLinearLayoutManager;
    private SearchHistoryAdapter searchHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initview();
        initSearchHistory();
    }

    private void initview(){
        imgBtn_search_return = (ImageButton) findViewById(R.id.imgBtn_search_return);
        imgBtn_search = (ImageButton) findViewById(R.id.imgBtn_search);
        imgBtn_search_return.setOnClickListener(this);
        imgBtn_search.setOnClickListener(this);
        et_search_content = (EditText) findViewById(R.id.et_search_content);
        ll_searchHistory_nodata = (LinearLayout) findViewById(R.id.ll_searchHistory_nodata);
        recycler_searchHistory = (RecyclerView) findViewById(R.id.recycler_searchHistory);
    }

    /**加载历史搜索记录*/
    private void initSearchHistory(){
        searchHistoryList = DataSupport.findAll(SearchHistory.class);
        if (searchHistoryList.size()>0){
            ll_searchHistory_nodata.setVisibility(View.GONE);
            mLinearLayoutManager = new LinearLayoutManager(this);
            recycler_searchHistory.setLayoutManager(mLinearLayoutManager);
            searchHistoryAdapter = new SearchHistoryAdapter(searchHistoryList, SearchActivity.this);
            recycler_searchHistory.setAdapter(searchHistoryAdapter);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgBtn_search_return:
                finish();
                break;
            case R.id.imgBtn_search://搜索
                if ("".equals(et_search_content.getText().toString())){
                    et_search_content.setHint("请输入搜索内容");
                }else {
                    saveSearchHistory(et_search_content.getText().toString());
                    search(et_search_content.getText().toString());
                }
                break;
        }
    }

    /**搜索*/
    private void search(String sSearchContent){
        Intent intentSearch = new Intent(this,SearchResultActivity.class);
        intentSearch.putExtra(CommonGlobal.searchContent,sSearchContent);
        startActivity(intentSearch);
    }

    /**保存搜索历史到本地*/
    private void saveSearchHistory(String sSearchContent){
        SearchHistory searchHistory = new SearchHistory();
        searchHistory.setSearchContent(sSearchContent);
        searchHistory.save();
    }
}
