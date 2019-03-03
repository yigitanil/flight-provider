package com.ygt.flightprovider.model;

import java.util.List;

import lombok.Getter;

@Getter
public class Page<T> {
    private int pageNumer;
    private int count;
    private int totalCount;
    private List<T> content;

    public Page(int pageNumer, int totalCount, List<T> content) {
        this.pageNumer = pageNumer;
        this.totalCount = totalCount;
        this.content = content;
        this.count = content.size();
    }
}
