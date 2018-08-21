package com.smartstudy.counselor_t.mvp.model;

import java.util.HashMap;

import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BaseModel;
import study.smart.baselib.server.api.ApiManager;
import study.smart.baselib.utils.HttpUrlUtils;

/**
 * @author yqy
 * @date on 2018/8/16
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class DistributionModel extends BaseModel {
    public void getTeacherList(String type, String page, ObserverListener listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page);
        if ("me_transfer".equals(type)) {
            apiSubscribe(ApiManager.getApiService().getShareQuestionSent(getHeadersMap(), params), listener);
        } else {
            apiSubscribe(ApiManager.getApiService().getShareQuestionReceived(getHeadersMap(), params), listener);
        }
    }

}
