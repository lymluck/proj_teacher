package com.smartstudy.counselor_t.mvp.contract;

import java.util.List;

import study.smart.baselib.entity.CityTeacherInfo;

import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.mvp.base.BaseView;

/**
 * @author yqy
 * @date on 2018/8/15
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface ChooseTeacherContract {
    interface View extends BaseView {
        void getTeacherListSuccess(List<CityTeacherInfo> cityTeacherInfo);

        void shareQuestionSuccess();

        void shareQuestionFail(String msg);
    }

    interface Presenter extends BasePresenter {
        void getTeacherList();

        void shareQuestion(String questionId, String receiverId, String note);
    }
}
