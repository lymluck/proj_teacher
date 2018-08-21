package com.smartstudy.counselor_t.mvp.presenter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.DistributionInfo;
import com.smartstudy.counselor_t.mvp.contract.DistributionContract;
import com.smartstudy.counselor_t.mvp.model.DistributionModel;

import java.util.List;

import io.reactivex.disposables.Disposable;
import study.smart.baselib.entity.DataListInfo;
import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BasePresenterImpl;
import study.smart.baselib.utils.DisplayImageUtils;
import study.smart.baselib.utils.Utils;

/**
 * @author yqy
 * @date on 2018/8/16
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class DistributionPresenter extends BasePresenterImpl<DistributionContract.View> implements DistributionContract.Presenter {

    private DistributionModel distributionModel;

    public DistributionPresenter(DistributionContract.View view) {
        super(view);
        distributionModel = new DistributionModel();
    }

    @Override
    public void detach() {
        super.detach();
        distributionModel = null;
    }

    @Override
    public void getShareQuestion(String type, String page, final int request_state) {
        distributionModel.getTeacherList(type, page, new ObserverListener<DataListInfo>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(DataListInfo dataListInfo) {
                List<DistributionInfo> distributionInfo = JSON.parseArray(dataListInfo.getData(), DistributionInfo.class);
                if (distributionInfo != null) {
                    view.getShareQuestionSuccess(distributionInfo, request_state);
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
        ImageView iv_err = (ImageView) emptyView.findViewById(R.id.iv_err);
        TextView tv_err_tip = (TextView) emptyView.findViewById(R.id.tv_err_tip);
        iv_err.setImageResource(R.drawable.ic_no_question);
        tv_err_tip.setText("还没有相关转介绍哦");
        view.showEmptyView(emptyView);
        emptyView = null;
    }
}
