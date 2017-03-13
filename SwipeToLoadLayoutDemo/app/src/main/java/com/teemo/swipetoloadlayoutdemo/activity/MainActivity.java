package com.teemo.swipetoloadlayoutdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.teemo.swipetoloadlayoutdemo.R;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.MultiItemTypeAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.list_view)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        List<String> list = new ArrayList();
        for (int i = 0; i < 10; i++) {
            list.add("666");
            list.add("999");
        }

        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter(this, list);
        listView.setAdapter(adapter);

        listView.setAdapter(new CommonAdapter<String>(this, R.layout.adapter_main, list) {
            @Override
            protected void convert(ViewHolder viewHolder, String item, int position) {
                viewHolder.setText(R.id.tv_name, item);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(MainActivity.this, SwipeActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, SHActivity.class));
                        break;
                }
            }
        });
    }

}
