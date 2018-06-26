package com.smartstudy.counselor_t.mvp.contract;


import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.mvp.base.BaseView;

/**
 * Created by louis on 2017/3/4.
 */

public interface SplashContract {

    interface View extends BaseView {

        void getAdSuccess(String result);
    }

    interface Presenter extends BasePresenter {

        void getAdInfo();

    }
}
