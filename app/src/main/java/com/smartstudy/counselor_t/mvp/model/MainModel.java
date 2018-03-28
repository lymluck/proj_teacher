package com.smartstudy.counselor_t.mvp.model;

import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BaseModel;
import com.smartstudy.counselor_t.server.api.ApiManager;
import com.smartstudy.counselor_t.util.AppUtils;
import com.smartstudy.counselor_t.util.HttpUrlUtils;

/**
 * @author yqy
 * @date on 2018/1/12
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MainModel extends BaseModel {

    public void getAuditResult(ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().getAuditResult(getHeadersMap()), listener);
    }

    public void checkVersion(ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().checkVersion(getHeadersMap(), String.format(HttpUrlUtils.URL_APP_VERSION, AppUtils.getVersionName())), listener);
    }

    public void getLogOut(ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().getLogOut(getHeadersMap()), listener);
    }
}

