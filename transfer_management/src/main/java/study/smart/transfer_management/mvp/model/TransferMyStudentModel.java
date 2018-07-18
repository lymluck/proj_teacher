package study.smart.transfer_management.mvp.model;

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
        apiSubscribe(ApiManager.getApiService().getMessageList(getHeadersMap()), listener);
    }
}
