package study.smart.transfer_management.mvp.model;

import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BaseModel;
import study.smart.baselib.server.api.ApiManager;
import study.smart.baselib.utils.HttpUrlUtils;

/**
 * @author yqy
 * @date on 2018/7/13
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TransferManagerMessageModel extends BaseModel {
    /**
     * 获取消息信息
     *
     * @param listener
     */
    public void getMessages(ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().getMessageList(getHeadersMap()), listener);
    }
}
