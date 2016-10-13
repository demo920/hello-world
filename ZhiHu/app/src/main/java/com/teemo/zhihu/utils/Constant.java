package com.teemo.zhihu.utils;

/**
 * Created by admin on 2016/8/11.
 */

public interface Constant {

    /**----------------------------------------------------------------------**/

    String NEWS_ID = "new_Id";

    /**----------------------------------------------------------------------**/

    /**
     * 服务器地址
     */
    String BASE_URL = "http://news-at.zhihu.com/api/4/";

    /**
     * 1. 启动界面图像获取
     * URL: http://news-at.zhihu.com/api/4/start-image/1080*1776
     */
    String start_image = "start-image/1080*1776";

    /**
     * 3. 最新消息
     * URL: http://news-at.zhihu.com/api/4/news/latest
     */
    String latest_news = Constant.NEWS + "latest";

    /**
     * 4.消息内容获取与离线下载
     * URL: http://news-at.zhihu.com/api/4/news/3892357
     */
    String NEWS = "news/";

    /**
     * 5.过往消息
     * URL: http://news.at.zhihu.com/api/4/news/before/20131119
     */
    String BEFORE = "before/";
}



