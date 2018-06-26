package com.smartstudy.counselor_t.mvp.contract;


import study.smart.baselib.entity.StudentPageInfo;
import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.mvp.base.BaseView;

/**
 * @author yqy
 * @date on 2018/1/15
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface StudentActivityContract {
    interface View extends BaseView {

        void getStudentInfoDetailSuccess(StudentPageInfo studentInfo);

    }

    interface Presenter extends BasePresenter {

        void getStudentDetailInfo(String userId);

    }
}
