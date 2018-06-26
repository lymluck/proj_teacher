package com.smartstudy.counselor_t.mvp.model;

import android.text.TextUtils;


import study.smart.baselib.entity.TeacherInfo;
import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BaseModel;
import study.smart.baselib.server.api.ApiManager;
import study.smart.baselib.utils.HttpUrlUtils;

import okhttp3.MultipartBody;

/**
 * @author yqy
 * @date on 2018/2/7
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class CommonEditNameModel extends BaseModel {

    public void updatePersonInfo(TeacherInfo teacherInfo, ObserverListener listener) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (!TextUtils.isEmpty(teacherInfo.getName())) {
            builder.addFormDataPart("name", teacherInfo.getName());
        }

        if (!TextUtils.isEmpty(teacherInfo.getTitle())) {
            builder.addFormDataPart("title", teacherInfo.getTitle());
        }

        if (!TextUtils.isEmpty(teacherInfo.getSchool())) {
            builder.addFormDataPart("school", teacherInfo.getSchool());
        }

        if (!TextUtils.isEmpty(teacherInfo.getEmail())) {
            builder.addFormDataPart("email", teacherInfo.getEmail());
        }

        if (!TextUtils.isEmpty(teacherInfo.getRealName())) {
            builder.addFormDataPart("realName", teacherInfo.getRealName());
        }

        if (!TextUtils.isEmpty(teacherInfo.getYearsOfWorking())) {
            builder.addFormDataPart("yearsOfWorking", teacherInfo.getYearsOfWorking());
        }
        apiSubscribe(ApiManager.getApiService().updatePersonInfo(getHeadersMap(), HttpUrlUtils.URL_COUNSELLOR_PROFILE, builder.build()), listener);
    }
}
