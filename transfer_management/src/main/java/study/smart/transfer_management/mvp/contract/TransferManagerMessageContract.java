package study.smart.transfer_management.mvp.contract;

import java.util.List;

import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.mvp.base.BaseView;
import study.smart.transfer_management.entity.MessageInfo;

/**
 * @author yqy
 * @date on 2018/7/13
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface TransferManagerMessageContract {
    interface View extends BaseView {
        void getMessageInfoSuccess(List<MessageInfo> messageInfos);
    }

    interface Presenter extends BasePresenter {
        void getMessageInfos();
    }
}
