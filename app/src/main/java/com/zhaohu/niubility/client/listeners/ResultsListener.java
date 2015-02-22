package com.zhaohu.niubility.client.listeners;

import java.util.List;

/**
 * Created by wen on 2/22/15.
 */
public interface ResultsListener<T> {
    public void update(List<T> results);
}
