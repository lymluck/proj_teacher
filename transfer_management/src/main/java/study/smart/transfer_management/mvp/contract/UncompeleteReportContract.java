package study.smart.transfer_management.mvp.contract;

import android.content.Context;

import java.util.List;

import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.mvp.base.BaseView;
import study.smart.transfer_management.entity.MyStudentInfo;

/**
 * @author yqy
 * @date on 2018/7/23
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface UncompeleteReportContract {
    interface View extends BaseView {
        void getUncompeleteReportSuccess(List<MyStudentInfo> teacherRankInfos, int request_state);

        void showEmptyView(android.view.View view);
    }

    interface Presenter extends BasePresenter {
        void getUncompeleteReport(String type, String page, int request_state);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(android.view.View emptyView);
    }
}
