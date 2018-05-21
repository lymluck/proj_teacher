package com.smartstudy.counselor_t.mvp.contract;

import android.widget.ImageView;

import com.smartstudy.counselor_t.entity.IdNameInfo;
import com.smartstudy.counselor_t.entity.TeacherInfo;
import com.smartstudy.counselor_t.entity.TokenBean;
import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.mvp.base.BaseView;

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

        void onLoading(int progress, String url);

        void refreshSuccess(TokenBean tokenBean);
    }

    interface Presenter extends BasePresenter {

        void getMyInfo();

        void uploadVideo(File file);

        void updateMyAvatarInfo(File avatar, ImageView ivAvatar);

        void getLogOut();

        void getOptions();

        void updateMyInfo(String name, File avatar, String title, String school, String yearsOfWorking,
                          String email, String realName, String introduction, String workingCityKey, String adeptWorksKey);

        void refreshTacken();
    }
}
