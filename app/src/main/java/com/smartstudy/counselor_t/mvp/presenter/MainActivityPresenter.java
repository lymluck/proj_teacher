package com.smartstudy.counselor_t.mvp.presenter;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.counselor_t.entity.TeacherInfo;
import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BasePresenterImpl;
import com.smartstudy.counselor_t.mvp.contract.MainActivityContract;
import com.smartstudy.counselor_t.mvp.model.MainModel;
import com.smartstudy.counselor_t.entity.StudentInfo;

import io.reactivex.disposables.Disposable;

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
                StudentInfo studentInfo = JSON.parseObject(object.getString(userId), StudentInfo.class);
                if (studentInfo != null) {
                    view.getStudentInfoSuccess(userId, studentInfo);
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

