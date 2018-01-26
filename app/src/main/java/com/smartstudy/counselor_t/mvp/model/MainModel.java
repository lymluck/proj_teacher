package com.smartstudy.counselor_t.mvp.model;

import com.smartstudy.counselor_t.api.ApiManager;
import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BaseModel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yqy
 * @date on 2018/1/12
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MainModel extends BaseModel {

    public void getStudentInfo(String id, ObserverListener listener) {
        Map<String, String> params = new HashMap<>();
        try {
            params.put("id", URLEncoder.encode(id, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        apiSubscribe(ApiManager.getApiService().getStudentInfo(getHeadersMap(), params), listener);
    }

    public void getAuditResult(ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().getAuditResult(getHeadersMap()), listener);
    }
}

