package com.smartstudy.counselor_t.mvp.presenter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import study.smart.baselib.entity.DataListInfo;
import study.smart.baselib.entity.TeacherRankInfo;
import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BasePresenterImpl;
import study.smart.baselib.utils.DisplayImageUtils;
import study.smart.baselib.utils.Utils;

import com.alibaba.fastjson.JSONObject;
import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.QuestionInfo;
import com.smartstudy.counselor_t.mvp.contract.QaListContract;
import com.smartstudy.counselor_t.mvp.model.QuestionsModel;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by louis on 2017/3/2.
 */

public class QuestionsPresenter extends BasePresenterImpl<QaListContract.View> implements QaListContract.Presenter {

    private QuestionsModel questionsModel;

    public QuestionsPresenter(QaListContract.View view) {
        super(view);
        questionsModel = new QuestionsModel();
    }


    @Override
    public void detach() {
        super.detach();
        questionsModel = null;
    }

    @Override
    public void getQuestions(int page, final int request_state) {
        questionsModel.getQuestions(page, new ObserverListener<DataListInfo>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(DataListInfo result) {
                int subCount = 0;
                List<QuestionInfo> data = JSON.parseArray(result.getData(), QuestionInfo.class);
                if (JSON.parseObject(result.getMeta()) != null) {
                    if (JSON.parseObject(result.getMeta()).containsKey("totalSubQuestionCountToMe")) {
                        subCount = JSON.parseObject(result.getMeta()).getIntValue("totalSubQuestionCountToMe");
                    }
                }
                if (data != null) {
                    view.getQuestionsSuccess(subCount, data, request_state);
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
    public void getTeacherRankImage(String type, String page) {
        questionsModel.getTeacherRank(type, page, new ObserverListener<DataListInfo>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(DataListInfo dataListInfo) {
                List<TeacherRankInfo> teacherRankInfos = JSONObject.parseArray(dataListInfo.getData(), TeacherRankInfo.class);
                if (teacherRankInfos != null) {
                    view.getTeacherRankSuccess(teacherRankInfos);
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
    public void setEmptyView(Context context, View emptyView, String flag) {
        emptyView.findViewById(R.id.llyt_err).setVisibility(View.VISIBLE);
        emptyView.findViewById(R.id.iv_loading).setVisibility(View.GONE);
        ImageView iv_err = (ImageView) emptyView.findViewById(R.id.iv_err);
        TextView tv_err_tip = (TextView) emptyView.findViewById(R.id.tv_err_tip);
        if (!"list".equals(flag)) {
            iv_err.setImageResource(R.drawable.ic_no_question);
            tv_err_tip.setText(context.getString(R.string.no_qa_tip));
        }
        view.showEmptyView(emptyView);
        context = null;
        emptyView = null;
    }
}