//package com.smartstudy.counselor_t.ui.activity;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.smartstudy.counselor_t.R;
//import com.smartstudy.counselor_t.entity.Answerer;
//import com.smartstudy.counselor_t.entity.QaDetailInfo;
//import com.smartstudy.counselor_t.mvp.contract.QaDetailContract;
//import com.smartstudy.counselor_t.mvp.presenter.QaDetailPresenter;
//import com.smartstudy.counselor_t.ui.adapter.CommonAdapter;
//import com.smartstudy.counselor_t.ui.adapter.base.ViewHolder;
//import com.smartstudy.counselor_t.ui.adapter.wrapper.EmptyWrapper;
//import com.smartstudy.counselor_t.ui.adapter.wrapper.HeaderAndFooterWrapper;
//import com.smartstudy.counselor_t.ui.base.BaseActivity;
//import com.smartstudy.counselor_t.ui.widget.NoScrollLinearLayoutManager;
//import com.smartstudy.counselor_t.ui.widget.TagsLayout;
//import com.smartstudy.counselor_t.util.DisplayImageUtils;
//import com.smartstudy.counselor_t.util.ParameterUtils;
//import com.smartstudy.counselor_t.util.SPCacheUtils;
//import com.smartstudy.counselor_t.util.ToastUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by yqy on 2017/12/4.
// */
//public class QaDetailActivity extends BaseActivity<QaDetailContract.Presenter> implements QaDetailContract.View {
//
//    private String questionId;
//
//    private QaDetailContract.Presenter presenter;
//
//    private HeaderAndFooterWrapper mHeader;
//
//    private NoScrollLinearLayoutManager mLayoutManager;
//
//    private RecyclerView recyclerView;
//
//    private CommonAdapter<Answerer> mAdapter;
//
//    private List<Answerer> answerers;
//
//    private EmptyWrapper<QaDetailInfo> emptyWrapper;
//
//    private View emptyView;
//
//    private View footView;
//
//    private QaDetailInfo qaDetailInfo;
//
//    private TextView tvCount;
//    private ImageView ivAsker;
//
//    private TagsLayout tagsLayout;
//
//    private TextView tvAskerName;
//
//    private TextView tvQuestion;
//
//    private TextView tvAskerTime;
//
//    private TextView tvAssignee;
//
//    private ImageView ivAssignee;
//
//    private TextView tvAnswer;
//
//    private TextView tvAsk;
//
//    private ImageView ivGoods;
//
//    private String type;
//
//    private int positionBody;
//
//    private int askPosition = 0;
//
//    private RelativeLayout rlPost;
//
//    private RelativeLayout relativeLayout;
//
//    private TextView tvPost;
//
//    private EditText etAnswer;
//
//    private TextView tvAnswerBody;
//
//    private LinearLayout llAdd;
//
//    private View vAnswer;
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_qa_detail_list);
//    }
//
//
//    @Override
//    public void initEvent() {
//        etAnswer.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!TextUtils.isEmpty(s.toString())) {
//                    tvPost.setBackgroundResource(R.drawable.bg_qa_posted_blue);
//                    tvPost.setTextColor(getResources().getColor(R.color.white));
//                    tvPost.setOnClickListener(QaDetailActivity.this);
//                } else {
//                    tvPost.setBackgroundResource(R.drawable.bg_qa_posted_grey);
//                    tvPost.setTextColor(getResources().getColor(R.color.qa_post_clor));
//                    tvPost.setOnClickListener(null);
//                }
//            }
//        });
//
//
//    }
//
//    @Override
//    public QaDetailContract.Presenter initPresenter() {
//        return new QaDetailPresenter(this);
//    }
//
//    @Override
//    public void initView() {
//        Intent data = getIntent();
//        setTitle("问题详情");
//        questionId = data.getStringExtra("id");
//        if (!TextUtils.isEmpty(questionId) && data.getExtras() != null) {
//            questionId = data.getExtras().getString("id");
//        }
//        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
//
//        recyclerView = findViewById(R.id.rv_qa);
//        rlPost = findViewById(R.id.rl_post);
//        tvPost = findViewById(R.id.tv_post);
//        etAnswer = findViewById(R.id.et_answer);
//        recyclerView.setHasFixedSize(true);
//        mLayoutManager = new NoScrollLinearLayoutManager(this);
//        mLayoutManager.setScrollEnabled(true);
//        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(mLayoutManager);
//
//        initAdapter();
//        initHeaderAndFooter();
//        RelativeLayout rlAll = findViewById(R.id.rl_all);
////        emptyView = this.getLayoutInflater().inflate(R.layout.layout_empty, rlAll, false);
////        presenter.showLoading(this, emptyView);
//
//        if (!TextUtils.isEmpty(questionId)) {
//            presenter.getQaDetails(questionId, ParameterUtils.PULL_DOWN);
//        }
//
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.tv_post:
//                if (askPosition == 0) {
//                    presenter.postQuestion(qaDetailInfo.getId(), etAnswer.getText().toString().trim());
//                } else {
//                    presenter.postQuestion(questionsAfters.get(askPosition - 1).getId(), etAnswer.getText().toString().trim());
//                }
//                break;
//
//            case R.id.topdefault_leftbutton:
//                finish();
//                break;
//            default:
//                break;
//
//        }
////    }
//
//    @Override
//    public void getQaDetails(QaDetailInfo data) {
//        if (qaDetailInfo != null) {
//            qaDetailInfo = null;
//        }
//        footView.setVisibility(View.VISIBLE);
//        qaDetailInfo = data;
//        if (data.getAsker() != null) {
//            DisplayImageUtils.displayCircleImage(getApplicationContext(), data.getAsker().getAvatar(), ivAsker);
//            tvAskerName.setText(data.getAsker().getName());
//        }
//
//        if (data.isLiked()) {
//            ivGoods.setImageResource(R.drawable.goodpress);
//        } else {
//            ivGoods.setImageResource(R.drawable.good);
//        }
//
//
//        tvCount.setText(data.getLikedCount() + "");
//
//        if (!TextUtils.isEmpty(data.getQuestion())) {
//            tvQuestion.setText(data.getQuestion());
//            tvAskerTime.setText(data.getAskTime());
//        }
//
//
//        if (!TextUtils.isEmpty(data.getAnswer())) {
//            tvAnswer.setText(data.getAnswer());
//        }
//
//        if (data.getAnswerer() != null) {
//            relativeLayout.setVisibility(View.VISIBLE);
//            tvAnswerBody.setVisibility(View.VISIBLE);
//            vAnswer.setVisibility(View.VISIBLE);
//            DisplayImageUtils.displayCircleImage(getApplicationContext(), data.getAnswerer().getAvatarUrl(), ivAssignee);
//            tvAssignee.setText(data.getAnswerer().getName());
//        } else {
//            relativeLayout.setVisibility(View.GONE);
//            vAnswer.setVisibility(View.GONE);
//            tvAnswerBody.setText("选校帝留学服务老师正在解答中,请稍后...");
//        }
//
//
//        if (data.getQuestionsAfter() != null && data.getQuestionsAfter().size() > 0) {
//            if (questionsAfters != null) {
//                questionsAfters.clear();
//                questionsAfters.addAll(data.getQuestionsAfter());
//                mHeader.notifyDataSetChanged();
//            }
//        }
//        askPosition = getAskPosition();
//        if (qaDetailInfo.getAsker() != null && qaDetailInfo.getAsker().getId().equals(SPCacheUtils.get("user_id", ""))) {
//            if (askPosition == 0) {
//                tvAsk.setVisibility(View.VISIBLE);
//            } else {
//                tvAsk.setVisibility(View.GONE);
//            }
//        } else {
//            tvAsk.setVisibility(View.GONE);
//        }
//
//        if (data.getTagsData() != null) {
//            showTab();
//        }
//
//        data = null;
//    }
//
//
//    @Override
//    public void checkFavorite(boolean checkResult) {
//        if (checkResult) {
//            ToastUtils.shortToast(this, "不能重复点赞");
//        } else {
////            presenter.addFavorite(id);
//        }
//    }
//
//    @Override
//    public void addFavorite(boolean checkResult) {
//        if (checkResult) {
//            if ("header".equals(type)) {
//                ivGoods.setImageResource(R.drawable.goodpress);
//                tvCount.setText((Integer.parseInt(tvCount.getText().toString().trim()) + 1) + "");
//            } else {
//                QaDetailInfo.QuestionsAfter questionsAfter = questionsAfters.get(positionBody - 1);
//                questionsAfter.setLikedCount(questionsAfter.getLikedCount() + 1);
//                questionsAfter.setLiked(true);
//                mHeader.notifyItemChanged(positionBody, questionsAfter);
//                questionsAfter = null;
//            }
//        } else {
//            ToastUtils.shortToast(this, "点赞失败");
//        }
//    }
//
//    @Override
//    public void postQuestionSuccess() {
//
//        rlPost.setVisibility(View.GONE);
//        hideWindowSoft();
//        presenter.getQaDetails(questionId, ParameterUtils.PULL_DOWN);
//    }
//
//
//    private void initAdapter() {
//        questionsAfters = new ArrayList<QaDetailInfo.QuestionsAfter>();
//        mAdapter = new CommonAdapter<QaDetailInfo.QuestionsAfter>(this, R.layout.item_qa_detail, questionsAfters) {
//
//            @Override
//            protected void convert(final ViewHolder holder, final QaDetailInfo.QuestionsAfter questionsAfter, final int position) {
//
////                String question="回复<font color='#008CF9'>"+"@"+questionsAfter.getAsker().getReplyPerson()+"</font>"+": "+questionsAfter.getQuestion().trim();
//                holder.setText(R.id.tv_question, questionsAfter.getQuestion());
//                holder.getView(R.id.ll_add).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        type = "body";
//                        positionBody = position;
//                        presenter.addFavorite(questionsAfter.getId());
//                    }
//                });
//
//                if (questionsAfter.getAnswer() != null) {
//                    holder.getView(R.id.rl_qa_detail).setVisibility(View.VISIBLE);
//                    holder.getView(R.id.v_answer).setVisibility(View.VISIBLE);
//                    DisplayImageUtils.displayCircleImage(QaDetailActivity.this, questionsAfter.getAnswerer().getAvatarUrl(), (ImageView) holder.getView(R.id.iv_assignee));
//                    holder.setText(R.id.tv_assignee, questionsAfter.getAnswerer().getName());
//
//                    holder.setText(R.id.tv_answer, questionsAfter.getAnswer());
//                    holder.setText(R.id.liked_count, questionsAfter.getLikedCount() + "");
//                    if (questionsAfter.isLiked()) {
//                        ((ImageView) holder.getView(R.id.iv_goods)).setImageResource(R.drawable.goodpress);
//                    } else {
//                        ((ImageView) holder.getView(R.id.iv_goods)).setImageResource(R.drawable.good);
//                    }
//
//                } else {
//                    holder.getView(R.id.rl_qa_detail).setVisibility(View.GONE);
//                    holder.getView(R.id.v_answer).setVisibility(View.GONE);
//                }
//
//
//                holder.getView(R.id.ll_ask).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (rlPost.isShown()) {
//                            rlPost.setVisibility(View.GONE);
//                            hideWindowSoft();
//                        }
//                    }
//                });
//                if (qaDetailInfo.getAsker() != null && qaDetailInfo.getAsker().getId().equals(SPCacheUtils.get("user_id", ""))) {
//                    if (position == askPosition) {
//                        holder.getView(R.id.tv_ask).setVisibility(View.VISIBLE);
//                    } else {
//                        holder.getView(R.id.tv_ask).setVisibility(View.GONE);
//                    }
//                } else {
//                    holder.getView(R.id.tv_ask).setVisibility(View.GONE);
//                }
//                holder.getView(R.id.rl_qa_detail).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (qaDetailInfo.getAsker() != null && qaDetailInfo.getAsker().getId().equals(SPCacheUtils.get("user_id", ""))) {
//                            if (position == askPosition) {
//                                if (!rlPost.isShown()) {
//                                    rlPost.setVisibility(View.VISIBLE);
//                                    showWindowSoft();
//                                }
//                            } else {
//                                rlPost.setVisibility(View.GONE);
//                                hideWindowSoft();
//                            }
//                        } else {
//                            rlPost.setVisibility(View.GONE);
//                            hideWindowSoft();
//                        }
//                    }
//                });
//            }
//        };
//    }
//
//
//    private void initHeaderAndFooter() {
//        footView = getLayoutInflater().inflate(R.layout.activity_question_list, null, false);
//
//        ivAsker = footView.findViewById(R.id.iv_asker);
//
//        tvAskerName = footView.findViewById(R.id.tv_asker_name);
//
//        tvQuestion = footView.findViewById(R.id.tv_question);
//
//        tvAskerTime = footView.findViewById(R.id.tv_ask_time);
//
//        tvAssignee = footView.findViewById(R.id.tv_assignee);
//
//        ivAssignee = footView.findViewById(R.id.iv_assignee);
//
//        tvAnswer = footView.findViewById(R.id.tv_answer);
//
//        ivGoods = footView.findViewById(R.id.iv_goods);
//
//        tvAsk = footView.findViewById(R.id.tv_ask);
//
//        tagsLayout = footView.findViewById(R.id.tl_qa);
//
//        vAnswer = footView.findViewById(R.id.v_answer);
//
//        relativeLayout = footView.findViewById(R.id.rl_answer);
//
//        llAdd = footView.findViewById(R.id.ll_add);
//        tvAnswerBody = footView.findViewById(R.id.answer);
//        relativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (qaDetailInfo.getAsker() != null && qaDetailInfo.getAsker().getId().equals(SPCacheUtils.get("user_id", ""))) {
//                    if (askPosition == 0) {
//                        if (!rlPost.isShown()) {
//                            rlPost.setVisibility(View.VISIBLE);
//                            showWindowSoft();
//                        } else {
//                            rlPost.setVisibility(View.GONE);
//                            hideWindowSoft();
//                        }
//                    } else {
//                        rlPost.setVisibility(View.GONE);
//                        hideWindowSoft();
//                    }
//                } else {
//                    rlPost.setVisibility(View.GONE);
//                    hideWindowSoft();
//                }
//            }
//        });
//
//
//        footView.findViewById(R.id.ll_all).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (rlPost.isShown()) {
//                    rlPost.setVisibility(View.GONE);
//                    hideWindowSoft();
//                }
//            }
//        });
//
//
//        llAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                type = "header";
//                presenter.addFavorite(qaDetailInfo.getId());
//            }
//        });
//
//        tvCount = footView.findViewById(R.id.liked_count);
//
//        tvAsk = footView.findViewById(R.id.tv_ask);
////        emptyWrapper = new EmptyWrapper<>(mAdapter);
//        mHeader = new HeaderAndFooterWrapper(mAdapter);
//        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//        footView.setLayoutParams(lp);
//        footView.setVisibility(View.GONE);
//        mHeader.addHeaderView(footView);
//        recyclerView.setAdapter(mHeader);
//    }
//
//    /**
//     * 获取显示我要追问的位置
//     *
//     * @return
//     */
//    public int getAskPosition() {
//        if (questionsAfters != null && questionsAfters.size() > 0) {
//            for (int i = questionsAfters.size() - 1; i >= 0; i--) {
//                if (questionsAfters.get(i).getAnswerer() != null) {
//                    return i + 1;
//                }
//            }
//        }
//        return 0;
//    }
//
//
//    private void showTab() {
//        List<String> list = new ArrayList<>();
//        for (int i = 0; i < qaDetailInfo.getTagsData().size(); i++) {
//            if (!TextUtils.isEmpty(qaDetailInfo.getTagsData().get(i).getName())) {
//                list.add(qaDetailInfo.getTagsData().get(i).getName());
//            }
//        }
//        tagsLayout.setMaxLine(2);
//        tagsLayout.setBackGroup(R.drawable.bg_qa_tab11);
//        tagsLayout.createTab(this, list);
//    }
//
//
//    private void showWindowSoft() {
//        etAnswer.setFocusable(true);
//        etAnswer.setFocusableInTouchMode(true);
//        etAnswer.requestFocus();
//        InputMethodManager inputManager = (InputMethodManager) etAnswer.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputManager.showSoftInput(etAnswer, 0);
//    }
//
//
//    private void hideWindowSoft() {
//        etAnswer.setText("");
//        etAnswer.clearFocus();
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(etAnswer.getWindowToken(), 0);
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        if (recyclerView != null) {
//            recyclerView.removeAllViews();
//            recyclerView = null;
//        }
//        if (mAdapter != null) {
//            mAdapter.destroy();
//            mAdapter = null;
//        }
//        if (questionsAfters != null) {
//            questionsAfters.clear();
//            questionsAfters = null;
//        }
//        if (presenter != null) {
//            presenter = null;
//        }
//        super.onDestroy();
//    }
//
//}
