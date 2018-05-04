package com.smartstudy.counselor_t.mvp.model;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BaseModel;
import com.smartstudy.counselor_t.server.api.ApiManager;
import com.smartstudy.counselor_t.util.HttpUrlUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yqy
 * @date on 2018/5/3
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class AddLabelModel extends BaseModel {
    public void getMyStudentTags(String studentId, ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().getMyStudentTag(getHeadersMap(), String.format(HttpUrlUtils.URL_QUESTS_MY_TAGS, studentId)), listener);
    }

    public void submitMyStudentTag(String studentId, List<String> tags, ObserverListener listener) {
        Map<String, String> params = new HashMap<>();
        Log.w("kim", "--->" + JSONArray.toJSONString(tags).toString());
        params.put("tags", JSONArray.toJSONString(tags).toString());
        apiSubscribe(ApiManager.getApiService().submitMyStudentTag(getHeadersMap(), String.format(HttpUrlUtils.URL_QUESTS_MY_TAGS, studentId), params), listener);
    }


    public void postHistoryTag(List<String> tags, ObserverListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("tags", JSONArray.toJSONString(tags).toString());
        apiSubscribe(ApiManager.getApiService().postHistoryTag(getHeadersMap(), params), listener);
    }

    public void getHisToryTag(ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().getHisToryTag(getHeadersMap()), listener);
    }
}
