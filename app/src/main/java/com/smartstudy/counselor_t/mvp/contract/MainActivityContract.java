package com.smartstudy.counselor_t.mvp.contract;


import study.smart.baselib.entity.TeacherInfo;
import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.mvp.base.BaseView;

/**
 * @author yqy
 * @date on 2018/1/12
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface MainActivityContract {
    interface View extends BaseView {

        void getAuditResult(TeacherInfo teacherInfo);

        void getLogOutSuccess();

        void updateable(String downUrl, String version, String des);

        void forceUpdate(String downUrl, String version, String des);

    }

    interface Presenter extends BasePresenter {

        void getAuditResult();

        void getLogOut();

        void checkVersion();

    }
}
