package com.smartstudy.counselor_t.mvp.presenter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.AddGoodInfo;
import com.smartstudy.counselor_t.entity.DataListInfo;
import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BasePresenterImpl;
import com.smartstudy.counselor_t.mvp.contract.AddGoodDetailContract;
import com.smartstudy.counselor_t.mvp.model.AddGoodDetailModel;
import com.smartstudy.counselor_t.util.DisplayImageUtils;
import com.smartstudy.counselor_t.util.Utils;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * @author yqy
 * @date on 2018/5/16
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class AddGoodDetailPresenter extends BasePresenterImpl<AddGoodDetailContract.View> implements AddGoodDetailContract.Presenter {

    private AddGoodDetailModel addGoodDetailModel;

    public AddGoodDetailPresenter(AddGoodDetailContract.View view) {
        super(view);
        addGoodDetailModel = new AddGoodDetailModel();
    }

    @Override
    public void detach() {
        super.detach();
        addGoodDetailModel = null;
    }

    @Override
    public void getAddGoodDetail(int page, final int request_state) {
        addGoodDetailModel.getAddGoodDetail(page, new ObserverListener<DataListInfo>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(DataListInfo result) {
                List<AddGoodInfo> data = JSON.parseArray(result.getData(), AddGoodInfo.class);
                if (data != null) {
                    view.getAddGoodDetailSucess(data, request_state);
                    data = null;
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
    public void setEmptyView(Context context, View emptyView) {
        emptyView.findViewById(R.id.llyt_err).setVisibility(View.VISIBLE);
        emptyView.findViewById(R.id.iv_loading).setVisibility(View.GONE);
        ImageView iv_err = (ImageView) emptyView.findViewById(R.id.iv_err);
        TextView tv_err_tip = (TextView) emptyView.findViewById(R.id.tv_err_tip);
        iv_err.setVisibility(View.INVISIBLE);
        tv_err_tip.setText("暂时还没有人点赞哦");
        view.showEmptyView(emptyView);
        context = null;
        emptyView = null;
    }

}