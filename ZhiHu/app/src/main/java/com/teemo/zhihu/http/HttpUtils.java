package com.teemo.zhihu.http;


import com.teemo.zhihu.utils.LogUtils;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Administrator on 2016/9/5.
 */
public class HttpUtils {

    private static HttpInterface create;
    private static Call call;

    static {
        create = RetrofitHelper.getInstance().getRetrofit().create(HttpInterface.class);
        if (call != null) {
            call.cancel();
            call = null;
        }
    }


    /**
     * 获取启动图片
     */
    public static void getImage(String url, Callback callback) {
        String base = url.substring(0, url.lastIndexOf("/") + 1);
        String path = url.substring(url.lastIndexOf("/") + 1, url.length());
        LogUtils.e("Activity", "base:" + base + "\n" + "path:" + path);

        HttpInterface create = RetrofitHelper.getInstance().getRetrofit(base).create(HttpInterface.class);
        call = create.getImage(path);
        call.enqueue(callback);
    }


    /**
     * 1.查询启动图片
     */
    public static void startImage(Callback callback) {
        call = create.startImage();
        call.enqueue(callback);
    }


    /**
     * 3.最新消息
     */
    public static void latestNews(Callback callback) {
        call = create.latestNews();
        call.enqueue(callback);
    }


    /**
     * 消息内容获取与离线下载

     URL: http://news-at.zhihu.com/api/4/news/3892357
     */
    public static void news(int newsId,Callback callback) {
        call = create.news(newsId);
        call.enqueue(callback);
    }


}
