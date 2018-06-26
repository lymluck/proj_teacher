package study.smart.baselib.mvp.model;

import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BaseModel;
import study.smart.baselib.server.api.ApiManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by louis on 17/4/5.
 */

public class LoginModel extends BaseModel {

    public void getPhoneCode(String phone, final ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().getPhoneCode(getHeadersMap(), phone), listener);
    }

    public void phoneCodeLogin(String phone, String code, ObserverListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("captcha", code);
        apiSubscribe(ApiManager.getApiService().phoneCodeLogin(getHeadersMap(), params), listener);
    }
}
