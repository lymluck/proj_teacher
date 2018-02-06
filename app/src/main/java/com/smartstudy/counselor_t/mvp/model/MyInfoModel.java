package com.smartstudy.counselor_t.mvp.model;

import android.text.TextUtils;

import com.smartstudy.counselor_t.server.api.ApiManager;
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


    public void updatePersonInfo(String name, File file, String title, String school, String yearsOfWorking, String email, String realName, ObserverListener listener) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (!TextUtils.isEmpty(name)) {
            builder.addFormDataPart("name", name);
        }
        if (file != null) {
            builder.addFormDataPart("avatar", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        }

        if (!TextUtils.isEmpty(title)) {
            builder.addFormDataPart("title", title);
        }

        if (!TextUtils.isEmpty(school)) {
            builder.addFormDataPart("school", school);
        }

        if (!TextUtils.isEmpty(email)) {
            builder.addFormDataPart("email", email);
        }

        if (!TextUtils.isEmpty(realName)) {
            builder.addFormDataPart("realName", realName);
        }

        if (!TextUtils.isEmpty(yearsOfWorking)) {
            builder.addFormDataPart("yearsOfWorking", yearsOfWorking);
        }
        apiSubscribe(ApiManager.getApiService().updatePersonInfo(getHeadersMap(), HttpUrlUtils.URL_COUNSELLOR_PROFILE, builder.build()), listener);
    }
}
