package com.teemo.testimageloader.image;

import android.content.Context;
import android.widget.ImageView;

import java.io.File;

/**
 * 图片加载功能接口
 * Created by Asus on 2017/3/28.
 */
public interface ImageLoaderWrapper {

    /**
     * 显示图片
     *
     * @param imageView 显示图片的ImageView
     * @param imageUrl  图片资源的URL
     * @param option    显示参数设置
     */
    void displayImage(Context context, ImageView imageView, String imageUrl, DisplayOption option);

    /**
     * 显示 图片
     *
     * @param imageView 显示图片的ImageView
     * @param imageFile 图片文件
     * @param option    显示参数设置
     */
    void displayImage(Context context, ImageView imageView, File imageFile, DisplayOption option);

    /**
     * 图片加载参数
     */
    class DisplayOption {
        /**
         * 加载中的资源id
         */
        public int loadingResId;
        /**
         * 加载失败的资源id
         */
        public int loadErrorResId;
    }

}
