package com.smartstudy.counselor_t.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.Answerer;
import com.smartstudy.counselor_t.entity.QaDetailInfo;
import com.smartstudy.counselor_t.mvp.contract.QaDetailContract;
import com.smartstudy.counselor_t.mvp.presenter.QaDetailPresenter;
import com.smartstudy.counselor_t.ui.adapter.QaDetailAdapter;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.ui.widget.AudioRecordView;
import com.smartstudy.counselor_t.ui.widget.HorizontalDividerItemDecoration;
import com.smartstudy.counselor_t.ui.widget.NoScrollLinearLayoutManager;
import com.smartstudy.counselor_t.ui.widget.audio.AudioRecorder;
import com.smartstudy.counselor_t.util.DensityUtils;
import com.smartstudy.counselor_t.util.DisplayImageUtils;
import com.smartstudy.counselor_t.util.ToastUtils;

import java.io.File;
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

    private TextView tvPost;

    private EditText etAnswer;

    private ImageView iv_speak;

    private AudioRecordView audioRecordView;


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
                    tvPost.setTextColor(Color.parseColor("#078CF1"));
                    tvPost.setOnClickListener(QaDetailActivity.this);
                } else {
                    tvPost.setTextColor(Color.parseColor("#949BA1"));
                    tvPost.setOnClickListener(null);
                }
            }
        });


        iv_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer integer = (Integer) iv_speak.getTag();
                integer = integer == null ? 0 : integer;
                switch (integer) {
                    case R.drawable.rc_audio_toggle:
                        iv_speak.setImageResource(R.drawable.rc_keyboard);
                        iv_speak.setTag(R.drawable.rc_keyboard);
                        hideWindowSoft();
                        audioRecordView.setVisibility(View.VISIBLE);
                        break;
                    case R.drawable.rc_keyboard:
                        iv_speak.setImageResource(R.drawable.rc_audio_toggle);
                        iv_speak.setTag(R.drawable.rc_audio_toggle);
                        showWindowSoft();
                        audioRecordView.setVisibility(View.GONE);
                        break;
                    default:
                        iv_speak.setImageResource(R.drawable.rc_keyboard);
                        iv_speak.setTag(R.drawable.rc_keyboard);
                        hideWindowSoft();
                        audioRecordView.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });


        audioRecordView.setsendOnClickListener(new AudioRecordView.SendOnClickListener() {
            @Override
            public void sendOnClick(String path) {
                File file = new File(path);
                if (file.exists()) {
                    presenter.postAnswerVoice(questionId, file);
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
        tvPost = findViewById(R.id.tv_post);

        etAnswer = findViewById(R.id.et_answer);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .size(DensityUtils.dip2px(0.5f)).colorResId(R.color.bg_recent_user).build());

        ivAsker = findViewById(R.id.iv_asker);

        tvAskerName = findViewById(R.id.tv_asker_name);

        tvQuestion = findViewById(R.id.tv_question);

        tvAskerTime = findViewById(R.id.tv_ask_time);

        iv_speak = findViewById(R.id.iv_speak);

        audioRecordView = findViewById(R.id.arv);

        audioRecordView.setAudioRecord(new AudioRecorder());

        initAdapter();

        if (!TextUtils.isEmpty(questionId)) {
            presenter.getQaDetails(questionId);

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_post:
                if (TextUtils.isEmpty(etAnswer.getText().toString().trim())) {
                    ToastUtils.shortToast(this, "发送消息不能为空");
                    return;
                }
                presenter.postAnswerText(questionId, etAnswer.getText().toString().trim());
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
            tvAskerTime.setText(data.getCreateTimeText());
            tvQuestion.setText(data.getContent());
            etAnswer.setHint("回复 @" + data.getAsker().getName());
        }

        if (data.getAnswers() != null && data.getAnswers().size() > 0) {
            qaDetailAdapter.setAnswers(data.getAnswers(), data.getAsker().getName());
        }
        data = null;
    }


    @Override
    public void postAnswerSuccess() {
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
