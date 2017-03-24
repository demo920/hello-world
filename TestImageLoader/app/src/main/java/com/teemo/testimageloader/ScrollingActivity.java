package com.teemo.testimageloader;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.teemo.testimageloader.database.Test;

import java.util.ArrayList;
import java.util.List;

public class ScrollingActivity extends AppCompatActivity {

    private static final String IMAGE_URL = "http://wx4.sinaimg.cn/large/63885668ly1fcxt31gn9gj21rw16lhdu.jpg";
    private static final String IMAGE_URL_2 = "http://ww3.sinaimg.cn/large/63885668gw1f72h4vzpckj21mo134kjm.jpg";
    private static final String IMAGE_URL_3 = "http://ww2.sinaimg.cn/large/63885668gw1f72gy7ai42j20rt15ob2a.jpg";
    private static final String IMAGE_URL_4 = "http://ww3.sinaimg.cn/large/63885668gw1f6xqh9jy09j20xc1e0npd.jpg";
    private static final String IMAGE_URL_5 = "http://wx2.sinaimg.cn/large/5033b6dbgy1fc5c38wofsj21l52c0x6t.jpg";
    private static final String IMAGE_URL_6 = "http://ww2.sinaimg.cn/large/dca7153bjw1ehrcw42lghj20go0nx0wu.jpg";
    private static final String IMAGE_URL_7 = "http://wx1.sinaimg.cn/large/dca7153bly1fah3zf4lssj218w0u0hb80.jpg";
    private static final String IMAGE_URL_8 = "http://ww3.sinaimg.cn/large/dca7153bjw1euarx6nxfrj20et0m8q4b.jpg";
    private static final String IMAGE_URL_9 = "http://ww3.sinaimg.cn/large/006fPIxLgw1f9hb7anfqlj30ku0rs437.jpg";
    private static final String IMAGE_URL_10 = "https://photos.google.com/share/AF1QipOpaKtS9C-Bs17Ahjq4Ob8IHuCwpYpHpmfSjE23o3TVVXfxj9-L95kK5xhbw_jXpw?key=T3Zicjd3Qlg0YUNuREFFdW5GZTNELWNYSFV0QnlB";

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_scrolling);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
//                RetrofitWithRxJava.getInstance().testMovie(mContext);
//                RetrofitUtil.getInstance().testGithub();
                Test.databaseTest();
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<String> lists = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            lists.add(IMAGE_URL);
            lists.add(IMAGE_URL_2);
            lists.add(IMAGE_URL_3);
            lists.add(IMAGE_URL_4);
            lists.add(IMAGE_URL_5);
            lists.add(IMAGE_URL_6);
            lists.add(IMAGE_URL_7);
            lists.add(IMAGE_URL_8);
            lists.add(IMAGE_URL_9);
            lists.add(IMAGE_URL_10);
        }
        recyclerView.setAdapter(new TestAdapter(this, lists, new TestAdapter.Listener() {
            @Override
            public void onImageClicked(View view, String drawable) {
                transition(view, drawable);
            }
        }));

    }

    private void transition(View view, String url) {
        if (Build.VERSION.SDK_INT < 21) {
            Toast.makeText(this, "21+ only, keep out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ActivityTransitionToActivity.class);
            intent.putExtra("bitmap", url);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, ActivityTransitionToActivity.class);
            intent.putExtra("bitmap", url);
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(this, view, getString(R.string.transition_test));
            startActivity(intent, options.toBundle());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
