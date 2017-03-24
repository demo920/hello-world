package com.teemo.testimageloader.http;

/**
 * 相同格式的Http请求数据该如何封装
 * RxJava+Retrofit，在联网返回后如何先进行统一的判断
 * <p>
 * 统一处理结果
 * <p>
 * Created by Asus on 2017/3/17.
 */

public class HttpResult<T> {
//    private int resultCode;
//    private String resultMessage;
//
//    private T data;

    //用来模仿resultCode和resultMessage
    private int count;
    private int start;
    private int total;
    private String title;

    //用来模仿Data
    private T subjects;

}
