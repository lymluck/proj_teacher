package study.smart.transfer_management.mvp.model;

import java.util.HashMap;

import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BaseModel;
import study.smart.baselib.server.api.ApiManager;
import study.smart.baselib.ui.base.BaseActivity;

/**
 * @author yqy
 * @date on 2018/7/23
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyReportModel extends BaseModel {
    /**
     * 获取我发布的报告
     *
     * @param listener
     */
    public void getMyReport(String type, String page, ObserverListener listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("type", type);
        params.put("page", page);
        params.put("isOwnReport", "true");
        apiSubscribe(ApiManager.getApiService().getMyReportList(getHeadersMap(), params), listener);
    }
}
