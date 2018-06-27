package study.smart.transfer_management.mvp.presenter;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

import io.reactivex.disposables.Disposable;
import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BasePresenterImpl;
import study.smart.transfer_management.entity.TransferManagerEntity;
import study.smart.transfer_management.mvp.contract.TransferManagerListContract;
import study.smart.transfer_management.mvp.model.TransferManagerListModel;


/**
 * @author yqy
 * @date on 2018/6/27
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TransferManagerPresenter extends BasePresenterImpl<TransferManagerListContract.View> implements TransferManagerListContract.Presenter {

    private TransferManagerListModel transferManagerListModel;

    public TransferManagerPresenter(TransferManagerListContract.View view) {
        super(view);
        transferManagerListModel = new TransferManagerListModel();
    }


    @Override
    public void detach() {
        super.detach();
        transferManagerListModel = null;
    }

    @Override
    public void getTransferList(String type, String page, final int request_state) {
        transferManagerListModel.getTransferManagerList(type, page, new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String result) {
                Log.w("kim", "--->" + result);
                List<TransferManagerEntity> transferManagerEntities = JSONObject.parseArray(result, TransferManagerEntity.class);
                if (transferManagerEntities != null) {
                    view.getTransferListSuccess(transferManagerEntities, request_state);
                }
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void showLoading(Context context, View emptyView) {

    }

    @Override
    public void setEmptyView(View emptyView) {

    }
}