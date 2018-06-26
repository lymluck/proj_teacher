package com.smartstudy.counselor_t.mvp.model;


import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BaseModel;
import study.smart.baselib.server.api.ApiManager;

/**
 * Created by louis on 17/4/5.
 */

public class SplashModel extends BaseModel {

    public void getAdInfo(ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().getAdInfo(getHeadersMap()), listener);
    }
}
