package study.smart.transfer_management.mvp.model;

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
    public void getMyStudent(ObserverListener listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("isOwnStudent", "true");
        apiSubscribe(ApiManager.getApiService().getMyStudent(getHeadersMap(), params), listener);
    }

    public void getCompeleteStudent(ObserverListener listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("isOwnStudent", "true");
        params.put("isIncomplete", "true");
        apiSubscribe(ApiManager.getApiService().getMyStudent(getHeadersMap(), params), listener);
    }
}
