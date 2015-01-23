package com.zhaohu.niubility.results;

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

    public EventItem(String guid, String title, String owner, String startTime, String endTime, String address, String imageUrl) {
        this.guid = guid;
        this.title = title;
        this.owner = owner;
        this.startTime = startTime;
        this.endTime = endTime;
        this.address = address;
        this.imageUrl = imageUrl;
    }

    public EventItem(JSONObject object) {
        try {
            this.guid = object.getString("guid");
            this.title = object.getString("title");
            this.owner = object.getString("owner_name");
            this.startTime = object.getString("start_date");
            this.endTime = object.getString("end_date");
            String tempAddress = object.getString("address");
            this.address = (tempAddress.length() == 0 ? "" : (tempAddress+", ")) + object.getString("city") + ", "+object.getString("state");
            this.imageUrl = object.getString("icon_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
