package com.smartstudy.counselor_t.mvp.contract;

import android.content.Context;

import com.smartstudy.counselor_t.entity.IdNameInfo;
import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.mvp.base.BaseView;

import java.util.List;

/**
 * Created by louis on 17/3/15.
 */

public interface ChooseListContract {

    interface View extends BaseView {

        void editMyInfoSuccess(String jsonObject, IdNameInfo nameInfo);

        void refreshItems();

//        void editData(IdNameInfo nameInfo, PersonalParamsInfo paramsModel);

        void showEmptyView(android.view.View view);

        void reload();


    }

    interface Presenter extends BasePresenter {

//        void editMyInfo(PersonalParamsInfo myInfo, IdNameInfo nameInfo);

        void getOptionsData(String type, String value, String flag, List<IdNameInfo> nameInfos);

        void doItemClick(String flag, int position, List<IdNameInfo> nameInfos);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(android.view.View emptyView);
    }
}
