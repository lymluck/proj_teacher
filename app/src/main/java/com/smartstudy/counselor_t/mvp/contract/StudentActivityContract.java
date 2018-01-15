package com.smartstudy.counselor_t.mvp.contract;

import com.smartstudy.counselor_t.entity.StudentInfo;
import com.smartstudy.counselor_t.entity.StudentPageInfo;
import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.mvp.base.BaseView;

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
