package com.smartstudy.counselor_t.mvp.model;

import com.smartstudy.counselor_t.api.ApiManager;
import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BaseMode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yqy
 * @date on 2018/1/15
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class StudentDetailInfoModel extends BaseMode {
    public void getStudentDetailInfo(String id, ObserverListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        apiSubscribe(ApiManager.getApiService().getStudentDetailInfo(params), listener);
    }
}
