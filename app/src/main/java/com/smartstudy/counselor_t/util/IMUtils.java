package com.smartstudy.counselor_t.util;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.smartstudy.counselor_t.api.ApiManager;
import com.smartstudy.counselor_t.entity.ResponseInfo;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * @author louis
 * @date on 2018/1/17
 * @describe IM相关的工具类
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */

public class IMUtils {

    public static RongIMClient.ConnectCallback getConnectCallback() {
        RongIMClient.ConnectCallback connectCallback = new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                //获取token,成功后重新connect
                reGetToken();
            }

            @Override
            public void onSuccess(String s) {
                SPCacheUtils.put("imUserId", s);
            }

            @Override
            public void onError(final RongIMClient.ErrorCode e) {
                Log.d("im--err=======", e.getMessage());
            }
        };
        return connectCallback;
    }

    public static void reGetToken() {
        ApiManager.getApiService().refreshToken().subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        if (responseInfo.isSuccess()) {
                            reConn(JSON.parseObject(responseInfo.getData()).getString("imToken"));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private static void reConn(String token) {
        RongIM.connect(JSON.parseObject(token).getString("imToken"), new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                Log.d("im--err=======", "reToken still Incorrect");
            }

            @Override
            public void onSuccess(String s) {
                SPCacheUtils.put("imUserId", s);
            }

            @Override
            public void onError(RongIMClient.ErrorCode e) {
                Log.d("im--err=======", e.getMessage());
            }
        });
    }
}
