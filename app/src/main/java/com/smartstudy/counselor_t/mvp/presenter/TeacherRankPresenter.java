package com.smartstudy.counselor_t.mvp.presenter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.mvp.contract.TeacherRankContract;
import com.smartstudy.counselor_t.mvp.model.TeacherRankModel;

import java.util.List;

import io.reactivex.disposables.Disposable;
import study.smart.baselib.entity.DataListInfo;
import study.smart.baselib.entity.TeacherRankInfo;
import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BasePresenterImpl;
import study.smart.baselib.utils.DisplayImageUtils;
import study.smart.baselib.utils.Utils;

/**
 * @author yqy
 * @date on 2018/7/18
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TeacherRankPresenter extends BasePresenterImpl<TeacherRankContract.View> implements TeacherRankContract.Presenter {

    private TeacherRankModel teacherRankModel;

    public TeacherRankPresenter(TeacherRankContract.View view) {
        super(view);
        teacherRankModel = new TeacherRankModel();
    }

    @Override
    public void detach() {
        super.detach();
        teacherRankModel = null;
    }

    @Override
    public void getTeacherRank(String type, String page, final int request_state) {
        teacherRankModel.getTeacherRank(type, page, new ObserverListener<DataListInfo>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(DataListInfo dataListInfo) {
                List<TeacherRankInfo> teacherRankInfos = JSONObject.parseArray(dataListInfo.getData(), TeacherRankInfo.class);
                if (teacherRankInfos != null) {
                    view.getTransferListSuccess(teacherRankInfos, request_state);
                }
                JSONObject jsonObject = JSONObject.parseObject(dataListInfo.getMeta());
                if (jsonObject != null && jsonObject.containsKey("myself")) {
                    TeacherRankInfo teacherRankInfo = JSONObject.parseObject(jsonObject.getString("myself"), TeacherRankInfo.class);
                    view.showMySelf(teacherRankInfo);
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