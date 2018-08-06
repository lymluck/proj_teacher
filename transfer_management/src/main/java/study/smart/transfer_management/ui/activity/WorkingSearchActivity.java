package study.smart.transfer_management.ui.activity;

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
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.medialib.ijkplayer.WeakHandler;

import java.util.ArrayList;
import java.util.List;

import study.smart.baselib.ui.adapter.wrapper.EmptyWrapper;
import study.smart.baselib.ui.base.BaseActivity;
import study.smart.baselib.ui.widget.EditTextWithClear;
import study.smart.baselib.ui.widget.NoScrollLinearLayoutManager;
import study.smart.baselib.utils.KeyBoardUtils;
import study.smart.baselib.utils.ParameterUtils;
import study.smart.baselib.utils.Utils;
import study.smart.transfer_management.R;
import study.smart.transfer_management.entity.WorkingSearchListInfo;
import study.smart.transfer_management.mvp.contract.WorkingSearchContract;
import study.smart.transfer_management.mvp.presenter.WorkingSearchPresenter;
import study.smart.transfer_management.ui.adapter.WorkingSearchAdapter;

/**
 * @author yqy
 * @date on 2018/7/27
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class WorkingSearchActivity extends BaseActivity<WorkingSearchContract.Presenter> implements WorkingSearchContract.View {
    private EditTextWithClear searchView;
    private RecyclerView rvSearch;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NoScrollLinearLayoutManager mLayoutManager;
    private LinearLayout llSearch;
    private WorkingSearchAdapter mAdapter;
    private EmptyWrapper emptyWrapper;
    private String keyword;
    private List<WorkingSearchListInfo> resultList;
    //时间间隔
    private static int spaceTime = 300;
    //上次搜索的时间
    private static long lastSearchTime = 0;
    private WeakHandler mHandler;
    private String from;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_working_search);
        KeyBoardUtils.openKeybord(searchView.getMyEditText(), this);
    }

    @Override
    public WorkingSearchContract.Presenter initPresenter() {
        return new WorkingSearchPresenter(this);
    }

    @Override
    public void initView() {
        setHeadVisible(View.GONE);
        from = getIntent().getStringExtra(ParameterUtils.TRANSITION_FLAG);
        Log.w("kim", "--->" + from);
        llSearch = (LinearLayout) findViewById(R.id.llyt_search);
        searchView = (EditTextWithClear) findViewById(R.id.searchView);
        searchView.getMyEditText().setHint(R.string.search);
        searchView.getMyEditText().requestFocus();
        searchView.setInputType(InputType.TYPE_CLASS_TEXT);
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srlt_search);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        rvSearch = (RecyclerView) findViewById(R.id.rclv_search);
        rvSearch.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvSearch.setLayoutManager(mLayoutManager);
        mHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        llSearch.setBackgroundResource(0);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        //初始化适配器
        initAdapter();
    }


    private void initAdapter() {
        resultList = new ArrayList<>();
        mAdapter = new WorkingSearchAdapter(this, resultList, from);
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        rvSearch.setAdapter(emptyWrapper);
    }

    @Override
    public void initEvent() {
        initRefresh();
        initEditor();
        initTextWatcher();
        findViewById(R.id.tv_cancle).setOnClickListener(this);
    }

    private void initRefresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //这里做刷新
                presenter.getResults(from, keyword);
            }
        });
    }

    private void initEditor() {
        searchView.getMyEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    KeyBoardUtils.closeKeybord(searchView.getMyEditText(), WorkingSearchActivity.this);
                    //这里做搜索
                    presenter.getResults(from, keyword);
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
                keyword = s.toString();
                if (s.length() > 0) {
                    //开始搜索
                    //延迟搜索，防止用户在输入过程中频繁调用搜索接口
                    //当前系统时间
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastSearchTime > spaceTime) {
                        presenter.getResults(from, keyword);
                    }
                    lastSearchTime = currentTime;
                } else {
                    //这里做清空操作
                    clearList();
                    mAdapter.notifyDataSetChanged();
                    Utils.convertActivityToTranslucent(WorkingSearchActivity.this);
                    showEmptyView(null);
                    mHandler.sendEmptyMessageDelayed(1, 250);
                }
            }
        });
    }

    private void clearList() {
        if (resultList != null) {
            resultList.clear();
        }
    }

    @Override
    public void finish() {
        KeyBoardUtils.closeKeybord(searchView.getMyEditText(), this);
        super.finish();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_cancle) {
            finish();
            overridePendingTransition(R.anim.image_fade_in, R.anim.image_fade_out);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.image_fade_in, R.anim.image_fade_out);
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        emptyWrapper.notifyDataSetChanged();
        mLayoutManager.setScrollEnabled(false);
    }

    @Override
    public void showResult(List<WorkingSearchListInfo> workingSearchListInfos) {
        if (presenter != null) {
            swipeRefreshLayout.setRefreshing(false);
            llSearch.setBackgroundResource(R.color.main_bg);
            presenter.setEmptyView(mInflater, this, rvSearch);
            mLayoutManager.setScrollEnabled(true);
            clearList();
            resultList.addAll(workingSearchListInfos);
            mAdapter.notifyDataSetChanged();
        }
    }

}
