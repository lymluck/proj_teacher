package com.smartstudy.counselor_t.mvp.contract;

import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.mvp.base.BaseView;

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
