package com.smartstudy.counselor_t.mvp.contract;


import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.mvp.base.BaseView;

import java.util.List;

/**
 * @author yqy
 * @date on 2018/5/3
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface AddLabelContract {
    interface View extends BaseView {

        void getMyTagSuccess(List<String> tags);

        void subitMyStudentSuccess();

        void postHistoryTagSueccess();

        void getHistoryTagSueccess(List<String> tags);
    }

    interface Presenter extends BasePresenter {

        void getMyStudentTag(String id);

        void submitMyStudentTag(String id, List<String> tags);

        void postHistoryTag(List<String> tags);

        void getHisToryTag();

    }
}
