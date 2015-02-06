package com.zhaohu.niubility.client;

import com.zhaohu.niubility.results.HotEventListItem;

import java.util.List;

/**
 * Created by wen on 1/24/15.
 */
public interface HotResultsListener {

    public void update(List<HotEventListItem> results);

}
