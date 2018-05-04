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
import com.smartstudy.counselor_t.mvp.contract.MyFocusContract;
import com.smartstudy.counselor_t.mvp.contract.QaListContract;
import com.smartstudy.counselor_t.mvp.model.MyFocusQuestionModel;
import com.smartstudy.counselor_t.mvp.model.QuestionsModel;
import com.smartstudy.counselor_t.util.DisplayImageUtils;
import com.smartstudy.counselor_t.util.Utils;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * @author yqy
 * @date on 2018/5/3
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyFocusQuestionPresenter extends BasePresenterImpl<MyFocusContract.View> implements MyFocusContract.Presenter {

    private MyFocusQuestionModel myFocusQuestionModel;

    public MyFocusQuestionPresenter(MyFocusContract.View view) {
        super(view);
        myFocusQuestionModel = new MyFocusQuestionModel();
    }


    @Override
    public void detach() {
        super.detach();
        myFocusQuestionModel = null;
    }


    @Override
    public void getMyFocusQuestions(int page, final int request_state) {
        myFocusQuestionModel.getMyFocusQuestions(page, new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String result) {
                DataListInfo dataListInfo = JSON.parseObject(result, DataListInfo.class);
                List<QuestionInfo> data = JSON.parseArray(dataListInfo.getData(), QuestionInfo.class);
                if (data != null) {
                    view.getMyFocusQuestionsSuccess(data, request_state);
                }
                data = null;
                dataListInfo = null;
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
        iv_err.setImageResource(R.drawable.ic_no_collection);
        tv_err_tip.setText("你还没有重点关注过用户哦");
        view.showEmptyView(emptyView);
        context = null;
        emptyView = null;
    }
}
