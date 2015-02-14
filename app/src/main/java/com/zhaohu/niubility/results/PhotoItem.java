package com.zhaohu.niubility.results;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wen on 1/30/15.
 */
public class PhotoItem {
    public String title;
    public String infoUrl;
    public String imageUrl;
    public String imageDetailUrl;

    public PhotoItem(JSONObject object) {
        try {
            this.title = object.getString("title");
            this.infoUrl = object.getString("url");
            this.imageUrl = object.getString("thumbnail_small");
            this.imageDetailUrl = object.getString("thumbnail_large");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
