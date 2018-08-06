package study.smart.transfer_management.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import study.smart.baselib.ui.adapter.CommonAdapter;
import study.smart.baselib.ui.adapter.MultiItemTypeAdapter;
import study.smart.baselib.ui.adapter.base.ViewHolder;
import study.smart.baselib.ui.adapter.wrapper.EmptyWrapper;
import study.smart.baselib.ui.adapter.wrapper.LoadMoreWrapper;
import study.smart.baselib.ui.base.UIFragment;
import study.smart.baselib.ui.widget.HorizontalDividerItemDecoration;
import study.smart.baselib.ui.widget.LoadMoreRecyclerView;
import study.smart.baselib.ui.widget.NoScrollLinearLayoutManager;
import study.smart.baselib.utils.DensityUtils;
import study.smart.baselib.utils.ParameterUtils;
import study.smart.baselib.utils.TimeUtil;
import study.smart.baselib.utils.ToastUtils;
import study.smart.transfer_management.R;
import study.smart.baselib.entity.MyStudentInfo;
import study.smart.transfer_management.entity.StateInfo;
import study.smart.transfer_management.mvp.contract.MyReportContract;
import study.smart.transfer_management.mvp.presenter.MyReportPresenter;

/**
 * @author yqy
 * @date on 2018/7/23
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyReportFragment extends UIFragment<MyReportContract.Presenter> implements MyReportContract.View {
    private LoadMoreRecyclerView lmrvReport;
    private EmptyWrapper<MyStudentInfo> emptyWrapper;
    private NoScrollLinearLayoutManager mLayoutManager;
    private View emptyView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int mPage = 1;
    private StateInfo stateInfo;
    private List<MyStudentInfo> myStudentInfos;
    private CommonAdapter<MyStudentInfo> mAdapter;
    private LoadMoreWrapper<MyStudentInfo> loadMoreWrapper;
    private String from;
    private String userId;

    public static MyReportFragment getInstance(Bundle bundle) {
        MyReportFragment fragment = new MyReportFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        emptyView = mActivity.getLayoutInflater().inflate(R.layout.layout_empty, lmrvReport,
            false);
        presenter.showLoading(mActivity, emptyView);
        presenter.getMyReport(stateInfo.getType(), mPage + "", ParameterUtils.PULL_DOWN);
    }


    @Override
    protected View getLayoutView() {
        return mActivity.getLayoutInflater().inflate(R.layout.fragment_my_report, null);
    }

    @Override
    protected void initView(View rootView) {
        swipeRefreshLayout = rootView.findViewById(R.id.srlt_report);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                presenter.getMyReport(stateInfo.getType(), mPage + "", ParameterUtils.PULL_DOWN);
            }
        });
        lmrvReport = rootView.findViewById(R.id.rclv_report);
        lmrvReport.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(mActivity);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lmrvReport.setLayoutManager(mLayoutManager);
        lmrvReport.setItemAnimator(new DefaultItemAnimator());
        lmrvReport.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mActivity)
            .size(DensityUtils.dip2px(0.5f))
            .colorResId(R.color.main_bg)
            .build());
        initAdapter();
        from = getArguments().getString("from");
        userId = getArguments().getString("id");
        stateInfo = getArguments().getParcelable("stateInfos");
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public MyReportContract.Presenter initPresenter() {
        return new MyReportPresenter(this);
    }

    @Override
    public void showTip(String message) {
        if (presenter != null) {
            swipeRefreshLayout.setRefreshing(false);
            if (lmrvReport != null) {
                lmrvReport.loadComplete(true);
            }
            ToastUtils.shortToast(message);
        }
    }

    @Override
    public void getMyReportSuccess(List<MyStudentInfo> myStudentInfos, int request_state) {
        if (presenter != null) {
            presenter.setEmptyView(emptyView);
            mLayoutManager.setScrollEnabled(true);
            int len = myStudentInfos.size();
            if (request_state == ParameterUtils.PULL_DOWN) {
                //下拉刷新
                if (len <= 0) {
                    lmrvReport.loadComplete(true);
                    mLayoutManager.setScrollEnabled(false);
                }
                this.myStudentInfos.clear();
                this.myStudentInfos.addAll(myStudentInfos);
                swipeRefreshLayout.setRefreshing(false);
                loadMoreWrapper.notifyDataSetChanged();
            } else if (request_state == ParameterUtils.PULL_UP) {
                //上拉加载
                if (len <= 0) {
                    //没有更多内容
                    if (mPage > 1) {
                        mPage = mPage - 1;
                    }
                    if (this.myStudentInfos.size() > 0) {
                        lmrvReport.loadComplete(false);
                    } else {
                        lmrvReport.loadComplete(true);
                    }
                } else {
                    lmrvReport.loadComplete(true);
                    this.myStudentInfos.addAll(myStudentInfos);
                    loadMoreWrapper.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        loadMoreWrapper.notifyDataSetChanged();
        if (lmrvReport != null) {
            lmrvReport.loadComplete(true);
        }
        mLayoutManager.setScrollEnabled(false);
        view = null;
    }

    private void initAdapter() {
        myStudentInfos = new ArrayList<>();
        mAdapter = new CommonAdapter<MyStudentInfo>(mActivity, R.layout.item_my_report, myStudentInfos) {
            @Override
            protected void convert(ViewHolder holder, MyStudentInfo myStudentInfo, int position) {
                if ("QUARTERLY".equals(stateInfo.getType())) {
                    holder.setText(R.id.tv_report_name, String.format(getString(R.string.name_report), (myStudentInfo.getUserName() == null ? "" : myStudentInfo.getUserName()), (myStudentInfo.getTime() == null ? "" : myStudentInfo.getTime()) + "报告"));
                } else {
                    holder.setText(R.id.tv_report_name, String.format(getString(R.string.name_report), myStudentInfo.getUserName() == null ? "" : myStudentInfo.getUserName(), myStudentInfo.getTypeTEXT() == null ? "" : myStudentInfo.getTypeTEXT()));
                }
                holder.setText(R.id.tv_report_center_name, myStudentInfo.getCenterName());
                holder.setText(R.id.tv_publish_time, TimeUtil.getStrTime(myStudentInfo.getPublishTime()));
            }
        };
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        lmrvReport.setAdapter(loadMoreWrapper);
        //加载更多
        lmrvReport.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void OnLoad() {
                if (swipeRefreshLayout.isRefreshing()) {
                    lmrvReport.loadComplete(true);
                    return;
                }
                mPage = mPage + 1;
                presenter.getMyReport(stateInfo.getType(), mPage + "", ParameterUtils.PULL_UP);
            }
        });

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
//                startActivity(new Intent(mActivity, StudentDetailActivity.class).putExtra("studentInfo", myStudentInfos.get(position)).putExtra("from", "report"));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (lmrvReport != null) {
            lmrvReport.removeAllViews();
            lmrvReport = null;
        }
        if (mLayoutManager != null) {
            mLayoutManager.removeAllViews();
            mLayoutManager = null;
        }

        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (myStudentInfos != null) {
            myStudentInfos.clear();
            myStudentInfos = null;
        }
    }
}