package com.smartstudy.counselor_t.mvp.presenter;

import android.text.TextUtils;

import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BasePresenterImpl;
import com.smartstudy.counselor_t.mvp.contract.SplashContract;
import com.smartstudy.counselor_t.mvp.model.SplashModel;

import io.reactivex.disposables.Disposable;

/**
 * Created by louis on 2017/3/4.
 */

public class SplashPresenter extends BasePresenterImpl<SplashContract.View> implements SplashContract.Presenter {

    private SplashModel splashModel;

    public SplashPresenter(SplashContract.View view) {
        super(view);
        splashModel = new SplashModel();
    }

    @Override
    public void getAdInfo() {
        splashModel.getAdInfo(new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String result) {
                if (!TextUtils.isEmpty(result)) {
                    view.getAdSuccess(result);
                }
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void detach() {
        super.detach();
        splashModel = null;
    }

}
