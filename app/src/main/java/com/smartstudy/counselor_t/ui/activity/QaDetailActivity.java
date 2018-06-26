package com.smartstudy.counselor_t.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import study.smart.baselib.entity.QaDetailInfo;
import study.smart.baselib.ui.base.BaseActivity;
import study.smart.baselib.utils.DisplayImageUtils;
import study.smart.baselib.utils.KeyBoardUtils;
import study.smart.baselib.utils.SPCacheUtils;
import study.smart.baselib.utils.ToastUtils;
import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.mvp.contract.QaDetailContract;
import com.smartstudy.counselor_t.mvp.presenter.QaDetailPresenter;
import com.smartstudy.counselor_t.ui.adapter.QaDetailOneTreeItemParent;
import com.smartstudy.counselor_t.ui.adapter.QaDetailTwoTreeItem;
import study.smart.baselib.ui.adapter.TreeRecyclerAdapter;
import study.smart.baselib.ui.adapter.base.BaseItem;
import study.smart.baselib.ui.adapter.base.BaseRecyclerAdapter;
import study.smart.baselib.ui.adapter.base.ItemFactory;
import study.smart.baselib.ui.adapter.base.TreeRecyclerViewType;
import study.smart.baselib.ui.adapter.base.ViewHolder;
import study.smart.baselib.ui.adapter.wrapper.TreeHeaderAndFootWapper;
import study.smart.baselib.ui.widget.DrawableTextView;
import study.smart.baselib.ui.widget.NoScrollLinearLayoutManager;
import study.smart.baselib.ui.widget.audio.AudioRecorder;
import study.smart.baselib.ui.widget.treeview.TreeItem;
import com.smartstudy.counselor_t.util.NoDoubleClickUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

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
    private TextView tvPost;
    private EditText etAnswer;
    private ImageView ivSpeak;
    private ImageView ivAudio;
    private LinearLayout llSpeak;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout llStudent;
    private TextView tvLocation;
    private TextView tvSchoolName;
    private QaDetailInfo detailInfo;
    private View headView;
    private DrawableTextView dtvFocus;
    private boolean isFocus = false;
    private DrawableTextView dtvCallout;
    private DrawableTextView dtvPhone;
    private String telephone;

    private TreeRecyclerAdapter qaAdapter;
    private ArrayList<TreeItem> qaTreeItems;
    private TreeHeaderAndFootWapper<TreeItem> mHeaderAdapter;
    private BaseItem lastBaseItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa_detail_list);
    }

    @Override
    public void initEvent() {
        ivAudio.setOnClickListener(this);
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
        llStudent.setOnClickListener(this);
        ivSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer integer = (Integer) ivSpeak.getTag();
                integer = integer == null ? 0 : integer;
                switch (integer) {
                    case R.drawable.rc_audio_toggle:
                        ivSpeak.setImageResource(R.drawable.rc_keyboard);
                        ivSpeak.setTag(R.drawable.rc_keyboard);
                        llSpeak.setVisibility(View.VISIBLE);
                        KeyBoardUtils.closeKeybord(etAnswer, QaDetailActivity.this);
                        break;
                    case R.drawable.rc_keyboard:
                        ivSpeak.setImageResource(R.drawable.rc_audio_toggle);
                        ivSpeak.setTag(R.drawable.rc_audio_toggle);
                        llSpeak.setVisibility(View.GONE);
                        KeyBoardUtils.openKeybord(etAnswer, QaDetailActivity.this);
                        break;
                    default:
                        ivSpeak.setImageResource(R.drawable.rc_keyboard);
                        ivSpeak.setTag(R.drawable.rc_keyboard);
                        KeyBoardUtils.closeKeybord(etAnswer, QaDetailActivity.this);
                        llSpeak.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        etAnswer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 此处为得到焦点时的处理内容
                ivSpeak.setImageResource(R.drawable.rc_audio_toggle);
                ivSpeak.setTag(R.drawable.rc_audio_toggle);
                etAnswer.setFocusable(true);
                etAnswer.setFocusableInTouchMode(true);
                llSpeak.setVisibility(View.GONE);
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
        View decorView = getWindow().getDecorView();
        // 此处的控件ID可以使用界面当中的指定的任意控件
        View contentView = findViewById(R.id.fl_container);
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(getGlobalLayoutListener(decorView, contentView));
        Intent data = getIntent();
        questionId = data.getStringExtra("id");
        setTitle(getString(R.string.qa_detail_title));
        setTopLineVisibility(View.VISIBLE);
        tvPost = findViewById(R.id.tv_post);
        etAnswer = findViewById(R.id.et_answer);
        ivAudio = findViewById(R.id.iv_audio);
        llSpeak = findViewById(R.id.ll_speak);
        ivSpeak = findViewById(R.id.iv_speak);
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
//        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
//            .size(DensityUtils.dip2px(0.5f)).colorResId(R.color.bg_home_search).build());
        recyclerView.setFocusable(false);
        initAdapter();
        if (!TextUtils.isEmpty(questionId)) {
            presenter.getQaDetails(questionId);
        }
    }

    private ViewTreeObserver.OnGlobalLayoutListener getGlobalLayoutListener(final View decorView, final View contentView) {
        return new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                decorView.getWindowVisibleDisplayFrame(r);

                int height = decorView.getContext().getResources().getDisplayMetrics().heightPixels;
                int diff = height - r.bottom;

                if (diff != 0) {
                    if (contentView.getPaddingBottom() != diff) {
                        contentView.setPadding(0, 0, 0, diff);
                    }
                } else {
                    if (contentView.getPaddingBottom() != 0) {
                        contentView.setPadding(0, 0, 0, 0);
                    }
                }
            }
        };
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
                    ToastUtils.shortToast("发送消息不能为空");
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
                    if (!NoDoubleClickUtils.isDoubleClick()) {
                        Intent intentStudent = new Intent();
                        intentStudent.putExtra("ids", detailInfo.getAsker().getId());
                        intentStudent.setClass(this, StudentInfoActivity.class);
                        startActivity(intentStudent);
                    }
                }
                break;
            case R.id.iv_audio:
                Intent intent = new Intent();
                intent.putExtra("question_id", questionId);
                intent.setClass(this, ReloadQaActivity.class);
                startActivity(intent);
                if (lastBaseItem != null) {
                    if (lastBaseItem instanceof QaDetailOneTreeItemParent) {
                        QaDetailOneTreeItemParent qaDetailOneTreeItemParent = (QaDetailOneTreeItemParent) lastBaseItem;
                        qaDetailOneTreeItemParent.setVoiceState();
                    } else {
                        QaDetailTwoTreeItem qaDetailTwoTreeItem = (QaDetailTwoTreeItem) lastBaseItem;
                        qaDetailTwoTreeItem.setVoiceState();
                    }
                }
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
            if (qaAdapter != null && data.getAsker() != null) {
                //存入询问人的名字
                SPCacheUtils.put("askName", data.getAsker().getName());
                answer.setText("回复");
                qaTreeItems.clear();
                qaTreeItems.addAll(ItemFactory.createTreeItemList(data.getAnswers(), QaDetailOneTreeItemParent.class, null));
                qaAdapter.setDatas(qaTreeItems);
                mHeaderAdapter.notifyDataSetChanged();
            }
        } else {
            answer.setText("暂时还没有人回答哦，快来抢答吧！");
        }
        if (TextUtils.isEmpty(data.getUserLocation())) {
            tvLocation.setVisibility(View.GONE);
        } else {
            tvLocation.setVisibility(View.VISIBLE);
            tvLocation.setText(data.getUserLocation());
        }

        if (TextUtils.isEmpty(data.getSchoolName())) {
            tvSchoolName.setVisibility(View.GONE);
        } else {
            tvSchoolName.setVisibility(View.VISIBLE);
            tvSchoolName.setText(data.getSchoolName());
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
        ToastUtils.shortToast(message);
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
        ToastUtils.shortToast(message);
        tvPost.setClickable(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void questionAddMarkSuccess() {
        Drawable leftDrawable = getResources().getDrawable(R.drawable.ic_followin_red);
        leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
        dtvFocus.setCompoundDrawables(leftDrawable, null, null, null);
        isFocus = true;
        ToastUtils.shortToast("关注成功");
    }

    @Override
    public void questionDeleteMarkSuccess() {
        Drawable leftDrawable = getResources().getDrawable(R.drawable.ic_follow_gray);
        leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
        dtvFocus.setCompoundDrawables(leftDrawable, null, null, null);
        isFocus = false;
        ToastUtils.shortToast("取消关注成功");
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getQaDetails(questionId);
    }

    private void initAdapter() {
        headView = mInflater.inflate(R.layout.layout_question_list, null, false);
        headView.setVisibility(View.GONE);
        llStudent = headView.findViewById(R.id.ll_student);
        ivAsker = headView.findViewById(R.id.iv_asker);
        dtvCallout = headView.findViewById(R.id.dtv_callout);
        tvAskerName = headView.findViewById(R.id.tv_asker_name);
        tvLocation = headView.findViewById(R.id.tv_location);
        tvSchoolName = headView.findViewById(R.id.tv_schoolName);
        dtvFocus = headView.findViewById(R.id.dtv_focus);
        tvQuestion = headView.findViewById(R.id.tv_question);
        tvAskerTime = headView.findViewById(R.id.tv_ask_time);
        answer = headView.findViewById(R.id.answer);
        dtvPhone = headView.findViewById(R.id.dtv_phone);
        qaAdapter = new TreeRecyclerAdapter();
        qaAdapter.setType(TreeRecyclerViewType.SHOW_ALL);
        mHeaderAdapter = new TreeHeaderAndFootWapper<>(qaAdapter);
        mHeaderAdapter.addHeaderView(headView);
        qaTreeItems = new ArrayList<>();
        recyclerView.setAdapter(mHeaderAdapter);

        qaAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(final ViewHolder viewHolder, final BaseItem baseItem, final int itemPosition) {
                if (baseItem instanceof QaDetailOneTreeItemParent) {
                    QaDetailOneTreeItemParent qaDetailOneTreeItemParent = (QaDetailOneTreeItemParent) baseItem;
                    if (lastBaseItem != null) {
                        //上一次点击的是二级目录
                        if (lastBaseItem instanceof QaDetailTwoTreeItem) {
                            //重置上次二级目录的播放状态
                            QaDetailTwoTreeItem qaDetailTwoTreeItem = (QaDetailTwoTreeItem) lastBaseItem;
                            qaDetailTwoTreeItem.setVoiceState();
                            qaDetailOneTreeItemParent.voiceOnclick(viewHolder);
                        } else {
                            QaDetailOneTreeItemParent lastOneItem = (QaDetailOneTreeItemParent) lastBaseItem;
                            if (qaDetailOneTreeItemParent == lastBaseItem) {
                                if (qaDetailOneTreeItemParent.getIsPlaying()) {
                                    lastOneItem.setVoiceState();
                                } else {
                                    qaDetailOneTreeItemParent.voiceOnclick(viewHolder);
                                }
                            } else {
                                lastOneItem.setVoiceState();
                                qaDetailOneTreeItemParent.voiceOnclick(viewHolder);
                            }
                        }
                    } else {
                        qaDetailOneTreeItemParent.voiceOnclick(viewHolder);
                    }
                }

                if (baseItem instanceof QaDetailTwoTreeItem) {
                    QaDetailTwoTreeItem qaDetailTwoTreeItem = (QaDetailTwoTreeItem) baseItem;
                    if (lastBaseItem != null) {
                        if (lastBaseItem instanceof QaDetailOneTreeItemParent) {
                            //上次点击的是一级目录，需要把上次上次点击的状态重置，包括播放状态等
                            QaDetailOneTreeItemParent qaDetailOneTreeItemParent = (QaDetailOneTreeItemParent) lastBaseItem;
                            qaDetailOneTreeItemParent.setVoiceState();
                            qaDetailTwoTreeItem.voiceOnclick(viewHolder);
                        } else {
                            QaDetailTwoTreeItem lastTwoItem = (QaDetailTwoTreeItem) lastBaseItem;
                            //如果上次点击的是二级目录本身，也需要重置状态
                            //判断是否同一条二级目录
                            if (qaDetailTwoTreeItem == lastBaseItem) {
                                if (qaDetailTwoTreeItem.getIsPlaying()) {
                                    lastTwoItem.setVoiceState();
                                } else {
                                    qaDetailTwoTreeItem.voiceOnclick(viewHolder);
                                }
                            } else {
                                lastTwoItem.setVoiceState();
                                qaDetailTwoTreeItem.voiceOnclick(viewHolder);
                            }
                        }
                    } else {
                        qaDetailTwoTreeItem.voiceOnclick(viewHolder);
                    }
                }
                lastBaseItem = baseItem;
            }
        });

    }


    private void hideAudioView() {
        etAnswer.setText("");
        etAnswer.clearFocus();
    }

    @Override
    protected void onDestroy() {
        if (recyclerView != null) {
            recyclerView.removeAllViews();
            recyclerView = null;
        }
        if (qaAdapter != null) {
            qaAdapter = null;
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
