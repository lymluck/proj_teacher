package com.smartstudy.counselor_t.mvp.presenter;

import com.alibaba.fastjson.JSON;
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
    public void getQaDetails(final String id, final int request_state) {
        qaDetailModel.getQaDetail(id, new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String result) {
                QaDetailInfo qaDetailInfo = JSON.parseObject(result, QaDetailInfo.class);
                if (qaDetailInfo != null) {
                    view.getQaDetails(qaDetailInfo);
                }
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }

        });
    }


    @Override
    public void checkFavorite(String id) {
        qaDetailModel.checkFavorite(id, new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String result) {
                if (result != null) {
                    view.checkFavorite(Boolean.parseBoolean(result));
                }
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void addFavorite(String id) {
        qaDetailModel.addFavorite(id, new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String result) {
                if (result != null) {
                    view.addFavorite(Boolean.parseBoolean(result));
                }
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void postQuestion(String id, String question) {
        qaDetailModel.postQuestion(id, question, new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String result) {
                view.postQuestionSuccess();
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }
}