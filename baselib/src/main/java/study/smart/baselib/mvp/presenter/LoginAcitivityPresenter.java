package study.smart.baselib.mvp.presenter;


import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import io.reactivex.disposables.Disposable;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;
import study.smart.baselib.BaseApplication;
import study.smart.baselib.R;
import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BasePresenterImpl;
import study.smart.baselib.mvp.contract.LoginActivityContract;
import study.smart.baselib.mvp.model.LoginModel;
import study.smart.baselib.utils.DisplayImageUtils;
import study.smart.baselib.utils.IMUtils;
import study.smart.baselib.utils.SPCacheUtils;


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
        loginModel.phoneCodeLogin(phone, code, new ObserverListener<JSONObject>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(JSONObject result) {
                if (result != null) {
                    String name = result.getString("name");
                    String avatar = result.getString("avatar");
                    String imToken = result.getString("imToken");
                    JSONObject organization = result.getJSONObject("organization");
                    if (organization != null) {
                        SPCacheUtils.put("company", organization.getString("name"));
                    }
                    SPCacheUtils.put("phone", phone);
                    SPCacheUtils.put("name", name);
                    SPCacheUtils.put("avatar", getCacheUrl(avatar));
                    SPCacheUtils.put("ticket", result.getString("ticket"));
                    SPCacheUtils.put("orgId", result.getString("orgId"));
                    SPCacheUtils.put("title", result.getString("title"));
                    SPCacheUtils.put("school", result.getString("school"));
                    SPCacheUtils.put("year", result.getString("yearsOfWorking"));
                    SPCacheUtils.put("imToken", imToken);
                    int status = result.getIntValue("status");
                    if (status == 4) {
                        view.showTip(BaseApplication.appContext.getString(R.string.blocked_msg));
                    } else if (status == 2) {
                        view.phoneCodeLoginSuccess(status);
                        //登录融云
                        loginRongIM(imToken, name, avatar);
                    } else {
                        view.toFillInfo();
                    }
                }
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    private String getCacheUrl(String avatar) {
        return DisplayImageUtils.formatImgUrl(avatar, 90, 90);
    }

    private void loginRongIM(String imToken, final String userName, final String avatar) {
        if (!TextUtils.isEmpty(imToken)) {
            RongIM.connect(imToken, new RongIMClient.ConnectCallback() {

                @Override
                public void onSuccess(String userid) {
                    //登录成功后保存imUserId
                    SPCacheUtils.put("imUserId", userid);
                    //更新用户信息
                    String cacheUrl = getCacheUrl(avatar);
                    UserInfo userInfo = new UserInfo(userid, userName, TextUtils.isEmpty(cacheUrl) ? null : Uri.parse(cacheUrl));
                    RongIM.getInstance().refreshUserInfoCache(userInfo);
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
