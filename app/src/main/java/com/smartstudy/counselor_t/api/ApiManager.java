package com.smartstudy.counselor_t.api;

import android.text.TextUtils;

import com.smartstudy.counselor_t.app.BaseApplication;
import com.smartstudy.counselor_t.converter.FastJsonConverterFactory;
import com.smartstudy.counselor_t.server.okhttpcache.HttpCommonInterceptor;
import com.smartstudy.counselor_t.util.AppUtils;
import com.smartstudy.counselor_t.util.ConstantUtils;
import com.smartstudy.counselor_t.util.DeviceUtils;
import com.smartstudy.counselor_t.util.HttpUrlUtils;
import com.smartstudy.counselor_t.util.SDCardUtils;
import com.smartstudy.counselor_t.util.SPCacheUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
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

        // 添加公共参数拦截器
        HttpCommonInterceptor commonInterceptor = new HttpCommonInterceptor.Builder()
                .addHeaderParams(getHttpHeaderParams())
                .build();


        File cache_file = SDCardUtils.getFileDirPath( "Xxd_im" + File.separator + "cache");
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
        ClientBuilder.addInterceptor(commonInterceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HttpUrlUtils.getBaseUrl())
                .client(ClientBuilder.build())
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    /**
     * 请求参数中加入公共header参数
     *
     * @return
     */
    private Map<String, String> getHttpHeaderParams() {
        Map<String, String> params = new HashMap<>();
        params.put("User-Agent", AppUtils.getUserAgent(AppUtils.getAndroidUserAgent(BaseApplication.getInstance())) + " Store/"
                + "xxd");
        params.put("X-xxd-uid", "00000000");
        String ticket = (String) SPCacheUtils.get("ticket", ConstantUtils.CACHE_NULL);
        if (!TextUtils.isEmpty(ticket) && !ConstantUtils.CACHE_NULL.equals(ticket)) {
            params.put("X-xxd-counsellor-ticket", ticket);
        }
        return params;
    }
}
