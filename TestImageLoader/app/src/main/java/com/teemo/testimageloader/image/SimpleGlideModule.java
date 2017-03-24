package com.teemo.testimageloader.image;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;

import java.io.File;

/**
 * 定制 Glide
 * Glide 会扫描 AndroidManifest.xml 为 Glide module 的 meta 声明
 * 如果你想删掉 Glide Module，只需要把它从 AndroidManifest.xml 中移除就可以了。
 * Java 类可以保存，说不定以后会用呢。如果它没有在 AndroidManifest.xml 中被引用，那它不会被加载或被使用。
 * <p>
 * Created by Asus on 2017/3/15.
 */

public class SimpleGlideModule implements GlideModule {
    @Override
    public void applyOptions(final Context context, GlideBuilder builder) {

        /**
         * 使用实例：增加 Glide 的图片质量
         * 在 Android 中有两个主要的方法对图片进行解码：ARGB8888 和 RGB565。
         * 前者为每个像素使用了 4 个字节，后者仅为每个像素使用了 2 个字节。
         * ARGB8888 的优势是图像质量更高以及能存储一个 alpha 通道。
         * Picasso 使用 ARGB8888，
         * Glide 默认使用低质量的 RGB565。
         * 对于 Glide 使用者来说：你使用 Glide module 方法去改变解码规则。
         */
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);

        /**
         *   .setMemoryCache(MemoryCache memoryCache)
         *   .setBitmapPool(BitmapPool bitmapPool)
         *   .setDiskCache(DiskCache.Factory diskCacheFactory)
         *   .setDiskCacheService(ExecutorService service)
         *   .setResizeService(ExecutorService service)
         *   .setDecodeFormat(DecodeFormat decodeFormat)
         *   你可以看到，这个 GlideBuilder 对象给你访问了 Glide 重要的核心组件。在这个博客中使用的方法，你可以改变磁盘缓存，内存缓存等等！
         *
         */
        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();
//        Log.e("Log", "--- " + defaultMemoryCacheSize + " , " + defaultBitmapPoolSize);

        //自定义内存缓存
        builder.setMemoryCache(new LruResourceCache((int) (defaultMemoryCacheSize * 0.5)));
        builder.setBitmapPool(new LruBitmapPool((int) (defaultBitmapPoolSize * 0.5)));

        //自定义磁盘缓存  磁盘缓存也可以位于外部存储，公有目录
        //不能一起设置这两个为之。Glide 为这两个选项都提供了它的实现：InternalCacheDiskCacheFactory 和 ExternalCacheDiskCacheFactory。
        final int cacheSize100MegaBytes = 104857600;

        //磁盘缓存到外部存储（设置了最大大小为 100M）
//        builder.setDiskCache(
//                new ExternalCacheDiskCacheFactory(app, cacheSize100MegaBytes)
//        );

        builder.setDiskCache(new DiskCache.Factory() {
            @Override
            public DiskCache build() {
                File directory = new File(context.getExternalCacheDir(),"image");
                directory.mkdirs();
                return DiskLruCacheWrapper.get(directory,cacheSize100MegaBytes);
            }
        });
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

        /**
         * 这周我们会用另外一个方法 registerComponents()，去设置不同的网络库。
         * 默认情况下，Glide 内部使用了标准的 HTTPURLConnection 去下载图片。
         *
         */

    }
}
