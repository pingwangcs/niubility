package com.zhaohu.niubility.client.clients;

import com.android.volley.toolbox.JsonObjectRequest;
import com.zhaohu.niubility.client.listeners.ResultsListener;

import java.util.List;

/**
 * Created by wen on 2/22/15.
 */
public interface ResultsClient {
    public String getUrl();
    public JsonObjectRequest getFetchRequest();
    public void addListener(ResultsListener listener);
}
