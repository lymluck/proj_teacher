package study.smart.transfer_management.mvp.model;

import java.util.HashMap;

import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BaseModel;
import study.smart.baselib.server.api.ApiManager;

/**
 * @author yqy
 * @date on 2018/7/25
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyTaskListModel extends BaseModel {
    /**
     * 获取我发步的任务
     *
     * @param listener
     */
    public void getMyTalkRecordList(String from, String userId, ObserverListener listener) {
        HashMap<String, String> params = new HashMap<>();
        if ("student".equals(from)) {
            params.put("userId", userId);
        } else {
            params.put("isOwnTask", "true");
        }
        apiSubscribe(ApiManager.getApiService().getMyTaskList(getHeadersMap(), params), listener);
    }
}
