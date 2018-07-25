package study.smart.transfer_management.mvp.model;

import java.util.HashMap;

import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BaseModel;
import study.smart.baselib.server.api.ApiManager;

/**
 * @author yqy
 * @date on 2018/7/24
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class UnTalkRecordModel extends BaseModel {
    /**
     * 获取未沟通记录
     *
     * @param listener
     */
    public void getUnTalkRecordList(String page, ObserverListener listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page);
        apiSubscribe(ApiManager.getApiService().getUnTalkRecordList(getHeadersMap(), params), listener);
    }
}
