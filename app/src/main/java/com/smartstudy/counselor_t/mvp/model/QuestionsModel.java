package com.smartstudy.counselor_t.mvp.model;


import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BaseModel;
import study.smart.baselib.server.api.ApiManager;
import study.smart.baselib.utils.HttpUrlUtils;

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

    public void getTeacherRank(String type, String page, ObserverListener listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page);
        apiSubscribe(ApiManager.getApiService().getTeacherRank(getHeadersMap(), String.format(HttpUrlUtils.URL_COUNSELLOR_RANKING, type), params), listener);
    }

}
