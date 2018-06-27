package study.smart.transfer_management.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import study.smart.baselib.ui.adapter.CommonAdapter;
import study.smart.baselib.ui.adapter.base.ViewHolder;
import study.smart.baselib.ui.adapter.wrapper.EmptyWrapper;
import study.smart.baselib.ui.adapter.wrapper.LoadMoreWrapper;
import study.smart.baselib.ui.base.UIFragment;
import study.smart.baselib.ui.widget.HorizontalDividerItemDecoration;
import study.smart.baselib.ui.widget.LoadMoreRecyclerView;
import study.smart.baselib.ui.widget.NoScrollLinearLayoutManager;
import study.smart.baselib.utils.DensityUtils;
import study.smart.baselib.utils.ParameterUtils;
import study.smart.baselib.utils.ToastUtils;
import study.smart.transfer_management.R;
import study.smart.transfer_management.entity.StateInfo;
import study.smart.transfer_management.entity.TransferManagerEntity;
import study.smart.transfer_management.mvp.contract.TransferManagerListContract;
import study.smart.transfer_management.mvp.presenter.TransferManagerPresenter;

/**
 * @author yqy
 * @date on 2018/6/26
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TransferMangerListFragment extends UIFragment<TransferManagerListContract.Presenter> implements TransferManagerListContract.View {
    private LoadMoreRecyclerView lmrvTransfer;
    private EmptyWrapper<TransferManagerEntity> emptyWrapper;
    private NoScrollLinearLayoutManager mLayoutManager;
    private View emptyView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int mPage = 1;
    private StateInfo stateInfo;
    private List<TransferManagerEntity> transferManagerEntities;
    private CommonAdapter<TransferManagerEntity> mAdapter;
    private LoadMoreWrapper<TransferManagerEntity> loadMoreWrapper;

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        emptyView = mActivity.getLayoutInflater().inflate(R.layout.layout_empty, lmrvTransfer,
            false);
        presenter.showLoading(mActivity, emptyView);
        presenter.getTransferList(stateInfo.getType(), mPage + "", ParameterUtils.PULL_DOWN);
    }

    @Override
    protected View getLayoutView() {
        return mActivity.getLayoutInflater().inflate(R.layout.fragment_transfer_list, null);
    }


    public static TransferMangerListFragment getInstance(Bundle bundle) {
        TransferMangerListFragment fragment = new TransferMangerListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        swipeRefreshLayout = rootView.findViewById(R.id.srlt_transfer);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                presenter.getTransferList(stateInfo.getType(), mPage + "", ParameterUtils.PULL_DOWN);
            }
        });
        lmrvTransfer = rootView.findViewById(R.id.rclv_transfer);
        lmrvTransfer.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(mActivity);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lmrvTransfer.setLayoutManager(mLayoutManager);
        lmrvTransfer.setItemAnimator(new DefaultItemAnimator());
        lmrvTransfer.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mActivity)
            .size(DensityUtils.dip2px(12f))
            .margin(DensityUtils.dip2px(16), 0)
            .colorResId(R.color.horizontal_line_color)
            .build());
        initAdapter();
        stateInfo = getArguments().getParcelable("title");
    }

    @Override
    protected void initEvent() {

    }

    private void initAdapter() {
        transferManagerEntities = new ArrayList<>();
        mAdapter = new CommonAdapter<TransferManagerEntity>(mActivity, R.layout.item_transfer_manager, transferManagerEntities) {
            @Override
            protected void convert(ViewHolder holder, TransferManagerEntity transferManagerEntity, int position) {
                if (transferManagerEntity != null) {

                }
            }
        };
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        lmrvTransfer.setAdapter(loadMoreWrapper);
        //加载更多
        lmrvTransfer.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void OnLoad() {
                if (swipeRefreshLayout.isRefreshing()) {
                    lmrvTransfer.loadComplete(true);
                    return;
                }
                mPage = mPage + 1;
                presenter.getTransferList(stateInfo.getType(), mPage + "", ParameterUtils.PULL_UP);
            }
        });
        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    public TransferManagerListContract.Presenter initPresenter() {
        return new TransferManagerPresenter(this);
    }

    @Override
    public void showTip(String message) {
        if (presenter != null) {
            swipeRefreshLayout.setRefreshing(false);
            lmrvTransfer.loadComplete(true);
            ToastUtils.shortToast(message);
        }
    }

    @Override
    public void getTransferListSuccess(List<TransferManagerEntity> transferManagerEntityList, int request_state) {
        if (presenter != null) {
            presenter.setEmptyView(emptyView);
            mLayoutManager.setScrollEnabled(true);
            int len = transferManagerEntityList.size();
            if (request_state == ParameterUtils.PULL_DOWN) {
                //下拉刷新
                if (len <= 0) {
                    lmrvTransfer.loadComplete(true);
                    mLayoutManager.setScrollEnabled(false);
                }
                this.transferManagerEntities.clear();
                this.transferManagerEntities.addAll(transferManagerEntityList);
                swipeRefreshLayout.setRefreshing(false);
                loadMoreWrapper.notifyDataSetChanged();
            } else if (request_state == ParameterUtils.PULL_UP) {
                //上拉加载
                if (len <= 0) {
                    //没有更多内容
                    if (mPage > 1) {
                        mPage = mPage - 1;
                    }
                    if (this.transferManagerEntities.size() > 0) {
                        lmrvTransfer.loadComplete(false);
                    } else {
                        lmrvTransfer.loadComplete(true);
                    }
                } else {
                    lmrvTransfer.loadComplete(true);
                    this.transferManagerEntities.addAll(transferManagerEntityList);
                    loadMoreWrapper.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        loadMoreWrapper.notifyDataSetChanged();
        lmrvTransfer.loadComplete(true);
        mLayoutManager.setScrollEnabled(false);
        view = null;
    }
}
