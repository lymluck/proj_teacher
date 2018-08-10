package study.smart.transfer_management.mvp.model;

import java.util.HashMap;

import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BaseModel;
import study.smart.baselib.server.api.ApiManager;
import study.smart.baselib.utils.ParameterUtils;

/**
 * @author yqy
 * @date on 2018/7/30
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class WorkingSearchModel extends BaseModel {
    /**
     * 获取中心
     *
     * @param listener
     */
    public void getWorkingSearchTask(String from, String keyWord, ObserverListener listener) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("keyword", keyWord);
        if (ParameterUtils.TASK_TRANSFER_MANAGER.equals(from)) {
            apiSubscribe(ApiManager.getApiService().getSearchTaskList(getHeadersMap(), hashMap), listener);
        } else if (ParameterUtils.REPORT_TRANSFER_MANAGER.equals(from)) {
            apiSubscribe(ApiManager.getApiService().getSearchReportList(getHeadersMap(), hashMap), listener);
        } else {
            apiSubscribe(ApiManager.getApiService().getSearchTalkList(getHeadersMap(), hashMap), listener);
        }
    }
}
