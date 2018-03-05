package com.smartstudy.counselor_t.mvp.presenter;

import com.alibaba.fastjson.JSON;
import com.smartstudy.counselor_t.entity.Answerer;
import com.smartstudy.counselor_t.entity.Asker;
import com.smartstudy.counselor_t.entity.QaDetailInfo;
import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BasePresenterImpl;
import com.smartstudy.counselor_t.mvp.contract.QaDetailContract;
import com.smartstudy.counselor_t.mvp.model.QaDetailModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by yqy on 2017/12/4.
 */

public class QaDetailPresenter extends BasePresenterImpl<QaDetailContract.View> implements QaDetailContract.Presenter {

    private QaDetailModel qaDetailModel;

    public QaDetailPresenter(QaDetailContract.View view) {
        super(view);
        qaDetailModel = new QaDetailModel();
    }

    @Override
    public void detach() {
        super.detach();
        qaDetailModel = null;
    }

    @Override
    public void getQaDetails(final String id) {
        qaDetailModel.getQaDetail(id, new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String result) {
                QaDetailInfo qaDetailInfo = JSON.parseObject(result, QaDetailInfo.class);
                if (qaDetailInfo == null) {
                    qaDetailInfo = new QaDetailInfo();
                    qaDetailInfo.setCreateTime("2018-05");
                    qaDetailInfo.setContent("我是中国人");
                    Asker asker = new Asker();
                    asker.setName("linkang");
                    asker.setAvatar("https://bkd-media.smartstudy.com/user/avatar/default/c1/96/c196d8a6bc7b1685bef28f7c8bb140984692.jpg");
                    qaDetailInfo.setAsker(asker);

                    List<Answerer> answers = new ArrayList<>();
                    Answerer answerer = new Answerer();
                    answerer.setContent("哈哈哈哈哈");
                    answerer.setCreateTime("2017-03");
                    Answerer.Commenter asker2 = new Answerer.Commenter();
                    asker.setName("yeqingyu");
                    asker.setAvatar("https://bkd-media.smartstudy.com/user/avatar/default/c1/96/c196d8a6bc7b1685bef28f7c8bb140984692.jpg");
                    answerer.setCommenter(asker2);
                    List<Answerer.Comments> comments=new ArrayList<>();
                    Answerer.Comments comment=new Answerer.Comments();
                    comment.setContent("222222");
                    comments.add(comment);
                    Answerer.Comments comment2=new Answerer.Comments();
                    comment.setContent("3333");
                    comments.add(comment2);
                    answerer.setComments(comments);
                    answers.add(answerer);
                }
                if (qaDetailInfo != null) {
                    view.getQaDetails(qaDetailInfo);
                }
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }

        });
    }


    @Override
    public void postQuestion(String id, String question) {
        qaDetailModel.postQuestion(id, question, new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String result) {
                view.postQuestionSuccess();
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }
}