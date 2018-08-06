package study.smart.transfer_management.mvp.contract;

import android.content.Context;

import java.util.List;

import study.smart.baselib.entity.CommunicationList;
import study.smart.baselib.entity.MyTalkRecordInfo;
import study.smart.baselib.entity.MyTaskInfo;
import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.mvp.base.BaseView;

/**
 * @author yqy
 * @date on 2018/7/30
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface PersonTalkRecordDetailContract {
    interface View extends BaseView {
        void getPersonTalkRecordSuccess(List<CommunicationList> communicationLists);

        void showEmptyView(android.view.View view);
    }

    interface Presenter extends BasePresenter {
        void getPersonTalkRecord(String from, String userId);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(android.view.View emptyView);
    }
}
