package com.smartstudy.counselor_t.mvp.contract;


import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.mvp.base.BaseView;

/**
 * Created by louis on 2017/3/4.
 */

public interface LoginActivityContract {
    interface View extends BaseView {

        void getPhoneCodeSuccess();

        void phoneCodeLoginSuccess(boolean created);
    }

    interface Presenter extends BasePresenter {

        void getPhoneCode(String phone);

        void phoneCodeLogin(String phone, String code);
    }
}
