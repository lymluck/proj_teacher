package com.smartstudy.counselor_t.mvp.model;

import com.smartstudy.counselor_t.api.ApiManager;
import com.smartstudy.counselor_t.mvp.base.BaseMode;
import com.smartstudy.counselor_t.listener.ObserverListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by louis on 17/4/5.
 */

public class LoginModel extends BaseMode {

    public void getPhoneCode(String phone, final ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().getPhoneCode(phone), listener);
    }

    public void phoneCodeLogin(String phone, String code, ObserverListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("name", phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
        params.put("phone", phone);
        params.put("password", phone.substring(phone.length() - 6));
        params.put("captcha", code);
        params.put("from", "app");
        params.put("sourceAction", "安卓用户注册");
        params.put("infoVersion", "v2");
        apiSubscribe(ApiManager.getApiService().phoneCodeLogin(params), listener);
    }
}
