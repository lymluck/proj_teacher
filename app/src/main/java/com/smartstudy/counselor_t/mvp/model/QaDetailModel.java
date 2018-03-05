package com.smartstudy.counselor_t.mvp.model;

import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BaseModel;
import com.smartstudy.counselor_t.server.api.ApiManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yqy
 * @date on 2018/2/27
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class QaDetailModel extends BaseModel {
    public void getQaDetail( final String id, ObserverListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id + "");
        apiSubscribe(ApiManager.getApiService().getQaDetail(getHeadersMap(), params), listener);
    }



    public void postQuestion(final String id, final String question, ObserverListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("question", question);
        apiSubscribe(ApiManager.getApiService().postQuestion(getHeadersMap(), params), listener);

    }

}
