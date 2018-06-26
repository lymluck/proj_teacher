package com.smartstudy.counselor_t.mvp.contract;

import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.mvp.base.BaseView;
import com.smartstudy.counselor_t.entity.ChatUserInfo;

import java.util.List;

/**
 * Created by louis on 2017/3/1.
 */

public interface MsgShareContract {

    interface View extends BaseView {

        void showUsers(List<ChatUserInfo> data);

    }

    interface Presenter extends BasePresenter {

        void getChatUsers();
    }
}
