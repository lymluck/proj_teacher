package com.smartstudy.counselor_t.mvp.model;

import com.smartstudy.counselor_t.api.ApiManager;
import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BaseModel;
import com.smartstudy.counselor_t.util.HttpUrlUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author yqy
 * @date on 2018/1/18
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class FillPersonModel extends BaseModel {
    public void postPersonInfo(String name, File file, String title, String school, String yearsOfWorking, String email, String realName, ObserverListener listener) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        builder.addFormDataPart("name", name)
                .addFormDataPart("title", title)
                .addFormDataPart("school", school)
                .addFormDataPart("yearsOfWorking", yearsOfWorking)
                .addFormDataPart("email", email)
                .addFormDataPart("realName", realName);

        if (file != null) {
            builder.addFormDataPart("avatar", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        }

        RequestBody requestBody = builder.build();
        apiSubscribe(ApiManager.getApiService().postPersonInfo(getHeadersMap(), HttpUrlUtils.URL_COUNSELLOR_VERIFY, requestBody), listener);
    }


    public void getAuditResult(ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().getAuditResult(getHeadersMap()), listener);
    }
}