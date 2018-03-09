package com.smartstudy.counselor_t.mvp.presenter;

import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BasePresenterImpl;
import com.smartstudy.counselor_t.mvp.contract.ReloadQaContract;
import com.smartstudy.counselor_t.mvp.model.ReloadQaModel;

import java.io.File;

import io.reactivex.disposables.Disposable;

/**
 * @author yqy
 * @date on 2018/3/9
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class ReloadQaPresenter extends BasePresenterImpl<ReloadQaContract.View> implements ReloadQaContract.Presenter {

    private ReloadQaModel reloadQaModel;

    public ReloadQaPresenter(ReloadQaContract.View view) {
        super(view);
        reloadQaModel = new ReloadQaModel();
    }

    @Override
    public void detach() {
        super.detach();
        reloadQaModel = null;
    }


    @Override
    public void postAnswerVoice(String id, File voice) {

        reloadQaModel.postAnswerVoice(id, voice, new ObserverListener<String>() {
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
                view.showTip(msg);
            }
        });
    }
}