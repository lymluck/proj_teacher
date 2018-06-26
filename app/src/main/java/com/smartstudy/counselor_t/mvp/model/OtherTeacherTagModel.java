package com.smartstudy.counselor_t.mvp.model;


import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BaseModel;
import study.smart.baselib.server.api.ApiManager;
import study.smart.baselib.utils.HttpUrlUtils;

/**
 * @author yqy
 * @date on 2018/5/4
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class OtherTeacherTagModel extends BaseModel {
    public void getOtherTeacherTag(String studentId, ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().getMyStudentTag(getHeadersMap(), String.format(HttpUrlUtils.URL_QUESTS_OTHER_TAGS, studentId)), listener);
    }
}
