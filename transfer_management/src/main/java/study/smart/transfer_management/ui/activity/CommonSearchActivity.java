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
import study.smart.baselib.entity.TransferManagerEntity;
import study.smart.baselib.mvp.contract.CommonSearchContract;
import study.smart.baselib.mvp.presenter.CommonSearchPresenter;
import study.smart.baselib.ui.adapter.CommonAdapter;
import study.smart.baselib.ui.adapter.base.ViewHolder;
import study.smart.baselib.ui.adapter.wrapper.EmptyWrapper;
import study.smart.baselib.ui.adapter.wrapper.LoadMoreWrapper;
import study.smart.baselib.ui.base.BaseActivity;
import study.smart.baselib.ui.widget.EditTextWithClear;
import study.smart.baselib.ui.widget.HorizontalDividerItemDecoration;
import study.smart.baselib.ui.widget.LoadMoreRecyclerView;
import study.smart.baselib.ui.widget.NoScrollLinearLayoutManager;
import study.smart.baselib.utils.DensityUtils;
import study.smart.baselib.utils.DisplayImageUtils;
import study.smart.baselib.utils.KeyBoardUtils;
import study.smart.baselib.utils.ParameterUtils;
import study.smart.baselib.utils.Utils;

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
    private List<TransferManagerEntity> transferManagerEntities;
    private WeakHandler mHandler;
    private int mPage = 1;
    private List<ChatUserInfo> chatUserInfoList;
    private String keyword;
    private String flag_value;
    private static int spaceTime = 300;//时间间隔
    private static long lastSearchTime = 0;//上次搜索的时间

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
        }
        rclvSearch.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
            .size(DensityUtils.dip2px(0.5f)).colorResId(R.color.horizontal_line_color).build());
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
        keyword = data.getStringExtra("keyword");
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
        if (data.hasExtra("searchHint")) {
            searchView.getMyEditText().setHint(data.getStringExtra("searchHint"));
        }
        searchView.setInputType(InputType.TYPE_CLASS_TEXT);
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        new CommonSearchPresenter(this);
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
        //默认搜索学校
        if (ParameterUtils.TRANSFER_MANAGER.equals(flag_value)) {
            presenter.getTransferManagerList(keyword, mPage, pullTo);
        }
        presenter.getSchools(cacheMode, getIntent().getStringExtra("countryId"), keyword, mPage, pullTo, flag_value);
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
        } else {
            initSchoolAdapter();
        }
        rclvSearch.setAdapter(loadMoreWrapper);
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
                        if ("已结案".equals(transferManagerEntity.getStatusName())) {
                            distribution_state.setTextColor(Color.parseColor("#949BA1"));
                        } else if ("服务中".equals(transferManagerEntity.getStatusName())) {
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
                if ("未分配中心".equals(transferManagerEntities.get(position).getStatusName())) {
                    model = "未分配中心";
                } else if ("选导师".equals(transferManagerEntities.get(position).getStatusName())) {
                    model = "已分配中心";
                } else if ("服务中".equals(transferManagerEntities.get(position).getStatusName())) {
                    model = "已分配中心";
                } else if ("已驳回".equals(transferManagerEntities.get(position).getStatusName())) {
                    model = "被驳回转案";
                } else {
                    //已结案状态，需要根据另外的字段进行判断
                    if ("REJECTED_CENTER".equals(transferManagerEntities.get(position).getPreClosedStatus())) {
                        model = "被驳回转案";
                    } else if ("UNALLOCATED_CENTER".equals(transferManagerEntities.get(position).getPreClosedStatus())) {
                        model = "未分配中心";
                    } else {
                        model = "已分配中心";
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
    public void showResult(List data, int request_state, String flag) {
        llytTopSearch.setBackgroundResource(R.color.main_bg);
        presenter.setEmptyView(mInflater, this, rclvSearch);
        mLayoutManager.setScrollEnabled(true);
        List datas = getNowList();
        int len = data.size();
        if (datas != null) {
            if (request_state == ParameterUtils.PULL_DOWN) {
                //下拉刷新
                if (len <= 0) {
                    rclvSearch.loadComplete(true);
                    mLayoutManager.setScrollEnabled(false);
                    if (ParameterUtils.MYSCHOOL_FLAG.equals(flag_value)) {
//                        llyt_choose.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (ParameterUtils.MYSCHOOL_FLAG.equals(flag_value)) {
//                        llyt_choose.setVisibility(View.GONE);
                    }
                }
                datas.clear();
                datas.addAll(data);
                swipeRefreshLayout.setRefreshing(false);
                loadMoreWrapper.notifyDataSetChanged();

                //下拉刷新
                if (len > 0) {
                    if (ParameterUtils.MYSCHOOL_FLAG.equals(flag_value)) {
//                        llyt_choose.setVisibility(View.GONE);
                    }
                } else {
                    rclvSearch.loadComplete(true);
                    mLayoutManager.setScrollEnabled(false);
                    if (ParameterUtils.MYSCHOOL_FLAG.equals(flag_value)) {
//                        llyt_choose.setVisibility(View.VISIBLE);
                    }
                }
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
                    if (ParameterUtils.MYSCHOOL_FLAG.equals(flag_value)) {
//                        llyt_choose.setVisibility(View.GONE);
                    }
                    rclvSearch.loadComplete(true);
                    datas.addAll(data);
                    loadMoreWrapper.notifyDataSetChanged();
                }
            }
        }
        data = null;
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

    private List getNowList() {
        //默认搜索学校
        if (ParameterUtils.TRANSFER_MANAGER.equals(flag_value)) {
            return transferManagerEntities;
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
