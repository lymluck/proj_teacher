package study.smart.transfer_management.mvp.model;

import java.util.HashMap;

import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BaseModel;
import study.smart.baselib.server.api.ApiManager;

/**
 * @author yqy
 * @date on 2018/7/23
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class UnCompeleteReportModel extends BaseModel {
    /**
     * 获取消息信息
     *
     * @param listener
     */
    public void getUnCompeleteReport(String type, String page, ObserverListener listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("type", type);
        params.put("page", page);
        apiSubscribe(ApiManager.getApiService().getUnCompeleteReportList(getHeadersMap(), params), listener);
    }
}
