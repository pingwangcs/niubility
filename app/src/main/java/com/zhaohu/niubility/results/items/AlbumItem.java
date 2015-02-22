package com.zhaohu.niubility.results.items;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wen on 2/11/15.
 */
public class AlbumItem {

    public String title;
    public String time;
    public String url;
    public String cover_url;

    public AlbumItem(JSONObject object) {
        try {
            this.title = object.getString("title");
            this.time = object.getString("time_updated");
            this.url = object.getString("url");
            this.cover_url = object.getString("cover");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
