package study.smart.transfer_management.mvp.presenter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.util.List;

import io.reactivex.disposables.Disposable;
import study.smart.baselib.entity.DataListInfo;
import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BasePresenterImpl;
import study.smart.baselib.utils.DisplayImageUtils;
import study.smart.baselib.utils.Utils;
import study.smart.transfer_management.R;
import study.smart.baselib.entity.MessageDetailItemInfo;
import study.smart.transfer_management.mvp.contract.TransferManagerMessageDetailContract;
import study.smart.transfer_management.mvp.model.TransferManagerMessageDetailModel;

/**
 * @author yqy
 * @date on 2018/7/13
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TransferManagerMessageDetailPresenter extends BasePresenterImpl<TransferManagerMessageDetailContract.View> implements TransferManagerMessageDetailContract.Presenter {

    private TransferManagerMessageDetailModel transferManagerMessageDetailModel;

    public TransferManagerMessageDetailPresenter(TransferManagerMessageDetailContract.View view) {
        super(view);
        transferManagerMessageDetailModel = new TransferManagerMessageDetailModel();
    }


    @Override
    public void detach() {
        super.detach();
        transferManagerMessageDetailModel = null;
    }


    @Override
    public void getMessageDetailList(String page, String type, final int request_state) {
        transferManagerMessageDetailModel.getMessagesDetailList(page, type, new ObserverListener<DataListInfo>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(DataListInfo dataListInfo) {
                List<MessageDetailItemInfo> detailItemInfoList = JSON.parseArray(dataListInfo.getData(), MessageDetailItemInfo.class);
                if (detailItemInfoList != null) {
                    view.getMessageDetailSuccess(detailItemInfoList, request_state);
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
        ImageView iv_err = (ImageView) emptyView.findViewById(R.id.iv_err);
        TextView tv_err_tip = (TextView) emptyView.findViewById(R.id.tv_err_tip);
        if (!Utils.isNetworkConnected()) {
            emptyView.findViewById(R.id.llyt_err).setVisibility(View.VISIBLE);
            emptyView.findViewById(R.id.iv_loading).setVisibility(View.GONE);
            iv_err.setImageResource(R.drawable.ic_net_err);
            tv_err_tip.setText(context.getString(R.string.no_net_tip));
        } else {
            emptyView.findViewById(R.id.llyt_err).setVisibility(View.GONE);
            ImageView iv_loading = (ImageView) emptyView.findViewById(R.id.iv_loading);
            iv_loading.setVisibility(View.VISIBLE);
            DisplayImageUtils.displayGif(context, R.drawable.gif_data_loading, iv_loading);
        }
        view.showEmptyView(emptyView);
        context = null;
        emptyView = null;
    }

    @Override
    public void setEmptyView(View emptyView) {
        emptyView.findViewById(R.id.llyt_err).setVisibility(View.VISIBLE);
        emptyView.findViewById(R.id.iv_loading).setVisibility(View.GONE);
        view.showEmptyView(emptyView);
        emptyView = null;
    }
}

