package com.smartstudy.counselor_t.mvp.contract;

import android.content.Context;


import java.util.List;

import study.smart.baselib.entity.TeacherRankInfo;
import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.mvp.base.BaseView;

/**
 * @author yqy
 * @date on 2018/7/18
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface TeacherRankContract {
    interface View extends BaseView {
        void getTransferListSuccess(List<TeacherRankInfo> teacherRankInfos, int request_state);

        void showMySelf(TeacherRankInfo teacherRankInfo);

        void showEmptyView(android.view.View view);
    }

    interface Presenter extends BasePresenter {
        void getTeacherRank(String type, String page, int request_state);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(android.view.View emptyView);
    }
}
