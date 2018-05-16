package com.smartstudy.counselor_t.mvp.model;

import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BaseModel;
import com.smartstudy.counselor_t.server.api.ApiManager;

/**
 * Created by louis on 17/4/5.
 */

public class SplashModel extends BaseModel {

    public void getAdInfo(ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().getAdInfo(getHeadersMap()), listener);
    }
}
