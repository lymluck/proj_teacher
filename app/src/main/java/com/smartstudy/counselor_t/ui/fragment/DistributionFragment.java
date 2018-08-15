package com.smartstudy.counselor_t.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.QuestionInfo;
import com.smartstudy.counselor_t.entity.RankInfo;
import com.smartstudy.counselor_t.mvp.contract.MyFocusContract;
import com.smartstudy.counselor_t.mvp.presenter.MyFocusQuestionPresenter;
import com.smartstudy.counselor_t.ui.activity.DistributionActivity;
import com.smartstudy.counselor_t.ui.activity.QaDetailActivity;
import com.smartstudy.counselor_t.ui.activity.TransferQaDetailActivity;

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
import study.smart.baselib.utils.ParameterUtils;
import study.smart.baselib.utils.ToastUtils;

/**
 * @author yqy
 * @date on 2018/8/14
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class DistributionFragment extends UIFragment<MyFocusContract.Presenter> implements MyFocusContract.View {

    private LoadMoreRecyclerView lmrvRank;
    private EmptyWrapper<QuestionInfo> emptyWrapper;
    private NoScrollLinearLayoutManager mLayoutManager;
    private View emptyView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int mPage = 1;
    private List<QuestionInfo> teacherRankInfos;
    private CommonAdapter<QuestionInfo> mAdapter;
    private LoadMoreWrapper<QuestionInfo> loadMoreWrapper;
    private List<QuestionInfo> questionInfoList;


    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        emptyView = mActivity.getLayoutInflater().inflate(R.layout.layout_empty, lmrvRank,
            false);
        presenter.showLoading(mActivity, emptyView);
        presenter.getMyFocusQuestions(mPage, ParameterUtils.PULL_DOWN);
    }


    public static DistributionFragment getInstance(Bundle bundle) {
        DistributionFragment fragment = new DistributionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onUserInvisible() {
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        super.onUserInvisible();
    }

    @Override
    public void onFirstUserInvisible() {
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        super.onFirstUserInvisible();
    }


    @Override
    protected View getLayoutView() {
        return mActivity.getLayoutInflater().inflate(
            R.layout.fragment_qa, null);
    }

    @Override
    protected void initView(View rootView) {
        lmrvRank = (LoadMoreRecyclerView) rootView.findViewById(R.id.rclv_qa);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.srlt_qa);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                getQa(ParameterUtils.PULL_DOWN);
            }
        });
        lmrvRank.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(mActivity);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lmrvRank.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
            .size(DensityUtils.dip2px(10f)).colorResId(R.color.bg_recent_user).build());
        lmrvRank.setLayoutManager(mLayoutManager);
        initAdapter();
        initEvent();
    }

    private void initAdapter() {
        questionInfoList = new ArrayList<>();
        mAdapter = new CommonAdapter<QuestionInfo>(mActivity, R.layout.item_question_list, questionInfoList) {
            @Override
            protected void convert(ViewHolder holder, QuestionInfo questionInfo, int position) {
                holder.setText(R.id.tv_create_time, questionInfo.getCreateTimeText());
                String avatar = questionInfo.getAsker().getAvatar();
                String askName = questionInfo.getAsker().getName();
                holder.setPersonImageUrl(R.id.iv_asker, avatar, true);
                holder.setText(R.id.tv_qa_name, askName);
                holder.setText(R.id.tv_qa, questionInfo.getContent());
                TextView answerCounnt = holder.getView(R.id.tv_answer_count);
                if (questionInfo.getAnswerCount() == 0) {
                    answerCounnt.setText("暂无人回答");
                    answerCounnt.setTextColor(Color.parseColor("#078CF1"));
                    holder.getView(R.id.v_cricle).setVisibility(View.GONE);
                } else {
                    if (questionInfo.getSubQuestionCount() != 0) {
                        answerCounnt.setText("对你有 " + questionInfo.getSubQuestionCount() + " 追问");
                        answerCounnt.setTextColor(Color.parseColor("#F6611D"));
                        holder.getView(R.id.v_cricle).setVisibility(View.VISIBLE);
                    } else {
                        answerCounnt.setText(questionInfo.getAnswerCount() + " 回答");
                        answerCounnt.setTextColor(Color.parseColor("#949BA1"));
                        holder.getView(R.id.v_cricle).setVisibility(View.GONE);
                    }
                }


                if (TextUtils.isEmpty(questionInfo.getUserLocation())) {
                    holder.getView(R.id.tv_location).setVisibility(View.GONE);
                } else {
                    holder.getView(R.id.tv_location).setVisibility(View.VISIBLE);
                    holder.setText(R.id.tv_location, questionInfo.getUserLocation());
                }


                if (TextUtils.isEmpty(questionInfo.getSchoolName())) {
                    holder.getView(R.id.tv_schoolName).setVisibility(View.GONE);
                } else {
                    holder.getView(R.id.tv_schoolName).setVisibility(View.VISIBLE);
                    holder.setText(R.id.tv_schoolName, questionInfo.getSchoolName());
                }
            }

        };
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        lmrvRank.setAdapter(loadMoreWrapper);
        lmrvRank.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void OnLoad() {
                if (swipeRefreshLayout.isRefreshing()) {
                    lmrvRank.loadComplete(true);
                    return;
                }
                mPage = mPage + 1;
                getQa(ParameterUtils.PULL_UP);
            }
        });
        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                mActivity.startActivity(new Intent(mActivity, TransferQaDetailActivity.class));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public MyFocusContract.Presenter initPresenter() {
        return new MyFocusQuestionPresenter(this);
    }

    @Override
    public void showTip(String message) {
        if (presenter != null) {
            swipeRefreshLayout.setRefreshing(false);
            lmrvRank.loadComplete(true);
            ToastUtils.shortToast(message);
        }
    }


    @Override
    public void getMyFocusQuestionsSuccess(List<QuestionInfo> data, int request_state) {
        if (presenter != null) {
            presenter.setEmptyView(mActivity, emptyView);
            mLayoutManager.setScrollEnabled(true);
            int len = data.size();
            if (request_state == ParameterUtils.PULL_DOWN) {
                //下拉刷新
                if (len <= 0) {
                    lmrvRank.loadComplete(true);
                    mLayoutManager.setScrollEnabled(false);
                }
                questionInfoList.clear();
                questionInfoList.addAll(data);
                swipeRefreshLayout.setRefreshing(false);
                loadMoreWrapper.notifyDataSetChanged();
            } else if (request_state == ParameterUtils.PULL_UP) {
                //上拉加载
                if (len <= 0) {
                    //没有更多内容
                    if (mPage > 1) {
                        mPage = mPage - 1;
                    }
                    lmrvRank.loadComplete(false);
                } else {
                    lmrvRank.loadComplete(true);
                    questionInfoList.addAll(data);
                    loadMoreWrapper.notifyDataSetChanged();
                }
            }
        }
        data = null;
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


    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        loadMoreWrapper.notifyDataSetChanged();
        lmrvRank.loadComplete(true);
        mLayoutManager.setScrollEnabled(false);
    }

    private void getQa(int pullAction) {
        presenter.getMyFocusQuestions(mPage, pullAction);
    }
}
