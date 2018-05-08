package com.smartstudy.counselor_t.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
import com.smartstudy.counselor_t.ui.adapter.wrapper.HeaderAndFooterWrapper;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.ui.widget.DrawableTextView;
import com.smartstudy.counselor_t.ui.widget.HorizontalDividerItemDecoration;
import com.smartstudy.counselor_t.ui.widget.NoScrollLinearLayoutManager;
import com.smartstudy.counselor_t.ui.widget.audio.AudioRecorder;
import com.smartstudy.counselor_t.util.DensityUtils;
import com.smartstudy.counselor_t.util.DisplayImageUtils;
import com.smartstudy.counselor_t.util.KeyBoardUtils;
import com.smartstudy.counselor_t.util.SPCacheUtils;
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

    private TextView tv_location;

    private TextView tv_platform;

    private TextView tv_schoolName;

    private QaDetailInfo detailInfo;

    private HeaderAndFooterWrapper mHeader;

    private View headView;

    private DrawableTextView dtvFocus;

    private boolean isFocus = false;

    private DrawableTextView dtvCallout;

    private DrawableTextView dtvPhone;

    private String telephone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa_detail_list);
    }


    @Override
    public void initEvent() {

        iv_audio.setOnClickListener(this);

        dtvFocus.setOnClickListener(this);

        dtvCallout.setOnClickListener(this);

        dtvPhone.setOnClickListener(this);

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
        iv_audio = findViewById(R.id.iv_audio);
        ll_speak = findViewById(R.id.ll_speak);
        iv_speak = findViewById(R.id.iv_speak);
        recyclerView = findViewById(R.id.rv_qa);
        swipeRefreshLayout = findViewById(R.id.srlt_qa);

        if (!(boolean) SPCacheUtils.get("is_guide", false)) {
            startActivity(new Intent(this, QaDetailGuideActivity.class));
            SPCacheUtils.put("is_guide", true);
            overridePendingTransition(0, 0);
        }

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
        initHeaderAndFooter();
        if (!TextUtils.isEmpty(questionId)) {
            presenter.getQaDetails(questionId);

        }
    }

    private void initHeaderAndFooter() {
        headView = mInflater.inflate(R.layout.layout_question_list, null, false);
        ll_student = headView.findViewById(R.id.ll_student);
        ivAsker = headView.findViewById(R.id.iv_asker);
        dtvCallout = headView.findViewById(R.id.dtv_callout);
        tvAskerName = headView.findViewById(R.id.tv_asker_name);
        tv_location = headView.findViewById(R.id.tv_location);
        tv_platform = headView.findViewById(R.id.tv_platform);
        tv_schoolName = headView.findViewById(R.id.tv_schoolName);
        dtvFocus = headView.findViewById(R.id.dtv_focus);
        tvQuestion = headView.findViewById(R.id.tv_question);
        tvAskerTime = headView.findViewById(R.id.tv_ask_time);
        answer = headView.findViewById(R.id.answer);
        dtvPhone = headView.findViewById(R.id.dtv_phone);
        mHeader = new HeaderAndFooterWrapper(qaDetailAdapter);
        mHeader.addHeaderView(headView);
        recyclerView.setAdapter(mHeader);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dtv_focus:
                if (!isFocus) {
                    presenter.questionAddMark(questionId);
                } else {
                    presenter.questionDeleteMark(questionId);
                }
                break;

            case R.id.dtv_phone:
                if (!TextUtils.isEmpty(telephone)) {
                    Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telephone));//跳转到拨号界面，同时传递电话号码
                    startActivity(dialIntent);

                }
                break;
            case R.id.tv_post:
                if (TextUtils.isEmpty(etAnswer.getText().toString().trim())) {
                    ToastUtils.shortToast(this, "发送消息不能为空");
                    return;
                }
                tvPost.setClickable(false);
                presenter.postAnswerText(questionId, etAnswer.getText().toString());
                break;

            case R.id.topdefault_leftbutton:
                finish();
                break;

            case R.id.dtv_callout:
                startActivity(new Intent(this, AddLabelActivity.class).putExtra("id", detailInfo.getAsker().getId()));
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
        headView.setVisibility(View.VISIBLE);
        isFocus = data.isMarked();
        this.detailInfo = data;
        if (data.getAsker() != null) {
            DisplayImageUtils.formatPersonImgUrl(getApplicationContext(), data.getAsker().getAvatar(), ivAsker);
            tvAskerName.setText(data.getAsker().getName());
            tvAskerTime.setText(data.getCreateTimeText());
            tvQuestion.setText(data.getContent());
            String name = data.getAsker().getName();
            if (!TextUtils.isEmpty(name)) {
                if (name.length() > 15) {
                    name = name.substring(0, 12) + "...";
                }
            }
            etAnswer.setHint("回复 @" + name);
            if (TextUtils.isEmpty(data.getAsker().getPhone())) {
                Drawable leftDrawable = getResources().getDrawable(R.drawable.ic_phone_disabled);
                leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
                dtvPhone.setCompoundDrawables(leftDrawable, null, null, null);
                dtvPhone.setTextColor(Color.parseColor("#C4C9CC"));
            } else {
                telephone = data.getAsker().getPhone();
                Drawable leftDrawable = getResources().getDrawable(R.drawable.ic_telephone);
                leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
                dtvPhone.setCompoundDrawables(leftDrawable, null, null, null);
                dtvPhone.setTextColor(Color.parseColor("#58646E"));
            }
        }

        if (data.getAnswers() != null && data.getAnswers().size() > 0) {
            if (qaDetailAdapter != null && data.getAsker() != null) {
                qaDetailAdapter.setAnswers(data.getAnswers(), data.getAsker().getName());
                answer.setText("回复");
                mHeader.notifyDataSetChanged();
            }
        } else {
            answer.setText("暂时还没有人回答哦，快来抢答吧！");
        }

        if (TextUtils.isEmpty(data.getUserLocation())) {
            tv_location.setVisibility(View.GONE);
        } else {
            tv_location.setVisibility(View.VISIBLE);
            tv_location.setText(data.getUserLocation());
        }

        if (TextUtils.isEmpty(data.getPlatform())) {
            tv_platform.setVisibility(View.GONE);
        } else {
            tv_platform.setVisibility(View.VISIBLE);
            tv_platform.setText(data.getPlatform());
        }

        if (TextUtils.isEmpty(data.getSchoolName())) {
            tv_schoolName.setVisibility(View.GONE);
        } else {
            tv_schoolName.setVisibility(View.VISIBLE);
            tv_schoolName.setText(data.getSchoolName());
        }

        if (isFocus) {
            Drawable leftDrawable = getResources().getDrawable(R.drawable.ic_followin_red);
            leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
            dtvFocus.setCompoundDrawables(leftDrawable, null, null, null);
        } else {
            Drawable leftDrawable = getResources().getDrawable(R.drawable.ic_follow_gray);
            leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
            dtvFocus.setCompoundDrawables(leftDrawable, null, null, null);
        }

        EventBus.getDefault().post(data);

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
        tvPost.setClickable(true);
        etAnswer.setText("");
        presenter.getQaDetails(questionId);
    }

    @Override
    public void postAnswerFail(String message) {
        ToastUtils.shortToast(this, message);
        tvPost.setClickable(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void questionAddMarkSuccess() {
        Drawable leftDrawable = getResources().getDrawable(R.drawable.ic_followin_red);
        leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
        dtvFocus.setCompoundDrawables(leftDrawable, null, null, null);
        isFocus = true;
        ToastUtils.shortToast(this, "关注成功");
    }

    @Override
    public void questionDeleteMarkSuccess() {
        Drawable leftDrawable = getResources().getDrawable(R.drawable.ic_follow_gray);
        leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
        dtvFocus.setCompoundDrawables(leftDrawable, null, null, null);
        isFocus = false;
        ToastUtils.shortToast(this, "取消关注成功");
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
