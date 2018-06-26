package com.smartstudy.counselor_t.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import study.smart.baselib.ui.base.BaseActivity;
import study.smart.baselib.utils.DensityUtils;
import study.smart.baselib.utils.ParameterUtils;
import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.AddGoodInfo;
import com.smartstudy.counselor_t.mvp.contract.AddGoodDetailContract;
import com.smartstudy.counselor_t.mvp.presenter.AddGoodDetailPresenter;
import study.smart.baselib.ui.adapter.CommonAdapter;
import study.smart.baselib.ui.adapter.base.ViewHolder;
import study.smart.baselib.ui.adapter.wrapper.EmptyWrapper;
import study.smart.baselib.ui.adapter.wrapper.LoadMoreWrapper;
import study.smart.baselib.ui.widget.HorizontalDividerItemDecoration;
import study.smart.baselib.ui.widget.LoadMoreRecyclerView;
import study.smart.baselib.ui.widget.NoScrollLinearLayoutManager;
import com.smartstudy.counselor_t.util.NoDoubleClickUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yqy
 * @date on 2018/5/16
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class AddGoodDetailActivity extends BaseActivity<AddGoodDetailContract.Presenter> implements AddGoodDetailContract.View {
    private LoadMoreRecyclerView rclvAddGood;
    private CommonAdapter<AddGoodInfo> mAdapter;
    private LoadMoreWrapper<AddGoodInfo> loadMoreWrapper;
    private EmptyWrapper<AddGoodInfo> emptyWrapper;
    private NoScrollLinearLayoutManager mLayoutManager;
    private View emptyView;
    private List<AddGoodInfo> addGoodInfoList;
    private int mPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_good_detail);
    }

    @Override
    public void getAddGoodDetailSucess(List<AddGoodInfo> data, int request_state) {
        if (presenter != null) {
            presenter.setEmptyView(this, emptyView);
            mLayoutManager.setScrollEnabled(true);
            int len = data.size();
            if (request_state == ParameterUtils.PULL_DOWN) {
                //下拉刷新
                if (len <= 0) {
                    rclvAddGood.loadComplete(true);
                    mLayoutManager.setScrollEnabled(false);
                }
                addGoodInfoList.clear();
                addGoodInfoList.addAll(data);
                loadMoreWrapper.notifyDataSetChanged();
            } else if (request_state == ParameterUtils.PULL_UP) {
                //上拉加载
                if (len <= 0) {
                    //没有更多内容
                    if (mPage > 1) {
                        mPage = mPage - 1;
                    }
                    rclvAddGood.loadComplete(false);
                } else {
                    rclvAddGood.loadComplete(true);
                    addGoodInfoList.addAll(data);
                    loadMoreWrapper.notifyDataSetChanged();
                }
            }
        }
        data = null;
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        loadMoreWrapper.notifyDataSetChanged();
        rclvAddGood.loadComplete(true);
        mLayoutManager.setScrollEnabled(false);
        view = null;
    }

    @Override
    public AddGoodDetailContract.Presenter initPresenter() {
        return new AddGoodDetailPresenter(this);
    }

    @Override
    public void initView() {
        topdefaultCentertitle.setText("谁赞了我");
        setTopLineVisibility(View.VISIBLE);
        rclvAddGood = (LoadMoreRecyclerView) findViewById(R.id.rclv_add_good);
        rclvAddGood.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclvAddGood.setLayoutManager(mLayoutManager);
        rclvAddGood.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
            .size(DensityUtils.dip2px(0.5f)).margin(DensityUtils.dip2px(16f), 0).colorResId(R.color.horizontal_line_color).build());
        emptyView = mInflater.inflate(R.layout.layout_empty, rclvAddGood, false);
        initAdapter();
        presenter.showLoading(this, emptyView);
        presenter.getAddGoodDetail(mPage, ParameterUtils.PULL_DOWN);
    }

    private void initAdapter() {
        addGoodInfoList = new ArrayList<>();
        mAdapter = new CommonAdapter<AddGoodInfo>(this, R.layout.item_add_goods, addGoodInfoList,
            mInflater) {
            @Override
            protected void convert(final ViewHolder holder, final AddGoodInfo addGoodInfo, int position) {
                if (addGoodInfo != null) {
                    holder.setPersonImageUrl(R.id.iv_avatar, addGoodInfo.getAvatar(), true);
                    holder.setText(R.id.tv_name, addGoodInfo.getName());
                    if (TextUtils.isEmpty(addGoodInfo.getLocation())) {
                        holder.getView(R.id.tv_location).setVisibility(View.GONE);
                    } else {
                        holder.getView(R.id.tv_location).setVisibility(View.VISIBLE);
                        holder.setText(R.id.tv_location, addGoodInfo.getLocation());
                    }


                    if (TextUtils.isEmpty(addGoodInfo.getTargetCountry())) {
                        holder.getView(R.id.tv_targe_country).setVisibility(View.GONE);
                    } else {
                        holder.getView(R.id.tv_targe_country).setVisibility(View.VISIBLE);
                        holder.setText(R.id.tv_targe_country, addGoodInfo.getTargetCountry());
                    }

                    if (TextUtils.isEmpty(addGoodInfo.getTargetDegree())) {
                        holder.getView(R.id.tv_schoolName).setVisibility(View.GONE);
                    } else {
                        holder.getView(R.id.tv_schoolName).setVisibility(View.VISIBLE);
                        holder.setText(R.id.tv_schoolName, addGoodInfo.getTargetDegree());
                    }
                    holder.setText(R.id.tv_time, addGoodInfo.getLikeTimeText());
                    String addCount = "赞了你<font color='#078CF1'>" + addGoodInfo.getLikesCount() + "</font>次";
                    ((TextView) holder.getView(R.id.tv_add_count)).setText(Html.fromHtml(addCount));
                    holder.getView(R.id.iv_avatar).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!NoDoubleClickUtils.isDoubleClick()) {
                                startActivity(new Intent(AddGoodDetailActivity.this, StudentInfoActivity.class).putExtra("ids", addGoodInfo.getId()));
                            }
                        }
                    });
                }
            }
        };
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        rclvAddGood.setAdapter(loadMoreWrapper);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        rclvAddGood.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void OnLoad() {
                mPage = mPage + 1;
                presenter.getAddGoodDetail(mPage, ParameterUtils.PULL_UP);
            }
        });
    }

    @Override
    public void showTip(String message) {
        super.showTip(message);
        if (presenter != null) {
            rclvAddGood.loadComplete(true);
            loadMoreWrapper.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter = null;
        }
        if (rclvAddGood != null) {
            rclvAddGood.removeAllViews();
            rclvAddGood = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (addGoodInfoList != null) {
            addGoodInfoList.clear();
            addGoodInfoList = null;
        }
    }
}
