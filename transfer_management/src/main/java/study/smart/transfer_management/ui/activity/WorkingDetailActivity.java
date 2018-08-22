package study.smart.transfer_management.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import study.smart.baselib.ui.adapter.CommonAdapter;
import study.smart.baselib.ui.adapter.MultiItemTypeAdapter;
import study.smart.baselib.ui.adapter.base.ViewHolder;
import study.smart.baselib.ui.adapter.wrapper.HeaderAndFooterWrapper;
import study.smart.baselib.ui.base.BaseActivity;
import study.smart.baselib.ui.widget.HorizontalDividerItemDecoration;
import study.smart.baselib.ui.widget.NoScrollLinearLayoutManager;
import study.smart.baselib.utils.DensityUtils;
import study.smart.baselib.utils.ParameterUtils;
import study.smart.transfer_management.R;
import study.smart.transfer_management.entity.CenterInfo;
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
    private CommonAdapter<CenterInfo> mAdapter;
    private List<CenterInfo> centerInfos;
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
    private View llSearch;

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
        llSearch = headView.findViewById(R.id.ll_search);
        if (ParameterUtils.STUDENT_TRANSFER_MANAGER.equals(from)) {
            setTitle(R.string.student_manager);
            ivTagOne.setImageResource(R.drawable.transfer_compelete_student);
            tvTagOne.setText(R.string.uncompelete_info_student);
            ivTagThree.setImageResource(R.drawable.transfer_my_student);
            tvTagThree.setText(R.string.my_student);
            llTagTwo.setVisibility(View.GONE);
        } else if (ParameterUtils.TASK_TRANSFER_MANAGER.equals(from)) {
            setTitle(getString(R.string.task_train_center));
            ivTagOne.setImageResource(R.drawable.transfer_my_task);
            tvTagOne.setText(R.string.my_arrange_task);
            ivTagThree.setImageResource(R.drawable.transfer_task_my_student);
            tvTagThree.setText(R.string.my_student);
            llTagTwo.setVisibility(View.GONE);
        } else if (ParameterUtils.REPORT_TRANSFER_MANAGER.equals(from)) {
            setTitle(R.string.report_center);
            ivTagOne.setImageResource(R.drawable.transfer_report_compelete);
            tvTagOne.setText(R.string.un_compelete_report);
            ivTagTwo.setImageResource(R.drawable.transfer_my_report);
            tvTagTwo.setText(R.string.my_release_report);
            ivTagThree.setImageResource(R.drawable.transfer_report_my_student);
            tvTagThree.setText(R.string.my_student);
        } else {
            setTitle(R.string.talk_record);
            ivTagOne.setImageResource(R.drawable.transfer_talk_complete);
            tvTagOne.setText(R.string.uncompelete_record);
            ivTagTwo.setImageResource(R.drawable.transfer_my_talk);
            tvTagTwo.setText(R.string.my_release_record);
            ivTagThree.setImageResource(R.drawable.transfer_task_my_student);
            tvTagThree.setText(R.string.my_student);
        }
        initAdapter();
        presenter.getCenter();
    }


    private void initAdapter() {
        centerInfos = new ArrayList<>();
        mAdapter = new CommonAdapter<CenterInfo>(this, R.layout.item_working_detail, centerInfos) {
            @Override
            protected void convert(ViewHolder holder, CenterInfo centerInfo, int position) {
                holder.setText(R.id.tv_name, centerInfo.getValue());
                holder.setCircleImageUrl(R.id.iv_logo, centerInfo.getLogo(), true);
            }
        };
        mHeader = new HeaderAndFooterWrapper(mAdapter);
        mHeader.addHeaderView(headView);
        rvWorking.setAdapter(mHeader);

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                startActivity(new Intent(WorkingDetailActivity.this, MyStudentActivity.class)
                    .putExtra("centerId", centerInfos.get(position - 1).getId())
                    .putExtra("from", from));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }


    @Override
    public void getCenterSuccess(List<CenterInfo> centerInfo) {
        if (centerInfo != null) {
            centerInfos.clear();
            centerInfos.addAll(centerInfo);
            mHeader.notifyDataSetChanged();
        }
    }

    @Override
    public void initEvent() {
        super.initEvent();
        llTagThree.setOnClickListener(this);
        llTagOne.setOnClickListener(this);
        llTagTwo.setOnClickListener(this);
        llSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toSearch;
                if (ParameterUtils.STUDENT_TRANSFER_MANAGER.equals(from)) {
                    toSearch = new Intent(WorkingDetailActivity.this, CommonSearchActivity.class);
                    toSearch.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.MY_ALL_STUDENT);
                } else {
                    toSearch = new Intent(WorkingDetailActivity.this, WorkingSearchActivity.class);
                    toSearch.putExtra(ParameterUtils.TRANSITION_FLAG, from);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(toSearch, ActivityOptionsCompat.makeSceneTransitionAnimation(WorkingDetailActivity.this,
                        llSearch, "btn_tr").toBundle());
                } else {
                    startActivity(toSearch);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.ll_tag_three) {
            startActivity(new Intent(this, MyStudentActivity.class).putExtra("from", from));
        } else if (v.getId() == R.id.ll_tag_one) {
            if (ParameterUtils.STUDENT_TRANSFER_MANAGER.equals(from)) {
                startActivity(new Intent(this, MyStudentActivity.class).putExtra("from", ParameterUtils.COMPELETE_STUDENT));
            } else if (ParameterUtils.TASK_TRANSFER_MANAGER.equals(from)) {
                startActivity(new Intent(this, MyTaskListActivity.class).putExtra("from", "working"));
            } else if (ParameterUtils.REPORT_TRANSFER_MANAGER.equals(from)) {
                startActivity(new Intent(this, UnCompeleteReportActivity.class));
            } else {
                startActivity(new Intent(this, UnTalkRecordActivity.class));
            }
        } else if (v.getId() == R.id.ll_tag_two) {
            if (ParameterUtils.STUDENT_TRANSFER_MANAGER.equals(from)) {
            } else if (ParameterUtils.TASK_TRANSFER_MANAGER.equals(from)) {

            } else if (ParameterUtils.REPORT_TRANSFER_MANAGER.equals(from)) {
                startActivity(new Intent(this, MyReportActivity.class));
            } else {
                startActivity(new Intent(this, MyTalkRecordActivity.class));
            }
        } else {
            finish();
        }
    }

}