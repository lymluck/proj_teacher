package com.smartstudy.counselor_t.mvp.presenter;

import com.alibaba.fastjson.JSON;
import com.smartstudy.counselor_t.entity.TeacherInfo;
import com.smartstudy.counselor_t.entity.VersionInfo;
import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BasePresenterImpl;
import com.smartstudy.counselor_t.mvp.contract.MainActivityContract;
import com.smartstudy.counselor_t.mvp.contract.MyInfoDetailContract;
import com.smartstudy.counselor_t.mvp.model.MainModel;
import com.smartstudy.counselor_t.mvp.model.MyInfoDetailModel;

import io.reactivex.disposables.Disposable;

/**
 * @author yqy
 * @date on 2018/3/27
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyInfoDetailPresenter extends BasePresenterImpl<MyInfoDetailContract.View> implements MyInfoDetailContract.Presenter {

    private MyInfoDetailModel myInfoDetailModel;

    public MyInfoDetailPresenter(MyInfoDetailContract.View view) {
        super(view);
        myInfoDetailModel = new MyInfoDetailModel();
    }


    @Override
    public void detach() {
        super.detach();
        myInfoDetailModel = null;
    }

    @Override
    public void getAuditResult() {
        myInfoDetailModel.getAuditResult(new ObserverListener<String>() {
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
    public void getLogOut() {
        myInfoDetailModel.getLogOut(new ObserverListener<String>() {
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

