package com.smartstudy.counselor_t.mvp.presenter;

import com.alibaba.fastjson.JSON;
import com.smartstudy.counselor_t.entity.TeacherInfo;
import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BasePresenterImpl;
import com.smartstudy.counselor_t.mvp.contract.MyInfoContract;
import com.smartstudy.counselor_t.mvp.model.MyInfoModel;

import io.reactivex.disposables.Disposable;

/**
 * @author yqy
 * @date on 2018/1/22
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyInfoActivityPresenter extends BasePresenterImpl<MyInfoContract.View> implements MyInfoContract.Presenter {

    private MyInfoModel myInfoModel;

    public MyInfoActivityPresenter(MyInfoContract.View view) {
        super(view);
        myInfoModel = new MyInfoModel();
    }


    @Override
    public void detach() {
        super.detach();
        myInfoModel = null;
    }


    @Override
    public void getMyInfo() {
        myInfoModel.getAuditResult(new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String s) {
                TeacherInfo teacherInfo = JSON.parseObject(s, TeacherInfo.class);
                if (teacherInfo != null) {
                    view.getMyInfoSuccess(teacherInfo);
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
        myInfoModel.getLogOut(new ObserverListener<String>() {
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

