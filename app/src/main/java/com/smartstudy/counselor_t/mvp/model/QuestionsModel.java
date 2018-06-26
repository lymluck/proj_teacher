package com.smartstudy.counselor_t.mvp.model;


import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BaseModel;
import study.smart.baselib.server.api.ApiManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by louis on 17/4/6.
 */

public class QuestionsModel extends BaseModel {

    public void getQuestions(int page, ObserverListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");
        apiSubscribe(ApiManager.getApiService().getQuestions(getHeadersMap(), params), listener);
    }

}
