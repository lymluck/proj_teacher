package com.smartstudy.counselor_t.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.ItemOnClick;
import com.smartstudy.counselor_t.entity.QaDetailInfo;
import com.smartstudy.counselor_t.mvp.contract.QaDetailContract;
import com.smartstudy.counselor_t.mvp.presenter.QaDetailPresenter;
import com.smartstudy.counselor_t.ui.adapter.QaDetailAdapter;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.ui.widget.HorizontalDividerItemDecoration;
import com.smartstudy.counselor_t.ui.widget.NoScrollLinearLayoutManager;
import com.smartstudy.counselor_t.ui.widget.audio.AudioRecorder;
import com.smartstudy.counselor_t.util.DensityUtils;
import com.smartstudy.counselor_t.util.DisplayImageUtils;
import com.smartstudy.counselor_t.util.KeyBoardUtils;
import com.smartstudy.counselor_t.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by yqy on 2017/12/4.
 */
public class QaDetailActivity extends BaseActivity<QaDetailContract.Presenter> implements QaDetailContract.View {

    private String questionId;

    private NoScrollLinearLayoutManager mLayoutManager;

    private RecyclerView recyclerView;

    private ImageView ivAsker;

    private TextView tvAskerName;

    private TextView tvQuestion;

    private TextView tvAskerTime;

    private TextView answer;

    private QaDetailAdapter qaDetailAdapter;

    private TextView tvPost;

    private EditText etAnswer;

    private ImageView iv_speak;

    private ImageView iv_audio;

    private LinearLayout ll_speak;

    private SwipeRefreshLayout swipeRefreshLayout;

    private LinearLayout ll_student;

    QaDetailInfo detailInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa_detail_list);
    }


    @Override
    public void initEvent() {

        iv_audio.setOnClickListener(this);

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

        ll_student.setOnClickListener(this);

        iv_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer integer = (Integer) iv_speak.getTag();
                integer = integer == null ? 0 : integer;
                switch (integer) {
                    case R.drawable.rc_audio_toggle:
                        iv_speak.setImageResource(R.drawable.rc_keyboard);
                        iv_speak.setTag(R.drawable.rc_keyboard);
                        ll_speak.setVisibility(View.VISIBLE);
                        KeyBoardUtils.closeKeybord(etAnswer, QaDetailActivity.this);
                        break;
                    case R.drawable.rc_keyboard:
                        iv_speak.setImageResource(R.drawable.rc_audio_toggle);
                        iv_speak.setTag(R.drawable.rc_audio_toggle);
                        ll_speak.setVisibility(View.GONE);
                        KeyBoardUtils.openKeybord(etAnswer, QaDetailActivity.this);
                        break;
                    default:
                        iv_speak.setImageResource(R.drawable.rc_keyboard);
                        iv_speak.setTag(R.drawable.rc_keyboard);
                        KeyBoardUtils.closeKeybord(etAnswer, QaDetailActivity.this);
                        ll_speak.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        etAnswer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // 此处为得到焦点时的处理内容
                iv_speak.setImageResource(R.drawable.rc_audio_toggle);
                iv_speak.setTag(R.drawable.rc_audio_toggle);
                etAnswer.setFocusable(true);
                etAnswer.setFocusableInTouchMode(true);
                ll_speak.setVisibility(View.GONE);
                return false;
            }

        });


        topdefaultLeftbutton.setOnClickListener(this);

    }


    @Override
    public QaDetailContract.Presenter initPresenter() {
        return new QaDetailPresenter(this);
    }

    @Override
    public void initView() {
        Intent data = getIntent();
        questionId = data.getStringExtra("id");

        setTitle(getString(R.string.qa_detail_title));
        setTopLineVisibility(View.VISIBLE);

        tvPost = findViewById(R.id.tv_post);

        etAnswer = findViewById(R.id.et_answer);
        answer = findViewById(R.id.answer);

        iv_audio = findViewById(R.id.iv_audio);

        ll_speak = findViewById(R.id.ll_speak);

        ivAsker = findViewById(R.id.iv_asker);

        tvAskerName = findViewById(R.id.tv_asker_name);

        tvQuestion = findViewById(R.id.tv_question);

        tvAskerTime = findViewById(R.id.tv_ask_time);

        iv_speak = findViewById(R.id.iv_speak);

        recyclerView = findViewById(R.id.rv_qa);

        ll_student = findViewById(R.id.ll_student);

        swipeRefreshLayout = findViewById(R.id.srlt_qa);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getQaDetails(questionId);
            }
        });

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .size(DensityUtils.dip2px(0.5f)).colorResId(R.color.bg_home_search).build());
        recyclerView.setFocusable(false);
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
                presenter.postAnswerText(questionId, etAnswer.getText().toString());
                break;

            case R.id.topdefault_leftbutton:
                finish();
                break;


            case R.id.ll_student:
                if (detailInfo != null && detailInfo.getAsker() != null) {
                    Intent intentStudent = new Intent();
                    intentStudent.putExtra("ids", detailInfo.getAsker().getId());
                    intentStudent.setClass(this, StudentInfoActivity.class);
                    startActivity(intentStudent);
                }
                break;

            case R.id.iv_audio:
                Intent intent = new Intent();
                intent.putExtra("question_id", questionId);
                intent.setClass(this, ReloadQaActivity.class);
                startActivity(intent);
                EventBus.getDefault().post(new ItemOnClick("Qa"));
                if (AudioRecorder.getInstance() != null) {
                    AudioRecorder.getInstance().setReset();
                }
                overridePendingTransition(0, 0);
                break;
            default:
                break;
        }
    }

    @Override
    public void getQaDetails(QaDetailInfo data) {
        swipeRefreshLayout.setRefreshing(false);
        this.detailInfo = data;
        if (data.getAsker() != null) {
            DisplayImageUtils.displayCircleImagePerson(getApplicationContext(), data.getAsker().getAvatar(), ivAsker);
            tvAskerName.setText(data.getAsker().getName());
            tvAskerTime.setText(data.getCreateTimeText());
            tvQuestion.setText(data.getContent());
            etAnswer.setHint("回复 @" + data.getAsker().getName());
        }

        if (data.getAnswers() != null && data.getAnswers().size() > 0) {
            if (qaDetailAdapter != null && data.getAsker() != null) {
                qaDetailAdapter.setAnswers(data.getAnswers(), data.getAsker().getName());
                answer.setText("回复");
            }
        } else {
            answer.setText("暂时还没有人回答哦，快来抢答吧！");
        }
        data = null;
    }

    @Override
    public void getQaDetailFail(String message) {
        ToastUtils.shortToast(this, message);
        swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void postAnswerSuccess() {
        KeyBoardUtils.closeKeybord(etAnswer, QaDetailActivity.this);
        hideAudioView();
        presenter.getQaDetails(questionId);
    }


    @Override
    protected void onResume() {
        super.onResume();
        presenter.getQaDetails(questionId);
    }

    private void initAdapter() {
        qaDetailAdapter = new QaDetailAdapter(this);
        recyclerView.setAdapter(qaDetailAdapter);
    }


    private void hideAudioView() {
        etAnswer.setText("");
        etAnswer.clearFocus();
        ll_speak.setVisibility(View.GONE);
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

        if (AudioRecorder.getInstance() != null) {
            AudioRecorder.getInstance().setReset();
        }

        super.onDestroy();
    }

}
