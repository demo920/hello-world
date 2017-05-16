package com.teemo.testimageloader.image;

/**
 * ImageLoader工厂类
 * Created by Asus on 2017/3/28.
 */

public class ImageLoaderFactory {
    private static volatile ImageLoaderWrapper sInstance;

    private enum WrapperType {
        Type_UIL, Type_Glide, Type_Picasso
    }

    private ImageLoaderFactory() {

    }

    /**
     * 获取图片加载器
     *
     * @return
     */
    public static ImageLoaderWrapper getLoader() {
        return getLoader(WrapperType.Type_Glide);
    }

    private static ImageLoaderWrapper getLoader(WrapperType type) {
        if (sInstance == null) {
            synchronized (ImageLoaderFactory.class) {
                if (sInstance == null) {
                    switch (type) {
                        case Type_UIL:
                            sInstance = new AndroidUILWrapper();
                            break;
                        case Type_Glide:
                            sInstance = new GlideWrapper();
                            break;
                        case Type_Picasso:
                            break;
                    }
                }
            }
        }
        return sInstance;
    }
}
