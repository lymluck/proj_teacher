package com.smartstudy.counselor_t.listener;

import io.reactivex.disposables.Disposable;

/**
 * Created by louis on 2018/1/3.
 */

public interface ObserverListener<T> {

    void onSubscribe(Disposable disposable);

    void onNext(T t);

    void onError(String msg);
}
