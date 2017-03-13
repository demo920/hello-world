package com.teemo.swipetoloadlayoutdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.teemo.swipetoloadlayoutdemo.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SwipeActivity extends AppCompatActivity implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        List<String> list = new ArrayList();
        for(int i=0;i<10;i++) {
            list.add("666");
            list.add("999");
        }

        swipeToLoadLayout.setOnRefreshListener(this);
//        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeTarget.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
//        swipeTarget.setAdapter(new MAdapter(list));
        swipeTarget.setAdapter(new CommonAdapter<String>(this,R.layout.adapter_main, list) {
            @Override
            protected void convert(ViewHolder holder, String s, final int position) {
                holder.setText(R.id.tv_name,s);
                holder.setOnClickListener(R.id.adapter_main, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("TAG","--- "+position);
                    }
                });
            }
        });

    }


    @Override
    public void onRefresh() {

        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(false);
                Toast.makeText(SwipeActivity.this, "刷新完成", Toast.LENGTH_SHORT).show();
            }
        }, 1600);
        Log.e("tag", "--onRefresh--");
    }

    @Override
    public void onLoadMore() {

        Log.e("tag", "--onLoadMore--");
    }

}
