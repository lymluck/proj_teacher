package study.smart.transfer_management.mvp.model;

import android.text.TextUtils;

import java.util.HashMap;

import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BaseModel;
import study.smart.baselib.server.api.ApiManager;

/**
 * @author yqy
 * @date on 2018/7/17
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TransferMyStudentModel extends BaseModel {
    public void getMyStudent(String page, String id, ObserverListener listener) {
        HashMap<String, String> params = new HashMap<>();
        if (TextUtils.isEmpty(id)) {
            params.put("isOwnStudent", "true");
        } else {
            params.put("centerId", id);
        }
        params.put("page", page);
        apiSubscribe(ApiManager.getApiService().getMyStudent(getHeadersMap(), params), listener);
    }

    public void getCompeleteStudent(String page, ObserverListener listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("isIncomplete", "true");
        params.put("page", page);
        apiSubscribe(ApiManager.getApiService().getMyStudent(getHeadersMap(), params), listener);
    }
}
