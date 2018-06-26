package com.smartstudy.counselor_t.mvp.model;


import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BaseModel;
import study.smart.baselib.server.api.ApiManager;
import study.smart.baselib.utils.HttpUrlUtils;

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
    public void getQaDetail(final String id, ObserverListener listener) {
        Map<String, String> params = new HashMap<>();
        apiSubscribe(ApiManager.getApiService().getQaDetail(getHeadersMap(), String.format(HttpUrlUtils.URL_QUESTS_LINK, id), params), listener);
    }


    public void postAnswerText(final String id, final String answer, ObserverListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("content", answer);
        apiSubscribe(ApiManager.getApiService().postQuestion(getHeadersMap(), String.format(HttpUrlUtils.URL_POST_ANSWER, id), params), listener);

    }


    public void questionAddMark(final String questionId, ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().questionAddMark(getHeadersMap(), String.format(HttpUrlUtils.URL_QUESTION_MARK, questionId)), listener);
    }

    public void questionDeleteMark(final String questionId, ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().questionDeleteMark(getHeadersMap(), String.format(HttpUrlUtils.URL_QUESTION_MARK, questionId)), listener);
    }
}
