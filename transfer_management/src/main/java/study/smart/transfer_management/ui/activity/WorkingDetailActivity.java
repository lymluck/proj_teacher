package study.smart.transfer_management.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import study.smart.baselib.ui.adapter.CommonAdapter;
import study.smart.baselib.ui.adapter.base.ViewHolder;
import study.smart.baselib.ui.adapter.wrapper.HeaderAndFooterWrapper;
import study.smart.baselib.ui.base.BaseActivity;
import study.smart.baselib.ui.widget.HorizontalDividerItemDecoration;
import study.smart.baselib.ui.widget.NoScrollLinearLayoutManager;
import study.smart.baselib.utils.DensityUtils;
import study.smart.transfer_management.R;
import study.smart.transfer_management.entity.WorkingDetailInfo;
import study.smart.transfer_management.mvp.contract.WorkingDetailContract;
import study.smart.transfer_management.mvp.presenter.WorkingDetailPresenter;

/**
 * @author yqy
 * @date on 2018/7/16
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class WorkingDetailActivity extends BaseActivity<WorkingDetailContract.Presenter> implements WorkingDetailContract.View {
    private RecyclerView rvWorking;
    private NoScrollLinearLayoutManager mLayoutManager;
    private View headView;
    private CommonAdapter<WorkingDetailInfo> mAdapter;
    private List<WorkingDetailInfo> workingDetailInfoList;
    private HeaderAndFooterWrapper mHeader;
    private String from;
    private ImageView ivTagOne;
    private TextView tvTagOne;
    private ImageView ivTagTwo;
    private TextView tvTagTwo;
    private ImageView ivTagThree;
    private TextView tvTagThree;
    private LinearLayout llTagTwo;
    private LinearLayout llTagOne;
    private LinearLayout llTagThree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_working_detail);
    }

    @Override
    public WorkingDetailContract.Presenter initPresenter() {
        return new WorkingDetailPresenter(this);
    }

    @Override
    public void initView() {
        from = getIntent().getStringExtra("from");
        rvWorking = findViewById(R.id.rv_working);
        rvWorking.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvWorking.setLayoutManager(mLayoutManager);
        rvWorking.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
            .size(DensityUtils.dip2px(0.5f)).margin(DensityUtils.dip2px(16f)).colorResId(study.smart.baselib.R.color.horizontal_line_color).build());
        headView = mInflater.inflate(R.layout.layout_working_detail_head, null, false);
        ivTagOne = headView.findViewById(R.id.iv_tag_one);
        tvTagOne = headView.findViewById(R.id.tv_tag_one);
        ivTagTwo = headView.findViewById(R.id.iv_tag_two);
        tvTagTwo = headView.findViewById(R.id.tv_tag_two);
        llTagTwo = headView.findViewById(R.id.ll_tag_two);
        ivTagThree = headView.findViewById(R.id.iv_tag_three);
        tvTagThree = headView.findViewById(R.id.tv_tag_three);
        llTagOne = headView.findViewById(R.id.ll_tag_one);
        llTagThree = headView.findViewById(R.id.ll_tag_three);
        if ("STUDENT_TRANSFER_MANAGER".equals(from)) {
            setTitle("学员管理");
            ivTagOne.setImageResource(R.drawable.transfer_compelete_student);
            tvTagOne.setText("待完善信息学员");
            ivTagThree.setImageResource(R.drawable.transfer_my_student);
            tvTagThree.setText("我的学员");
            llTagTwo.setVisibility(View.GONE);
        } else if ("TASK_TRANSFER_MANAGER".equals(from)) {
            setTitle("任务/训练中心");
            ivTagOne.setImageResource(R.drawable.transfer_my_task);
            tvTagOne.setText("我布置的任务");
            ivTagThree.setImageResource(R.drawable.transfer_task_my_student);
            tvTagThree.setText("我的学员");
            llTagTwo.setVisibility(View.GONE);
        } else if ("REPORT_TRANSFER_MANAGER".equals(from)) {
            setTitle("报告中心");
            ivTagOne.setImageResource(R.drawable.transfer_report_compelete);
            tvTagOne.setText("待完成报告");
            ivTagTwo.setImageResource(R.drawable.transfer_my_report);
            tvTagTwo.setText("我发布的报告");
            ivTagThree.setImageResource(R.drawable.transfer_report_my_student);
            tvTagThree.setText("我的学员");
        } else {
            setTitle("沟通记录");
            ivTagOne.setImageResource(R.drawable.transfer_talk_complete);
            tvTagOne.setText("待完成记录");
            ivTagTwo.setImageResource(R.drawable.transfer_my_talk);
            tvTagTwo.setText("我发布的记录");
            ivTagThree.setImageResource(R.drawable.transfer_task_my_student);
            tvTagThree.setText("我的学员");
        }
        initAdapter();
        presenter.getWorkingDetail();
    }


    private void initAdapter() {
        workingDetailInfoList = new ArrayList<>();
        mAdapter = new CommonAdapter<WorkingDetailInfo>(this, R.layout.item_working_detail, workingDetailInfoList) {
            @Override
            protected void convert(ViewHolder holder, WorkingDetailInfo workingDetailInfo, int position) {

            }
        };
        mHeader = new HeaderAndFooterWrapper(mAdapter);
        mHeader.addHeaderView(headView);
        rvWorking.setAdapter(mHeader);
    }

    @Override
    public void getWorkingDetail(List<WorkingDetailInfo> workingDetailInfos) {
        if (this.workingDetailInfoList != null) {
            workingDetailInfoList.clear();
            workingDetailInfoList.addAll(workingDetailInfos);
            mHeader.notifyDataSetChanged();
        }
    }

    @Override
    public void initEvent() {
        super.initEvent();
        llTagThree.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.ll_tag_three) {
            startActivity(new Intent(this, MyStudentActivity.class));
        }
    }
}
