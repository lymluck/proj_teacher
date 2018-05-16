package com.smartstudy.counselor_t.mvp.presenter;

import com.alibaba.fastjson.JSON;
import com.smartstudy.counselor_t.entity.TeacherInfo;
import com.smartstudy.counselor_t.entity.VersionInfo;
import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BasePresenterImpl;
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

    @Override
    public void checkVersion() {
        mainModel.checkVersion(new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String result) {
                VersionInfo info = JSON.parseObject(result, VersionInfo.class);
                if (info != null) {
                    if (info.isNeedUpdate()) {
                        if (info.isForceUpdate()) {
                            view.forceUpdate(info.getPackageUrl(), info.getLatestVersion(), info.getDescription());
                        } else {
                            view.updateable(info.getPackageUrl(), info.getLatestVersion(), info.getDescription());
                        }
                    }
                    info = null;
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

