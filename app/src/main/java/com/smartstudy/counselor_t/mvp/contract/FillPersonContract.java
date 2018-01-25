package com.smartstudy.counselor_t.mvp.contract;

import com.smartstudy.counselor_t.entity.StudentPageInfo;
import com.smartstudy.counselor_t.entity.TeacherInfo;
import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.mvp.base.BaseView;
import com.smartstudy.counselor_t.ui.activity.FillPersonActivity;

import java.io.File;

/**
 * @author yqy
 * @date on 2018/1/18
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface FillPersonContract {
    interface View extends BaseView {

        void getStudentInfoDetailSuccess();

        void showAuditResult(TeacherInfo teacherInfo);
    }

    interface Presenter extends BasePresenter {

        void postPersonInfo(String name, File avatar, String title, String school, String yearsOfWorking, String email, String realName);

        void getAuditResult();
    }
}
