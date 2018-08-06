package study.smart.transfer_management.mvp.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BasePresenterImpl;
import study.smart.baselib.utils.Utils;
import study.smart.transfer_management.R;
import study.smart.baselib.entity.WorkingSearchInfo;
import study.smart.transfer_management.entity.WorkingSearchListInfo;
import study.smart.transfer_management.mvp.contract.WorkingSearchContract;
import study.smart.transfer_management.mvp.model.WorkingSearchModel;

/**
 * @author yqy
 * @date on 2018/7/30
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class WorkingSearchPresenter extends BasePresenterImpl<WorkingSearchContract.View> implements WorkingSearchContract.Presenter {

    private WorkingSearchModel workingSearchModel;

    public WorkingSearchPresenter(WorkingSearchContract.View view) {
        super(view);
        workingSearchModel = new WorkingSearchModel();
    }


    @Override
    public void detach() {
        super.detach();
        workingSearchModel = null;
    }


    @Override
    public void getResults(String from, final String keyword) {
        workingSearchModel.getWorkingSearchTask(from, keyword, new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String result) {
                List<WorkingSearchListInfo> workingSearchInfos = new ArrayList<>();
                JSONObject jsonObject = JSONObject.parseObject(result);
                int studentTotal = jsonObject.getIntValue("studentCount");
                if (studentTotal > 0) {
                    WorkingSearchListInfo studentInfo = new WorkingSearchListInfo();
                    studentInfo.setTypeName("学员");
                    studentInfo.setKeyword(keyword);
                    List<WorkingSearchInfo> datas = JSON.parseArray(jsonObject.getString("students"), WorkingSearchInfo.class);
                    studentInfo.setWorkingSearchInfos(datas);
                    studentInfo.setViewType(WorkingSearchListInfo.STUDENT_TYPE);
                    studentInfo.setTypeTotal(studentTotal + "");
                    workingSearchInfos.add(studentInfo);
                }

                if (jsonObject.containsKey("tasks")) {
                    int taskTotal = jsonObject.getIntValue("dataCount");
                    if (taskTotal > 0) {
                        WorkingSearchListInfo taskInfo = new WorkingSearchListInfo();
                        taskInfo.setTypeName("任务");
                        taskInfo.setKeyword(keyword);
                        taskInfo.setViewType(WorkingSearchListInfo.TASK_TYPE);
                        List<WorkingSearchInfo> datas = JSON.parseArray(jsonObject.getString("tasks"), WorkingSearchInfo.class);
                        taskInfo.setWorkingSearchInfos(datas);
                        taskInfo.setTypeTotal(taskTotal + "");
                        workingSearchInfos.add(taskInfo);
                    }

                }

                if (jsonObject.containsKey("reports")) {
                    int reportTotal = jsonObject.getIntValue("dataCount");
                    if (reportTotal > 0) {
                        WorkingSearchListInfo reportInfo = new WorkingSearchListInfo();
                        reportInfo.setTypeName("报告");
                        reportInfo.setKeyword(keyword);
                        reportInfo.setViewType(WorkingSearchListInfo.REPORT_TYPE);
                        List<WorkingSearchInfo> datas = JSON.parseArray(jsonObject.getString("reports"), WorkingSearchInfo.class);
                        reportInfo.setWorkingSearchInfos(datas);
                        reportInfo.setTypeTotal(reportTotal + "");
                        workingSearchInfos.add(reportInfo);
                    }
                }

                if (jsonObject.containsKey("communicationReports")) {
                    int reportTotal = jsonObject.getIntValue("dataCount");
                    if (reportTotal > 0) {
                        WorkingSearchListInfo reportInfo = new WorkingSearchListInfo();
                        reportInfo.setTypeName("沟通记录");
                        reportInfo.setKeyword(keyword);
                        reportInfo.setViewType(WorkingSearchListInfo.TALK_TYPE);
                        List<WorkingSearchInfo> datas = JSON.parseArray(jsonObject.getString("communicationReports"), WorkingSearchInfo.class);
                        reportInfo.setWorkingSearchInfos(datas);
                        reportInfo.setTypeTotal(reportTotal + "");
                        workingSearchInfos.add(reportInfo);
                    }
                }
                view.showResult(workingSearchInfos);
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void setEmptyView(LayoutInflater mInflater, Context context, ViewGroup parent) {
        View emptyView = mInflater.inflate(R.layout.layout_empty, parent, false);
        emptyView.findViewById(R.id.iv_loading).setVisibility(View.GONE);
        emptyView.findViewById(R.id.llyt_err).setVisibility(View.VISIBLE);
        ImageView iv_err = (ImageView) emptyView.findViewById(R.id.iv_err);
        Button tv_qa_btn = (Button) emptyView.findViewById(R.id.tv_qa_btn);
        TextView tv_err_tip = (TextView) emptyView.findViewById(R.id.tv_err_tip);
        tv_err_tip.setText(context.getString(R.string.no_search_tip));
        if (!Utils.isNetworkConnected()) {
            emptyView.findViewById(R.id.llyt_err).setVisibility(View.VISIBLE);
            tv_qa_btn.setVisibility(View.GONE);
            iv_err.setImageResource(R.drawable.ic_net_err);
            tv_err_tip.setText(context.getString(R.string.no_net_tip));
        }
        view.showEmptyView(emptyView);
    }
}
