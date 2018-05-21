package com.smartstudy.counselor_t.mvp.model;

import android.text.TextUtils;

import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.listener.OnUploadFileListener;
import com.smartstudy.counselor_t.mvp.base.BaseModel;
import com.smartstudy.counselor_t.server.api.ApiManager;
import com.smartstudy.counselor_t.server.api.FileUploadRequestBody;
import com.smartstudy.counselor_t.util.HttpUrlUtils;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author yqy
 * @date on 2018/3/27
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyInfoDetailModel extends BaseModel {

    public void uploadVideo(File file, OnUploadFileListener listener) {
        final RequestBody requestBody = RequestBody.create(MediaType.parse("video/*"), file);
        FileUploadRequestBody fileUploadRequestBody = new FileUploadRequestBody(requestBody, listener);
        Map map = getHeadersMap();
        map.put("Connection", "close");
        fileObservalbe(ApiManager.getApiService().upLoadTeacherVideo(map, fileUploadRequestBody)).subscribe();
    }

    public void getAuditResult(ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().getMyInfo(getHeadersMap()), listener);
    }


    public void updatePersonInfo(File file, ObserverListener listener) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        if (file != null) {
            builder.addFormDataPart("avatar", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        }

        apiSubscribe(ApiManager.getApiService().updatePersonInfo(getHeadersMap(), HttpUrlUtils.URL_COUNSELLOR_PROFILE, builder.build()), listener);
    }


    public void getLogOut(ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().getLogOut(getHeadersMap()), listener);
    }


    public void getOptions(ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().getOptions(getHeadersMap()), listener);
    }


    public void updateMyInfo(String name, File avatar, String title, String school, String yearsOfWorking,
                             String email, String realName, String introduction, String workingCityKey,
                             String adeptWorksKey, ObserverListener listener) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (!TextUtils.isEmpty(name)) {
            builder.addFormDataPart("name", name);
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
        if (!TextUtils.isEmpty(introduction)) {
            builder.addFormDataPart("introduction", introduction);
        }
        if (!TextUtils.isEmpty(workingCityKey)) {
            builder.addFormDataPart("workingCityKey", workingCityKey);
        }

        if (!TextUtils.isEmpty(adeptWorksKey)) {
            builder.addFormDataPart("adeptWorksKey", adeptWorksKey);
        }
        if (avatar != null) {
            builder.addFormDataPart("avatar", avatar.getName(), RequestBody.create(MediaType.parse("image/*"), avatar));
        }
        apiSubscribe(ApiManager.getApiService().updatePersonInfo(getHeadersMap(), HttpUrlUtils.URL_COUNSELLOR_PROFILE, builder.build()), listener);
    }
}