package study.smart.transfer_management.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.medialib.ijkplayer.WeakHandler;

import java.util.ArrayList;
import java.util.List;

import study.smart.baselib.R;
import study.smart.baselib.entity.ChatUserInfo;
import study.smart.baselib.entity.CityTeacherInfo;
import study.smart.baselib.entity.MessageDetailItemInfo;
import study.smart.baselib.entity.MyStudentInfo;
import study.smart.baselib.entity.Teacher;
import study.smart.baselib.entity.TeacherInfo;
import study.smart.baselib.entity.TransferManagerEntity;
import study.smart.baselib.entity.WorkingSearchInfo;
import study.smart.baselib.listener.OnSendMsgDialogClickListener;
import study.smart.baselib.mvp.contract.CommonSearchContract;
import study.smart.baselib.mvp.presenter.CommonSearchPresenter;
import study.smart.baselib.ui.adapter.CommonAdapter;
import study.smart.baselib.ui.adapter.MultiItemTypeAdapter;
import study.smart.baselib.ui.adapter.base.ViewHolder;
import study.smart.baselib.ui.adapter.wrapper.EmptyWrapper;
import study.smart.baselib.ui.adapter.wrapper.LoadMoreWrapper;
import study.smart.baselib.ui.base.BaseActivity;
import study.smart.baselib.ui.widget.EditTextWithClear;
import study.smart.baselib.ui.widget.HorizontalDividerItemDecoration;
import study.smart.baselib.ui.widget.LoadMoreRecyclerView;
import study.smart.baselib.ui.widget.NoScrollLinearLayoutManager;
import study.smart.baselib.ui.widget.dialog.AppBasicDialog;
import study.smart.baselib.ui.widget.dialog.DialogCreator;
import study.smart.baselib.utils.DensityUtils;
import study.smart.baselib.utils.DisplayImageUtils;
import study.smart.baselib.utils.KeyBoardUtils;
import study.smart.baselib.utils.ParameterUtils;
import study.smart.baselib.utils.TimeUtil;
import study.smart.baselib.utils.ToastUtils;
import study.smart.baselib.utils.Utils;
import study.smart.transfer_management.entity.WorkingSearchListInfo;

public class CommonSearchActivity extends BaseActivity<CommonSearchContract.Presenter> implements CommonSearchContract.View {
    private EditTextWithClear searchView;
    private LinearLayout llytTopSearch;
    private LoadMoreRecyclerView rclvSearch;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CommonAdapter<ChatUserInfo> mSchoolAdapter;
    private LoadMoreWrapper loadMoreWrapper;
    private EmptyWrapper emptyWrapper;
    private NoScrollLinearLayoutManager mLayoutManager;
    private CommonAdapter<TransferManagerEntity> mTransferAdapter;
    private CommonAdapter<MessageDetailItemInfo> msgDetailAdapter;
    private CommonAdapter<MyStudentInfo> allStudentAdapter;
    private CommonAdapter<Teacher> teacherAdapter;
    private CommonAdapter<WorkingSearchInfo> workingSearchInfoAdapter;
    private List<TransferManagerEntity> transferManagerEntities;
    private List<MessageDetailItemInfo> messageDetailItemInfos;
    private List<WorkingSearchInfo> workingSearchListInfos;
    private List<MyStudentInfo> myStudentInfos;
    private List<Teacher> teacherInfos;
    private WeakHandler mHandler;
    private int mPage = 1;
    private List<ChatUserInfo> chatUserInfoList;
    private String keyword;
    private String flag_value;
    private int typeName;
    private AppBasicDialog teacherDialog;
    private static int spaceTime = 300;//时间间隔
    private static long lastSearchTime = 0;//上次搜索的时间
    private String askName;
    private String question;
    private String questionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_dialog);
    }

    @Override
    protected void onDestroy() {
        releaseRes();
        super.onDestroy();
    }

    private void releaseRes() {
        if (rclvSearch != null) {
            rclvSearch.removeAllViews();
            rclvSearch = null;
        }
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.removeAllViews();
            swipeRefreshLayout = null;
        }
        if (mSchoolAdapter != null) {
            mSchoolAdapter.destroy();
            mSchoolAdapter = null;
        }
        if (mTransferAdapter != null) {
            mTransferAdapter.destroy();
            mTransferAdapter = null;
        }
        clearList();
        chatUserInfoList = null;
    }

    //item分割线匹配
    private void initItemDecoration() {
        if (ParameterUtils.TRANSFER_MANAGER.equals(flag_value)) {
            rclvSearch.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .size(DensityUtils.dip2px(12f)).colorResId(R.color.main_bg).build());
        } else if (ParameterUtils.MY_ALL_STUDENT.equals(flag_value)) {
            rclvSearch.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .size(DensityUtils.dip2px(0.5f)).colorResId(R.color.horizontal_line_color).build());
        } else if (ParameterUtils.STUDENT_TRANSFER_MANAGER.equals(flag_value) || ParameterUtils.COMPELETE_STUDENT.equals(flag_value)) {
            rclvSearch.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .size(DensityUtils.dip2px(0.5f)).colorResId(R.color.horizontal_line_color).build());
        } else if (ParameterUtils.SEARCH_TEACHER.equals(flag_value)) {
            rclvSearch.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .size(DensityUtils.dip2px(0.5f)).margin(DensityUtils.dip2px(16f), DensityUtils.dip2px(0f)).colorResId(R.color.horizontal_line_color).build());
        } else {
            rclvSearch.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .size(DensityUtils.dip2px(0.5f)).colorResId(R.color.horizontal_line_color).build());
        }
    }

    @Override
    public void initEvent() {
        super.initEvent();
        initRefresh();
        initTextWatcher();
        initEditor();
        initLoadMore();
    }

    @Override
    public CommonSearchContract.Presenter initPresenter() {
        return new CommonSearchPresenter(this);
    }

    @Override
    public void initView() {
        setHeadVisible(View.GONE);
        llytTopSearch = (LinearLayout) findViewById(R.id.llyt_top_search);
        Intent data = getIntent();
        flag_value = data.getStringExtra(ParameterUtils.TRANSITION_FLAG);
        if (ParameterUtils.SEARCH_TEACHER.equals(flag_value)) {
            askName = getIntent().getStringExtra("askName");
            question = getIntent().getStringExtra("question");
            questionId = getIntent().getStringExtra("questionId");
        }
        keyword = data.getStringExtra("keyword");
        typeName = data.getIntExtra("typeName", 0);
        rclvSearch = (LoadMoreRecyclerView) findViewById(R.id.rclv_search);
        rclvSearch.setHasFixedSize(true);
        //这里做layoutmanager匹配
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclvSearch.setLayoutManager(mLayoutManager);
        initItemDecoration();
        initAdapter();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srlt_search);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        searchView = (EditTextWithClear) findViewById(R.id.searchView);
        TextView tv_cancle = (TextView) findViewById(R.id.tv_cancle);
        tv_cancle.setOnClickListener(this);
        mHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        llytTopSearch.setBackgroundResource(0);
                        break;
                }
                return false;
            }
        });
        searchView.getMyEditText().setHint(R.string.search);
        searchView.getMyEditText().setHintTextColor(Color.parseColor("#949BA1"));
        searchView.getMyEditText().setTextSize(15);
        if (data.hasExtra("searchHint")) {
            searchView.getMyEditText().setHint(data.getStringExtra("searchHint"));
        }
        searchView.setInputType(InputType.TYPE_CLASS_TEXT);
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(keyword)) {
            searchView.getMyEditText().setText(keyword);
            pullRefresh(ParameterUtils.CACHED_ELSE_NETWORK, ParameterUtils.PULL_DOWN);
        } else {
            searchView.getMyEditText().requestFocus();
            KeyBoardUtils.openKeybord(searchView.getMyEditText(), this);
        }
    }

    private void initRefresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                //开始搜索
                //这里做刷新匹配
                pullRefresh(ParameterUtils.NETWORK_ELSE_CACHED, ParameterUtils.PULL_DOWN);
            }
        });
    }

    private void pullRefresh(int cacheMode, int pullTo) {
        //默认搜索转案管理
        if (ParameterUtils.TRANSFER_MANAGER.equals(flag_value)) {
            presenter.getTransferManagerList(keyword, mPage, pullTo);
        } else if (ParameterUtils.SEARCH_TEACHER.equals(flag_value)) {
            presenter.getTeacheList(keyword, mPage, pullTo);
        } else if (ParameterUtils.MSG_DETAIL.equals(flag_value)) {
            presenter.getMsgList(keyword, mPage, pullTo);
        } else if (ParameterUtils.MY_ALL_STUDENT.equals(flag_value)) {
            presenter.getAllStudentList(keyword, mPage, pullTo);
        } else if (ParameterUtils.STUDENT_TRANSFER_MANAGER.equals(flag_value)
            || ParameterUtils.TASK_TRANSFER_MANAGER.equals(flag_value)
            || ParameterUtils.REPORT_TRANSFER_MANAGER.equals(flag_value)
            || ParameterUtils.TALK_TRANSFER_MANAGER.equals(flag_value)) {
            if (typeName == WorkingSearchListInfo.TASK_TYPE) {
                presenter.getTaskList(keyword, mPage, pullTo);
            } else if (typeName == WorkingSearchListInfo.REPORT_TYPE) {
                presenter.getReportLit(keyword, mPage, pullTo);
            } else if (typeName == WorkingSearchListInfo.TALK_TYPE) {
                presenter.getTalkList(keyword, mPage, pullTo);
            } else {
                presenter.getMyStudentList(keyword, mPage, pullTo);
            }
        } else if (ParameterUtils.COMPELETE_STUDENT.equals(flag_value)) {
            presenter.getUnCompeleteStudent(keyword, mPage, pullTo);
        }
    }

    private void initLoadMore() {
        rclvSearch.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void OnLoad() {
                if (swipeRefreshLayout.isRefreshing()) {
                    if (rclvSearch != null) {
                        rclvSearch.loadComplete(true);
                    }
                    return;
                }
                mPage = mPage + 1;
                //这里做上拉匹配
                pullRefresh(ParameterUtils.NETWORK_ELSE_CACHED, ParameterUtils.PULL_UP);
            }
        });
    }

    private void initEditor() {
        searchView.getMyEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    KeyBoardUtils.closeKeybord(searchView.getMyEditText(), CommonSearchActivity.this);
                    //这里做搜索匹配
                    pullRefresh(ParameterUtils.NETWORK_ELSE_CACHED, ParameterUtils.PULL_DOWN);
                }
                return true;
            }
        });
    }

    private void initTextWatcher() {
        searchView.getMyEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    keyword = s.toString();
                    //开始搜索
                    //这里做搜索匹配
                    //延迟搜索，防止用户在输入过程中频繁调用搜索接口
                    long currentTime = System.currentTimeMillis();//当前系统时间
                    if (currentTime - lastSearchTime > spaceTime) {
                        pullRefresh(ParameterUtils.NETWORK_ELSE_CACHED, ParameterUtils.PULL_DOWN);
                    }
                    lastSearchTime = currentTime;
                } else {
                    //这里做清空操作
                    clearList();
                    rclvSearch.loadComplete(true);
                    loadMoreWrapper.notifyDataSetChanged();
                    Utils.convertActivityToTranslucent(CommonSearchActivity.this);
                    if (!ParameterUtils.MYSCHOOL_FLAG.equals(flag_value)) {
                        showEmptyView(null);
                        mHandler.sendEmptyMessageDelayed(1, 250);
                    } else {
                        llytTopSearch.setBackgroundResource(R.color.main_bg);
                    }
                }
            }
        });
    }

    private void clearList() {
        keyword = null;
        if (chatUserInfoList != null) {
            chatUserInfoList.clear();
        }
        if (transferManagerEntities != null) {
            transferManagerEntities.clear();
        }

    }

    @Override
    public void onClick(View v) {
        KeyBoardUtils.closeKeybord(searchView.getMyEditText(), this);
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.tv_cancle) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition();
            } else {
                finish();
            }

        }
    }

    private void initAdapter() {
        //这里做列表adapter匹配
        //默认适配搜索学校
        if (ParameterUtils.TRANSFER_MANAGER.equals(flag_value)) {
            initTransferAdapter();
        } else if (ParameterUtils.SEARCH_TEACHER.equals(flag_value)) {
            initTeacherListAdapter();
        } else {
            if (ParameterUtils.MSG_DETAIL.equals(flag_value)) {
                initMsgDetailAdapter();
            } else if (ParameterUtils.MY_ALL_STUDENT.equals(flag_value)
                || ParameterUtils.STUDENT_TRANSFER_MANAGER.equals(flag_value)
                || ParameterUtils.COMPELETE_STUDENT.equals(flag_value) ||
                ParameterUtils.TASK_TRANSFER_MANAGER.equals(flag_value) ||
                ParameterUtils.REPORT_TRANSFER_MANAGER.equals(flag_value) ||
                ParameterUtils.TALK_TRANSFER_MANAGER.equals(flag_value)) {
                if (typeName == WorkingSearchListInfo.TASK_TYPE) {
                    initTaskListAdapter();
                } else if (typeName == WorkingSearchListInfo.REPORT_TYPE) {
                    initReportListAdapter();
                } else if (typeName == WorkingSearchListInfo.TALK_TYPE) {
                    initTalkListAdapter();
                } else {
                    initAllStudentAdapter();
                }
            } else {
                initSchoolAdapter();
            }
        }
        rclvSearch.setAdapter(loadMoreWrapper);
    }

    private void initAllStudentAdapter() {
        myStudentInfos = new ArrayList<>();
        allStudentAdapter = new CommonAdapter<MyStudentInfo>(this, R.layout.item_my_student, myStudentInfos) {
            @Override
            protected void convert(ViewHolder holder, MyStudentInfo myStudentInfo, int position) {
                holder.setText(study.smart.transfer_management.R.id.tv_name, myStudentInfo.getName());
                holder.setText(study.smart.transfer_management.R.id.tv_target_year_season, myStudentInfo.getTargetApplicationYearSeason() + "/" + myStudentInfo.getTargetDegreeName());

            }
        };
        emptyWrapper = new EmptyWrapper<>(allStudentAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        rclvSearch.setAdapter(loadMoreWrapper);
        allStudentAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (ParameterUtils.TASK_TRANSFER_MANAGER.equals(flag_value)) {
                    startActivity(new Intent(CommonSearchActivity.this, MyTaskListActivity.class).putExtra("id", myStudentInfos.get(position).getId())
                        .putExtra("name", myStudentInfos.get(position).getName()));
                } else if (ParameterUtils.REPORT_TRANSFER_MANAGER.equals(flag_value)) {
                    startActivity(new Intent(CommonSearchActivity.this, StudentDetailReportActivity.class).putExtra("id", myStudentInfos.get(position).getUserId())
                        .putExtra("name", myStudentInfos.get(position).getName()));
                } else if (ParameterUtils.TALK_TRANSFER_MANAGER.equals(flag_value)) {
                    startActivity(new Intent(CommonSearchActivity.this, StudentDetailTalkActivity.class)
                        .putExtra("id", myStudentInfos.get(position).getUserId())
                        .putExtra("name", myStudentInfos.get(position).getName()));
                } else {
                    startActivity(new Intent(CommonSearchActivity.this, StudentDetailActivity.class)
                        .putExtra("studentInfo", myStudentInfos.get(position))
                        .putExtra("from", ParameterUtils.STUDENT_TRANSFER_MANAGER));
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder,
                                           int position) {
                return false;
            }
        });
    }


    private void initMsgDetailAdapter() {
        messageDetailItemInfos = new ArrayList<>();
        msgDetailAdapter = new CommonAdapter<MessageDetailItemInfo>(this, R.layout.item_message_detail, messageDetailItemInfos) {
            @Override
            protected void convert(ViewHolder holder, MessageDetailItemInfo messageDetailItemInfo, int position) {
                //转案和学员模块
                ImageView ivLogo = holder.getView(R.id.iv_logo);
                if (ParameterUtils.TASK_TRAINING.equals(messageDetailItemInfo.getType())) {
                    DisplayImageUtils.displayCircleImage(CommonSearchActivity.this, R.drawable.transfer_task_manager, ivLogo);
                    holder.getView(R.id.ll_trnsfer).setVisibility(View.GONE);
                    holder.getView(R.id.ll_train).setVisibility(View.VISIBLE);
                    holder.setText(R.id.tv_time, messageDetailItemInfo.getCreatedAtText());
                    holder.setText(R.id.tv_content, messageDetailItemInfo.getContent());
                    holder.setText(R.id.tv_type, messageDetailItemInfo.getData().getTypeText());
                    holder.setText(R.id.tv_start_time, TimeUtil.getStrTime(messageDetailItemInfo.getData().getStartTime()));
                    holder.setText(R.id.tv_end_time, TimeUtil.getStrTime(messageDetailItemInfo.getData().getEndTime()));
                } else {
                    holder.getView(R.id.ll_trnsfer).setVisibility(View.VISIBLE);
                    holder.getView(R.id.ll_train).setVisibility(View.GONE);
                    holder.setText(R.id.tv_content, messageDetailItemInfo.getContent());
                    holder.setText(R.id.tv_time, messageDetailItemInfo.getCreatedAtText());
                    holder.setText(R.id.tv_type, messageDetailItemInfo.getData().getTypeText());
                    holder.setText(R.id.tv_name, String.format(getString(study.smart.transfer_management.R.string.name_phone), messageDetailItemInfo.getData().getName(), messageDetailItemInfo.getData().getPhone()));
                    holder.setText(R.id.tv_number, messageDetailItemInfo.getData().getOrderId());
                    holder.setText(R.id.tv_product, messageDetailItemInfo.getData().getServiceProductNames());
                    holder.setText(R.id.tv_year_season, messageDetailItemInfo.getData().getTargetApplicationYearSeason());
                    holder.setText(R.id.tv_contractor, messageDetailItemInfo.getData().getContractor());
                    holder.setText(R.id.tv_signed_time, TimeUtil.getStrTime(messageDetailItemInfo.getData().getSignedTime()));
                    if (ParameterUtils.TRANSFER_CASE.equals(messageDetailItemInfo.getType())) {
                        DisplayImageUtils.displayCircleImage(CommonSearchActivity.this, R.drawable.transfer_icon_manager, ivLogo);
                    } else {
                        DisplayImageUtils.displayCircleImage(CommonSearchActivity.this, R.drawable.transfer_student_manager, ivLogo);
                    }
                }
                if (messageDetailItemInfo.isRead()) {
                    holder.getView(R.id.v_state).setVisibility(View.GONE);
                } else {
                    holder.getView(R.id.v_state).setVisibility(View.VISIBLE);
                }
            }
        };
        emptyWrapper = new EmptyWrapper<>(msgDetailAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);

        msgDetailAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (ParameterUtils.TRANSFER_CASE.equals(messageDetailItemInfos.get(position).getType())) {
                    presenter.getMessageDetail(messageDetailItemInfos.get(position));
                } else if (ParameterUtils.TASK_TRAINING.equals(messageDetailItemInfos.get(position).getType())) {
                    startActivity(new Intent(CommonSearchActivity.this, TaskDetailActivity.class).putExtra("id", messageDetailItemInfos.get(position).getData().getMessageId()));
                } else {
                    startActivity(new Intent(CommonSearchActivity.this, StudentDetailActivity.class)
                        .putExtra("id", messageDetailItemInfos.get(position).getData().getMessageId())
                        .putExtra("from", "message"));
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder,
                                           int position) {
                return false;
            }
        });

    }


    private void initTeacherListAdapter() {
        teacherInfos = new ArrayList<>();
        teacherAdapter = new CommonAdapter<Teacher>(this, R.layout.item_teacher_search, teacherInfos) {
            @Override
            protected void convert(ViewHolder holder, Teacher teacherInfo, int position) {
                holder.setPersonImageUrl(R.id.iv_avatar, teacherInfo.getAvatar(), true);
                holder.setText(R.id.tv_name, teacherInfo.getRealName());
                holder.setText(R.id.tv_center_name, teacherInfo.getGroup());
            }
        };
        emptyWrapper = new EmptyWrapper<>(teacherAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        rclvSearch.setAdapter(loadMoreWrapper);
        teacherAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, final int position) {
                if (teacherDialog == null) {
                    teacherDialog = DialogCreator.createTranferTeacherDialog(CommonSearchActivity.this, teacherInfos.get(position), askName, question, new OnSendMsgDialogClickListener() {
                        @Override
                        public void onPositive(String word) {
                            presenter.shareQuestion(questionId, teacherInfos.get(position).getId() + "", word);
                        }

                        @Override
                        public void onNegative() {
                            teacherDialog.dismiss();
                        }
                    });
                    teacherDialog.show();
                } else {
                    teacherDialog.show();
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder,
                                           int position) {
                return false;
            }
        });
    }


    private void initTransferAdapter() {
        transferManagerEntities = new ArrayList<>();
        mTransferAdapter = new CommonAdapter<TransferManagerEntity>(this, R.layout.item_transfer_manager, transferManagerEntities) {
            @Override
            protected void convert(ViewHolder holder, TransferManagerEntity transferManagerEntity, int position) {
                if (transferManagerEntity != null) {
                    holder.setText(R.id.tv_name, String.format(getString(R.string.transfer_owner_name),
                        TextUtils.isEmpty(transferManagerEntity.getName()) ? "" : transferManagerEntity.getName(),
                        TextUtils.isEmpty(transferManagerEntity.getTargetApplicationYearSeason()) ? "" : transferManagerEntity.getTargetApplicationYearSeason(),
                        TextUtils.isEmpty(transferManagerEntity.getTargetDegreeName()) ? "" : transferManagerEntity.getTargetDegreeName()));

                    TextView distribution_state = holder.getView(R.id.distribution_state);
                    if (!TextUtils.isEmpty(transferManagerEntity.getStatusName())) {
                        if (getString(R.string.closed).equals(transferManagerEntity.getStatusName())) {
                            distribution_state.setTextColor(Color.parseColor("#949BA1"));
                        } else if (getString(R.string.in_service).equals(transferManagerEntity.getStatusName())) {
                            distribution_state.setTextColor(Color.parseColor("#078CF1"));
                        } else {
                            distribution_state.setTextColor(Color.parseColor("#F23D18"));
                        }
                        distribution_state.setText(transferManagerEntity.getStatusName());
                    }
                    holder.setText(R.id.tv_goods_name, String.format(getString(R.string.transfer_goods_name),
                        TextUtils.isEmpty(transferManagerEntity.getServiceProductNames()) ? "" : transferManagerEntity.getServiceProductNames()));

                    holder.setText(R.id.tv_contractor, String.format(getString(R.string.transfer_contractor),
                        TextUtils.isEmpty(transferManagerEntity.getContractor()) ? "" : transferManagerEntity.getContractor()));

                    holder.setText(R.id.tv_order_number, String.format(getString(R.string.transfer_orderId), TextUtils.isEmpty(transferManagerEntity.getOrderId()) ? "" : transferManagerEntity.getOrderId()));
                    holder.setText(R.id.tv_time, transferManagerEntity.getSignedTime());
                }
            }
        };
        emptyWrapper = new EmptyWrapper<>(mTransferAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        mTransferAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                String model = "";
                //把模块名传送过去
                if (getString(R.string.un_allocate_center).equals(transferManagerEntities.get(position).getStatusName())) {
                    model = getString(R.string.un_allocate_center);
                } else if (getString(R.string.choose_teacher).equals(transferManagerEntities.get(position).getStatusName())) {
                    model = getString(R.string.allocate_center);
                } else if (getString(R.string.in_service).equals(transferManagerEntities.get(position).getStatusName())) {
                    model = getString(R.string.allocate_center);
                } else if ("已驳回".equals(transferManagerEntities.get(position).getStatusName())) {
                    model = getString(R.string.turn_down_case);
                } else {
                    //已结案状态，需要根据另外的字段进行判断
                    if ("REJECTED_CENTER".equals(transferManagerEntities.get(position).getPreClosedStatus())) {
                        model = getString(R.string.turn_down_case);
                    } else if ("UNALLOCATED_CENTER".equals(transferManagerEntities.get(position).getPreClosedStatus())) {
                        model = getString(R.string.un_allocate_center);
                    } else {
                        model = getString(R.string.allocate_center);
                    }
                }
                Intent intent = new Intent();
                //把模块名传送过去
                intent.putExtra("model", model);
                intent.putExtra("id", transferManagerEntities.get(position).getId());
                //把订单状态传送过去
                intent.putExtra("order_state", transferManagerEntities.get(position).getStatusName());
                intent.setClass(CommonSearchActivity.this, TransferManagerDetailActivity.class);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }


    private void initSchoolAdapter() {
        chatUserInfoList = new ArrayList<>();
        mSchoolAdapter = new CommonAdapter<ChatUserInfo>(this, R.layout.item_chat_user_list, chatUserInfoList, mInflater) {
            @Override
            protected void convert(ViewHolder holder, ChatUserInfo chatUserInfo, int position) {
                if (chatUserInfo != null) {
                    DisplayImageUtils.displayPersonImage(CommonSearchActivity.this, chatUserInfo.getAvatar(), (ImageView) holder.getView(R.id.iv_avatar));
                    holder.setText(R.id.tv_name, chatUserInfo.getName());
                }
            }
        };
        mSchoolAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (position < chatUserInfoList.size()) {
                    ChatUserInfo info = chatUserInfoList.get(position);

                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        emptyWrapper = new EmptyWrapper<>(mSchoolAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
    }

    private void initReportListAdapter() {
        workingSearchListInfos = new ArrayList<>();
        workingSearchInfoAdapter = new CommonAdapter<WorkingSearchInfo>(CommonSearchActivity.this, R.layout.item_my_report, workingSearchListInfos, mInflater) {
            @Override
            protected void convert(ViewHolder holder, WorkingSearchInfo workingSearchInfo, int position) {
                holder.setText(R.id.tv_report_name, String.format(mContext.getString(R.string.name_report), workingSearchInfo.getUserName(), workingSearchInfo.getTime()));
                holder.setText(R.id.tv_report_center_name, workingSearchInfo.getCenterName());
                holder.setText(R.id.tv_publish_time, TimeUtil.getStrTime(workingSearchInfo.getPublishTime()));
            }
        };

        workingSearchInfoAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                startActivity(new Intent(CommonSearchActivity.this, StudentDetailReportActivity.class).putExtra("id", workingSearchListInfos.get(position).getUserId())
                    .putExtra("name", workingSearchListInfos.get(position).getUserName()));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        emptyWrapper = new EmptyWrapper<>(workingSearchInfoAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
    }


    private void initTalkListAdapter() {
        workingSearchListInfos = new ArrayList<>();
        workingSearchInfoAdapter = new CommonAdapter<WorkingSearchInfo>(CommonSearchActivity.this, R.layout.item_talk_record_two, workingSearchListInfos, mInflater) {
            @Override
            protected void convert(ViewHolder holder, WorkingSearchInfo workingSearchInfo, int position) {
                holder.setText(R.id.tv_talk_name, workingSearchInfo.getUserName());
                holder.setText(R.id.tv_talk_content, workingSearchInfo.getName());
                holder.setText(R.id.tv_talk_center_name, workingSearchInfo.getCenterName());
                holder.setText(R.id.tv_talk_time, TimeUtil.getStrTime(workingSearchInfo.getTime()));
            }
        };

        workingSearchInfoAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                startActivity(new Intent(CommonSearchActivity.this, PersonTalkRecordDetailActivity.class).putExtra("working_search", workingSearchListInfos.get(position)));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        emptyWrapper = new EmptyWrapper<>(workingSearchInfoAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
    }


    private void initTaskListAdapter() {
        workingSearchListInfos = new ArrayList<>();
        workingSearchInfoAdapter = new CommonAdapter<WorkingSearchInfo>(CommonSearchActivity.this, R.layout.item_task_two, workingSearchListInfos, mInflater) {
            @Override
            protected void convert(ViewHolder holder, WorkingSearchInfo workingSearchInfo, int position) {
                if (TextUtils.isEmpty(workingSearchListInfos.get(position).getName())) {
                    holder.setText(R.id.tv_content, workingSearchListInfos.get(position).getTypeText());
                } else {
                    holder.setText(R.id.tv_content, workingSearchListInfos.get(position).getTypeText() + ":" + workingSearchListInfos.get(position).getName());
                }
                holder.setText(R.id.tv_center_name, workingSearchListInfos.get(position).getCenterName() + "-" + workingSearchListInfos.get(position).getUserName());
                holder.setText(R.id.tv_time, TimeUtil.getStrTime(workingSearchListInfos.get(position).getEndTime(), "MM-dd") + "截止");

                if (ParameterUtils.ALERT.equals(workingSearchListInfos.get(position).getStatus())) {
                    //临期
                    holder.getView(R.id.v_status).setBackgroundColor(Color.parseColor("#FAAD14"));
                } else if (ParameterUtils.EXPIRED.equals(workingSearchListInfos.get(position).getStatus())) {
                    //过期
                    holder.getView(R.id.v_status).setBackgroundColor(Color.parseColor("#7f000000"));
                } else if (ParameterUtils.PENDING.equals(workingSearchListInfos.get(position).getStatus())) {
                    //进行中
                    holder.getView(R.id.v_status).setBackgroundColor(Color.parseColor("#1890FF"));

                } else {
                    //已完成
                    holder.getView(R.id.v_status).setBackgroundColor(Color.parseColor("#52C41A"));
                }
            }
        };

        workingSearchInfoAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                startActivity(new Intent(CommonSearchActivity.this, TaskDetailActivity.class).putExtra("working_search_info", workingSearchListInfos.get(position)));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        emptyWrapper = new EmptyWrapper<>(workingSearchInfoAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
    }

    @Override
    public void finish() {
        KeyBoardUtils.closeKeybord(searchView.getMyEditText(), this);
        super.finish();
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }


    @Override
    public void showTransferManagerList(List<TransferManagerEntity> transferManagerEntities, int request_state) {
        if (presenter != null) {
            llytTopSearch.setBackgroundResource(R.color.main_bg);
            presenter.setEmptyView(mInflater, this, rclvSearch);
            mLayoutManager.setScrollEnabled(true);
            List datas = getNowList();
            int len = transferManagerEntities.size();
            if (datas != null) {
                if (request_state == ParameterUtils.PULL_DOWN) {
                    //下拉刷新
                    if (len <= 0) {
                        rclvSearch.loadComplete(true);
                        mLayoutManager.setScrollEnabled(false);
                    }
                    datas.clear();
                    datas.addAll(transferManagerEntities);
                    swipeRefreshLayout.setRefreshing(false);
                    loadMoreWrapper.notifyDataSetChanged();
                } else if (request_state == ParameterUtils.PULL_UP) {
                    //上拉加载
                    if (len <= 0) {
                        //没有更多内容
                        if (mPage > 1) {
                            mPage = mPage - 1;
                        }
                        if (datas.size() > 0) {
                            rclvSearch.loadComplete(false);
                        } else {
                            rclvSearch.loadComplete(true);
                        }
                    } else {
                        rclvSearch.loadComplete(true);
                        datas.addAll(transferManagerEntities);
                        loadMoreWrapper.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    @Override
    public void showMsgList(List<MessageDetailItemInfo> messageDetailItemInfos, int request_state) {
        if (presenter != null) {
            llytTopSearch.setBackgroundResource(R.color.main_bg);
            presenter.setEmptyView(mInflater, this, rclvSearch);
            mLayoutManager.setScrollEnabled(true);
            List datas = getNowList();
            int len = messageDetailItemInfos.size();
            if (datas != null) {
                if (request_state == ParameterUtils.PULL_DOWN) {
                    //下拉刷新
                    if (len <= 0) {
                        rclvSearch.loadComplete(true);
                        mLayoutManager.setScrollEnabled(false);
                    }
                    datas.clear();
                    datas.addAll(messageDetailItemInfos);
                    swipeRefreshLayout.setRefreshing(false);
                    loadMoreWrapper.notifyDataSetChanged();
                } else if (request_state == ParameterUtils.PULL_UP) {
                    //上拉加载
                    if (len <= 0) {
                        //没有更多内容
                        if (mPage > 1) {
                            mPage = mPage - 1;
                        }
                        if (datas.size() > 0) {
                            rclvSearch.loadComplete(false);
                        } else {
                            rclvSearch.loadComplete(true);
                        }
                    } else {
                        rclvSearch.loadComplete(true);
                        datas.addAll(messageDetailItemInfos);
                        loadMoreWrapper.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    @Override
    public void getAllStudentListSuccess(List<MyStudentInfo> myStudentInfos, int request_state) {
        if (presenter != null) {
            llytTopSearch.setBackgroundResource(R.color.main_bg);
            presenter.setEmptyView(mInflater, this, rclvSearch);
            mLayoutManager.setScrollEnabled(true);
            List datas = getNowList();
            int len = myStudentInfos.size();
            if (datas != null) {
                if (request_state == ParameterUtils.PULL_DOWN) {
                    //下拉刷新
                    if (len <= 0) {
                        rclvSearch.loadComplete(true);
                        mLayoutManager.setScrollEnabled(false);
                    }
                    datas.clear();
                    datas.addAll(myStudentInfos);
                    swipeRefreshLayout.setRefreshing(false);
                    loadMoreWrapper.notifyDataSetChanged();
                } else if (request_state == ParameterUtils.PULL_UP) {
                    //上拉加载
                    if (len <= 0) {
                        //没有更多内容
                        if (mPage > 1) {
                            mPage = mPage - 1;
                        }
                        if (datas.size() > 0) {
                            rclvSearch.loadComplete(false);
                        } else {
                            rclvSearch.loadComplete(true);
                        }
                    } else {
                        rclvSearch.loadComplete(true);
                        datas.addAll(myStudentInfos);
                        loadMoreWrapper.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    @Override
    public void getTaskListSuccess(List<WorkingSearchInfo> workingSearchInfos, int request_state) {
        if (presenter != null) {
            llytTopSearch.setBackgroundResource(R.color.main_bg);
            presenter.setEmptyView(mInflater, this, rclvSearch);
            mLayoutManager.setScrollEnabled(true);
            List datas = getNowList();
            int len = workingSearchInfos.size();
            if (datas != null) {
                if (request_state == ParameterUtils.PULL_DOWN) {
                    //下拉刷新
                    if (len <= 0) {
                        rclvSearch.loadComplete(true);
                        mLayoutManager.setScrollEnabled(false);
                    }
                    datas.clear();
                    datas.addAll(workingSearchInfos);
                    swipeRefreshLayout.setRefreshing(false);
                    loadMoreWrapper.notifyDataSetChanged();
                } else if (request_state == ParameterUtils.PULL_UP) {
                    //上拉加载
                    if (len <= 0) {
                        //没有更多内容
                        if (mPage > 1) {
                            mPage = mPage - 1;
                        }
                        if (datas.size() > 0) {
                            rclvSearch.loadComplete(false);
                        } else {
                            rclvSearch.loadComplete(true);
                        }
                    } else {
                        rclvSearch.loadComplete(true);
                        datas.addAll(workingSearchInfos);
                        loadMoreWrapper.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    @Override
    public void getReportListSuccess(List<WorkingSearchInfo> workingSearchInfos, int request_state) {
        if (presenter != null) {
            llytTopSearch.setBackgroundResource(R.color.main_bg);
            presenter.setEmptyView(mInflater, this, rclvSearch);
            mLayoutManager.setScrollEnabled(true);
            List datas = getNowList();
            int len = workingSearchInfos.size();
            if (datas != null) {
                if (request_state == ParameterUtils.PULL_DOWN) {
                    //下拉刷新
                    if (len <= 0) {
                        rclvSearch.loadComplete(true);
                        mLayoutManager.setScrollEnabled(false);
                    }
                    datas.clear();
                    datas.addAll(workingSearchInfos);
                    swipeRefreshLayout.setRefreshing(false);
                    loadMoreWrapper.notifyDataSetChanged();
                } else if (request_state == ParameterUtils.PULL_UP) {
                    //上拉加载
                    if (len <= 0) {
                        //没有更多内容
                        if (mPage > 1) {
                            mPage = mPage - 1;
                        }
                        if (datas.size() > 0) {
                            rclvSearch.loadComplete(false);
                        } else {
                            rclvSearch.loadComplete(true);
                        }
                    } else {
                        rclvSearch.loadComplete(true);
                        datas.addAll(workingSearchInfos);
                        loadMoreWrapper.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    @Override
    public void getTalkListSuccess(List<WorkingSearchInfo> workingSearchInfos, int request_state) {
        if (presenter != null) {
            llytTopSearch.setBackgroundResource(R.color.main_bg);
            presenter.setEmptyView(mInflater, this, rclvSearch);
            mLayoutManager.setScrollEnabled(true);
            List datas = getNowList();
            int len = workingSearchInfos.size();
            if (datas != null) {
                if (request_state == ParameterUtils.PULL_DOWN) {
                    //下拉刷新
                    if (len <= 0) {
                        rclvSearch.loadComplete(true);
                        mLayoutManager.setScrollEnabled(false);
                    }
                    datas.clear();
                    datas.addAll(workingSearchInfos);
                    swipeRefreshLayout.setRefreshing(false);
                    loadMoreWrapper.notifyDataSetChanged();
                } else if (request_state == ParameterUtils.PULL_UP) {
                    //上拉加载
                    if (len <= 0) {
                        //没有更多内容
                        if (mPage > 1) {
                            mPage = mPage - 1;
                        }
                        if (datas.size() > 0) {
                            rclvSearch.loadComplete(false);
                        } else {
                            rclvSearch.loadComplete(true);
                        }
                    } else {
                        rclvSearch.loadComplete(true);
                        datas.addAll(workingSearchInfos);
                        loadMoreWrapper.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    @Override
    public void getMessageDetailSuccess(TransferManagerEntity transferManagerEntity) {
        String model = "";
        //把模块名传送过去
        if (getString(R.string.un_allocate_center).equals(transferManagerEntity.getStatusName())) {
            model = getString(R.string.un_allocate_center);
        } else if (getString(R.string.choose_teacher).equals(transferManagerEntity.getStatusName())) {
            model = getString(R.string.allocate_center);
        } else if (getString(R.string.in_service).equals(transferManagerEntity.getStatusName())) {
            model = getString(R.string.allocate_center);
        } else if ("已驳回".equals(transferManagerEntity.getStatusName())) {
            model = getString(R.string.turn_down_case);
        } else {
            //已结案状态，需要根据另外的字段进行判断
            if ("REJECTED_CENTER".equals(transferManagerEntity.getPreClosedStatus())) {
                model = getString(R.string.turn_down_case);
            } else if ("UNALLOCATED_CENTER".equals(transferManagerEntity.getPreClosedStatus())) {
                model = getString(R.string.un_allocate_center);
            } else {
                model = getString(R.string.allocate_center);
            }
        }
        Intent intent = new Intent();
        //把模块名传送过去
        intent.putExtra("model", model);
        intent.putExtra("id", transferManagerEntity.getId());
        //把订单状态传送过去
        intent.putExtra("order_state", transferManagerEntity.getStatusName());
        intent.setClass(CommonSearchActivity.this, TransferManagerDetailActivity.class);
        startActivity(intent);

    }

    @Override
    public void getTeacherListSuccess(List<Teacher> teacherInfos, int request_state) {
        if (presenter != null) {
            llytTopSearch.setBackgroundResource(R.color.main_bg);
            presenter.setEmptyView(mInflater, this, rclvSearch);
            mLayoutManager.setScrollEnabled(true);
            List datas = getNowList();
            int len = teacherInfos.size();
            if (datas != null) {
                if (request_state == ParameterUtils.PULL_DOWN) {
                    //下拉刷新
                    if (len <= 0) {
                        rclvSearch.loadComplete(true);
                        mLayoutManager.setScrollEnabled(false);
                    }
                    datas.clear();
                    datas.addAll(teacherInfos);
                    swipeRefreshLayout.setRefreshing(false);
                    loadMoreWrapper.notifyDataSetChanged();
                } else if (request_state == ParameterUtils.PULL_UP) {
                    //上拉加载
                    if (len <= 0) {
                        //没有更多内容
                        if (mPage > 1) {
                            mPage = mPage - 1;
                        }
                        if (datas.size() > 0) {
                            rclvSearch.loadComplete(false);
                        } else {
                            rclvSearch.loadComplete(true);
                        }
                    } else {
                        rclvSearch.loadComplete(true);
                        datas.addAll(teacherInfos);
                        loadMoreWrapper.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    private List getNowList() {
        //默认搜索学校
        if (ParameterUtils.TRANSFER_MANAGER.equals(flag_value)) {
            return transferManagerEntities;
        } else if (ParameterUtils.MSG_DETAIL.equals(flag_value)) {
            return messageDetailItemInfos;
        } else if (ParameterUtils.SEARCH_TEACHER.equals(flag_value)) {
            return teacherInfos;
        } else if (ParameterUtils.MY_ALL_STUDENT.equals(flag_value)
            || ParameterUtils.STUDENT_TRANSFER_MANAGER.equals(flag_value) ||
            ParameterUtils.COMPELETE_STUDENT.equals(flag_value) ||
            ParameterUtils.REPORT_TRANSFER_MANAGER.equals(flag_value)
            || ParameterUtils.TASK_TRANSFER_MANAGER.equals(flag_value) ||
            ParameterUtils.TALK_TRANSFER_MANAGER.equals(flag_value)) {
            if (typeName == WorkingSearchListInfo.TASK_TYPE || typeName == WorkingSearchListInfo.REPORT_TYPE || typeName == WorkingSearchListInfo.TALK_TYPE) {
                return workingSearchListInfos;
            }
            return myStudentInfos;
        }
        return chatUserInfoList;
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        loadMoreWrapper.notifyDataSetChanged();
        rclvSearch.loadComplete(true);
        mLayoutManager.setScrollEnabled(false);
    }

    @Override
    public void shareQuestionSuccess() {
        finish();
        ToastUtils.shortToast("发送成功");
    }

    @Override
    public void shareQuestionFail() {
        finish();
        ToastUtils.shortToast("发送失败，请重试！");
    }

    @Override
    public void showTip(String message) {
        super.showTip(message);
        if (!Utils.isNetworkConnected()) {
            llytTopSearch.setBackgroundResource(R.color.main_bg);
            presenter.setEmptyView(mInflater, this, rclvSearch);
        }
        swipeRefreshLayout.setRefreshing(false);
        rclvSearch.loadComplete(true);
    }
}
