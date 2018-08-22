package study.smart.transfer_management.mvp.presenter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

import io.reactivex.disposables.Disposable;
import study.smart.baselib.entity.DataListInfo;
import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BasePresenterImpl;
import study.smart.baselib.utils.DisplayImageUtils;
import study.smart.baselib.utils.Utils;
import study.smart.transfer_management.R;
import study.smart.baselib.entity.MyStudentInfo;
import study.smart.transfer_management.mvp.contract.StudentDetailReportContract;
import study.smart.transfer_management.mvp.model.StudentDetailReportModel;

/**
 * @author yqy
 * @date on 2018/7/24
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class StudentDetailReportPresenter extends BasePresenterImpl<StudentDetailReportContract.View> implements StudentDetailReportContract.Presenter {

    private StudentDetailReportModel studentDetailReportModel;

    public StudentDetailReportPresenter(StudentDetailReportContract.View view) {
        super(view);
        studentDetailReportModel = new StudentDetailReportModel();
    }


    @Override
    public void detach() {
        super.detach();
        studentDetailReportModel = null;
    }


    @Override
    public void getStudentDetailReport(String userId, String type, String page, final int request_state) {
        studentDetailReportModel.getStudentDetailReport(userId, type, page, new ObserverListener<DataListInfo>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(DataListInfo dataListInfo) {
                List<MyStudentInfo> myStudentInfos = JSONObject.parseArray(dataListInfo.getData(), MyStudentInfo.class);
                view.getStudentDetailReportSuccess(myStudentInfos, request_state);
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void showLoading(Context context, View emptyView) {
        ImageView iv_err = (ImageView) emptyView.findViewById(R.id.iv_err);
        TextView tv_err_tip = (TextView) emptyView.findViewById(R.id.tv_err_tip);
        if (!Utils.isNetworkConnected()) {
            emptyView.findViewById(R.id.llyt_err).setVisibility(View.VISIBLE);
            emptyView.findViewById(R.id.iv_loading).setVisibility(View.GONE);
            iv_err.setImageResource(R.drawable.ic_net_err);
            tv_err_tip.setText(context.getString(R.string.no_net_tip));
        } else {
            emptyView.findViewById(R.id.llyt_err).setVisibility(View.GONE);
            ImageView iv_loading = (ImageView) emptyView.findViewById(R.id.iv_loading);
            iv_loading.setVisibility(View.VISIBLE);
            tv_err_tip.setText("没有相关数据或者没有查看权限");
            DisplayImageUtils.displayGif(context, R.drawable.gif_data_loading, iv_loading);
        }
        view.showEmptyView(emptyView);
        context = null;
        emptyView = null;
    }

    @Override
    public void setEmptyView(View emptyView) {
        emptyView.findViewById(R.id.llyt_err).setVisibility(View.VISIBLE);
        emptyView.findViewById(R.id.iv_loading).setVisibility(View.GONE);
        view.showEmptyView(emptyView);
        emptyView = null;
    }

}