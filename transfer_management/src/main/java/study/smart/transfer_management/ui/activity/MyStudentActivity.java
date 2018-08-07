package study.smart.transfer_management.ui.activity;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.smartstudy.medialib.ijkplayer.WeakHandler;

import java.util.ArrayList;
import java.util.List;

import study.smart.baselib.listener.HidingScrollListener;
import study.smart.baselib.ui.adapter.CommonAdapter;
import study.smart.baselib.ui.adapter.MultiItemTypeAdapter;
import study.smart.baselib.ui.adapter.base.ViewHolder;
import study.smart.baselib.ui.adapter.wrapper.EmptyWrapper;
import study.smart.baselib.ui.adapter.wrapper.LoadMoreWrapper;
import study.smart.baselib.ui.base.BaseActivity;
import study.smart.baselib.ui.widget.HorizontalDividerItemDecoration;
import study.smart.baselib.ui.widget.LoadMoreRecyclerView;
import study.smart.baselib.ui.widget.NoScrollLinearLayoutManager;
import study.smart.baselib.utils.DensityUtils;
import study.smart.baselib.utils.ParameterUtils;
import study.smart.baselib.utils.ScreenUtils;
import study.smart.transfer_management.R;
import study.smart.baselib.entity.MyStudentInfo;
import study.smart.transfer_management.mvp.contract.TransferMyStudentContract;
import study.smart.transfer_management.mvp.presenter.TransferMyStudentPresenter;

/**
 * @author yqy
 * @date on 2018/7/17
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyStudentActivity extends BaseActivity<TransferMyStudentContract.Presenter> implements TransferMyStudentContract.View {
    private LoadMoreRecyclerView lmrvStudent;
    private CommonAdapter<MyStudentInfo> mAdapter;
    private LoadMoreWrapper<MyStudentInfo> loadMoreWrapper;
    private EmptyWrapper<MyStudentInfo> emptyWrapper;
    private View searchView;
    private NoScrollLinearLayoutManager mLayoutManager;
    private View emptyView;
    private List<MyStudentInfo> myStudentInfos;
    private WeakHandler mHandler;
    private int mPage = 1;
    private boolean canPullUp;
    private String from;
    private String centerId;
    private boolean isFirstLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_student);
    }

    @Override
    public TransferMyStudentContract.Presenter initPresenter() {
        return new TransferMyStudentPresenter(this);
    }

    @Override
    public void initView() {
        from = getIntent().getStringExtra("from");
        if ("compelete_student".equals(from)) {
            setTitle("待完善信息学员");
        } else {
            setTitle("我的学员");
        }
        setTopLineVisibility(View.VISIBLE);
        centerId = getIntent().getStringExtra("centerId");
        isFirstLoad = true;
        searchView = findViewById(R.id.searchView);
        lmrvStudent = findViewById(R.id.rclv_student);
        lmrvStudent.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lmrvStudent.setLayoutManager(mLayoutManager);
        lmrvStudent.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
            .size(DensityUtils.dip2px(0.5f)).colorResId(R.color.horizontal_line_color).build());
        initAdapter();
        emptyView = mInflater.inflate(R.layout.layout_empty, lmrvStudent, false);
        presenter.showLoading(this, emptyView);
        getStudentList(centerId);
    }

    private void initAdapter() {
        myStudentInfos = new ArrayList<>();
        mAdapter = new CommonAdapter<MyStudentInfo>(this, R.layout.item_my_student, myStudentInfos) {
            @Override
            protected void convert(ViewHolder holder, MyStudentInfo myStudentInfo, int position) {
                holder.setText(R.id.tv_name, myStudentInfo.getName());
                holder.setText(R.id.tv_target_year_season, myStudentInfo.getTargetApplicationYearSeason() + "/" + myStudentInfo.getTargetDegreeName());

            }
        };
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        lmrvStudent.setAdapter(loadMoreWrapper);

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if ("TASK_TRANSFER_MANAGER".equals(from)) {
                    startActivity(new Intent(MyStudentActivity.this, MyTaskListActivity.class)
                        .putExtra("from", "student")
                        .putExtra("id", myStudentInfos.get(position).getUserId())
                        .putExtra("name", myStudentInfos.get(position).getName()));
                } else if ("REPORT_TRANSFER_MANAGER".equals(from)) {
                    startActivity(new Intent(MyStudentActivity.this, StudentDetailReportActivity.class)
                        .putExtra("id", myStudentInfos.get(position).getUserId())
                        .putExtra("name", myStudentInfos.get(position).getName()));
                } else if ("STUDENT_TRANSFER_MANAGER".equals(from) || "compelete_student".equals(from)) {
                    startActivity(new Intent(MyStudentActivity.this, StudentDetailActivity.class)
                        .putExtra("studentInfo", myStudentInfos.get(position))
                        .putExtra("from", from));
                } else {
                    startActivity(new Intent(MyStudentActivity.this, StudentDetailTalkActivity.class)
                        .putExtra("id", myStudentInfos.get(position).getUserId())
                        .putExtra("name", myStudentInfos.get(position).getName()));
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder,
                                           int position) {
                return false;
            }
        });
    }


    @Override
    public void initEvent() {
        super.initEvent();
        mHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case ParameterUtils.MSG_WHAT_REFRESH:
                        lmrvStudent.scrollBy(0, searchView.getHeight());
                        // 将searchView隐藏掉
                        searchView.animate()
                            .translationY(-searchView.getHeight())
                            .setDuration(600)
                            .setInterpolator(new AccelerateInterpolator(2))
                            .start();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        lmrvStudent.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                searchView.animate()
                    .translationY(-ScreenUtils.getScreenHeight())
                    .setDuration(800)
                    .setInterpolator(new AccelerateInterpolator(2))
                    .start();
                searchView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onShow() {
                searchView.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2))
                    .setDuration(800).start();
                searchView.setVisibility(View.VISIBLE);
            }
        });

        lmrvStudent.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void OnLoad() {
                if (canPullUp) {
                    mPage = mPage + 1;
                    if ("compelete_student".equals(from)) {
                        presenter.getCompeleteStudent(mPage + "", ParameterUtils.PULL_UP);
                    } else {
                        presenter.getMyStudent(mPage + "", centerId, ParameterUtils.PULL_UP);
                    }
                    canPullUp = false;
                }
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.sendEmptyMessageAtTime(ParameterUtils.MSG_WHAT_REFRESH, 600);
                Intent toSearch = new Intent(MyStudentActivity.this, CommonSearchActivity.class);
                toSearch.putExtra(ParameterUtils.TRANSITION_FLAG, from);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(toSearch, ActivityOptionsCompat.makeSceneTransitionAnimation(MyStudentActivity.this,
                        searchView, "btn_tr").toBundle());
                } else {
                    startActivity(toSearch);
                }
            }
        });
    }

    @Override
    public void getTransferStudentSuccess(List<MyStudentInfo> myStudentInfos,
                                          int request_state) {
        if (presenter != null) {
            // 右上角按钮逻辑
            presenter.setEmptyView(emptyView);
            mLayoutManager.setScrollEnabled(true);
            int len = myStudentInfos.size();
            if (request_state == ParameterUtils.PULL_DOWN) {
                //下拉刷新
                if (len <= 0) {
                    lmrvStudent.loadComplete(true);
                    mLayoutManager.setScrollEnabled(false);
                }
                this.myStudentInfos.clear();
                this.myStudentInfos.addAll(myStudentInfos);
                loadMoreWrapper.notifyDataSetChanged();
                if (isFirstLoad) {
                    isFirstLoad = false;
                    lmrvStudent.scrollBy(0, searchView.getHeight());
                }
                //判断是否可滑动， -1 表示 向上， 1 表示向下
                if (!lmrvStudent.canScrollVertically(-1)) {
                    searchView.setVisibility(View.VISIBLE);
                }
            } else if (request_state == ParameterUtils.PULL_UP) {
                //上拉加载
                if (len <= 0) {
                    //没有更多内容
                    if (mPage > 1) {
                        mPage = mPage - 1;
                    }
                    lmrvStudent.loadComplete(false);
                } else {
                    lmrvStudent.loadComplete(true);
                    this.myStudentInfos.addAll(myStudentInfos);
                    loadMoreWrapper.notifyDataSetChanged();
                }
            }
            canPullUp = true;
        }
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        loadMoreWrapper.notifyDataSetChanged();
        lmrvStudent.loadComplete(true);
        mLayoutManager.setScrollEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isFirstLoad = false;
    }

    private void getStudentList(String centerId) {
        if ("compelete_student".equals(from)) {
            presenter.getCompeleteStudent(mPage + "", ParameterUtils.PULL_DOWN);
        } else {
            presenter.getMyStudent(mPage + "", centerId, ParameterUtils.PULL_DOWN);
        }
    }
}
