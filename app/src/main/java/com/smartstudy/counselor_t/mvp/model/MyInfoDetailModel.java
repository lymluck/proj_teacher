package com.smartstudy.counselor_t.mvp.model;

import android.text.TextUtils;
import android.util.Log;

import com.smartstudy.counselor_t.entity.ResponseInfo;
import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.listener.OnUploadFileListener;
import com.smartstudy.counselor_t.mvp.base.BaseModel;
import com.smartstudy.counselor_t.server.api.ApiManager;
import com.smartstudy.counselor_t.server.api.FileUploadRequestBody;
import com.smartstudy.counselor_t.util.AppUtils;
import com.smartstudy.counselor_t.util.HttpUrlUtils;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.File;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * @author yqy
 * @date on 2018/3/27
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyInfoDetailModel extends BaseModel {

    public void uploadVideo(File file, OnUploadFileListener listener) {
        final RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        FileUploadRequestBody fileUploadRequestBody = new FileUploadRequestBody(requestBody, listener);
        fileObservalbe(ApiManager.getApiService().upLoadTeacherVideo(getHeadersMap(), fileUploadRequestBody)).subscribe(new Observer<ResponseBody>() {

            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                Log.w("kim", "-----" + requestBody.toString());
            }

            @Override
            public void onError(Throwable t) {
                Log.w("kim", "错误======");
            }

            @Override
            public void onComplete() {
            }
        });
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