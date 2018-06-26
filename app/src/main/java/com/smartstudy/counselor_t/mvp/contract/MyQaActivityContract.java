package com.smartstudy.counselor_t.mvp.contract;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import study.smart.baselib.entity.TeacherInfo;
import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.mvp.base.BaseView;


/**
 * @author yqy
 * @date on 2018/3/20
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface MyQaActivityContract {
    interface View extends BaseView {

        void getAuditResult(TeacherInfo teacherInfo);

        void getLogOutSuccess();

        void hideFragment(FragmentManager fragmentManager);

        void showQaFragment(FragmentTransaction ft);

        void showMyQaFragment(FragmentTransaction ft);

        void showMyFocus(FragmentTransaction ft);

        void updateable(String downUrl, String version, String des);

        void forceUpdate(String downUrl, String version, String des);

    }

    interface Presenter extends BasePresenter {

        void getAuditResult();

        void getLogOut();

        void showFragment(FragmentManager fragmentManager, int index);

        int currentIndex();

        void checkVersion();
    }
}
