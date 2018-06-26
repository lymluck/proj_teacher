package com.smartstudy.counselor_t.mvp.contract;


import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.mvp.base.BaseView;

import java.io.File;

/**
 * @author yqy
 * @date on 2018/3/9
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface ReloadQaContract {

    interface View extends BaseView {
        void postAnswerSuccess();

        void postAnswerFail(String message);
    }

    interface Presenter extends BasePresenter {
        void postAnswerVoice(String id, File voice);
    }
}
