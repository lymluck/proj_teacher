package study.smart.transfer_management.mvp.contract;

import android.content.Context;

import java.util.List;

import study.smart.baselib.entity.MessageDetailItemInfo;
import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.mvp.base.BaseView;
import study.smart.transfer_management.entity.WorkingDetailInfo;

/**
 * @author yqy
 * @date on 2018/7/17
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface WorkingDetailContract {
    interface View extends BaseView {

        void getWorkingDetail(List<WorkingDetailInfo> messageDetailItemInfos);


    }

    interface Presenter extends BasePresenter {
        void getWorkingDetail();
    }
}
