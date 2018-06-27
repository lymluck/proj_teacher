package com.smartstudy.counselor_t.mvp.presenter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import study.smart.baselib.entity.TeacherInfo;
import study.smart.baselib.entity.VersionInfo;
import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BasePresenterImpl;
import study.smart.baselib.utils.ParameterUtils;
import com.smartstudy.counselor_t.mvp.contract.MyQaActivityContract;
import com.smartstudy.counselor_t.mvp.model.MyQaActivityModel;

import io.reactivex.disposables.Disposable;

/**
 * @author yqy
 * @date on 2018/3/20
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyQaActivityPresenter extends BasePresenterImpl<MyQaActivityContract.View> implements MyQaActivityContract.Presenter {

    private MyQaActivityModel myQaModel;

    private int currentIndex;

    public MyQaActivityPresenter(MyQaActivityContract.View view) {
        super(view);
        myQaModel = new MyQaActivityModel();
    }


    @Override
    public void detach() {
        super.detach();
        myQaModel = null;
    }



    @Override
    public void getLogOut() {
        myQaModel.getLogOut(new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String s) {
                view.getLogOutSuccess();
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void showFragment(FragmentManager fragmentManager, int index) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        view.hideFragment(fragmentManager);
        //注意这里设置位置
        currentIndex = index;
        switch (index) {
            case ParameterUtils.FRAGMENT_ONE:
                view.showQaFragment(ft);
                break;
            case ParameterUtils.FRAGMENT_TWO:
                view.showMyQaFragment(ft);
                break;
            case ParameterUtils.FRAGMENT_THREE:
                view.showMyFocus(ft);
                break;
            default:
                view.showQaFragment(ft);
                break;
        }
        ft.commitAllowingStateLoss();
        fragmentManager = null;
        ft = null;
    }

    @Override
    public int currentIndex() {
        return currentIndex;
    }

}

