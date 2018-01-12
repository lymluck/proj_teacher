package com.smartstudy.counselor_t.mvp.model;

import android.util.Log;

import com.smartstudy.counselor_t.api.ApiManager;
import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BaseMode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yqy
 * @date on 2018/1/12
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MainModel extends BaseMode {

    public void getStudentInfo(String id, ObserverListener listener) {
//        Map<String, String> params = new HashMap<>();
//        try {
//            params.put("id", URLEncoder.encode(id, "utf-8"));
//            Log.w("kim","----->"+URLEncoder.encode(id, "utf-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        try {
            apiSubscribe(ApiManager.getApiService().getStudentInfo(URLEncoder.encode(id, "utf-8")), listener);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


}
