package com.zhaohu.niubility.client;

import com.zhaohu.niubility.results.PhotoItem;

import java.util.List;

/**
 * Created by wen on 1/30/15.
 */
public interface PhotoWallListener {
    public void update(List<PhotoItem> results);
}
