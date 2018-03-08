package com.smartstudy.counselor_t.mvp.model;

import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BaseModel;
import com.smartstudy.counselor_t.server.api.ApiManager;

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

    public void getQuestions(final String keyword, final boolean answered, final int page,
                             ObserverListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("answered", answered + "");
        params.put("keyword", keyword);
        params.put("page", page + "");
        apiSubscribe(ApiManager.getApiService().getQuestions(getHeadersMap(), params), listener);
    }

    public void getMyQuestions(final int page, ObserverListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");
//        apiSubscribe(ApiManager.getApiService().getMyQuestions(getHeadersMap(), params), listener);
    }

    public void getSchoolQa(final String schoolId, final int page, ObserverListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");
        params.put("answered", true + "");
        params.put("schoolId", schoolId);
        apiSubscribe(ApiManager.getApiService().getQuestions(getHeadersMap(), params), listener);
    }
}
