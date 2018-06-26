package study.smart.baselib.mvp.contract;


import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.mvp.base.BaseView;

/**
 * Created by louis on 2017/3/4.
 */

public interface LoginActivityContract {
    interface View extends BaseView {

        void getPhoneCodeSuccess();

        void phoneCodeLoginSuccess(int status);

        void toFillInfo();

    }

    interface Presenter extends BasePresenter {

        void getPhoneCode(String phone);

        void phoneCodeLogin(String phone, String code);
    }
}
