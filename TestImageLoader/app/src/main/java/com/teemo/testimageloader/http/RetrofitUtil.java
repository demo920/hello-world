package com.teemo.testimageloader.http;

import android.os.Environment;
import android.util.Log;

import com.teemo.testimageloader.BuildConfig;
import com.teemo.testimageloader.MyApplication;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 原始方法
 * Created by Asus on 2017/3/17.
 */

public class RetrofitUtil {
    public static final String API_URL = "https://api.github.com/";
    private static final long DEFAULT_TIMEOUT = 5000;

    // 双重检查锁定不是线程安全的，如果要用这种方式，需要使用volatile关键字。
    private static volatile RetrofitUtil mInstance = null;

    private OkHttpClient.Builder mBuild;
    private GitHubService mApiService;

    /**
     * 私有构造器，初始化
     */
    private RetrofitUtil() {
        initHttpClient();
        Retrofit mRetrofit = new Retrofit.Builder()
                .client(mBuild.build())
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mApiService = mRetrofit.create(GitHubService.class);
    }

    //双重检查
    public static RetrofitUtil getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitUtil.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitUtil();
                }
            }
        }
        return mInstance;
    }

    private GitHubService getApiService() {
        return mApiService;
    }

    /**
     * 初始化client,定义log拦截器
     */
    private void initHttpClient() {
        mBuild = new OkHttpClient.Builder();
        mBuild.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        .cache(new Cache(MyApplication.app.getExternalFilesDir(Environment.DIRECTORY_MOVIES),10*1024*104));

        //定义log拦截器
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.i("Logging", message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (BuildConfig.DEBUG) {
            mBuild.addInterceptor(interceptor);
        }
        mBuild.addNetworkInterceptor(new CacheInterceptor());
    }

    /**
     * 原始
     */
    public void testGithub() {
        GitHubService github = RetrofitUtil.getInstance().getApiService();
        Call<List<Contributors>> call = mApiService.contributors("square", "retrofit");
        call.enqueue(new Callback<List<Contributors>>() {
            @Override
            public void onResponse(Call<List<Contributors>> call, Response<List<Contributors>> response) {

                Log.d("TAG", "testNet: " + "--ok--");
                for (Contributors object : response.body()) {
                    Log.d("TAG", "testNet: " + object.toString() + "/n");
                }
            }

            @Override
            public void onFailure(Call<List<Contributors>> call, Throwable t) {
                Log.d("TAG", "testNet: " + "--failure--");
            }
        });

    }

//    public static class SingletonInstance {
//        private static final RetrofitUtil instance = new RetrofitUtil();
//    }

//    //静态内部类
//    public static RetrofitUtil getInstance() {
//        return RetrofitUtil.SingletonInstance.instance;
//    }


}
