package study.smart.transfer_management.mvp.model;

import java.util.HashMap;

import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BaseModel;
import study.smart.baselib.server.api.ApiManager;

/**
 * @author yqy
 * @date on 2018/7/24
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class StudentDetailReportModel extends BaseModel {
    /**
     * 获取学生详细报告
     *
     * @param listener
     */
    public void getStudentDetailReport(String userId, String type, String page, ObserverListener listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("type", type);
        params.put("page", page);
        params.put("userId", userId);
        apiSubscribe(ApiManager.getApiService().getStudentDetailReport(getHeadersMap(), params), listener);
    }
}
