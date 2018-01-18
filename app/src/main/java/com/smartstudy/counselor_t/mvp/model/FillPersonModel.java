package com.smartstudy.counselor_t.mvp.model;

import com.smartstudy.counselor_t.api.ApiManager;
import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BaseMode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yqy
 * @date on 2018/1/18
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class FillPersonModel extends BaseMode {
    public void postPersonInfo(String name, String avatar, String title, String school, String yearsOfWorking, String email, String realName, ObserverListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("avatar", avatar);
        params.put("title", title);
        params.put("school", school);
        params.put("yearsOfWorking", yearsOfWorking);
        params.put("email", email);
        params.put("realName", realName);
        apiSubscribe(ApiManager.getApiService().postPersonInfo(params), listener);
    }
}