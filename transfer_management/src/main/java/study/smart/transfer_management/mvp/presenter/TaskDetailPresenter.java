package study.smart.transfer_management.mvp.presenter;

import android.util.Log;

import io.reactivex.disposables.Disposable;
import study.smart.baselib.entity.TaskDetailInfo;
import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BasePresenterImpl;
import study.smart.transfer_management.mvp.contract.TaskDetailContract;
import study.smart.transfer_management.mvp.model.TaskDetailModel;

/**
 * @author yqy
 * @date on 2018/7/27
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TaskDetailPresenter extends BasePresenterImpl<TaskDetailContract.View> implements TaskDetailContract.Presenter {

    private TaskDetailModel taskDetailModel;

    public TaskDetailPresenter(TaskDetailContract.View view) {
        super(view);
        taskDetailModel = new TaskDetailModel();
    }


    @Override
    public void detach() {
        super.detach();
        taskDetailModel = null;
    }

    @Override
    public void getTaskDetail(String id) {
        taskDetailModel.getTaskDetail(id, new ObserverListener<TaskDetailInfo>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(TaskDetailInfo dataList) {
                if (dataList != null) {
                    view.getTaskDetailSuccess(dataList);
                }
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }
}
