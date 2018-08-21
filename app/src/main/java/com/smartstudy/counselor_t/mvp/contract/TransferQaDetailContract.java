package com.smartstudy.counselor_t.mvp.contract;

import android.content.Context;

import com.smartstudy.counselor_t.entity.DistributionInfo;

import java.util.List;

import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.mvp.base.BaseView;

/**
 * @author yqy
 * @date on 2018/8/17
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface TransferQaDetailContract {
    interface View extends BaseView {
        void postReceiveSuccess();

    }

    interface Presenter extends BasePresenter {

        void postReceive(String questionId, String sendesId);

    }
}
