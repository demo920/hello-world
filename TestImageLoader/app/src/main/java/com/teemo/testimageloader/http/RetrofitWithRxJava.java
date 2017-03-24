package com.teemo.testimageloader.http;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 网络请求管理类
 * 增加RxJava
 * <p>
 * https://gank.io/post/56e80c2c677659311bed9841
 * <p>
 * Created by Asus on 2017/3/6.
 */

public class RetrofitWithRxJava {

    private static final String TAG = "RetrofitWithRxJava";
    private static final String API_URL = "https://api.douban.com/v2/movie/";
    private static final long DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;
    private GitHubService service;

    //构造方法私有
    private RetrofitWithRxJava() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                //cookie设置
//                .cookieJar(new CookiesManager(app))
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        service = retrofit.create(GitHubService.class);
    }

    //在访问HttpManager时创建单例(静态内部类)
    private static class SingletonInstance {
        private static final RetrofitWithRxJava INSTANCE = new RetrofitWithRxJava();
    }

    //获取单例,供外部调用
    public static RetrofitWithRxJava getInstance() {
        return SingletonInstance.INSTANCE;
    }

    //方式一，初级调用RxJava
    public void testMovie(final Context context) {
        service.getTopMovie(0, 10)
                .subscribeOn(Schedulers.io())// 指定 subscribe() 发生在 IO 线程
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())// 指定 Subscriber 的回调发生在主线程
                .subscribe(new Subscriber<MovieEntity>() {
                    @Override
                    public void onCompleted() {
                        Log.e("TAG", "------onCompleted----------");
                        Toast.makeText(context, "Get Top Movie Completed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "onError-" + e.getMessage());
                    }

                    @Override
                    public void onNext(MovieEntity movieEntity) {
                        Log.e("TAG", "------onNext----------");
                        Log.e("TAG", movieEntity.toString());
                    }
                });
    }

    /********************************方式二,简易封装线程切换操作********************************************/
    //方式二,简易封装线程切换操作
    private void getMovie(final Context context) {
        Subscriber subscriber = new Subscriber<MovieEntity>() {
            @Override
            public void onCompleted() {
                Toast.makeText(context, "Get Top Movie Completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG", "onError-" + e.getMessage());
            }

            @Override
            public void onNext(MovieEntity movieEntity) {
                Log.e("TAG", movieEntity.toString());
            }
        };
        RetrofitWithRxJava.getInstance().getTopMovie(subscriber, 0, 10);
    }

    /**
     * 方式二
     * 用于获取豆瓣电影Top250的数据
     *
     * @param subscriber 由调用者传过来的观察者对象
     * @param start      起始位置
     * @param count      获取长度
     */
    public void getTopMovie(Subscriber<MovieEntity> subscriber, int start, int count) {
        service.getTopMovie(start, count)
                .subscribeOn(Schedulers.io())// 网络请求切换在io线程中调用
                .unsubscribeOn(Schedulers.io())// 取消网络请求放在io线程
                .observeOn(AndroidSchedulers.mainThread())// 观察后放在主线程调用
                .subscribe(subscriber);
    }
    /********************************方式二********************************************/


}
