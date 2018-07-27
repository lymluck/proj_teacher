package study.smart.transfer_management.mvp.contract;

import android.content.Context;

import java.util.List;

import study.smart.baselib.entity.MyTalkRecordInfo;
import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.mvp.base.BaseView;

/**
 * @author yqy
 * @date on 2018/7/25
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface StudentDetailTalkContract {
    interface View extends BaseView {
        void gettudentDetailTalkSuccess(List<MyTalkRecordInfo> myTalkRecordInfo);

        void showEmptyView(android.view.View view);
    }

    interface Presenter extends BasePresenter {
        void gettudentDetailTalk(String userId);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(android.view.View emptyView);
    }
}
