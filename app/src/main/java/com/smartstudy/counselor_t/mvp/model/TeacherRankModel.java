package com.smartstudy.counselor_t.mvp.model;


import java.util.HashMap;

import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BaseModel;
import study.smart.baselib.server.api.ApiManager;
import study.smart.baselib.utils.HttpUrlUtils;

/**
 * @author yqy
 * @date on 2018/7/18
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TeacherRankModel extends BaseModel {
    public void getTeacherRank(String type, String page, ObserverListener listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page);
        apiSubscribe(ApiManager.getApiService().getTeacherRank(getHeadersMap(), String.format(HttpUrlUtils.URL_COUNSELLOR_RANKING, type), params), listener);
    }
}
