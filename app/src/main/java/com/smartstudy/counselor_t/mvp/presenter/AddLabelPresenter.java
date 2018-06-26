package com.smartstudy.counselor_t.mvp.presenter;

import android.text.TextUtils;

import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BasePresenterImpl;
import com.smartstudy.counselor_t.mvp.contract.AddLabelContract;
import com.smartstudy.counselor_t.mvp.model.AddLabelModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * @author yqy
 * @date on 2018/5/3
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class AddLabelPresenter extends BasePresenterImpl<AddLabelContract.View> implements AddLabelContract.Presenter {

    private AddLabelModel addLabelModel;

    public AddLabelPresenter(AddLabelContract.View view) {
        super(view);
        addLabelModel = new AddLabelModel();
    }

    @Override
    public void detach() {
        super.detach();
        addLabelModel = null;
    }

    @Override
    public void getMyStudentTag(String id) {
        addLabelModel.getMyStudentTags(id, new ObserverListener<String>() {
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
                view.getMyTagSuccess(list);
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void submitMyStudentTag(String id, List<String> tags) {
        addLabelModel.submitMyStudentTag(id, tags, new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String result) {
                view.subitMyStudentSuccess();
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });

    }

    @Override
    public void postHistoryTag(List<String> tags) {
        addLabelModel.postHistoryTag(tags, new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String result) {
                view.postHistoryTagSueccess();
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void getHisToryTag() {
        addLabelModel.getHisToryTag(new ObserverListener<String>() {
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
                view.getHistoryTagSueccess(list);
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }
}
