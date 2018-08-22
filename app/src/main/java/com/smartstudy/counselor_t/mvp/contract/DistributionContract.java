package com.smartstudy.counselor_t.mvp.contract;

import android.content.Context;

import com.smartstudy.counselor_t.entity.DistributionInfo;

import java.util.List;

import study.smart.baselib.entity.TeacherRankInfo;
import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.mvp.base.BaseView;

/**
 * @author yqy
 * @date on 2018/8/14
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface DistributionContract {
    interface View extends BaseView {
        void getShareQuestionSuccess(List<DistributionInfo> distributionInfos, int request_state);

        void showEmptyView(android.view.View view);
    }

    interface Presenter extends BasePresenter {
        void getShareQuestion(String type, String page, int request_state);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(android.view.View emptyView);
    }
}