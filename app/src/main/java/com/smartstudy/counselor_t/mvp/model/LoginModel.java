package com.smartstudy.counselor_t.mvp.model;

import com.smartstudy.counselor_t.api.ApiManager;
import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BaseMode;

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
        params.put("phone", phone);
        params.put("captcha", code);
        apiSubscribe(ApiManager.getApiService().phoneCodeLogin(params), listener);
    }
}
