package com.smartstudy.counselor_t.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.Answerer;
import com.smartstudy.counselor_t.entity.QaDetailInfo;
import com.smartstudy.counselor_t.mvp.contract.QaDetailContract;
import com.smartstudy.counselor_t.mvp.presenter.QaDetailPresenter;
import com.smartstudy.counselor_t.ui.adapter.CommonAdapter;
import com.smartstudy.counselor_t.ui.adapter.QaDetailAdapter;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.ui.widget.HorizontalDividerItemDecoration;
import com.smartstudy.counselor_t.ui.widget.NoScrollLinearLayoutManager;
import com.smartstudy.counselor_t.util.DensityUtils;
import com.smartstudy.counselor_t.util.DisplayImageUtils;

import java.util.List;

/**
 * Created by yqy on 2017/12/4.
 */
public class QaDetailActivity extends BaseActivity<QaDetailContract.Presenter> implements QaDetailContract.View {

    private String questionId;

    private NoScrollLinearLayoutManager mLayoutManager;

    private RecyclerView recyclerView;

    private List<Answerer> answerers;


    private ImageView ivAsker;

    private TextView tvAskerName;

    private TextView tvQuestion;

    private TextView tvAskerTime;

    private QaDetailAdapter qaDetailAdapter;


    private RelativeLayout rlPost;


    private TextView tvPost;

    private EditText etAnswer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa_detail_list);
    }


    @Override
    public void initEvent() {
        etAnswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    tvPost.setBackgroundResource(R.drawable.bg_qa_posted_blue);
                    tvPost.setTextColor(getResources().getColor(R.color.white));
                    tvPost.setOnClickListener(QaDetailActivity.this);
                } else {
                    tvPost.setBackgroundResource(R.drawable.bg_qa_posted_grey);
                    tvPost.setTextColor(getResources().getColor(R.color.qa_post_clor));
                    tvPost.setOnClickListener(null);
                }
            }
        });


    }

    @Override
    public QaDetailContract.Presenter initPresenter() {
        return new QaDetailPresenter(this);
    }

    @Override
    public void initView() {
        Intent data = getIntent();
        setTitle("问题详情");
        questionId = data.getStringExtra("id");

        recyclerView = findViewById(R.id.rv_qa);
        rlPost = findViewById(R.id.rl_post);
        tvPost = findViewById(R.id.tv_post);
        etAnswer = findViewById(R.id.et_answer);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
//                .size(DensityUtils.dip2px(0.5f)).colorResId(R.color.bg_recent_user).build());

        ivAsker = findViewById(R.id.iv_asker);

        tvAskerName = findViewById(R.id.tv_asker_name);

        tvQuestion = findViewById(R.id.tv_question);

        tvAskerTime = findViewById(R.id.tv_ask_time);

        initAdapter();

        presenter.getQaDetails("1");

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_post:
//                if (askPosition == 0) {
//                    presenter.postQuestion(qaDetailInfo.getId(), etAnswer.getText().toString().trim());
//                } else {
//                    presenter.postQuestion(questionsAfters.get(askPosition - 1).getId(), etAnswer.getText().toString().trim());
//                }
                break;

            case R.id.topdefault_leftbutton:
                finish();
                break;
            default:
                break;

        }
    }

    @Override
    public void getQaDetails(QaDetailInfo data) {
        if (data.getAsker() != null) {
            DisplayImageUtils.displayCircleImage(getApplicationContext(), data.getAsker().getAvatar(), ivAsker);
            tvAskerName.setText(data.getAsker().getName());
            tvAskerTime.setText(data.getCreateTime());
            tvQuestion.setText(data.getContent());
        }

        if (data.getAnswers() != null && data.getAnswers().size() > 0) {
            qaDetailAdapter.setAnswers(data.getAnswers());
        }
        data = null;
    }


    @Override
    public void postQuestionSuccess() {
        rlPost.setVisibility(View.GONE);
        hideWindowSoft();
        presenter.getQaDetails(questionId);
    }


    private void initAdapter() {
        qaDetailAdapter = new QaDetailAdapter(this);
        recyclerView.setAdapter(qaDetailAdapter);
    }


    private void showWindowSoft() {
        etAnswer.setFocusable(true);
        etAnswer.setFocusableInTouchMode(true);
        etAnswer.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) etAnswer.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(etAnswer, 0);
    }


    private void hideWindowSoft() {
        etAnswer.setText("");
        etAnswer.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etAnswer.getWindowToken(), 0);
    }


    @Override
    protected void onDestroy() {
        if (recyclerView != null) {
            recyclerView.removeAllViews();
            recyclerView = null;
        }
        if (qaDetailAdapter != null) {
            qaDetailAdapter = null;
        }

        if (presenter != null) {
            presenter = null;
        }
        super.onDestroy();
    }

}
