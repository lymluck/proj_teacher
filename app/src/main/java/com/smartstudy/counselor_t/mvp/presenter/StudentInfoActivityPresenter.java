package com.smartstudy.counselor_t.mvp.presenter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.counselor_t.entity.StudentInfo;
import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BasePresenterImpl;
import com.smartstudy.counselor_t.mvp.contract.MainActivityContract;
import com.smartstudy.counselor_t.mvp.contract.StudentActivityContract;
import com.smartstudy.counselor_t.mvp.model.MainModel;

import io.reactivex.disposables.Disposable;

/**
 * @author yqy
 * @date on 2018/1/15
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class StudentInfoActivityPresenter extends BasePresenterImpl<StudentActivityContract.View> implements StudentActivityContract.Presenter {

    private MainModel mainModel;

    public StudentInfoActivityPresenter(StudentActivityContract.View view) {
        super(view);
        mainModel = new MainModel();
    }


    @Override
    public void detach() {
        super.detach();
        mainModel = null;
    }



    @Override
    public void getStudentDetailInfo(String userId) {

    }
}

