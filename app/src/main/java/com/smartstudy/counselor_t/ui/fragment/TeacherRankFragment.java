package com.smartstudy.counselor_t.ui.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.RankInfo;
import com.smartstudy.counselor_t.mvp.contract.TeacherRankContract;
import com.smartstudy.counselor_t.mvp.presenter.TeacherRankPresenter;

import java.util.ArrayList;
import java.util.List;

import study.smart.baselib.entity.TeacherRankInfo;
import study.smart.baselib.ui.adapter.CommonAdapter;
import study.smart.baselib.ui.adapter.base.ViewHolder;
import study.smart.baselib.ui.adapter.wrapper.EmptyWrapper;
import study.smart.baselib.ui.adapter.wrapper.LoadMoreWrapper;
import study.smart.baselib.ui.base.UIFragment;
import study.smart.baselib.ui.widget.HorizontalDividerItemDecoration;
import study.smart.baselib.ui.widget.LoadMoreRecyclerView;
import study.smart.baselib.ui.widget.NoScrollLinearLayoutManager;
import study.smart.baselib.utils.DensityUtils;
import study.smart.baselib.utils.DisplayImageUtils;
import study.smart.baselib.utils.ParameterUtils;
import study.smart.baselib.utils.ToastUtils;

/**
 * @author yqy
 * @date on 2018/7/18
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TeacherRankFragment extends UIFragment<TeacherRankContract.Presenter> implements TeacherRankContract.View {

    private LoadMoreRecyclerView lmrvRank;
    private EmptyWrapper<TeacherRankInfo> emptyWrapper;
    private NoScrollLinearLayoutManager mLayoutManager;
    private View emptyView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int mPage = 1;
    private RankInfo rankInfo;
    private List<TeacherRankInfo> teacherRankInfos;
    private CommonAdapter<TeacherRankInfo> mAdapter;
    private LoadMoreWrapper<TeacherRankInfo> loadMoreWrapper;
    private View myself;


    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        emptyView = mActivity.getLayoutInflater().inflate(R.layout.layout_empty, lmrvRank,
            false);
        presenter.showLoading(mActivity, emptyView);
        presenter.getTeacherRank(rankInfo.getType(), mPage + "", ParameterUtils.PULL_DOWN);
    }


    public static TeacherRankFragment getInstance(Bundle bundle) {
        TeacherRankFragment fragment = new TeacherRankFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View getLayoutView() {
        return mActivity.getLayoutInflater().inflate(R.layout.fragment_teacher_rank, null);
    }

    @Override
    protected void initView(View rootView) {
        swipeRefreshLayout = rootView.findViewById(R.id.srlt_rank);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                presenter.getTeacherRank(rankInfo.getType(), mPage + "", ParameterUtils.PULL_DOWN);
            }
        });
        myself = rootView.findViewById(R.id.myself);
        lmrvRank = rootView.findViewById(R.id.rclv_rank);
        lmrvRank.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(mActivity);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lmrvRank.setLayoutManager(mLayoutManager);
        lmrvRank.setItemAnimator(new DefaultItemAnimator());
        lmrvRank.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mActivity)
            .size(DensityUtils.dip2px(0.5f)).margin(DensityUtils.dip2px(40f), 0)
            .colorResId(R.color.main_bg)
            .build());
        initAdapter();
        rankInfo = getArguments().getParcelable("rankInfos");
    }

    private void initAdapter() {
        teacherRankInfos = new ArrayList<>();
        mAdapter = new CommonAdapter<TeacherRankInfo>(mActivity, R.layout.item_teacher_rank, teacherRankInfos) {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            protected void convert(ViewHolder holder, TeacherRankInfo teacherRankInfo, int position) {
                if (teacherRankInfos != null) {
                    TextView tvRank = holder.getView(R.id.tv_rank);
                    ImageView ivRank = holder.getView(R.id.iv_rank);
                    if (teacherRankInfo.getRank() == 1) {
                        tvRank.setVisibility(View.GONE);
                        ivRank.setVisibility(View.VISIBLE);
                        ivRank.setImageResource(R.drawable.icon_number_one);
                    } else if (teacherRankInfo.getRank() == 2) {
                        tvRank.setVisibility(View.GONE);
                        ivRank.setVisibility(View.VISIBLE);
                        ivRank.setImageResource(R.drawable.icon_number_two);
                    } else if (teacherRankInfo.getRank() == 3) {
                        tvRank.setVisibility(View.GONE);
                        ivRank.setVisibility(View.VISIBLE);
                        ivRank.setImageResource(R.drawable.icon_number_three);
                    } else {
                        tvRank.setVisibility(View.VISIBLE);
                        ivRank.setVisibility(View.GONE);
                        Typeface typeFace = Typeface.createFromAsset(mActivity.getAssets(), "fonts/GothamMedium.otf");
                        // 应用字体
                        tvRank.setTypeface(typeFace);
                        tvRank.setText(teacherRankInfo.getRank() + "");
                    }

                    holder.setPersonImageUrl(R.id.iv_avatar, teacherRankInfo.getAvatar(), true);
                    holder.setText(R.id.tv_name, teacherRankInfo.getName());
                    String comment = "";
                    if (teacherRankInfo.getRatingsCount() == 0) {
                        comment = "暂未收到评价";
                        holder.setText(R.id.tv_comment, comment);
                    } else {
                        comment = "收到 " + "<font color='#FE824D'>" + teacherRankInfo.getRatingsCount() + "</font> 次评价  平均分 <font color='#FE824D'>" + teacherRankInfo.getAverageScore() + "</font>";
                        holder.setText(R.id.tv_comment, Html.fromHtml(comment));
                    }

                    TextView tvCount = holder.getView(R.id.tv_answer_count);
                    Typeface typeFace = Typeface.createFromAsset(mActivity.getAssets(), "fonts/GothamBook.otf");
                    tvCount.setTypeface(typeFace);
                    tvCount.setText(teacherRankInfo.getAnsweredQuestionsCount());
                }
            }
        };
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        lmrvRank.setAdapter(loadMoreWrapper);
        //加载更多
        lmrvRank.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void OnLoad() {
                if (swipeRefreshLayout.isRefreshing()) {
                    lmrvRank.loadComplete(true);
                    return;
                }
                mPage = mPage + 1;
                presenter.getTeacherRank(rankInfo.getType(), mPage + "", ParameterUtils.PULL_UP);
            }
        });
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public TeacherRankContract.Presenter initPresenter() {
        return new TeacherRankPresenter(this);
    }

    @Override
    public void showTip(String message) {
        if (presenter != null) {
            swipeRefreshLayout.setRefreshing(false);
            if (lmrvRank != null) {
                lmrvRank.loadComplete(true);
            }
            ToastUtils.shortToast(message);
        }
    }

    @Override
    public void getTransferListSuccess(List<TeacherRankInfo> teacherRankInfos, int request_state) {
        if (presenter != null) {
            presenter.setEmptyView(emptyView);
            mLayoutManager.setScrollEnabled(true);
            int len = teacherRankInfos.size();
            if (request_state == ParameterUtils.PULL_DOWN) {
                //下拉刷新
                if (len <= 0) {
                    lmrvRank.loadComplete(true);
                    mLayoutManager.setScrollEnabled(false);
                }
                this.teacherRankInfos.clear();
                this.teacherRankInfos.addAll(teacherRankInfos);
                swipeRefreshLayout.setRefreshing(false);
                loadMoreWrapper.notifyDataSetChanged();
            } else if (request_state == ParameterUtils.PULL_UP) {
                //上拉加载
                if (len <= 0) {
                    //没有更多内容
                    if (mPage > 1) {
                        mPage = mPage - 1;
                    }
                    if (this.teacherRankInfos.size() > 0) {
                        lmrvRank.loadComplete(false);
                    } else {
                        lmrvRank.loadComplete(true);
                    }
                } else {
                    lmrvRank.loadComplete(true);
                    this.teacherRankInfos.addAll(teacherRankInfos);
                    loadMoreWrapper.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void showMySelf(TeacherRankInfo teacherRankInfo) {
        if (teacherRankInfo != null) {
            myself.setVisibility(View.VISIBLE);
            TextView tvBank = myself.findViewById(R.id.tv_rank);
            TextView tvName = myself.findViewById(R.id.tv_name);
            TextView tvComment = myself.findViewById(R.id.tv_comment);
            ImageView ivAvatar = myself.findViewById(R.id.iv_avatar);
            TextView tvCount = myself.findViewById(R.id.tv_answer_count);
            ImageView ivRank = myself.findViewById(R.id.iv_rank);
            ivRank.setVisibility(View.GONE);
            tvBank.setVisibility(View.VISIBLE);
            Typeface typeFaceRank = Typeface.createFromAsset(mActivity.getAssets(), "fonts/GothamMedium.otf");
            // 应用字体
            tvBank.setTypeface(typeFaceRank);
            myself.setBackgroundColor(Color.parseColor("#F5B400"));
            tvBank.setTextColor(Color.parseColor("#FFFFFF"));
            tvBank.setText(teacherRankInfo.getRank() + "");
            tvName.setTextColor(Color.parseColor("#FFFFFF"));
            tvName.setText(teacherRankInfo.getName());
            tvComment.setTextColor(Color.parseColor("#FFFFFF"));
            String comment = "";
            if (teacherRankInfo.getRatingsCount() == 0) {
                comment = "暂未收到评价";
            } else {
                comment = "收到 " + teacherRankInfo.getRatingsCount() + " 次评价  平均分 " + teacherRankInfo.getAverageScore();
            }
            tvComment.setText(comment);
            tvCount.setTextColor(Color.parseColor("#FFFFFF"));
            Typeface typeFace = Typeface.createFromAsset(mActivity.getAssets(), "fonts/GothamBook.otf");
            tvCount.setTypeface(typeFace);
            tvCount.setText(teacherRankInfo.getAnsweredQuestionsCount());
            ((TextView) myself.findViewById(R.id.tv_answer)).setTextColor(Color.parseColor("#FFFFFF"));
            ((TextView) myself.findViewById(R.id.tv_question)).setTextColor(Color.parseColor("#FFFFFF"));
            ivAvatar.setBackgroundResource(R.drawable.bg_teacher_avatar);
            DisplayImageUtils.displayPersonImage(mActivity, teacherRankInfo.getAvatar(), ivAvatar);
        } else {
            myself.setVisibility(View.GONE);
        }
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        loadMoreWrapper.notifyDataSetChanged();
        if (lmrvRank != null) {
            lmrvRank.loadComplete(true);
        }
        mLayoutManager.setScrollEnabled(false);
        view = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (lmrvRank != null) {
            lmrvRank.removeAllViews();
            lmrvRank = null;
        }
        if (mLayoutManager != null) {
            mLayoutManager.removeAllViews();
            mLayoutManager = null;
        }

        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }

        if (teacherRankInfos != null) {
            teacherRankInfos.clear();
            teacherRankInfos = null;
        }
    }
}
