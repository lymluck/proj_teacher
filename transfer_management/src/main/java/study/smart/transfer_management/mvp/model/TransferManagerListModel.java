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
        if ("已分配中心".equals(type)) {
            apiSubscribe(ApiManager.getApiService().getAllocatedCenter(getHeadersMap(), params), listener);
        } else if ("未分配中心".equals(type)) {
            apiSubscribe(ApiManager.getApiService().getUnallocatedCenter(getHeadersMap(), params), listener);
        } else if ("被驳回转案".equals(type)) {
            apiSubscribe(ApiManager.getApiService().getRejectedCenter(getHeadersMap(), params), listener);
        } else if ("未分配导师".equals(type)) {
            apiSubscribe(ApiManager.getApiService().getUnallocatedCoach(getHeadersMap(), params), listener);
        } else if ("已分配导师".equals(type)) {
            apiSubscribe(ApiManager.getApiService().getAllocatedCoach(getHeadersMap(), params), listener);
        } else {
            apiSubscribe(ApiManager.getApiService().getUnallocatedCenter(getHeadersMap(), params), listener);
        }
    }
}
