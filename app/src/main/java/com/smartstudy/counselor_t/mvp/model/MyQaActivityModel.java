package com.smartstudy.counselor_t.mvp.model;

import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BaseModel;
import com.smartstudy.counselor_t.server.api.ApiManager;

/**
 * @author yqy
 * @date on 2018/3/20
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyQaActivityModel extends BaseModel {
    public void getAuditResult(ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().getAuditResult(getHeadersMap()), listener);
    }

    public void getLogOut(ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().getLogOut(getHeadersMap()), listener);
    }
}
