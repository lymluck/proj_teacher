package com.smartstudy.counselor_t.mvp.contract;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.smartstudy.counselor_t.entity.TeacherInfo;
import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.mvp.base.BaseView;

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

    }

    interface Presenter extends BasePresenter {

        void getAuditResult();

        void getLogOut();

        void showFragment(FragmentManager fragmentManager, int index);

        int currentIndex();
    }
}
