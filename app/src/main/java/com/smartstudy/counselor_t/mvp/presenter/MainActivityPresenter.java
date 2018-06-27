package com.smartstudy.counselor_t.mvp.presenter;

import com.alibaba.fastjson.JSON;

import study.smart.baselib.entity.TeacherInfo;
import study.smart.baselib.entity.VersionInfo;
import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BasePresenterImpl;

import com.smartstudy.counselor_t.mvp.contract.MainActivityContract;
import com.smartstudy.counselor_t.mvp.model.MainModel;

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
    public void getAuditResult() {
        mainModel.getAuditResult(new ObserverListener<TeacherInfo>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(TeacherInfo teacherInfo) {
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

    @Override
    public void checkVersion() {
        mainModel.checkVersion(new ObserverListener<VersionInfo>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(VersionInfo result) {
                if (result != null) {
                    if (result.isNeedUpdate()) {
                        if (result.isForceUpdate()) {
                            view.forceUpdate(result.getPackageUrl(), result.getLatestVersion(), result.getDescription());
                        } else {
                            view.updateable(result.getPackageUrl(), result.getLatestVersion(), result.getDescription());
                        }
                    }
                    result = null;
                }
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void getLogOut() {
        mainModel.getLogOut(new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String s) {
                view.getLogOutSuccess();
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }
}

