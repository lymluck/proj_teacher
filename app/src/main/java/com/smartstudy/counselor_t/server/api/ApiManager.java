package com.smartstudy.counselor_t.server.api;

import com.smartstudy.counselor_t.converter.FastJsonConverterFactory;
import com.smartstudy.counselor_t.server.okhttpcache.interceptor.CacheAndCookieInterceptor;
import com.smartstudy.counselor_t.util.HttpUrlUtils;
import com.smartstudy.counselor_t.util.SDCardUtils;
import com.smartstudy.counselor_t.util.Utils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by louis on 2018/1/3.
 */

public class ApiManager {

    private static ApiService apiService;

    //单例
    public static ApiService getApiService() {
        if (apiService == null) {
            synchronized (ApiManager.class) {
                if (apiService == null) {
                    new ApiManager();
                }
            }
        }
        return apiService;
    }

    private ApiManager() {

        File cache_file = SDCardUtils.getFileDirPath("Xxd_im" + File.separator + "cache");
        int cache_size = 150 * 1024 * 1024;
        Cache cache = new Cache(cache_file, cache_size);
        OkHttpClient.Builder ClientBuilder = new OkHttpClient.Builder();
        //读取超时
        ClientBuilder.readTimeout(30, TimeUnit.SECONDS);
        //连接超时
        ClientBuilder.connectTimeout(5, TimeUnit.SECONDS);
        //写入超时
        ClientBuilder.writeTimeout(30, TimeUnit.SECONDS);
        ClientBuilder.cache(cache);
        ClientBuilder.retryOnConnectionFailure(true);
        ClientBuilder.addInterceptor(new CacheAndCookieInterceptor());
        if (Utils.isApkInDebug()) {
            ClientBuilder.addInterceptor(new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY));
        }
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(HttpUrlUtils.getBaseUrl())
            .client(ClientBuilder.build())
            .addConverterFactory(FastJsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
        apiService = retrofit.create(ApiService.class);
    }
}
