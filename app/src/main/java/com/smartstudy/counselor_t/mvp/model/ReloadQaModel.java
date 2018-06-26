package com.smartstudy.counselor_t.mvp.model;


import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BaseModel;
import study.smart.baselib.server.api.ApiManager;
import study.smart.baselib.utils.HttpUrlUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author yqy
 * @date on 2018/3/9
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class ReloadQaModel extends BaseModel {

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
