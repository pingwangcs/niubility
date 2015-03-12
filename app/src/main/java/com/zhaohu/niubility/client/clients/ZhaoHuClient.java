package com.zhaohu.niubility.client.clients;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.zhaohu.niubility.R;
import com.zhaohu.niubility.cache.BitmapCache;
import com.zhaohu.niubility.client.listeners.ResultsListener;
import com.zhaohu.niubility.types.EventsType;
import com.zhaohu.niubility.views.ZoomImageView;

/**
 * Created by wen on 1/13/15.
 */
public class ZhaoHuClient {

    private static ZhaoHuClient mInstance = null;

    private Context mContext;
    private RequestQueue mQueue;

    private ResultsClient resultsClient;


    public ZhaoHuClient(Context context) {
        mContext = context;
        mQueue = Volley.newRequestQueue(mContext);
    }

    public static ZhaoHuClient getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new ZhaoHuClient(context);
        }
        return mInstance;
    }

    public void fetchResult(EventsType type, ResultsListener listener) {
        ResultsClient client = getSpecifcClient(type);
        client.addListener(listener);
        JsonObjectRequest request = client.getFetchRequest();
        mQueue.add(request);
    }

    public void loadMore(EventsType type, int offset) {
        ResultsClient client = getSpecifcClient(type);
        JsonObjectRequest request = client.getLoadMoreRequest(offset);
        mQueue.add(request);
    }

    public void fetchResult(String url, ResultsListener listener) {
        JsonObjectRequest request;
        resultsClient = new PhotoWallClient(url);
        resultsClient.addListener(listener);
        request = resultsClient.getFetchRequest();
        mQueue.add(request);
    }

    public void setNetworkImage(String imageUrl, NetworkImageView imageView) {
        ImageLoader imageLoader = new ImageLoader(mQueue, BitmapCache.getInstance(mContext));

        imageView.setDefaultImageResId(R.drawable.loading);
        imageView.setErrorImageResId(R.drawable.pic_load_failed);
        imageView.setImageUrl(imageUrl, imageLoader);
    }

    public void loadImage(String imageUrl, ImageView imageView) {

        ImageLoader imageLoader = new ImageLoader(mQueue, BitmapCache.getInstance(mContext));

        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView,
                R.drawable.loading, R.drawable.pic_load_failed);

        imageLoader.get(imageUrl, listener);
    }

    public void loadImageUsingRequest(String url, final ZoomImageView zoomImageView) {
        ImageRequest imageRequest = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        zoomImageView.setImageBitmap(response);
                    }
                }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.pic_load_failed);
                zoomImageView.setImageBitmap(bitmap);
            }
        });
        mQueue.add(imageRequest);
    }

    public void loadImage(String url, final ZoomImageView zoomImageView) {
        ImageLoader imageLoader = new ImageLoader(mQueue, BitmapCache.getInstance(mContext));

        ImageLoader.ImageListener listener = new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                zoomImageView.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.pic_load_failed);
                zoomImageView.setImageBitmap(bitmap);
            }
        };

        imageLoader.get(url, listener);
    }

    private ResultsClient getSpecifcClient(EventsType type) {
        ResultsClient client;
        switch (type) {
            case HOME_EVENTS:
                client = HomeResultsClient.getInstance();
                break;
            case HOT_EVENTS:
                client = new HotResultsClient();
                break;
            case ALBUM_EVENTS:
                client = new AlbumClient();
                break;
            default:
                client = null;
        }
        return client;
    }

}
