package study.smart.transfer_management.mvp.model;

import android.text.TextUtils;

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
public class MyTalkRecordListModel extends BaseModel {
    /**
     * 获取未沟通记录
     *
     * @param listener
     */
    public void getMyTalkRecordList(ObserverListener listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("isOwnCommunicationReport", "true");
        apiSubscribe(ApiManager.getApiService().getMyTalkRecordList(getHeadersMap(), params), listener);
    }
}
