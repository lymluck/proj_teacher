package study.smart.transfer_management.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import study.smart.baselib.entity.MyTalkRecordInfo;
import study.smart.baselib.ui.adapter.TreeRecyclerAdapter;
import study.smart.baselib.ui.adapter.base.ItemFactory;
import study.smart.baselib.ui.adapter.base.TreeRecyclerViewType;
import study.smart.baselib.ui.adapter.wrapper.EmptyWrapper;
import study.smart.baselib.ui.adapter.wrapper.TreeHeaderAndFootWapper;
import study.smart.baselib.ui.base.BaseActivity;
import study.smart.baselib.ui.widget.treeview.TreeItem;
import study.smart.transfer_management.R;
import study.smart.transfer_management.mvp.contract.MyTalkRecordContract;
import study.smart.transfer_management.mvp.presenter.MyTalkRecordListPresenter;
import study.smart.transfer_management.ui.adapter.TalkRecordTreeItemParent;

/**
 * @author yqy
 * @date on 2018/7/24
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyTalkRecordActivity extends BaseActivity<MyTalkRecordContract.Presenter> implements MyTalkRecordContract.View {
    private RecyclerView recyclerView;
    LinearLayoutManager mLayoutManager;
    private TreeRecyclerAdapter talkAdapter;
    private View emptyView;
    private ArrayList<TreeItem> talkTreeItems;
    private EmptyWrapper<TreeItem> emptyWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_talk_record);
    }

    @Override
    public void getMyUnTalkRecordSuccess(List<MyTalkRecordInfo> myTalkRecordInfo) {
        if (presenter != null) {
            presenter.setEmptyView(emptyView);
            if (myTalkRecordInfo != null && myTalkRecordInfo.size() > 0) {
                talkTreeItems.clear();
                talkTreeItems.addAll(ItemFactory.createTreeItemList(handleData(myTalkRecordInfo), TalkRecordTreeItemParent.class, null));
                talkAdapter.setDatas(talkTreeItems);
                recyclerView.setAdapter(talkAdapter);
            }
        }
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        emptyWrapper.notifyDataSetChanged();
    }

    @Override
    public MyTalkRecordContract.Presenter initPresenter() {
        return new MyTalkRecordListPresenter(this);
    }

    @Override
    public void initView() {
        recyclerView = findViewById(R.id.rv_talk);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        initAdapter();
        emptyView = mInflater.inflate(R.layout.layout_empty, recyclerView, false);
        presenter.showLoading(this, emptyView);
        presenter.getMyUnTalkRecordList();

    }

    private void initAdapter() {
        talkAdapter = new TreeRecyclerAdapter();
        talkAdapter.setType(TreeRecyclerViewType.SHOW_ALL);
        emptyWrapper = new EmptyWrapper<>(talkAdapter);
        talkTreeItems = new ArrayList<>();
        recyclerView.setAdapter(emptyWrapper);
    }

    private List<MyTalkRecordInfo.ShowMonth> handleData(List<MyTalkRecordInfo> myTalkRecordInfos) {
        List<MyTalkRecordInfo.ShowMonth> showMonths = new ArrayList<>();
        for (MyTalkRecordInfo myTalkRecordInfo : myTalkRecordInfos) {
            if (myTalkRecordInfo.getMonths() != null && myTalkRecordInfo.getMonths().size() > 0) {
                for (MyTalkRecordInfo.ShowMonth showMonth : myTalkRecordInfo.getMonths()) {
                    showMonth.setYear(myTalkRecordInfo.getYear());
                    showMonths.add(showMonth);
                }
            }
        }
        return showMonths;
    }
}
