package com.smartstudy.counselor_t.mvp.model;

import android.util.Log;

import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BaseModel;
import com.smartstudy.counselor_t.server.api.ApiManager;
import com.smartstudy.counselor_t.util.HttpUrlUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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


    public void postAnswerVoice(final String id, File voice, ObserverListener listener) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        if (voice != null) {
            builder.addFormDataPart("voice", voice.getName(), RequestBody.create(MediaType.parse("image/*"), voice));
        }
        RequestBody requestBody = builder.build();
        apiSubscribe(ApiManager.getApiService().postAnswerVoice(getHeadersMap(), String.format(HttpUrlUtils.URL_POST_ANSWER, id), requestBody), listener);

    }

}
