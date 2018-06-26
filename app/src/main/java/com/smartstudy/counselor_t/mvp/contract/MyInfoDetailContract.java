package com.smartstudy.counselor_t.mvp.contract;

import android.widget.ImageView;

import study.smart.baselib.entity.TeacherInfo;
import study.smart.baselib.entity.TokenBean;
import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.mvp.base.BaseView;
import com.smartstudy.counselor_t.entity.IdNameInfo;

import java.io.File;
import java.util.List;

/**
 * @author yqy
 * @date on 2018/3/27
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface MyInfoDetailContract {
    interface View extends BaseView {

        void getMyInfoSuccess(TeacherInfo teacherInfo);

        void updateMyAvatarSuccesee();

        void getLogOutSuccess();

        void getOptionsSuccess(List<IdNameInfo> workIdNameInfos, List<IdNameInfo> adeptIdNameInfos);

        void updateMyInfoSuccess();

        void refreshSuccess(TokenBean tokenBean);

        void updateVideoUrlSuccess();

        void updateFail(String message);
    }

    interface Presenter extends BasePresenter {

        void getMyInfo();

        void updateMyAvatarInfo(File avatar, ImageView ivAvatar);

        void getLogOut();

        void getOptions();

        void updateMyInfo(String name, File avatar, String title, String school, String yearsOfWorking,
                          String email, String realName, String introduction, String workingCityKey, String adeptWorksKey);

        void refreshTacken();

        void updateVideoUrl(String videoUrl);
    }
}
