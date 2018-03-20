package com.smartstudy.counselor_t.mvp.model;

import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BaseModel;
import com.smartstudy.counselor_t.server.api.ApiManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yqy
 * @date on 2018/3/20
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyQaFragmentModel extends BaseModel {

    public void getMyQuestions(final int page, ObserverListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");
        apiSubscribe(ApiManager.getApiService().getMyQuestion(getHeadersMap(), params), listener);
    }

}