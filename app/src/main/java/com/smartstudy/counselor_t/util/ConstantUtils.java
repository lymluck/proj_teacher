package com.smartstudy.counselor_t.util;

/**
 * Created by louis on 2018/1/2.
 */

public class ConstantUtils {

    //只查询网络数据
    public static final String ONLY_NETWORK = "1";
    //只查询本地缓存
    public static final String ONLY_CACHED = "2";
    //先查询本地缓存，如果本地没有，再查询网络数据
    public static final String CACHED_ELSE_NETWORK = "3";
    //先查询网络数据，如果没有，再查询本地缓
    public static final String NETWORK_ELSE_CACHED = "4";
    //短信验证倒计时
    public static final long SMS_TIMEOUT = 30 * 1000;
    //登录请求码
    public static final int REQUEST_CODE_LOGIN = 15;
    public static final String API_SERVER = "api_server";
    public static final String CACHE_NULL = "xxd_null";
}
