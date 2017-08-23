package com.camhelp.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by storm on 2017-08-23.
 */

public class SearchHistory extends DataSupport implements Serializable {
    @Override
    public String toString() {
        return "SearchHistory{" +
                "searchContent='" + searchContent + '\'' +
                '}';
    }

    private String searchContent;

    public String getSearchContent() {
        return searchContent;
    }

    public void setSearchContent(String searchContent) {
        this.searchContent = searchContent;
    }
}
