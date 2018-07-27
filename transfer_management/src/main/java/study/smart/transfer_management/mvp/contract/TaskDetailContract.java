package study.smart.transfer_management.mvp.contract;

import study.smart.baselib.entity.TaskDetailInfo;
import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.mvp.base.BaseView;
import study.smart.baselib.entity.MyTaskInfo;

/**
 * @author yqy
 * @date on 2018/7/27
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface TaskDetailContract {
    interface View extends BaseView {
        void getTaskDetailSuccess(TaskDetailInfo dataList);

    }

    interface Presenter extends BasePresenter {
        void getTaskDetail(String id);
    }
}
