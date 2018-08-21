package com.smartstudy.counselor_t.mvp.model;

import java.util.HashMap;

import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BaseModel;
import study.smart.baselib.server.api.ApiManager;
import study.smart.baselib.utils.HttpUrlUtils;

/**
 * @author yqy
 * @date on 2018/8/17
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TransferQaDetailModel extends BaseModel {
    public void postQuestionReceive(String questionId, String senderId, ObserverListener listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("senderId", senderId);
        apiSubscribe(ApiManager.getApiService().postQuestionReceive(getHeadersMap(), String.format(HttpUrlUtils.QUESTION_SHARE_RECEIVE, questionId), params), listener);
    }
}
