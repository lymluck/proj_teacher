package com.smartstudy.counselor_t.mvp.model;

import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BaseModel;
import com.smartstudy.counselor_t.server.api.ApiManager;
import com.smartstudy.counselor_t.util.HttpUrlUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yqy
 * @date on 2018/5/16
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class AddGoodDetailModel extends BaseModel {
    public void getAddGoodDetail(int page, ObserverListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");
        apiSubscribe(ApiManager.getApiService().getAddGoodDetail(getHeadersMap(), params), listener);
    }
}
