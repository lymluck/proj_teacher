package com.smartstudy.counselor_t.mvp.contract;

import study.smart.baselib.entity.TeacherInfo;
import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.mvp.base.BaseView;
import com.smartstudy.counselor_t.entity.IdNameInfo;

import java.io.File;
import java.util.List;

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

        void getOptionsSuccess(List<IdNameInfo> workIdNameInfos, List<IdNameInfo> adeptIdNameInfos);
    }

    interface Presenter extends BasePresenter {

        void postPersonInfo(String name, File avatar, String title, String school, String yearsOfWorking,
                            String email, String realName, String workingCityKey, String adeptWorksKey, String introdction);

        void getAuditResult();

        void getOptions();
    }
}
