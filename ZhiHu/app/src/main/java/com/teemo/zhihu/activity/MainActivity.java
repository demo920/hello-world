package com.teemo.zhihu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.teemo.zhihu.R;
import com.teemo.zhihu.adapter.TopStoryPagerAdapter;
import com.teemo.zhihu.http.HttpUtils;
import com.teemo.zhihu.model.LatestNews;
import com.teemo.zhihu.utils.Constant;
import com.teemo.zhihu.utils.LogUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String TAG = this.getClass().getSimpleName();

    private Context mContext;
    private List<LatestNews.StoriesBean> mDatas = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private String mDate;
    private ViewPager mViewPager;
    private List<LatestNews.TopStoriesBean> mTopDatas = new ArrayList<>();
    private LinearLayout mIndicatorParents;
    private TextView mTitle;
    private int mSelect = R.drawable.dot_select;
    private int mUnSelect = R.drawable.dot_unselect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);

        initView();

        requestNetData();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(getAdapter());
    }

    private void requestNetData() {
        HttpUtils.latestNews(new Callback<LatestNews>() {
            @Override
            public void onResponse(Call<LatestNews> call, Response<LatestNews> response) {
                mDatas.addAll(response.body().getStories());
                mTopDatas.addAll(response.body().getTop_stories());
                mDate = response.body().getDate();
                LogUtils.i(TAG, "----onResponse:" + response.body().toString() + "\n--mDatas.size():" + mDatas.size());

                mRecyclerView.getAdapter().notifyDataSetChanged();
                mViewPager.setAdapter(new TopStoryPagerAdapter(mContext, mTopDatas));
                initIndicator();
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public RecyclerView.Adapter getAdapter() {
        CommonAdapter adapter = new CommonAdapter<LatestNews.StoriesBean>(mContext, R.layout.adapter_main, mDatas) {
            @Override
            protected void convert(ViewHolder holder, final LatestNews.StoriesBean storiesBean, int position) {
                ImageView img = holder.getView(R.id.iv_img);
                if (storiesBean.getImages().size() >= 1) {
                    Glide.with(mContext).load(storiesBean.getImages().get(0)).into(img);
                }
                holder.setText(R.id.tv_title, storiesBean.getTitle());
                holder.setOnClickListener(R.id.adapter_main, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, NewsActivity.class);
                        intent.putExtra(Constant.NEWS_ID,storiesBean.getId());
                        startActivity(intent);
                    }
                });
            }
        };

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View headView = inflater.inflate(R.layout.viewpager, null);
        mViewPager = (ViewPager) headView.findViewById(R.id.vp_top_stories);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTitle.setText(mTopDatas.get(position).getTitle());
                switchBannerIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mTitle = (TextView) headView.findViewById(R.id.tv_title);
        mIndicatorParents = (LinearLayout) headView.findViewById(R.id.dots_parent);
        headView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        HeaderAndFooterWrapper header = new HeaderAndFooterWrapper(adapter);
        header.addHeaderView(headView);

        return header;
    }

    private void initIndicator() {
        if (mIndicatorParents != null) {
            for (int i = 0; i < mTopDatas.size(); i++) {
                // 导航点直接的间距
                int margin = 20;

                // 设置未选中的也没的导航点的图片
                ImageView view = new ImageView(mViewPager.getContext());
                view.setBackgroundResource(mUnSelect);

                // 设置图片的属性
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                if (i < mTopDatas.size() - 1) {
                    params.rightMargin = margin;
                }

                // 将导航点加入到容器中
                mIndicatorParents.addView(view, params);
            }
            // 单独设置选中的导航点的图片
            mIndicatorParents.getChildAt(0).setBackgroundResource(mSelect);
        }
        mTitle.setText(mTopDatas.get(0).getTitle());
    }

    /**
     * 设置指示器当前页码
     */
    private void switchBannerIndicator(int index) {
        if (mIndicatorParents != null) {
            for (int i = 0; i < mIndicatorParents.getChildCount(); i++) {
                View view = mIndicatorParents.getChildAt(i);
                if (i == index) {
                    view.setBackgroundResource(mSelect);
                } else {
                    view.setBackgroundResource(mUnSelect);
                }
            }
        }
    }


}
