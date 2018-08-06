package study.smart.baselib.mvp.model;

import android.text.TextUtils;

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
public class CommonSearchModel extends BaseModel {
    public void searTransferManagerList(String keyWord, int page, final ObserverListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");
        if (!TextUtils.isEmpty(keyWord)) {
            params.put("keyword", keyWord);
        }
        apiSubscribe(ApiManager.getApiService().searchTransferCase(getHeadersMap(), params), listener);
    }

    public void searchMsgDetail(String keyWord, int page, final ObserverListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");
        if (!TextUtils.isEmpty(keyWord)) {
            params.put("keyword", keyWord);
        }
        apiSubscribe(ApiManager.getApiService().searchMsgList(getHeadersMap(), params), listener);
    }

    public void getAllStudentList(String keyWord, int page, final ObserverListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");
        if (!TextUtils.isEmpty(keyWord)) {
            params.put("keyword", keyWord);
        }
        apiSubscribe(ApiManager.getApiService().getMyStudent(getHeadersMap(), params), listener);
    }

    public void getMyStudentList(String keyWord, int page, final ObserverListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");
        if (!TextUtils.isEmpty(keyWord)) {
            params.put("keyword", keyWord);
        }
        params.put("isOwnStudent", "true");
        apiSubscribe(ApiManager.getApiService().getMyStudent(getHeadersMap(), params), listener);
    }


    public void getUnCompeleteStudentList(String keyWord, int page, final ObserverListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");
        if (!TextUtils.isEmpty(keyWord)) {
            params.put("keyword", keyWord);
        }
        params.put("isIncomplete", "true");
        apiSubscribe(ApiManager.getApiService().getMyStudent(getHeadersMap(), params), listener);
    }

    public void getTaskList(String keyWord, int page, final ObserverListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");
        if (!TextUtils.isEmpty(keyWord)) {
            params.put("keyword", keyWord);
        }
        params.put("blockType", "DATA");
        apiSubscribe(ApiManager.getApiService().getTaskList(getHeadersMap(), params), listener);
    }

    public void getReportList(String keyWord, int page, final ObserverListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");
        if (!TextUtils.isEmpty(keyWord)) {
            params.put("keyword", keyWord);
        }
        params.put("blockType", "DATA");
        apiSubscribe(ApiManager.getApiService().getReportList(getHeadersMap(), params), listener);
    }

    public void getTalkList(String keyWord, int page, final ObserverListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");
        if (!TextUtils.isEmpty(keyWord)) {
            params.put("keyword", keyWord);
        }
        params.put("blockType", "DATA");
        apiSubscribe(ApiManager.getApiService().getTalkList(getHeadersMap(), params), listener);
    }
}
