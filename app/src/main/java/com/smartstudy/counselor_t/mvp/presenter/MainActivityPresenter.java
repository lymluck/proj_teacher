package com.smartstudy.counselor_t.mvp.presenter;

import android.net.Uri;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.counselor_t.entity.TeacherInfo;
import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BasePresenterImpl;
import com.smartstudy.counselor_t.mvp.contract.MainActivityContract;
import com.smartstudy.counselor_t.mvp.model.MainModel;

import io.reactivex.disposables.Disposable;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * @author yqy
 * @date on 2018/1/12
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MainActivityPresenter extends BasePresenterImpl<MainActivityContract.View> implements MainActivityContract.Presenter {

    private MainModel mainModel;

    public MainActivityPresenter(MainActivityContract.View view) {
        super(view);
        mainModel = new MainModel();
    }


    @Override
    public void detach() {
        super.detach();
        mainModel = null;
    }

    @Override
    public void getStudentInfo(final String userId) {
        mainModel.getStudentInfo(userId, new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String s) {
                JSONObject object = JSON.parseObject(s);
                if (object != null) {
                    RongIM.getInstance().refreshUserInfoCache(new UserInfo(userId, object.getString("name")
                            , TextUtils.isEmpty(object.getString("avatar")) ? null : Uri.parse(object.getString("avatar"))));
                }
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void getAuditResult() {
        mainModel.getAuditResult(new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String s) {
                TeacherInfo teacherInfo = JSON.parseObject(s, TeacherInfo.class);
                if (teacherInfo != null) {
                    view.getAuditResult(teacherInfo);
                }

            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }
}

