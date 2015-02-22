package com.zhaohu.niubility.types;

/**
 * Created by wen on 2/11/15.
 */
public enum EventsType {
    HOME_EVENTS("http://51zhaohu.com/services/api/rest/json/?method=event.search&keyword=All&offset=0"),
    HOT_EVENTS("http://51zhaohu.com/services/api/rest/json/?method=event.search&featured=y&offset=0"),
    ALBUM_EVENTS("http://51zhaohu.com/services/api/rest/json/?method=album.list&offset=0"),
    PHOTO_WALL_EVENTS("");

    private String url;
    EventsType(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
