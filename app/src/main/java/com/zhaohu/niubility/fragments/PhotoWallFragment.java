package com.zhaohu.niubility.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridView;

import com.zhaohu.niubility.R;
import com.zhaohu.niubility.client.PhotoWallListener;
import com.zhaohu.niubility.client.ZhaoHuClient;
import com.zhaohu.niubility.results.PhotoItem;
import com.zhaohu.niubility.results.PhotoWallAdapter;

import java.util.List;

/**
 * Created by wen on 1/11/15.
 */
public class PhotoWallFragment extends Fragment {
    private Context mContext;
    PhotoWallAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = container.getContext();
        View view = inflater.inflate(R.layout.photo_wall_layout, container, false);

        final GridView photoGridView = (GridView) view.findViewById(R.id.photo_wall);

        adapter = new PhotoWallAdapter(mContext, photoGridView);

        ZhaoHuClient client = ZhaoHuClient.getInstance(mContext);

        client.addPhotoWallListener(new PhotoWallListener() {
            @Override
            public void update(List<PhotoItem> results) {
                adapter.setData(results);
                photoGridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        final int mImageThumbSize = mContext.getResources().getDimensionPixelSize(
                R.dimen.image_thumbnail_size);
        final int mImageThumbSpacing = mContext.getResources().getDimensionPixelSize(
                R.dimen.image_thumbnail_spacing);

        photoGridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        final int numColumns = (int) Math.floor(photoGridView
                                .getWidth()
                                / (mImageThumbSize + mImageThumbSpacing));
                        if (numColumns > 0) {
                            int columnWidth = (photoGridView.getWidth() / numColumns)
                                    - mImageThumbSpacing;
                            adapter.setItemHeight(columnWidth);
                            photoGridView.getViewTreeObserver()
                                    .removeGlobalOnLayoutListener(this);
                        }
                    }
                });
        client.fetchPhotoWall();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        adapter.fluchCache();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 退出程序时结束所有的下载任务
        adapter.cancelAllTasks();
    }
}
