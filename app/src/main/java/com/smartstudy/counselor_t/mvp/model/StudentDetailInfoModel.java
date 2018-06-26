package com.smartstudy.counselor_t.mvp.model;


import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BaseModel;
import study.smart.baselib.server.api.ApiManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yqy
 * @date on 2018/1/15
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class StudentDetailInfoModel extends BaseModel {

    public void getStudentDetailInfo(String id, ObserverListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        apiSubscribe(ApiManager.getApiService().getStudentDetailInfo(getHeadersMap(), params), listener);
    }
}
