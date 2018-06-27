package study.smart.transfer_management.mvp.model;

import java.util.HashMap;
import java.util.Map;

import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BaseModel;
import study.smart.baselib.server.api.ApiManager;

/**
 * @author yqy
 * @date on 2018/6/27
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TransferManagerListModel extends BaseModel {
    public void getTransferManagerList(String type, String page, ObserverListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("page", page);
        if (type.equals("未分配中心")) {
            apiSubscribe(ApiManager.getApiService().getUnallocatedCenter(getHeadersMap(), params), listener);
        }
    }
}
