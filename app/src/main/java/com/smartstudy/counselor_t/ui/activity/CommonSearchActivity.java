package com.smartstudy.counselor_t.ui.activity;

import android.content.Intent;
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

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.ChatUserInfo;
import com.smartstudy.counselor_t.handler.WeakHandler;
import com.smartstudy.counselor_t.mvp.contract.CommonSearchContract;
import com.smartstudy.counselor_t.mvp.presenter.CommonSearchPresenter;
import com.smartstudy.counselor_t.ui.adapter.CommonAdapter;
import com.smartstudy.counselor_t.ui.adapter.base.ViewHolder;
import com.smartstudy.counselor_t.ui.adapter.wrapper.EmptyWrapper;
import com.smartstudy.counselor_t.ui.adapter.wrapper.LoadMoreWrapper;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.ui.widget.EditTextWithClear;
import com.smartstudy.counselor_t.ui.widget.HorizontalDividerItemDecoration;
import com.smartstudy.counselor_t.ui.widget.LoadMoreRecyclerView;
import com.smartstudy.counselor_t.ui.widget.NoScrollLinearLayoutManager;
import com.smartstudy.counselor_t.util.DensityUtils;
import com.smartstudy.counselor_t.util.DisplayImageUtils;
import com.smartstudy.counselor_t.util.KeyBoardUtils;
import com.smartstudy.counselor_t.util.ParameterUtils;
import com.smartstudy.counselor_t.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class CommonSearchActivity extends BaseActivity<CommonSearchContract.Presenter> implements CommonSearchContract.View {
    private EditTextWithClear searchView;
    private LinearLayout llyt_top_search;
    private LoadMoreRecyclerView rclv_search;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CommonAdapter<ChatUserInfo> mSchoolAdapter;
    private LoadMoreWrapper loadMoreWrapper;
    private EmptyWrapper emptyWrapper;
    private NoScrollLinearLayoutManager mLayoutManager;

    private WeakHandler mHandler;
    private int mPage = 1;
    private String flag_value;
    private List<ChatUserInfo> chatUserInfoList;
    private String keyword;
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
        if (rclv_search != null) {
            rclv_search.removeAllViews();
            rclv_search = null;
        }
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.removeAllViews();
            swipeRefreshLayout = null;
        }
        if (mSchoolAdapter != null) {
            mSchoolAdapter.destroy();
            mSchoolAdapter = null;
        }
        clearList();
        chatUserInfoList = null;
    }

    //item分割线匹配
    private void initItemDecoration() {
        rclv_search.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
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
        llyt_top_search = (LinearLayout) findViewById(R.id.llyt_top_search);
        Intent data = getIntent();
        flag_value = data.getStringExtra(ParameterUtils.TRANSITION_FLAG);
        keyword = data.getStringExtra("keyword");
        rclv_search = (LoadMoreRecyclerView) findViewById(R.id.rclv_search);
        rclv_search.setHasFixedSize(true);
        //这里做layoutmanager匹配
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclv_search.setLayoutManager(mLayoutManager);
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
                        llyt_top_search.setBackgroundResource(0);
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
        presenter.getSchools(cacheMode, getIntent().getStringExtra("countryId"), keyword, mPage, pullTo, flag_value);
    }

    private void initLoadMore() {
        rclv_search.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void OnLoad() {
                if (swipeRefreshLayout.isRefreshing()) {
                    if (rclv_search != null) {
                        rclv_search.loadComplete(true);
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
                    rclv_search.loadComplete(true);
                    loadMoreWrapper.notifyDataSetChanged();
                    Utils.convertActivityToTranslucent(CommonSearchActivity.this);
                    if (!ParameterUtils.MYSCHOOL_FLAG.equals(flag_value)) {
                        showEmptyView(null);
                        mHandler.sendEmptyMessageDelayed(1, 250);
                    } else {
                        llyt_top_search.setBackgroundResource(R.color.main_bg);
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
    }

    @Override
    public void onClick(View v) {
        KeyBoardUtils.closeKeybord(searchView.getMyEditText(), this);
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_cancle:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
                break;
        }
    }

    private void initAdapter() {
        //这里做列表adapter匹配
        //默认适配搜索学校
        initSchoolAdapter();
        rclv_search.setAdapter(loadMoreWrapper);
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
        llyt_top_search.setBackgroundResource(R.color.main_bg);
        presenter.setEmptyView(mInflater, this, rclv_search, flag_value);
        mLayoutManager.setScrollEnabled(true);
        List datas = getNowList();
        int len = data.size();
        if (datas != null) {
            if (request_state == ParameterUtils.PULL_DOWN) {
                //下拉刷新
                if (len <= 0) {
                    rclv_search.loadComplete(true);
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
                    rclv_search.loadComplete(true);
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
                        rclv_search.loadComplete(false);
                    } else {
                        rclv_search.loadComplete(true);
                    }
                } else {
                    if (ParameterUtils.MYSCHOOL_FLAG.equals(flag_value)) {
//                        llyt_choose.setVisibility(View.GONE);
                    }
                    rclv_search.loadComplete(true);
                    datas.addAll(data);
                    loadMoreWrapper.notifyDataSetChanged();
                }
            }
        }
        data = null;
    }

    private List getNowList() {
        //默认搜索学校
        return chatUserInfoList;
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        loadMoreWrapper.notifyDataSetChanged();
        rclv_search.loadComplete(true);
        mLayoutManager.setScrollEnabled(false);
    }

    @Override
    public void showTip(String message) {
        super.showTip(message);
        if (!Utils.isNetworkConnected()) {
            llyt_top_search.setBackgroundResource(R.color.main_bg);
            presenter.setEmptyView(mInflater, this, rclv_search, flag_value);
        }
        swipeRefreshLayout.setRefreshing(false);
        rclv_search.loadComplete(true);
    }
}
