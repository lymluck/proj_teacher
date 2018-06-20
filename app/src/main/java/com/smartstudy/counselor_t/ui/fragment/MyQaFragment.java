package com.smartstudy.counselor_t.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.QuestionInfo;
import com.smartstudy.counselor_t.mvp.contract.MyQaFragmentContract;
import com.smartstudy.counselor_t.mvp.presenter.MyQaFragmentPresenter;
import com.smartstudy.counselor_t.ui.activity.MyQaActivity;
import com.smartstudy.counselor_t.ui.activity.QaDetailActivity;
import com.smartstudy.counselor_t.ui.adapter.CommonAdapter;
import com.smartstudy.counselor_t.ui.adapter.base.ViewHolder;
import com.smartstudy.counselor_t.ui.adapter.wrapper.EmptyWrapper;
import com.smartstudy.counselor_t.ui.adapter.wrapper.LoadMoreWrapper;
import com.smartstudy.counselor_t.ui.base.UIFragment;
import com.smartstudy.counselor_t.ui.widget.HorizontalDividerItemDecoration;
import com.smartstudy.counselor_t.ui.widget.LoadMoreRecyclerView;
import com.smartstudy.counselor_t.ui.widget.NoScrollLinearLayoutManager;
import com.smartstudy.counselor_t.util.DensityUtils;
import com.smartstudy.counselor_t.util.ParameterUtils;
import com.smartstudy.counselor_t.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yqy
 * @date on 2018/3/20
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyQaFragment extends UIFragment<MyQaFragmentContract.Presenter> implements MyQaFragmentContract.View {

    private LoadMoreRecyclerView rclv_qa;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CommonAdapter<QuestionInfo> mAdapter;
    private LoadMoreWrapper<QuestionInfo> loadMoreWrapper;
    private EmptyWrapper<QuestionInfo> emptyWrapper;
    private NoScrollLinearLayoutManager mLayoutManager;
    private View emptyView;

    private List<QuestionInfo> questionInfoList;
    private int mPage = 1;
    private MyQaActivity myQaActivity;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        myQaActivity = (MyQaActivity) activity;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isPrepared = true;
    }

    @Override
    protected View getLayoutView() {
        return mActivity.getLayoutInflater().inflate(
            R.layout.fragment_qa, null);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public MyQaFragmentContract.Presenter initPresenter() {
        return new MyQaFragmentPresenter(this);
    }


    @Override
    public void onDetach() {
        if (presenter != null) {
            presenter = null;
        }
        if (rclv_qa != null) {
            rclv_qa.removeAllViews();
            rclv_qa = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (questionInfoList != null) {
            questionInfoList.clear();
            questionInfoList = null;
        }
        super.onDetach();
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        emptyView = mActivity.getLayoutInflater().inflate(R.layout.layout_empty, rclv_qa, false);
        presenter.showLoading(mActivity, emptyView);
        getMyQa(ParameterUtils.PULL_DOWN);
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
    protected void initView(View rootView) {
        rclv_qa = (LoadMoreRecyclerView) rootView.findViewById(R.id.rclv_qa);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.srlt_qa);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                getMyQa(ParameterUtils.PULL_DOWN);
            }
        });
        rclv_qa.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(mActivity);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclv_qa.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
            .size(DensityUtils.dip2px(10f)).colorResId(R.color.bg_recent_user).build());
        rclv_qa.setLayoutManager(mLayoutManager);
        initAdapter();
        initEvent();

    }

    @Override
    protected void initEvent() {

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
                if (questionInfo.getAsker().isCanContact()) {
                    holder.getView(R.id.iv_phone).setVisibility(View.VISIBLE);
                } else {
                    holder.getView(R.id.iv_phone).setVisibility(View.GONE);
                }

                if (questionInfo.getSubQuestionCount() != 0) {
                    answerCounnt.setText("对你有 " + questionInfo.getSubQuestionCount() + " 追问");
                    answerCounnt.setTextColor(Color.parseColor("#F6611D"));
                    holder.getView(R.id.v_cricle).setVisibility(View.VISIBLE);
                } else {
                    if (!questionInfo.isHasUnreadAnswersOfMe()) {
                        answerCounnt.setText("已看");
                        answerCounnt.setTextColor(Color.parseColor("#949BA1"));
                        holder.getView(R.id.v_cricle).setVisibility(View.GONE);
                    } else {
                        answerCounnt.setText(" 未看");
                        answerCounnt.setTextColor(Color.parseColor("#078CF1"));
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
        rclv_qa.setAdapter(loadMoreWrapper);
        rclv_qa.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void OnLoad() {
                if (swipeRefreshLayout.isRefreshing()) {
                    rclv_qa.loadComplete(true);
                    return;
                }
                mPage = mPage + 1;
                getMyQa(ParameterUtils.PULL_UP);
            }
        });
        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                QuestionInfo info = questionInfoList.get(position);
                Intent toMoreDetails = new Intent(mActivity, QaDetailActivity.class);
                toMoreDetails.putExtra("id", info.getId() + "");
                startActivity(toMoreDetails);
                info = null;
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void getMyQa(int pullAction) {
        presenter.getMyQuestions(mPage, pullAction);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                mActivity.finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void getQuestionsSuccess(int subCount, List<QuestionInfo> data, int request_state) {
        if (myQaActivity != null) {
            TextView tvSubCount = myQaActivity.getSubCountTextView();
            if (subCount == 0) {
                tvSubCount.setVisibility(View.GONE);
            } else {
                tvSubCount.setVisibility(View.VISIBLE);
                if (subCount < 100) {
                    if (subCount < 10) {
                        tvSubCount.setBackgroundResource(R.drawable.bg_circle_answer_count);
                    } else {
                        tvSubCount.setBackgroundResource(R.drawable.bg_count_answer);
                    }
                    tvSubCount.setText(subCount + "");
                } else {
                    tvSubCount.setBackgroundResource(R.drawable.bg_count_answer);
                    tvSubCount.setText("99+");
                }
            }
        }
        if (presenter != null) {
            presenter.setEmptyView(mActivity, emptyView);
            mLayoutManager.setScrollEnabled(true);
            int len = data.size();
            if (request_state == ParameterUtils.PULL_DOWN) {
                //下拉刷新
                if (len <= 0) {
                    rclv_qa.loadComplete(true);
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
                    rclv_qa.loadComplete(false);
                } else {
                    rclv_qa.loadComplete(true);
                    questionInfoList.addAll(data);
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
        rclv_qa.loadComplete(true);
        mLayoutManager.setScrollEnabled(false);
    }

    @Override
    public void showTip(String message) {
        if (presenter != null) {
            swipeRefreshLayout.setRefreshing(false);
            rclv_qa.loadComplete(true);
            ToastUtils.shortToast(message);
        }
    }
}
