package study.smart.transfer_management.mvp.contract;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.mvp.base.BaseView;
import study.smart.transfer_management.entity.WorkingSearchListInfo;

/**
 * @author yqy
 * @date on 2018/7/27
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface WorkingSearchContract {

    interface View extends BaseView {

        void showEmptyView(android.view.View view);

        void showResult(List<WorkingSearchListInfo> workingSearchListInfos);
    }

    interface Presenter extends BasePresenter {

        void getResults(String from,String keyword);

        void setEmptyView(LayoutInflater mInflater, Context context, ViewGroup parent);
    }
}
