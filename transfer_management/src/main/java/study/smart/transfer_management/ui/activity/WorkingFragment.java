package study.smart.transfer_management.ui.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.ui.adapter.CommonAdapter;
import study.smart.baselib.ui.adapter.base.ViewHolder;
import study.smart.baselib.ui.base.UIFragment;
import study.smart.transfer_management.R;
import study.smart.transfer_management.entity.WorkingInfo;

/**
 * @author yqy
 * @date on 2018/7/16
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class WorkingFragment extends UIFragment {
    private RecyclerView rvWorking;
    private CommonAdapter<WorkingInfo> adapter;
    private GridLayoutManager mLayoutManager;
    private List<WorkingInfo> workingInfos;

    @Override
    protected View getLayoutView() {
        return mActivity.getLayoutInflater().inflate(R.layout.fragment_transfer_working, null);
    }

    @Override
    protected void initView(View rootView) {
        rvWorking = rootView.findViewById(R.id.rv_working);
        mLayoutManager = new GridLayoutManager(mActivity, 3);
        rvWorking.setLayoutManager(mLayoutManager);
        initAdapter();
        initData();
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }


    private void initAdapter() {
        workingInfos = new ArrayList<>();
        adapter = new CommonAdapter<WorkingInfo>(mActivity, R.layout.item_working, workingInfos) {
            @Override
            protected void convert(ViewHolder holder, WorkingInfo workingInfo, int position) {
                holder.setImageResource(R.id.iv_working, workingInfo.getIcon());
                holder.setText(R.id.tv_working, workingInfo.getName());
            }
        };
        rvWorking.setAdapter(adapter);
    }

    private void initData() {
        WorkingInfo workingInfo = new WorkingInfo();
        //转案管理
        workingInfo.setId("TRANSFER_MANAGER");
        workingInfo.setName("转案管理");
        workingInfo.setIcon(R.drawable.transfer_icon_manager);
        workingInfos.add(workingInfo);

        WorkingInfo studentWorkingInfo = new WorkingInfo();
        //学员管理
        studentWorkingInfo.setId("STUDENT_TRANSFER_MANAGER");
        studentWorkingInfo.setName("学员管理");
        studentWorkingInfo.setIcon(R.drawable.transfer_student_manager);
        workingInfos.add(studentWorkingInfo);

        WorkingInfo taskWorkingInfo = new WorkingInfo();
        //任务训练
        taskWorkingInfo.setId("TASK_TRANSFER_MANAGER");
        taskWorkingInfo.setName("任务/训练");
        taskWorkingInfo.setIcon(R.drawable.transfer_task_manager);
        workingInfos.add(taskWorkingInfo);

        WorkingInfo reportWorkingInfo = new WorkingInfo();
        //报告中心
        reportWorkingInfo.setId("REPORT_TRANSFER_MANAGER");
        reportWorkingInfo.setName("报告中心");
        reportWorkingInfo.setIcon(R.drawable.transfer_working_report);
        workingInfos.add(reportWorkingInfo);


        WorkingInfo talkWorkingInfo = new WorkingInfo();
        //沟通记录
        talkWorkingInfo.setId("TALK_TRANSFER_MANAGER");
        talkWorkingInfo.setName("沟通记录");
        talkWorkingInfo.setIcon(R.drawable.transfer_talk_icon);
        workingInfos.add(talkWorkingInfo);

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
