package study.smart.transfer_management.mvp.contract;

import android.content.Context;

import java.util.List;

import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.mvp.base.BaseView;
import study.smart.baselib.entity.MyTaskInfo;

/**
 * @author yqy
 * @date on 2018/7/25
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface MyTaskListContract {
    interface View extends BaseView {
        void getMyTaskListSuccess(List<MyTaskInfo> myTaskInfos);

        void showEmptyView(android.view.View view);
    }

    interface Presenter extends BasePresenter {
        void getMyTaskList(String from, String userId);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(android.view.View emptyView);
    }
}
