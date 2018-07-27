package study.smart.transfer_management.mvp.presenter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import study.smart.baselib.entity.DataListInfo;
import study.smart.baselib.entity.TransferManagerEntity;
import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BasePresenterImpl;
import study.smart.baselib.utils.DisplayImageUtils;
import study.smart.baselib.utils.Utils;
import study.smart.transfer_management.R;
import study.smart.transfer_management.entity.CenterInfo;
import study.smart.transfer_management.entity.WorkingDetailInfo;
import study.smart.transfer_management.mvp.contract.TransferManagerListContract;
import study.smart.transfer_management.mvp.contract.WorkingDetailContract;
import study.smart.transfer_management.mvp.model.TransferManagerListModel;
import study.smart.transfer_management.mvp.model.WorkingDetailModel;

/**
 * @author yqy
 * @date on 2018/7/17
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class WorkingDetailPresenter extends BasePresenterImpl<WorkingDetailContract.View> implements WorkingDetailContract.Presenter {

    private WorkingDetailModel workingDetailModel;

    public WorkingDetailPresenter(WorkingDetailContract.View view) {
        super(view);
        workingDetailModel = new WorkingDetailModel();
    }


    @Override
    public void detach() {
        super.detach();
        workingDetailModel = null;
    }

    @Override
    public void getCenter() {
        workingDetailModel.getCenter(new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String result) {
                List<CenterInfo> centerInfos = JSONObject.parseArray(result, CenterInfo.class);
                if (centerInfos != null) {
                    view.getCenterSuccess(centerInfos);
                }
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }
}