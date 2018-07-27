package study.smart.transfer_management.mvp.contract;

import study.smart.baselib.entity.MyStudentInfo;
import study.smart.baselib.entity.TaskDetailInfo;
import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.mvp.base.BaseView;

/**
 * @author yqy
 * @date on 2018/7/27
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface StudentDetailContract {
    interface View extends BaseView {
        void getStudentDetailSuccess(MyStudentInfo myStudentInfo);

    }

    interface Presenter extends BasePresenter {
        void getStudentDetail(String id);
    }
}

