package com.smartstudy.counselor_t.mvp.model;

import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BaseModel;
import com.smartstudy.counselor_t.server.api.ApiManager;
import com.smartstudy.counselor_t.util.HttpUrlUtils;

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
