package com.smartstudy.counselor_t.mvp.presenter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.DistributionInfo;
import com.smartstudy.counselor_t.mvp.contract.DistributionContract;
import com.smartstudy.counselor_t.mvp.contract.TransferQaDetailContract;
import com.smartstudy.counselor_t.mvp.model.DistributionModel;
import com.smartstudy.counselor_t.mvp.model.TransferQaDetailModel;

import java.util.List;

import io.reactivex.disposables.Disposable;
import study.smart.baselib.entity.DataListInfo;
import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BasePresenterImpl;
import study.smart.baselib.utils.DisplayImageUtils;
import study.smart.baselib.utils.Utils;

/**
 * @author yqy
 * @date on 2018/8/17
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TransferQaDetailPresenter extends BasePresenterImpl<TransferQaDetailContract.View> implements TransferQaDetailContract.Presenter {

    private TransferQaDetailModel transferQaDetailModel;

    public TransferQaDetailPresenter(TransferQaDetailContract.View view) {
        super(view);
        transferQaDetailModel = new TransferQaDetailModel();
    }

    @Override
    public void detach() {
        super.detach();
        transferQaDetailModel = null;
    }

    @Override
    public void postReceive(String questionId, String sendesId) {
        transferQaDetailModel.postQuestionReceive(questionId, sendesId, new ObserverListener() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(Object result) {
                view.postReceiveSuccess();
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

}

