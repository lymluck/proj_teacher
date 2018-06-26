package com.smartstudy.counselor_t.mvp.contract;

import android.content.Context;

import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.mvp.base.BaseView;
import com.smartstudy.counselor_t.entity.QuestionInfo;

import java.util.List;

/**
 * @author yqy
 * @date on 2018/5/3
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface MyFocusContract {
    interface View extends BaseView {

        void getMyFocusQuestionsSuccess(List<QuestionInfo> data, int request_state);

        void showEmptyView(android.view.View view);
    }

    interface Presenter extends BasePresenter {

        void getMyFocusQuestions(int page, int request_state);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(Context context, android.view.View emptyView);
    }
}
