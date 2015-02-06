package com.zhaohu.niubility.results;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wen on 1/30/15.
 */
public class PhotoItem {
    private String title;
    private String jsonUrl;
    private String coverUrl;

    public PhotoItem(JSONObject object) {
        try {
            this.title = object.getString("title");
            this.jsonUrl = object.getString("url");
            this.coverUrl = object.getString("cover");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getCoverUrl() {
        return coverUrl;
    }
}
