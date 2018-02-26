package com.smartstudy.counselor_t.mvp.contract;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.mvp.base.BaseView;

import java.util.List;

/**
 * Created by louis on 2017/3/4.
 */

public interface CommonSearchContract {

    interface View extends BaseView {

        void showResult(List data, int request_state, String flag);

        void showEmptyView(android.view.View view);
    }

    interface Presenter extends BasePresenter {

        void getSchools(int cacheType, String countryId, String keyword, int page, int request_state, String flag);

        void setEmptyView(LayoutInflater mInflater, Context context, ViewGroup parent, String from);
    }
}
