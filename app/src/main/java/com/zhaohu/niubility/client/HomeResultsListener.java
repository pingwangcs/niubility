package com.zhaohu.niubility.client;

import com.zhaohu.niubility.results.EventItem;

import java.util.List;

/**
 * Created by wen on 1/15/15.
 */
public interface HomeResultsListener {

    public void update(List<EventItem> results);
}
