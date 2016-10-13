package com.teemo.zhihu.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.teemo.zhihu.R;
import com.teemo.zhihu.activity.NewsActivity;
import com.teemo.zhihu.model.LatestNews;
import com.teemo.zhihu.utils.Constant;
import com.teemo.zhihu.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页面顶部循环广告
 * Created by admin on 2016/10/12.
 */

public class TopStoryPagerAdapter extends PagerAdapter {

    List<ImageView> views = new ArrayList<>();
    private Context mContext;

    public TopStoryPagerAdapter(Context context, List<LatestNews.TopStoriesBean> mDatas) {
        mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        LogUtils.e("Activity", "--size:" + mDatas.size());
        if (mDatas == null) {
            return;
        }
//        ImageView imageView = (ImageView) group.findViewById(R.id.iv_top);
//        TextView title = (TextView) group.findViewById(R.id.tv_top);
        for (final LatestNews.TopStoriesBean bean : mDatas) {
            ImageView group = (ImageView) inflater.inflate(R.layout.image, null);
            group.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, NewsActivity.class);
                    intent.putExtra(Constant.NEWS_ID, bean.getId());
                    mContext.startActivity(intent);
                }
            });
            //将要分页显示的View装入数组中
//            title.setText(bean.getTitle());
            Glide.with(context).load(bean.getImage()).into(group);
            views.add(group);
        }
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position));
        return views.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }


}
