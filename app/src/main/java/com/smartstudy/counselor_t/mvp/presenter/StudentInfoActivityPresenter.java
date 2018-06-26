package com.smartstudy.counselor_t.mvp.presenter;

import study.smart.baselib.entity.StudentPageInfo;
import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BasePresenterImpl;
import com.smartstudy.counselor_t.mvp.contract.StudentActivityContract;
import com.smartstudy.counselor_t.mvp.model.StudentDetailInfoModel;

import io.reactivex.disposables.Disposable;

/**
 * @author yqy
 * @date on 2018/1/15
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class StudentInfoActivityPresenter extends BasePresenterImpl<StudentActivityContract.View> implements StudentActivityContract.Presenter {

    private StudentDetailInfoModel studentDetailInfoModel;

    public StudentInfoActivityPresenter(StudentActivityContract.View view) {
        super(view);
        studentDetailInfoModel = new StudentDetailInfoModel();
    }


    @Override
    public void detach() {
        super.detach();
        studentDetailInfoModel = null;
    }


    @Override
    public void getStudentDetailInfo(String userId) {

        studentDetailInfoModel.getStudentDetailInfo(userId, new ObserverListener<StudentPageInfo>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(StudentPageInfo result) {
                if (result != null) {
//                    String cacheUrl = DisplayImageUtils.formatImgUrl(studentPageInfo.getAvatar(), 90, 90);
//                    RongIM.getInstance().refreshUserInfoCache(new UserInfo(studentPageInfo.getImUserId(), studentPageInfo.getName(), TextUtils.isEmpty(cacheUrl) ? null : Uri.parse(cacheUrl)));
                    view.getStudentInfoDetailSuccess(result);
                }
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });

    }

}

