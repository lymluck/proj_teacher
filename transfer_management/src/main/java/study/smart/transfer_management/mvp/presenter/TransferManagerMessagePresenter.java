package study.smart.transfer_management.mvp.presenter;

import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import study.smart.baselib.entity.DataListInfo;
import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BasePresenterImpl;
import study.smart.transfer_management.entity.MessageInfo;
import study.smart.transfer_management.mvp.contract.TransferManagerMessageContract;
import study.smart.transfer_management.mvp.model.TransferManagerMessageModel;

/**
 * @author yqy
 * @date on 2018/7/13
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TransferManagerMessagePresenter extends BasePresenterImpl<TransferManagerMessageContract.View> implements TransferManagerMessageContract.Presenter {

    private TransferManagerMessageModel transferManagerMessageModel;

    public TransferManagerMessagePresenter(TransferManagerMessageContract.View view) {
        super(view);
        transferManagerMessageModel = new TransferManagerMessageModel();
    }


    @Override
    public void detach() {
        super.detach();
        transferManagerMessageModel = null;
    }


    @Override
    public void getMessageInfos() {
        transferManagerMessageModel.getMessages(new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String result) {
                List<MessageInfo> data = JSON.parseArray(result, MessageInfo.class);
                if (data != null) {
                    view.getMessageInfoSuccess(data);
                }
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }
}
