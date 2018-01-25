package com.smartstudy.counselor_t.mvp.base;


import android.text.TextUtils;

import com.smartstudy.counselor_t.app.BaseApplication;
import com.smartstudy.counselor_t.entity.ResponseInfo;
import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.util.AppUtils;
import com.smartstudy.counselor_t.util.ConstantUtils;
import com.smartstudy.counselor_t.util.SPCacheUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 封装线程管理和订阅的过程
 * Created by louis on 18/1/2.
 */
public class BaseModel {

    protected void apiSubscribe(Observable observable, final ObserverListener listener) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        listener.onSubscribe(d);
                    }

                    @Override
                    public void onNext(ResponseInfo responseInfo) {
                        if (responseInfo.isSuccess()) {
                            listener.onNext(responseInfo.getData());
                        } else {
                            listener.onError(responseInfo.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!TextUtils.isEmpty(e.getMessage())) {
                            listener.onError(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    protected Map<String, String> getHeadersMap() {
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