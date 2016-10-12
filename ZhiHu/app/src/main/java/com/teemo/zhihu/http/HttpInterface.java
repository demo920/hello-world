package com.teemo.zhihu.http;

import com.teemo.zhihu.model.StartImage;
import com.teemo.zhihu.utils.Constant;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * retrofit 配置网络请求接口
 * Created by Administrator on 2016/10/11.
 */
interface HttpInterface {

    @GET(Constant.start_image)
    Call<StartImage> startImage();


}
