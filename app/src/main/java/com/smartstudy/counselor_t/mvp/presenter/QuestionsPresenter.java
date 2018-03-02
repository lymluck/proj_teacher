package com.smartstudy.counselor_t.mvp.presenter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.DataListInfo;
import com.smartstudy.counselor_t.entity.QuestionInfo;
import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BasePresenterImpl;
import com.smartstudy.counselor_t.mvp.contract.QaListContract;
import com.smartstudy.counselor_t.mvp.model.QuestionsModel;
import com.smartstudy.counselor_t.util.Utils;

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
    public void getQuestions(boolean answered, int page, final int request_state) {
        questionsModel.getQuestions(answered, page, new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String result) {
                DataListInfo dataListInfo = JSON.parseObject((String) result, DataListInfo.class);
                List<QuestionInfo> data = JSON.parseArray(dataListInfo.getData(), QuestionInfo.class);
                dataListInfo = null;
                QuestionInfo questionInfo = new QuestionInfo();
                questionInfo.setAnswerCount(2);
                questionInfo.setContent("测试问题：天有多高？");
                questionInfo.setCreateTime("2017-05-02");
                QuestionInfo.Asker answerer = new QuestionInfo.Asker();
                answerer.setAvatar("https://bkd-media.smartstudy.com/user/avatar/default/c1/96/c196d8a6bc7b1685bef28f7c8bb140984692.jpg");
                answerer.setName("小明");
                questionInfo.setAsker(answerer);
                data.add(questionInfo);
                if (data != null) {
                    view.getQuestionsSuccess(data, request_state);
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
    public void getMyQuestions(int page, final int request_state) {
        questionsModel.getMyQuestions(page, new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String result) {
                DataListInfo dataListInfo = JSON.parseObject(result, DataListInfo.class);
                List<QuestionInfo> data = JSON.parseArray(dataListInfo.getData(), QuestionInfo.class);
                dataListInfo = null;
                if (data != null) {
                    view.getQuestionsSuccess(data, request_state);
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
    public void getSchoolQa(String schoolId, int page, final int request_state) {

        questionsModel.getSchoolQa(schoolId, page, new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String result) {
                DataListInfo dataListInfo = JSON.parseObject(result, DataListInfo.class);
                List<QuestionInfo> data = JSON.parseArray(dataListInfo.getData(), QuestionInfo.class);
                dataListInfo = null;
                if (data != null) {
                    view.getQuestionsSuccess(data, request_state);
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
//            DisplayImageUtils.displayGif(context, R.drawable.gif_data_loading, iv_loading);
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