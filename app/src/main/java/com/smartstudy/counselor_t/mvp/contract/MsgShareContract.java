package com.smartstudy.counselor_t.mvp.contract;

import android.content.Context;

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

        void showEmptyView(android.view.View view);

    }

    interface Presenter extends BasePresenter {

        void getChatUsers();

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(android.view.View emptyView);
    }
}
