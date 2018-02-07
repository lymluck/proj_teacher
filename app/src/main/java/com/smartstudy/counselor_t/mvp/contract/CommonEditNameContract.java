package com.smartstudy.counselor_t.mvp.contract;

import android.widget.ImageView;

import com.smartstudy.counselor_t.entity.TeacherInfo;
import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.mvp.base.BaseView;

import java.io.File;

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
