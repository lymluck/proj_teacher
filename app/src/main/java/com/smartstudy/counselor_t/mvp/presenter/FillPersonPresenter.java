package com.smartstudy.counselor_t.mvp.presenter;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.counselor_t.entity.StudentPageInfo;
import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BasePresenterImpl;
import com.smartstudy.counselor_t.mvp.contract.FillPersonContract;
import com.smartstudy.counselor_t.mvp.model.FillPersonModel;

import java.io.File;

import io.reactivex.disposables.Disposable;
import okhttp3.MultipartBody;

/**
 * @author yqy
 * @date on 2018/1/18
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class FillPersonPresenter extends BasePresenterImpl<FillPersonContract.View> implements FillPersonContract.Presenter {

    private FillPersonModel fillPersonModel;

    public FillPersonPresenter(FillPersonContract.View view) {
        super(view);
        fillPersonModel = new FillPersonModel();
    }


    @Override
    public void detach() {
        super.detach();
        fillPersonModel = null;
    }

    @Override
    public void postPersonInfo(String name, File avatar, String title, String school, String yearsOfWorking, String email, String realName) {
        fillPersonModel.postPersonInfo(name, avatar, title, school, yearsOfWorking, email, realName, new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String s) {
               Log.w("kim","------>"+s);
               view.getStudentInfoDetailSuccess();
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }
}

