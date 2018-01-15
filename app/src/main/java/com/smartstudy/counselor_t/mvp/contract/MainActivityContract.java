package com.smartstudy.counselor_t.mvp.contract;

import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.mvp.base.BaseView;
import com.smartstudy.counselor_t.entity.StudentInfo;

/**
 * @author yqy
 * @date on 2018/1/12
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface MainActivityContract {
    interface View extends BaseView {

        void getStudentInfoSuccess(String mUserId,StudentInfo studentInfo);

    }

    interface Presenter extends BasePresenter {

        void getStudentInfo(String userId);

    }
}
