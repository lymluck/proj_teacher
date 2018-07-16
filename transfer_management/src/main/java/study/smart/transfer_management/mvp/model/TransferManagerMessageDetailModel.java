package study.smart.transfer_management.mvp.model;

import java.util.HashMap;

import study.smart.baselib.entity.StudentPageInfo;
import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BaseModel;
import study.smart.baselib.server.api.ApiManager;

/**
 * @author yqy
 * @date on 2018/7/13
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TransferManagerMessageDetailModel extends BaseModel {
    /**
     * 获取消息信息
     *
     * @param listener
     */
    public void getMessagesDetailList(String page, String type, ObserverListener listener) {
        HashMap paras = new HashMap();
        paras.put("page", page);
        paras.put("type", type);
        apiSubscribe(ApiManager.getApiService().getMessageList(getHeadersMap(), paras), listener);
    }
}
