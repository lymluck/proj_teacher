package com.smartstudy.counselor_t.mvp.model;


import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BaseModel;
import study.smart.baselib.server.api.ApiManager;
import study.smart.baselib.utils.AppUtils;
import study.smart.baselib.utils.HttpUrlUtils;

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
        apiSubscribe(ApiManager.getApiService().checkVersion(getHeadersMap(),
            String.format(HttpUrlUtils.URL_APP_VERSION, AppUtils.getVersionName())), listener);
    }

    public void getLogOut(ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().getLogOut(getHeadersMap()), listener);
    }
}

