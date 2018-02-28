package com.smartstudy.counselor_t.mvp.contract;

import com.smartstudy.counselor_t.entity.ChatUserInfo;
import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.mvp.base.BaseView;

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
