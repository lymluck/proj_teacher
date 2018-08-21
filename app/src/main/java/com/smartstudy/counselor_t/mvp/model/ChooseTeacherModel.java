package com.smartstudy.counselor_t.mvp.model;

import java.util.HashMap;

import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BaseModel;
import study.smart.baselib.server.api.ApiManager;
import study.smart.baselib.utils.HttpUrlUtils;

/**
 * @author yqy
 * @date on 2018/8/15
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class ChooseTeacherModel extends BaseModel {
    public void getTeacherList(ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().getTeacherList(getHeadersMap()), listener);
    }

    public void shareQuestion(String questionId, String receiverId, String note, ObserverListener listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("receiverId", receiverId);
        params.put("note", note);
        apiSubscribe(ApiManager.getApiService().shareQuestion(getHeadersMap(), String.format(HttpUrlUtils.QUESTION_SHARE_TEACHER, questionId), params), listener);
    }
}
