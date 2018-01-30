package com.smartstudy.counselor_t.mvp.presenter;

import com.alibaba.fastjson.JSON;
import com.smartstudy.counselor_t.entity.StudentPageInfo;
import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BasePresenterImpl;
import com.smartstudy.counselor_t.mvp.contract.StudentActivityContract;
import com.smartstudy.counselor_t.mvp.model.StudentDetailInfoModel;

import io.reactivex.disposables.Disposable;

/**
 * @author yqy
 * @date on 2018/1/15
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class StudentInfoActivityPresenter extends BasePresenterImpl<StudentActivityContract.View> implements StudentActivityContract.Presenter {

    private StudentDetailInfoModel studentDetailInfoModel;

    public StudentInfoActivityPresenter(StudentActivityContract.View view) {
        super(view);
        studentDetailInfoModel = new StudentDetailInfoModel();
    }


    @Override
    public void detach() {
        super.detach();
        studentDetailInfoModel = null;
    }


    @Override
    public void getStudentDetailInfo(String userId) {

        studentDetailInfoModel.getStudentDetailInfo(userId, new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String s) {
                StudentPageInfo studentPageInfo = JSON.parseObject(s, StudentPageInfo.class);
                if (studentPageInfo != null) {
                    view.getStudentInfoDetailSuccess(studentPageInfo);
                }
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });

    }

}

