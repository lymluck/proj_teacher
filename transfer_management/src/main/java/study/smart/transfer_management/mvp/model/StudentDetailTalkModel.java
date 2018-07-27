package study.smart.transfer_management.mvp.model;

import java.util.HashMap;

import study.smart.baselib.BaseApplication;
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
public class StudentDetailTalkModel extends BaseModel {
    /**
     * 获取未沟通记录
     *
     * @param listener
     */
    public void getMyTalkRecordList(String userid, ObserverListener listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", userid);
        apiSubscribe(ApiManager.getApiService().getMyTalkRecordList(getHeadersMap(), params), listener);
    }
}
