package com.smartstudy.counselor_t.mvp.presenter;


import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BasePresenterImpl;
import com.smartstudy.counselor_t.mvp.contract.LoginActivityContract;
import com.smartstudy.counselor_t.mvp.model.LoginModel;
import com.smartstudy.counselor_t.util.IMUtils;
import com.smartstudy.counselor_t.util.SPCacheUtils;

import io.reactivex.disposables.Disposable;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;


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
                    String id = data.getString("id");
                    String phone = data.getString("phone");
                    String name = data.getString("name");
                    String avatar = data.getString("avatar");
                    String orgId = data.getString("orgId");
                    String title = data.getString("title");
                    String imToken = data.getString("imToken");
                    String imUserId = data.getString("imUserId");
                    String ticket = data.getString("ticket");
                    int status = data.getInteger("status");

                    SPCacheUtils.put("id", id);
                    SPCacheUtils.put("phone", phone);
                    SPCacheUtils.put("name", name);
                    SPCacheUtils.put("avatar", avatar);
                    SPCacheUtils.put("ticket", ticket);
                    SPCacheUtils.put("orgId", orgId);
                    SPCacheUtils.put("title", title);
                    SPCacheUtils.put("imToken", imToken);
                    view.phoneCodeLoginSuccess(status);
                    //登录融云
                    loginRongIM(imToken, name, avatar);
                }
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    private void loginRongIM(String imToken, final String userName, final String avatar) {
        if (!TextUtils.isEmpty(imToken)) {
            RongIM.connect(imToken, new RongIMClient.ConnectCallback() {

                @Override
                public void onSuccess(String userid) {
                    //登录成功后保存imUserId
                    SPCacheUtils.put("imUserId", userid);
                    //更新用户信息
//                    RongIM.getInstance().refreshUserInfoCache(new UserInfo(userid, userName, Uri.parse(avatar)));
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.d("RongIM===", errorCode.getMessage());
                }

                @Override
                public void onTokenIncorrect() {
                    IMUtils.reGetToken();
                }
            });
        }
    }

    @Override
    public void detach() {
        super.detach();
        loginModel = null;
    }
}
