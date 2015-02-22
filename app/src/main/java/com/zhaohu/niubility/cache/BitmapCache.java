package com.zhaohu.niubility.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by wen on 1/16/15.
 */
public class BitmapCache implements ImageLoader.ImageCache {

    private Context context;
    private LruCache<String, Bitmap> mMemoryCache;

    private static BitmapCache mInstance = null;

    public static BitmapCache getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new BitmapCache(context);
        }
        return mInstance;
    }

    public BitmapCache(Context context) {
        this.context = context;
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 2;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };


    }

    @Override
    public Bitmap getBitmap(String url) {
        String key = createKey(url);
        return mMemoryCache.get(key);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        String key = createKey(url);
        mMemoryCache.put(key, bitmap);
    }

    private String createKey(String url){
        return String.valueOf(url.hashCode());
    }
}
