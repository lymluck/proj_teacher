package com.smartstudy.counselor_t.mvp.model;

import com.smartstudy.counselor_t.api.ApiManager;
import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BaseModel;

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
        params.put("ids", id);
        apiSubscribe(ApiManager.getApiService().getStudentInfo(params), listener);
    }
}

