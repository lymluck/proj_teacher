package study.smart.baselib.mvp.contract;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import study.smart.baselib.entity.MessageDetailItemInfo;
import study.smart.baselib.entity.TransferManagerEntity;
import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.mvp.base.BaseView;

import java.util.List;

/**
 * Created by louis on 2017/3/4.
 */

public interface CommonSearchContract {

    interface View extends BaseView {

        void showTransferManagerList(List<TransferManagerEntity> transferManagerEntities, int request_state);

        void showMsgList(List<MessageDetailItemInfo> messageDetailItemInfos, int request_state);

        void showEmptyView(android.view.View view);
    }

    interface Presenter extends BasePresenter {

        void getTransferManagerList(String keyword, int page, int request_state);

        void getMsgList(String keyword, int page, int request_state);

        void setEmptyView(LayoutInflater mInflater, Context context, ViewGroup parent);
    }
}
