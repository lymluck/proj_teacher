package study.smart.transfer_management.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import study.smart.baselib.ui.adapter.CommonAdapter;
import study.smart.baselib.ui.adapter.MultiItemTypeAdapter;
import study.smart.baselib.ui.adapter.base.ViewHolder;
import study.smart.baselib.ui.adapter.wrapper.EmptyWrapper;
import study.smart.baselib.ui.adapter.wrapper.LoadMoreWrapper;
import study.smart.baselib.ui.base.BaseActivity;
import study.smart.baselib.ui.widget.LoadMoreRecyclerView;
import study.smart.baselib.ui.widget.NoScrollLinearLayoutManager;
import study.smart.baselib.utils.DisplayImageUtils;
import study.smart.baselib.utils.ParameterUtils;
import study.smart.baselib.utils.TimeUtil;
import study.smart.baselib.utils.ToastUtils;
import study.smart.transfer_management.R;
import study.smart.baselib.entity.MessageDetailItemInfo;
import study.smart.transfer_management.mvp.contract.TransferManagerMessageDetailContract;
import study.smart.transfer_management.mvp.presenter.TransferManagerMessageDetailPresenter;

/**
 * @author yqy
 * @date on 2018/7/13
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TransferManagerMessageDetailActivity extends BaseActivity<TransferManagerMessageDetailContract.Presenter> implements TransferManagerMessageDetailContract.View {
    private LoadMoreRecyclerView rclvMessageDetail;
    private EmptyWrapper<MessageDetailItemInfo> emptyWrapper;
    private NoScrollLinearLayoutManager mLayoutManager;
    private View emptyView;
    private SwipeRefreshLayout srltMessageDetail;
    private int mPage = 1;
    private CommonAdapter<MessageDetailItemInfo> mAdapter;
    private LoadMoreWrapper<MessageDetailItemInfo> loadMoreWrapper;
    private List<MessageDetailItemInfo> messageDetailItemInfos;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
    }

    @Override
    public TransferManagerMessageDetailContract.Presenter initPresenter() {
        return new TransferManagerMessageDetailPresenter(this);
    }

    @Override
    public void initView() {
        type = getIntent().getStringExtra("type");
        if (type.equals("TASK_TRAINING")) {
            setTitle(R.string.task_train);
        } else if (type.equals("TRANSFER_CASE")) {
            setTitle("转案");
        } else {
            setTitle("学员");
        }
        setTopLineVisibility(View.VISIBLE);
        srltMessageDetail = rootView.findViewById(R.id.srlt_message_detail);
        srltMessageDetail.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        srltMessageDetail.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                presenter.getMessageDetailList(mPage + "", type, ParameterUtils.PULL_DOWN);
            }
        });
        rclvMessageDetail = rootView.findViewById(R.id.rclv_message_detail);
        rclvMessageDetail.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclvMessageDetail.setLayoutManager(mLayoutManager);
        rclvMessageDetail.setItemAnimator(new DefaultItemAnimator());
        initAdapter();
        emptyView = getLayoutInflater().inflate(R.layout.layout_empty, rclvMessageDetail,
            false);
        presenter.showLoading(this, emptyView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getMessageDetailList(mPage + "", type, ParameterUtils.PULL_DOWN);
    }

    @Override
    public void getMessageDetailSuccess(List<MessageDetailItemInfo> messageDetailItemInfos, int request_state) {
        if (presenter != null) {
            presenter.setEmptyView(emptyView);
            mLayoutManager.setScrollEnabled(true);
            int len = messageDetailItemInfos.size();
            if (request_state == ParameterUtils.PULL_DOWN) {
                //下拉刷新
                if (len <= 0) {
                    rclvMessageDetail.loadComplete(true);
                    mLayoutManager.setScrollEnabled(false);
                }
                this.messageDetailItemInfos.clear();
                this.messageDetailItemInfos.addAll(messageDetailItemInfos);
                srltMessageDetail.setRefreshing(false);
                loadMoreWrapper.notifyDataSetChanged();
            } else if (request_state == ParameterUtils.PULL_UP) {
                //上拉加载
                if (len <= 0) {
                    //没有更多内容
                    if (mPage > 1) {
                        mPage = mPage - 1;
                    }
                    if (this.messageDetailItemInfos.size() > 0) {
                        rclvMessageDetail.loadComplete(false);
                    } else {
                        rclvMessageDetail.loadComplete(true);
                    }
                } else {
                    rclvMessageDetail.loadComplete(true);
                    this.messageDetailItemInfos.addAll(messageDetailItemInfos);
                    loadMoreWrapper.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void getMessageDetailSuccess(MessageDetailItemInfo messageDetailItemInfos) {
        String model = "";
        String order_state = "";
        if (messageDetailItemInfos.getDetailedType().equals("ALLOCATE_CENTER")) {
            model = "未分配导师";
            order_state = "选导师";
        } else if (messageDetailItemInfos.getDetailedType().equals("CRM_TRANSFER_CASE")) {
            model = "未分配中心";
            order_state = "未分配中心";
        } else {
            model = "被驳回转案";
            order_state = "被驳回转案";
        }
        Intent intent = new Intent();
        //把模块名传送过去
        intent.putExtra("model", model);
        intent.putExtra("id", messageDetailItemInfos.getData().getId());
        //把订单状态传送过去
        intent.putExtra("order_state", order_state);
        intent.setClass(TransferManagerMessageDetailActivity.this, TransferManagerDetailActivity.class);
        startActivity(intent);
    }


    private void initAdapter() {
        messageDetailItemInfos = new ArrayList<>();
        mAdapter = new CommonAdapter<MessageDetailItemInfo>(this, R.layout.item_message_detail, messageDetailItemInfos) {
            @Override
            protected void convert(ViewHolder holder, MessageDetailItemInfo messageDetailItemInfo, int position) {
                //转案和学员模块
                ImageView ivLogo = holder.getView(R.id.iv_logo);
                if (messageDetailItemInfo.getType().equals("TRANSFER_CASE") || messageDetailItemInfo.getType().equals("ARCHIVE")) {
                    holder.getView(R.id.ll_trnsfer).setVisibility(View.VISIBLE);
                    holder.getView(R.id.ll_train).setVisibility(View.GONE);
                    holder.setText(R.id.tv_content, messageDetailItemInfo.getContent());
                    holder.setText(R.id.tv_time, messageDetailItemInfo.getCreatedAtText());
                    holder.setText(R.id.tv_type, messageDetailItemInfo.getData().getTypeText());
                    holder.setText(R.id.tv_name, String.format(getString(R.string.name_phone), messageDetailItemInfo.getData().getName(), messageDetailItemInfo.getData().getPhone()));
                    holder.setText(R.id.tv_number, messageDetailItemInfo.getData().getOrderId());
                    holder.setText(R.id.tv_product, messageDetailItemInfo.getData().getServiceProductNames());
                    holder.setText(R.id.tv_year_season, messageDetailItemInfo.getData().getTargetApplicationYearSeason());
                    holder.setText(R.id.tv_contractor, messageDetailItemInfo.getData().getContractor());
                    holder.setText(R.id.tv_signed_time, TimeUtil.getStrTime(messageDetailItemInfo.getData().getSignedTime()));
                    if ("TRANSFER_CASE".equals(messageDetailItemInfo.getType())) {
                        DisplayImageUtils.displayCircleImage(TransferManagerMessageDetailActivity.this, study.smart.baselib.R.drawable.transfer_icon_manager, ivLogo);
                    } else {
                        DisplayImageUtils.displayCircleImage(TransferManagerMessageDetailActivity.this, study.smart.baselib.R.drawable.transfer_student_manager, ivLogo);
                    }
                } else {
                    DisplayImageUtils.displayCircleImage(TransferManagerMessageDetailActivity.this, study.smart.baselib.R.drawable.transfer_task_manager, ivLogo);
                    holder.getView(R.id.ll_trnsfer).setVisibility(View.GONE);
                    holder.getView(R.id.ll_train).setVisibility(View.VISIBLE);
                    holder.setText(R.id.tv_content, messageDetailItemInfo.getContent());
                    holder.setText(R.id.tv_type, messageDetailItemInfo.getData().getTypeText());
                    holder.setText(R.id.tv_start_time, TimeUtil.getStrTime(messageDetailItemInfo.getData().getStartTime()));
                    holder.setText(R.id.tv_end_time, TimeUtil.getStrTime(messageDetailItemInfo.getData().getEndTime()));
                }
                if (messageDetailItemInfo.isRead()) {
                    holder.getView(R.id.v_state).setVisibility(View.GONE);
                } else {
                    holder.getView(R.id.v_state).setVisibility(View.VISIBLE);
                }
            }
        };
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        rclvMessageDetail.setAdapter(loadMoreWrapper);
        //加载更多
        rclvMessageDetail.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void OnLoad() {
                if (srltMessageDetail.isRefreshing()) {
                    rclvMessageDetail.loadComplete(true);
                    return;
                }
                mPage = mPage + 1;
                presenter.getMessageDetailList(mPage + "", type, ParameterUtils.PULL_UP);
            }
        });

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (type.equals("TRANSFER_CASE")) {
                    presenter.getMessageDetail(messageDetailItemInfos.get(position));
                } else if ("TASK_TRAINING".equals(type)) {
                    //任务训练
                    startActivity(new Intent(TransferManagerMessageDetailActivity.this, TaskDetailActivity.class).putExtra("id", messageDetailItemInfos.get(position).getData().getMessageId()));
                } else {
                    startActivity(new Intent(TransferManagerMessageDetailActivity.this, StudentDetailActivity.class)
                        .putExtra("id", messageDetailItemInfos.get(position).getData().getMessageId())
                        .putExtra("from", "message"));
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    public void showTip(String message) {
        if (presenter != null) {
            srltMessageDetail.setRefreshing(false);
            rclvMessageDetail.loadComplete(true);
            ToastUtils.shortToast(message);
        }
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        loadMoreWrapper.notifyDataSetChanged();
        rclvMessageDetail.loadComplete(true);
        mLayoutManager.setScrollEnabled(false);
        view = null;
    }
}

