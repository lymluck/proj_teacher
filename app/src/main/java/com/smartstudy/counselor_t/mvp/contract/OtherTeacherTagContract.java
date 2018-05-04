package com.smartstudy.counselor_t.mvp.contract;

import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.mvp.base.BaseView;

import java.util.List;

/**
 * @author yqy
 * @date on 2018/5/4
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface OtherTeacherTagContract {
    interface View extends BaseView {
        void getOtherTeacherTagSueccess(List<String> list);
    }

    interface Presenter extends BasePresenter {
        void getOtherTeacherTag(String userId);
    }
}