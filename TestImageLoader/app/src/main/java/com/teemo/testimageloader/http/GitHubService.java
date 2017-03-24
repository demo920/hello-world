package com.teemo.testimageloader.http;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Retrofit网络请求接口
 * Created by Asus on 2017/3/6.
 */

interface GitHubService {

    /**
     * 原始加载GitHub
     *
     * @param owner .
     * @param repo  .
     * @return .
     */
    @GET("repos/{owner}/{repo}/contributors")
    Call<List<Contributors>> contributors(
            @Path("owner") String owner,
            @Path("repo") String repo);

    /**
     * 豆瓣电影
     *
     * @param start .
     * @param count .
     * @return .
     */
    @GET("top250")
    Observable<MovieEntity> getTopMovie(@Query("start") int start, @Query("count") int count);

    /**
     * 豆瓣电影
     *
     * @param start .
     * @param count .
     * @return .
     */
    @GET("top250")
    Observable<HttpResult<List<MovieEntity>>> getTopMovie2(@Query("start") int start, @Query("count") int count);



    @Multipart
    @POST("{url}")
    Observable<ResponseBody> upLoadFile(
            @Path("url") String url,
            @Part("image\"; filename=\"image.jpg") RequestBody avatar);


    @POST("{url}")
    Observable<ResponseBody> uploadFiles(
            @Path("url") String url,
            @Path("headers") Map<String, String> headers,
            @Part("filename") String description,
            @PartMap()  Map<String, RequestBody> maps);

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);

    /**
     * 通过 List<MultipartBody.Part> 传入多个part实现多文件上传
     * @param parts 每个part代表一个
     * @return 状态信息
     */
    @Multipart
    @POST("users/image")
    Call<String> uploadFilesWithParts(@Part() List<MultipartBody.Part> parts);

    /**
     * 通过 MultipartBody和@body作为参数来上传
     * @param multipartBody MultipartBody包含多个Part
     * @return 状态信息
     */
    @POST("users/image")
    Call<String> uploadFileWithRequestBody(@Body MultipartBody multipartBody);


//    /**
//     * 测试post
//     * @param loginname .
//     * @param nloginpwd .
//     * @return .
//     */
//    @POST("user/submit.html")
//    Call<String> getString(@Query("loginname") String loginname,
//                           @Query("nloginpwd") String nloginpwd);
//
//    @Headers("Authorization: authorization")
//    Call getUser();
//
//    Call getUser2(@Header("Authorization") String authorization);
}
