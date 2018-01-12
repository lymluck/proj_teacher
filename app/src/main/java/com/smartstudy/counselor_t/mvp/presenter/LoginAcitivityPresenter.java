package com.smartstudy.counselor_t.mvp.presenter;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.counselor_t.mvp.base.BasePresenterImpl;
import com.smartstudy.counselor_t.mvp.contract.LoginActivityContract;
import com.smartstudy.counselor_t.mvp.model.LoginModel;
import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.util.SPCacheUtils;

import io.reactivex.disposables.Disposable;


/**
 * Created by louis on 2017/3/4.
 */

public class LoginAcitivityPresenter extends BasePresenterImpl<LoginActivityContract.View> implements LoginActivityContract.Presenter {

    private LoginModel loginModel;

    public LoginAcitivityPresenter(LoginActivityContract.View view) {
        super(view);
        loginModel = new LoginModel();
    }

    @Override
    public void getPhoneCode(String phone) {
        loginModel.getPhoneCode(phone, new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String s) {
                view.getPhoneCodeSuccess();
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void phoneCodeLogin(final String phone, final String code) {
        loginModel.phoneCodeLogin(phone, code, new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String result) {
                Log.d("login======", result);
                JSONObject data = JSON.parseObject(result);
                if (data != null) {
                    SPCacheUtils.put("ticket", data.getString("ticket"));
                    SPCacheUtils.put("imToken", data.getString("imToken"));
                    view.phoneCodeLoginSuccess(false);
                    String id = data.getString("id");
                    String phone = data.getString("phone");
                    String name = data.getString("name");
                    String avatar = data.getString("avatar");
                    String orgId = data.getString("orgId");
                    String title = data.getString("title");
                    String imToken = data.getString("imToken");
                    String imUserId = data.getString("imUserId");
                    String ticket = data.getString("ticket");

                    SPCacheUtils.put("id", id);
                    SPCacheUtils.put("phone", phone);
                    SPCacheUtils.put("name", name);
                    SPCacheUtils.put("avatar", avatar);
                    SPCacheUtils.put("ticket", ticket);
                    SPCacheUtils.put("orgId", orgId);
                    SPCacheUtils.put("title", title);
                    SPCacheUtils.put("imToken", imToken);
                    SPCacheUtils.put("imUserId", imUserId);
                    view.getPhoneCodeSuccess();
                }
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void detach() {
        super.detach();
        loginModel = null;
    }
}
