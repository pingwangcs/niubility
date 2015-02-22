package com.zhaohu.niubility.results.items;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wen on 1/14/15.
 */
public class EventItem {
    public String guid;
    public String title;
    public String owner;
    public String startTime;
    public String endTime;
    public String address;
    public String imageUrl;
    public String webViewUrl;

    public EventItem(JSONObject object) {
        try {
            this.guid = object.getString("guid");
            this.title = object.getString("title");
            this.owner = object.getString("owner_name");
            this.startTime = object.getString("start_date");
            this.endTime = object.getString("end_date");
            this.address = object.getString("full_address");
            this.imageUrl = object.getString("icon_url_medium");
            this.webViewUrl = object.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
