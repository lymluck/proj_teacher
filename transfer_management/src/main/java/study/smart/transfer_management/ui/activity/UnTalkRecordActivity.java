package study.smart.transfer_management.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import study.smart.baselib.entity.MessageDetailItemInfo;
import study.smart.baselib.ui.adapter.CommonAdapter;
import study.smart.baselib.ui.adapter.MultiItemTypeAdapter;
import study.smart.baselib.ui.adapter.base.ViewHolder;
import study.smart.baselib.ui.adapter.wrapper.EmptyWrapper;
import study.smart.baselib.ui.adapter.wrapper.LoadMoreWrapper;
import study.smart.baselib.ui.base.BaseActivity;
import study.smart.baselib.ui.widget.LoadMoreRecyclerView;
import study.smart.baselib.ui.widget.NoScrollLinearLayoutManager;
import study.smart.baselib.utils.ParameterUtils;
import study.smart.baselib.utils.ToastUtils;
import study.smart.transfer_management.R;
import study.smart.transfer_management.entity.MyStudentInfo;
import study.smart.transfer_management.mvp.contract.UnTalkRecordContract;
import study.smart.transfer_management.mvp.presenter.UnTalkRecordListPresenter;

/**
 * @author yqy
 * @date on 2018/7/24
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class UnTalkRecordActivity extends BaseActivity<UnTalkRecordContract.Presenter> implements UnTalkRecordContract.View {
    private LoadMoreRecyclerView rclvTalkDetail;
    private EmptyWrapper<MyStudentInfo> emptyWrapper;
    private NoScrollLinearLayoutManager mLayoutManager;
    private View emptyView;
    private SwipeRefreshLayout srltTalkDetail;
    private int mPage = 1;
    private CommonAdapter<MyStudentInfo> mAdapter;
    private LoadMoreWrapper<MyStudentInfo> loadMoreWrapper;
    private List<MyStudentInfo> myStudentInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_talk_record);
    }

    @Override
    public UnTalkRecordContract.Presenter initPresenter() {
        return new UnTalkRecordListPresenter(this);
    }

    @Override
    public void initView() {
        setTitle("待沟通记录");
        setTopLineVisibility(View.VISIBLE);
        srltTalkDetail = rootView.findViewById(R.id.srlt_talk_detail);
        srltTalkDetail.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        srltTalkDetail.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                presenter.getUnTalkList(mPage + "", ParameterUtils.PULL_DOWN);
            }
        });
        rclvTalkDetail = rootView.findViewById(R.id.rclv_talk_detail);
        rclvTalkDetail.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclvTalkDetail.setLayoutManager(mLayoutManager);
        rclvTalkDetail.setItemAnimator(new DefaultItemAnimator());
        initAdapter();
        emptyView = getLayoutInflater().inflate(R.layout.layout_empty, rclvTalkDetail,
            false);
        presenter.showLoading(this, emptyView);
        presenter.getUnTalkList(mPage + "", ParameterUtils.PULL_DOWN);

    }

    @Override
    public void getUnTalkListSuccess(List<MyStudentInfo> myStudentInfos, int request_state) {
        if (presenter != null) {
            presenter.setEmptyView(emptyView);
            mLayoutManager.setScrollEnabled(true);
            int len = myStudentInfos.size();
            if (request_state == ParameterUtils.PULL_DOWN) {
                //下拉刷新
                if (len <= 0) {
                    rclvTalkDetail.loadComplete(true);
                    mLayoutManager.setScrollEnabled(false);
                }
                this.myStudentInfos.clear();
                this.myStudentInfos.addAll(myStudentInfos);
                srltTalkDetail.setRefreshing(false);
                loadMoreWrapper.notifyDataSetChanged();
            } else if (request_state == ParameterUtils.PULL_UP) {
                //上拉加载
                if (len <= 0) {
                    //没有更多内容
                    if (mPage > 1) {
                        mPage = mPage - 1;
                    }
                    if (this.myStudentInfos.size() > 0) {
                        rclvTalkDetail.loadComplete(false);
                    } else {
                        rclvTalkDetail.loadComplete(true);
                    }
                } else {
                    rclvTalkDetail.loadComplete(true);
                    this.myStudentInfos.addAll(myStudentInfos);
                    loadMoreWrapper.notifyDataSetChanged();
                }
            }
        }
    }


    private void initAdapter() {
        myStudentInfos = new ArrayList<>();
        mAdapter = new CommonAdapter<MyStudentInfo>(this, R.layout.item_un_talk_record, myStudentInfos) {
            @Override
            protected void convert(ViewHolder holder, MyStudentInfo myStudentInfo, int position) {
                holder.setText(R.id.tv_name, myStudentInfo.getName());
                String recordCount = "本月完成沟通<font color='#078CF1'> " + myStudentInfo.getCompletedCommunicationReportCount() + " </font>次";
                holder.setText(R.id.tv_record, Html.fromHtml(recordCount));
            }
        };
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        rclvTalkDetail.setAdapter(loadMoreWrapper);

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                startActivity(new Intent(UnTalkRecordActivity.this, StudentDetailActivity.class)
                    .putExtra("studentInfo", myStudentInfos.get(position))
                    .putExtra("from", "student"));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    public void showTip(String message) {
        if (presenter != null) {
            srltTalkDetail.setRefreshing(false);
            rclvTalkDetail.loadComplete(true);
            ToastUtils.shortToast(message);
        }
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        loadMoreWrapper.notifyDataSetChanged();
        rclvTalkDetail.loadComplete(true);
        mLayoutManager.setScrollEnabled(false);
        view = null;
    }
}
