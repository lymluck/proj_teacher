package study.smart.transfer_management.mvp.contract;

import android.content.Context;

import java.util.List;

import study.smart.baselib.entity.TransferManagerEntity;
import study.smart.baselib.manager.StudentInfoManager;
import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.mvp.base.BaseView;
import study.smart.baselib.entity.MessageDetailItemInfo;

/**
 * @author yqy
 * @date on 2018/7/13
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface TransferManagerMessageDetailContract {
    interface View extends BaseView {

        void getMessageDetailSuccess(List<MessageDetailItemInfo> messageDetailItemInfos, int request_state);

        void getMessageDetailSuccess(MessageDetailItemInfo messageDetailItemInfo,TransferManagerEntity transferManagerEntity);

        void showEmptyView(android.view.View view);

    }

    interface Presenter extends BasePresenter {
        void getMessageDetailList(String page, String type, int request_state);

        void getMessageDetail(MessageDetailItemInfo messageDetailItemInfo);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(android.view.View emptyView);
    }
}
