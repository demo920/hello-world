package com.teemo.testimageloader.image;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

/**
 * Created by Asus on 2017/3/28.
 */

public class GlideWrapper implements ImageLoaderWrapper {

    @Override
    public void displayImage(Context context, ImageView imageView, String url, DisplayOption option) {

        Glide.with(context)
                .load(url)
//                .override(768, 1280)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
//                .centerCrop()
//                .animate(R.anim.pic_anim)
//                .thumbnail(Glide.with(app)
//                        .load("http://wx4.sinaimg.cn/large/63885668ly1fcxt31gn9gj21rw16lhdu.jpg"))
//                .thumbnail((float) 0.1)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .transform(new GlideCircleTransform(app))
                .dontAnimate()
                .into(imageView);
    }

    @Override
    public void displayImage(Context context, ImageView imageView, File imageFile, DisplayOption option) {

    }
}
