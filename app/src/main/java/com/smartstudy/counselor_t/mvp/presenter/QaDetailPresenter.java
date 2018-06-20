package com.smartstudy.counselor_t.mvp.presenter;

import com.smartstudy.counselor_t.entity.QaDetailInfo;
import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BasePresenterImpl;
import com.smartstudy.counselor_t.mvp.contract.QaDetailContract;
import com.smartstudy.counselor_t.mvp.model.QaDetailModel;

import io.reactivex.disposables.Disposable;

/**
 * Created by yqy on 2017/12/4.
 */

public class QaDetailPresenter extends BasePresenterImpl<QaDetailContract.View> implements QaDetailContract.Presenter {

    private QaDetailModel qaDetailModel;

    public QaDetailPresenter(QaDetailContract.View view) {
        super(view);
        qaDetailModel = new QaDetailModel();
    }

    @Override
    public void detach() {
        super.detach();
        qaDetailModel = null;
    }

    @Override
    public void getQaDetails(final String id) {
        qaDetailModel.getQaDetail(id, new ObserverListener<QaDetailInfo>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(QaDetailInfo result) {
                if (result != null) {
                    view.getQaDetails(result);
                }
            }

            @Override
            public void onError(String msg) {
                view.getQaDetailFail(msg);
            }

        });
    }

    @Override
    public void postAnswerText(String id, String answer) {
        qaDetailModel.postAnswerText(id, answer, new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String result) {
                view.postAnswerSuccess();
            }

            @Override
            public void onError(String msg) {
                view.postAnswerFail(msg);
            }
        });
    }

    @Override
    public void questionAddMark(String questionId) {
        qaDetailModel.questionAddMark(questionId, new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String result) {
                view.questionAddMarkSuccess();
            }

            @Override
            public void onError(String msg) {
                view.postAnswerFail(msg);
            }
        });
    }

    @Override
    public void questionDeleteMark(String questionId) {
        qaDetailModel.questionDeleteMark(questionId, new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String result) {
                view.questionDeleteMarkSuccess();
            }

            @Override
            public void onError(String msg) {
                view.postAnswerFail(msg);
            }
        });
    }

}