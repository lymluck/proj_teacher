package com.smartstudy.counselor_t.mvp.presenter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.DataListInfo;
import com.smartstudy.counselor_t.entity.QuestionInfo;
import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BasePresenterImpl;
import com.smartstudy.counselor_t.mvp.contract.MyQaFragmentContract;
import com.smartstudy.counselor_t.mvp.contract.QaListContract;
import com.smartstudy.counselor_t.mvp.model.MyQaFragmentModel;
import com.smartstudy.counselor_t.mvp.model.QuestionsModel;
import com.smartstudy.counselor_t.util.DisplayImageUtils;
import com.smartstudy.counselor_t.util.Utils;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * @author yqy
 * @date on 2018/3/20
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyQaFragmentPresenter extends BasePresenterImpl<MyQaFragmentContract.View> implements MyQaFragmentContract.Presenter {

    private MyQaFragmentModel myQaFragmentModel;

    public MyQaFragmentPresenter(MyQaFragmentContract.View view) {
        super(view);
        myQaFragmentModel = new MyQaFragmentModel();
    }


    @Override
    public void detach() {
        super.detach();
        myQaFragmentModel = null;
    }


    @Override
    public void getMyQuestions(int page, final int request_state) {
        myQaFragmentModel.getMyQuestions(page, new ObserverListener<String>() {
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
                    view.getQuestionsSuccess(dataListInfo.getMeta().getTotalSubQuestionCountToMe(), data, request_state);
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