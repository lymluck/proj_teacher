package com.smartstudy.counselor_t.mvp.contract;


import study.smart.baselib.entity.TeacherInfo;
import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.mvp.base.BaseView;

/**
 * @author yqy
 * @date on 2018/2/7
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface CommonEditNameContract {

    interface View extends BaseView {

        void updateMyInfoSuccesee(TeacherInfo teacherInfo);

    }

    interface Presenter extends BasePresenter {

        void updateMyInfo(TeacherInfo teacherInfo);

    }
}
