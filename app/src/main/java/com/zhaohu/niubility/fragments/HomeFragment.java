package com.zhaohu.niubility.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

import com.zhaohu.niubility.R;
import com.zhaohu.niubility.client.HomeResultsListener;
import com.zhaohu.niubility.client.ZhaoHuClient;
import com.zhaohu.niubility.results.EventItem;
import com.zhaohu.niubility.results.EventResultsListAdapter;

import java.util.List;

/**
 * Created by wen on 1/11/15.
 */
public class HomeFragment extends Fragment{
    private Context mContext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = container.getContext();
        View view = inflater.inflate(R.layout.home_page_layout, container, false);


//        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//        DisplayMetrics dm = getResources().getDisplayMetrics();
//        final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, dm);
//        TextView textView = (TextView) view.findViewById(R.id.title);
//        params.setMargins(margin, margin, margin, margin);
//        textView.setLayoutParams(params);
//        textView.setLayoutParams(params);
//        textView.setGravity(Gravity.CENTER);
//        textView.setText("首页界面");
//        textView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, dm));


        final ListView resultsListView = (ListView) view.findViewById(R.id.results_list);

        final EventResultsListAdapter adapter = new EventResultsListAdapter(mContext);

        ZhaoHuClient client = ZhaoHuClient.getInstance(mContext);

        client.addHomeResultsListener(new HomeResultsListener() {
            @Override
            public void update(List<EventItem> results) {
                adapter.setData(results);
                resultsListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });



        client.fetchHomeResults();

        return view;
    }
}
