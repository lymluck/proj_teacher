package study.smart.transfer_management.mvp.presenter;

import io.reactivex.disposables.Disposable;
import study.smart.baselib.entity.MyStudentInfo;
import study.smart.baselib.entity.TaskDetailInfo;
import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BasePresenterImpl;
import study.smart.transfer_management.mvp.contract.StudentDetailContract;
import study.smart.transfer_management.mvp.contract.TaskDetailContract;
import study.smart.transfer_management.mvp.model.StudentDetailModel;
import study.smart.transfer_management.mvp.model.TaskDetailModel;

/**
 * @author yqy
 * @date on 2018/7/27
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class StudentDetailPresenter extends BasePresenterImpl<StudentDetailContract.View> implements StudentDetailContract.Presenter {

    private StudentDetailModel studentDetailModel;

    public StudentDetailPresenter(StudentDetailContract.View view) {
        super(view);
        studentDetailModel = new StudentDetailModel();
    }


    @Override
    public void detach() {
        super.detach();
        studentDetailModel = null;
    }

    @Override
    public void getStudentDetail(String id) {
        studentDetailModel.getTaskDetail(id, new ObserverListener<MyStudentInfo>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(MyStudentInfo myStudentInfo) {
                if (myStudentInfo != null) {
                    view.getStudentDetailSuccess(myStudentInfo);
                }
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }
}
