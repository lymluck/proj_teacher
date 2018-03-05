package com.smartstudy.counselor_t.manager;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.smartstudy.counselor_t.server.api.ApiManager;
import com.smartstudy.counselor_t.entity.StudentPageInfo;
import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BaseModel;
import com.smartstudy.counselor_t.util.DisplayImageUtils;

import io.reactivex.disposables.Disposable;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * @author louis
 * @date on 2018/1/30
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */

public class StudentInfoManager extends BaseModel {

    private static StudentInfoManager instance;

    public static StudentInfoManager getInstance() {
        if (null == instance) {
            synchronized (StudentInfoManager.class) {
                if (null == instance) {
                    instance = new StudentInfoManager();
                }
            }
        }
        return instance;
    }

    public void getStudentInfo(String userId) {
        apiSubscribe(ApiManager.getApiService().getStudentInfo(getHeadersMap(), userId), new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }

            @Override
            public void onNext(String s) {
                StudentPageInfo studentPageInfo = JSON.parseObject(s, StudentPageInfo.class);
                if (studentPageInfo != null) {
                    String cacheUrl = DisplayImageUtils.formatImgUrl(studentPageInfo.getAvatar(), 90, 90);
                    RongIM.getInstance().refreshUserInfoCache(new UserInfo(studentPageInfo.getImUserId(), studentPageInfo.getName(), TextUtils.isEmpty(cacheUrl) ? null : Uri.parse(cacheUrl)));
                }
            }

            @Override
            public void onError(String msg) {
            }
        });
    }

}
