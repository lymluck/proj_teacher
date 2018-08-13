package study.smart.transfer_management.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import study.smart.baselib.entity.TaskDetailInfo;
import study.smart.baselib.entity.WorkingSearchInfo;
import study.smart.baselib.ui.base.BaseActivity;
import study.smart.baselib.utils.ParameterUtils;
import study.smart.baselib.utils.TimeUtil;
import study.smart.transfer_management.R;
import study.smart.baselib.entity.MyTaskInfo;
import study.smart.transfer_management.mvp.contract.TaskDetailContract;
import study.smart.transfer_management.mvp.presenter.TaskDetailPresenter;

/**
 * @author yqy
 * @date on 2018/7/25
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TaskDetailActivity extends BaseActivity<TaskDetailContract.Presenter> implements TaskDetailContract.View {
    private TaskDetailInfo dataList;
    private View vTitle;
    private String id;
    private TextView tvContent;
    private TextView tvTaskCreater;
    private TextView tvUserName;
    private TextView tvTime;
    private TextView tvCenterName;
    private TextView tvStatus;
    private WorkingSearchInfo workingSearchInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
    }

    @Override
    public TaskDetailContract.Presenter initPresenter() {
        return new TaskDetailPresenter(this);
    }

    @Override
    public void initView() {
        setTitle(getString(R.string.task_detail));
        setTopLineVisibility(View.VISIBLE);
        id = getIntent().getStringExtra("id");
        workingSearchInfo = (WorkingSearchInfo) getIntent().getSerializableExtra("working_search_info");
        dataList = (TaskDetailInfo) getIntent().getSerializableExtra("taskDetail");
        tvContent = findViewById(R.id.tv_content);
        tvTaskCreater = findViewById(R.id.tv_task_creater);
        tvUserName = findViewById(R.id.tv_user_name);
        tvTime = findViewById(R.id.tv_time);
        tvCenterName = findViewById(R.id.tv_center_name);
        tvStatus = findViewById(R.id.tv_status);
        vTitle = findViewById(R.id.v_title);
        if (!TextUtils.isEmpty(id)) {
            presenter.getTaskDetail(id);
        } else if (workingSearchInfo != null) {
            initWorkingData(workingSearchInfo);
        } else {
            initData(dataList);
        }
    }

    @Override
    public void getTaskDetailSuccess(TaskDetailInfo dataList) {
        initData(dataList);
    }

    private void initData(TaskDetailInfo dataList) {
        if (dataList != null) {
            if (TextUtils.isEmpty(dataList.getName())) {
                tvContent.setText(dataList.getTypeText());
            } else {
                tvContent.setText(dataList.getTypeText() + "：" + dataList.getName());
            }
            tvTaskCreater.setText(dataList.getCreatorName());
            tvUserName.setText(dataList.getUserName());
            tvCenterName.setText(dataList.getCenterName());
            tvTime.setText(TimeUtil.getStrTime(dataList.getStartTime()) + "-" + TimeUtil.getStrTime(dataList.getEndTime()));
            tvStatus.setText(dataList.getStatusName());

            if (ParameterUtils.ALERT.equals(dataList.getStatus())) {
                //临期
                vTitle.setBackgroundColor(Color.parseColor("#FAAD14"));
            } else if (ParameterUtils.EXPIRED.equals(dataList.getStatus())) {
                //过期
                vTitle.setBackgroundColor(Color.parseColor("#7f000000"));
            } else if (ParameterUtils.PENDING.equals(dataList.getStatus())) {
                //进行中
                vTitle.setBackgroundColor(Color.parseColor("#1890FF"));

            } else {
                //已完成
                vTitle.setBackgroundColor(Color.parseColor("#52C41A"));
            }
        }
    }

    private void initWorkingData(WorkingSearchInfo workingSearchInfo) {
        if (workingSearchInfo != null) {
            if (TextUtils.isEmpty(workingSearchInfo.getName())) {
                tvContent.setText(workingSearchInfo.getTypeText());
            } else {
                tvContent.setText(workingSearchInfo.getTypeText() + "：" + workingSearchInfo.getName());
            }
            tvTaskCreater.setText(workingSearchInfo.getCreatorName());
            tvUserName.setText(workingSearchInfo.getUserName());
            tvCenterName.setText(workingSearchInfo.getCenterName());
            tvTime.setText(TimeUtil.getStrTime(workingSearchInfo.getStartTime()) + "-" + TimeUtil.getStrTime(workingSearchInfo.getEndTime()));
            tvStatus.setText(workingSearchInfo.getStatusName());

            if (ParameterUtils.ALERT.equals(workingSearchInfo.getStatus())) {
                //临期
                vTitle.setBackgroundColor(Color.parseColor("#FAAD14"));
            } else if (ParameterUtils.EXPIRED.equals(workingSearchInfo.getStatus())) {
                //过期
                vTitle.setBackgroundColor(Color.parseColor("#7f000000"));
            } else if (ParameterUtils.PENDING.equals(workingSearchInfo.getStatus())) {
                //进行中
                vTitle.setBackgroundColor(Color.parseColor("#1890FF"));
            } else {
                //已完成
                vTitle.setBackgroundColor(Color.parseColor("#52C41A"));
            }
        }
    }

}
