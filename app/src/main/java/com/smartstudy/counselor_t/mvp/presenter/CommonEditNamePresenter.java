package com.smartstudy.counselor_t.mvp.presenter;

import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.smartstudy.counselor_t.entity.TeacherInfo;
import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BasePresenterImpl;
import com.smartstudy.counselor_t.mvp.contract.CommonEditNameContract;
import com.smartstudy.counselor_t.mvp.contract.MyInfoContract;
import com.smartstudy.counselor_t.mvp.model.CommonEditNameModel;
import com.smartstudy.counselor_t.mvp.model.MyInfoModel;
import com.smartstudy.counselor_t.util.DisplayImageUtils;
import com.smartstudy.counselor_t.util.SPCacheUtils;

import java.io.File;

import io.reactivex.disposables.Disposable;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * @author yqy
 * @date on 2018/2/7
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class CommonEditNamePresenter extends BasePresenterImpl<CommonEditNameContract.View> implements CommonEditNameContract.Presenter {

    private CommonEditNameModel commonEditNameModel;

    public CommonEditNamePresenter(CommonEditNameContract.View view) {
        super(view);
        commonEditNameModel = new CommonEditNameModel();
    }


    @Override
    public void detach() {
        super.detach();
        commonEditNameModel = null;
    }


    @Override
    public void updateMyInfo(TeacherInfo teacherInfo) {
        commonEditNameModel.updatePersonInfo(teacherInfo, new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String s) {

                TeacherInfo teacherInfo = JSON.parseObject(s, TeacherInfo.class);
                if (teacherInfo != null) {
                    view.updateMyInfoSuccesee(teacherInfo);
                }
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }
}


