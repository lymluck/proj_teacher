package com.smartstudy.counselor_t.mvp.presenter;

import android.text.TextUtils;

import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BasePresenterImpl;
import com.smartstudy.counselor_t.mvp.contract.OtherTeacherTagContract;
import com.smartstudy.counselor_t.mvp.model.OtherTeacherTagModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * @author yqy
 * @date on 2018/5/4
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class OtherTeacherTagPresenter extends BasePresenterImpl<OtherTeacherTagContract.View> implements OtherTeacherTagContract.Presenter {

    private OtherTeacherTagModel otherTeacherTagModel;

    public OtherTeacherTagPresenter(OtherTeacherTagContract.View view) {
        super(view);
        otherTeacherTagModel = new OtherTeacherTagModel();
    }

    @Override
    public void detach() {
        super.detach();
        otherTeacherTagModel = null;
    }

    @Override
    public void getOtherTeacherTag(String userId) {
        otherTeacherTagModel.getOtherTeacherTag(userId, new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String result) {
                String results = result.replace("[", "");
                results = results.replace("]", "");
                results = results.replace("\"", "");
                List<String> list = new ArrayList<>();
                if (!TextUtils.isEmpty(results)) {
                    String[] tags = results.split(",");
                    if (tags != null && tags.length > 0) {
                        for (String tag : tags) {
                            list.add(tag);
                        }
                    }
                }
                view.getOtherTeacherTagSueccess(list);
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }
}
