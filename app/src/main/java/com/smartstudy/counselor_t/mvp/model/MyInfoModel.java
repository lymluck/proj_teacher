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
 * @date on 2018/1/22
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyInfoModel extends BaseModel {
    public void getAuditResult(ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().getMyInfo(getHeadersMap()), listener);
    }

    public void getLogOut(ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().getLogOut(getHeadersMap()), listener);
    }

    public void updatePersonInfo(String name, File file, String title, String school, String yearsOfWorking, String email, String realName, ObserverListener listener) {
        RequestBody requestBody;
        if (file != null) {
            requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("name", name)
                    .addFormDataPart("title", title)
                    .addFormDataPart("school", school)
                    .addFormDataPart("yearsOfWorking", yearsOfWorking)
                    .addFormDataPart("email", email)
                    .addFormDataPart("realName", realName)
                    .addFormDataPart("avatar", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                    .build();
        } else {
            requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("name", name)
                    .addFormDataPart("title", title)
                    .addFormDataPart("school", school)
                    .addFormDataPart("yearsOfWorking", yearsOfWorking)
                    .addFormDataPart("email", email)
                    .addFormDataPart("realName", realName)
                    .build();
        }

        apiSubscribe(ApiManager.getApiService().updatePersonInfo(getHeadersMap(), HttpUrlUtils.URL_COUNSELLOR_PROFILE, requestBody), listener);
    }
}
