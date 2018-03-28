package com.smartstudy.counselor_t.mvp.contract;

import com.smartstudy.counselor_t.entity.TeacherInfo;
import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.mvp.base.BaseView;

/**
 * @author yqy
 * @date on 2018/3/27
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface MyInfoDetailContract {
    interface View extends BaseView {

        void getAuditResult(TeacherInfo teacherInfo);

        void getLogOutSuccess();


    }

    interface Presenter extends BasePresenter {

        void getAuditResult();

        void getLogOut();

    }
}
