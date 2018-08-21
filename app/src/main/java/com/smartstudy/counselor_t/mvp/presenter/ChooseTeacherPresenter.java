package com.smartstudy.counselor_t.mvp.presenter;

import com.alibaba.fastjson.JSON;
import com.smartstudy.counselor_t.mvp.contract.ChooseTeacherContract;
import com.smartstudy.counselor_t.mvp.model.ChooseTeacherModel;

import java.util.List;

import io.reactivex.disposables.Disposable;
import study.smart.baselib.entity.CityTeacherInfo;
import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BasePresenterImpl;

/**
 * @author yqy
 * @date on 2018/8/15
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class ChooseTeacherPresenter extends BasePresenterImpl<ChooseTeacherContract.View> implements ChooseTeacherContract.Presenter {

    private ChooseTeacherModel chooseTeacherModel;

    public ChooseTeacherPresenter(ChooseTeacherContract.View view) {
        super(view);
        chooseTeacherModel = new ChooseTeacherModel();
    }

    @Override
    public void detach() {
        super.detach();
        chooseTeacherModel = null;
    }

    @Override
    public void getTeacherList() {
        chooseTeacherModel.getTeacherList(new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String result) {
                List<CityTeacherInfo> cityTeacherInfo = JSON.parseArray(result, CityTeacherInfo.class);
                if (cityTeacherInfo != null && cityTeacherInfo.size() > 0) {
                    view.getTeacherListSuccess(cityTeacherInfo);
                }
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void shareQuestion(String questionId, String receiverId, String note) {
        chooseTeacherModel.shareQuestion(questionId, receiverId, note, new ObserverListener() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(Object result) {
                view.shareQuestionSuccess();
            }

            @Override
            public void onError(String msg) {
                view.shareQuestionFail(msg);
            }
        });
    }
}
