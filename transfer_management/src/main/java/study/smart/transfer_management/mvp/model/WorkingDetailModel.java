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
public class WorkingDetailModel extends BaseModel {
    /**
     * 获取中心
     *
     * @param listener
     */
    public void getCenter(ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().getCenterList(getHeadersMap()), listener);
    }
}
