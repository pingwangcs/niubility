package com.zhaohu.niubility.client;

import com.zhaohu.niubility.results.AlbumItem;

import java.util.List;

/**
 * Created by wen on 2/11/15.
 */
public interface AlbumListener {
    public void update(List<AlbumItem> results);
}
