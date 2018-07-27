package study.smart.transfer_management.mvp.model;

import java.util.HashMap;

import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BaseModel;
import study.smart.baselib.server.api.ApiManager;
import study.smart.baselib.utils.HttpUrlUtils;

/**
 * @author yqy
 * @date on 2018/7/27
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TaskDetailModel extends BaseModel {
    /**
     * 获取未沟通记录
     *
     * @param listener
     */
    public void getTaskDetail(String id, ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().getMessageDetail(getHeadersMap(), String.format(HttpUrlUtils.MESSAGE_DETAIL, id)), listener);
    }
}
