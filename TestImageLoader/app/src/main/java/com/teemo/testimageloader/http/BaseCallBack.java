package com.teemo.testimageloader.http;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 统一处理
 * Created by Asus on 2017/3/21.
 */

public abstract class BaseCallBack<T extends BaseCallModel> implements Callback<T> {
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.raw().isSuccessful()){
            Log.d("TAG","--isSuccessful--");
            onSuccess(response);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.e("BaseCallBack", "--failure--"+t.getMessage());
    }

    protected abstract void onSuccess(Response<T> response);
}
