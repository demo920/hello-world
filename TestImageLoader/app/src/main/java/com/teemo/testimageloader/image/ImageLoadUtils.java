package com.teemo.testimageloader.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Asus on 2017/3/13.
 */

public class ImageLoadUtils {

    public static void loadImage(Context context, String url, ImageView imageView) {
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

    /**
     * 从指定URL获取图片
     *
     * @param url
     * @return
     */
    public static Bitmap getImageBitmap(String url) {
        URL imgUrl = null;
        Bitmap bitmap = null;
        try {
            imgUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
