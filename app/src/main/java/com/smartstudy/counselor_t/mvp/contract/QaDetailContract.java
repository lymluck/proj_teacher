package com.smartstudy.counselor_t.mvp.contract;


import com.smartstudy.counselor_t.entity.QaDetailInfo;
import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.mvp.base.BaseView;

import java.io.File;
import java.util.List;

/**
 * Created by yqy on 2017/12/4.
 */

public interface QaDetailContract {

    interface View extends BaseView {

        void getQaDetails(QaDetailInfo data);

        void getQaDetailFail(String message);

        void postAnswerSuccess();

        void postAnswerFail(String message);

        void questionAddMarkSuccess();

        void questionDeleteMarkSuccess();
    }

    interface Presenter extends BasePresenter {

        void getQaDetails(String id);

        void postAnswerText(String id, String answer);

        void questionAddMark(String questionId);

        void questionDeleteMark(String questionId);

    }
}
