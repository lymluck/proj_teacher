package study.smart.baselib.mvp.base;

import android.text.TextUtils;
import android.util.Log;

import study.smart.baselib.BaseApplication;
import study.smart.baselib.entity.ResponseInfo;
import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.utils.AppUtils;
import study.smart.baselib.utils.ConstantUtils;
import study.smart.baselib.utils.DeviceUtils;
import study.smart.baselib.utils.SPCacheUtils;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
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

    protected Observable fileObservalbe(Observable observable) {
        return observable.subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }

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
                    if (responseInfo != null) {
                        Log.d("result======", responseInfo.toString());
                    }
                    if (responseInfo.isSuccess()) {
                        listener.onNext(responseInfo.getData());
                    } else {
                        listener.onError(responseInfo.getMsg());
                    }
                }

                @Override
                public void onError(Throwable e) {
                    Log.d("err======", e.getMessage());
                    if (!TextUtils.isEmpty(e.getMessage())) {
                        Log.d("err======", e.getMessage());
                        listener.onError(e.getMessage());
                    }
                }

                @Override
                public void onComplete() {
                    Log.d("complete=====", "");
                }
            });
    }

    protected Map<String, String> getHeadersMap() {
        Map<String, String> params = new HashMap<>();
        params.put("User-Agent", AppUtils.getUserAgent(AppUtils.getAndroidUserAgent(BaseApplication.getInstance())) + " Store/"
            + "xxd");
        params.put("x-smartsa-uid", DeviceUtils.getUniquePsuedoID());
        params.put("x-smartsa-push-reg-id", JPushInterface.getRegistrationID(BaseApplication.appContext));
        String ticket = (String) SPCacheUtils.get("ticket", ConstantUtils.CACHE_NULL);
        if (!TextUtils.isEmpty(ticket) && !ConstantUtils.CACHE_NULL.equals(ticket)) {
            params.put("x-smartsa-counsellor-ticket", ticket);
        }
        String smartTicket = (String) SPCacheUtils.get("smart_ticket", ConstantUtils.CACHE_NULL);
        if (!TextUtils.isEmpty(smartTicket) && !ConstantUtils.CACHE_NULL.equals(ticket)) {
            params.put("x-smartsa-ticket", smartTicket);
        }
        return params;
    }

}
