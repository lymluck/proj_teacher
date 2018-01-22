package com.smartstudy.counselor_t.mvp.contract;

import com.smartstudy.counselor_t.entity.StudentPageInfo;
import com.smartstudy.counselor_t.entity.TeacherInfo;
import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.mvp.base.BaseView;

/**
 * @author yqy
 * @date on 2018/1/22
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface MyInfoContract {

    interface View extends BaseView {

        void getMyInfoSuccess(TeacherInfo teacherInfo);

        void getLogOutSuccess();

    }

    interface Presenter extends BasePresenter {

        void getMyInfo();

        void getLogOut();
    }
}
