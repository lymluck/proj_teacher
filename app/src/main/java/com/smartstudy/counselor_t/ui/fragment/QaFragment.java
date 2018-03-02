package com.smartstudy.counselor_t.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.QuestionInfo;
import com.smartstudy.counselor_t.entity.SchoolInfo;
import com.smartstudy.counselor_t.handler.WeakHandler;
import com.smartstudy.counselor_t.mvp.contract.QaListContract;
import com.smartstudy.counselor_t.mvp.presenter.QuestionsPresenter;
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
import com.smartstudy.counselor_t.util.Utils;

import java.util.ArrayList;
import java.util.List;


public class QaFragment extends UIFragment<QaListContract.Presenter> implements QaListContract.View {

    private LoadMoreRecyclerView rclv_qa;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CommonAdapter<QuestionInfo> mAdapter;
    private LoadMoreWrapper<QuestionInfo> loadMoreWrapper;
    private EmptyWrapper<SchoolInfo> emptyWrapper;
    private NoScrollLinearLayoutManager mLayoutManager;
    private View emptyView;

    private List<QuestionInfo> questionInfoList;
    private int mPage = 1;
    private String data_tag;

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
    public QaListContract.Presenter initPresenter() {
        return new QuestionsPresenter(this);
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
        getQa(ParameterUtils.PULL_DOWN);
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
        data_tag = "list";
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.srlt_qa);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                getQa(ParameterUtils.PULL_DOWN);
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
                if ("list".equals(data_tag)) {
                    holder.setText(R.id.tv_create_time, questionInfo.getCreateTime());
                    String avatar = questionInfo.getAsker().getAvatar();
                    String askName = questionInfo.getAsker().getName();
                    holder.setPersonImageUrl(R.id.iv_asker, avatar, true);
                    holder.setText(R.id.tv_qa_name, askName);
                    holder.setText(R.id.tv_qa, questionInfo.getContent());
                    TextView answerCounnt = holder.getView(R.id.tv_answer_count);
                    if (questionInfo.getAnswerCount() == 0) {
                        answerCounnt.setText("暂无人回答");
                        answerCounnt.setTextColor(Color.parseColor("#078CF1"));
                    } else {
                        if (questionInfo.getSubQuestionCount() != 0) {
                            answerCounnt.setText("对你有 " + questionInfo.getAnswerCount() + " 追问");
                            answerCounnt.setTextColor(Color.parseColor("#F6611D"));
                        } else {
                            answerCounnt.setText(questionInfo.getAnswerCount() + " 回答");
                            answerCounnt.setTextColor(Color.parseColor("#949BA1"));
                        }
                    }
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
                getQa(ParameterUtils.PULL_UP);
            }
        });
        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
//                QuestionInfo info = questionInfoList.get(position);
//                Intent toMoreDetails = new Intent(mActivity, QaDetailActivity.class);
//                toMoreDetails.putExtra("id", info.getId() + "");
//                startActivity(toMoreDetails);
//                info = null;
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void getQa(int pullAction) {
        if ("list".equals(data_tag)) {
            presenter.getQuestions(true, mPage, pullAction);
        } else if ("my".equals(data_tag)) {
            presenter.getMyQuestions(mPage, pullAction);
        } else if ("school".equals(data_tag)) {
            String schoolId = getArguments().getString("schoolId");
            presenter.getSchoolQa(schoolId, mPage, pullAction);
        }
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
    public void getQuestionsSuccess(List<QuestionInfo> data, int request_state) {
        if (presenter != null) {
            presenter.setEmptyView(mActivity, emptyView, data_tag);
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
            if (!"list".equals(data_tag)) {
//                if (ParameterUtils.RESPONSE_CODE_NOLOGIN.equals(errCode)) {
//                    DialogCreator.createLoginDialog(mActivity);
//                }
            }
            swipeRefreshLayout.setRefreshing(false);
            rclv_qa.loadComplete(true);
            ToastUtils.shortToast(mActivity, message);
        }
    }
}
