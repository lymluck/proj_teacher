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
                JSONObject data = JSON.parseObject(result);
                if (data != null) {
                    boolean created = data.getBoolean("created");
                    String ss_user = data.getString("ssUser");
                    JSONObject user_data = data.getJSONObject("user");
                    JSONObject person_data = user_data.getJSONObject("info");
                    String ticket = data.getString("ticket");
                    String user_account = user_data.getString("phone");
                    String user_id = user_data.getString("id");
                    int xxd_unread = user_data.getIntValue("notificationUnreadCount");
                    if (person_data.getJSONObject("targetSection") != null) {
                        if (person_data.getJSONObject("targetSection").getJSONObject("targetCountry") != null) {
                            SPCacheUtils.put("target_countryInfo", person_data.getJSONObject("targetSection").getJSONObject("targetCountry").toString());
                        }
                        if (person_data.getJSONObject("targetSection").getJSONObject("targetDegree") != null) {
                            SPCacheUtils.put("project_name", person_data.getJSONObject("targetSection").getJSONObject("targetDegree").getString("name"));
                        }
                    }
                    SPCacheUtils.put("user_name", user_data.getString("name"));
                    SPCacheUtils.put("user_pic", user_data.getString("avatar"));
                    SPCacheUtils.put("user_account", user_account);
                    SPCacheUtils.put("ss_user", ss_user);
                    SPCacheUtils.put("ticket", ticket);
                    SPCacheUtils.put("user", user_data.getString("info"));
                    SPCacheUtils.put("xxd_unread", xxd_unread);
                    SPCacheUtils.put("user_id", user_id);
                    view.phoneCodeLoginSuccess(created, user_id);
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
