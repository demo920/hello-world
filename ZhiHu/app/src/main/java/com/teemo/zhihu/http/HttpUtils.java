package com.teemo.zhihu.http;


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
     * 1.查询启动图片
     */
    public static void start_image(Callback callback) {
        call = create.startImage();
        call.enqueue(callback);
    }


}
