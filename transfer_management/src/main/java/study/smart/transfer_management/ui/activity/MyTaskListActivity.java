package study.smart.transfer_management.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import study.smart.baselib.ui.adapter.TreeRecyclerAdapter;
import study.smart.baselib.ui.adapter.base.ItemFactory;
import study.smart.baselib.ui.adapter.base.TreeRecyclerViewType;
import study.smart.baselib.ui.adapter.wrapper.EmptyWrapper;
import study.smart.baselib.ui.base.BaseActivity;
import study.smart.baselib.ui.widget.treeview.TreeItem;
import study.smart.transfer_management.R;
import study.smart.baselib.entity.MyTaskInfo;
import study.smart.transfer_management.mvp.contract.MyTaskListContract;
import study.smart.transfer_management.mvp.presenter.MyTaskListPresenter;
import study.smart.transfer_management.ui.adapter.MyTaskTreeItemParent;

/**
 * @author yqy
 * @date on 2018/7/25
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyTaskListActivity extends BaseActivity<MyTaskListContract.Presenter> implements MyTaskListContract.View {
    private RecyclerView recyclerView;
    LinearLayoutManager mLayoutManager;
    private TreeRecyclerAdapter taskAdapter;
    private View emptyView;
    private ArrayList<TreeItem> taskTreeItems;
    private EmptyWrapper<TreeItem> emptyWrapper;
    private String from;
    private String id;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_task_list);
    }

    @Override
    public MyTaskListContract.Presenter initPresenter() {
        return new MyTaskListPresenter(this);
    }

    @Override
    public void getMyTaskListSuccess(List<MyTaskInfo> myTaskInfoList) {
        if (presenter != null) {
            presenter.setEmptyView(emptyView);
            if (myTaskInfoList != null && myTaskInfoList.size() > 0) {
                taskTreeItems.clear();
                taskTreeItems.addAll(ItemFactory.createTreeItemList(handleData(myTaskInfoList), MyTaskTreeItemParent.class, null));
                taskAdapter.setDatas(taskTreeItems);
                recyclerView.setAdapter(taskAdapter);
            }
        }
    }


    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        emptyWrapper.notifyDataSetChanged();
    }


    @Override
    public void initView() {
        from = getIntent().getStringExtra("from");
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        if ("working".equals(from)) {
            setTitle(R.string.my_arrange_task);
        } else {
            setTitle(name + "的任务");
        }
        setTitleLineVisible(View.VISIBLE);
        recyclerView = findViewById(R.id.rv_task);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        initAdapter();
        emptyView = mInflater.inflate(R.layout.layout_empty, recyclerView, false);
        presenter.showLoading(this, emptyView);
        presenter.getMyTaskList(from, id);
    }

    private void initAdapter() {
        taskAdapter = new TreeRecyclerAdapter();
        taskAdapter.setType(TreeRecyclerViewType.SHOW_ALL);
        emptyWrapper = new EmptyWrapper<>(taskAdapter);
        taskTreeItems = new ArrayList<>();
        recyclerView.setAdapter(emptyWrapper);
    }

    private List<MyTaskInfo.DataMonths> handleData(List<MyTaskInfo> myTaskInfos) {
        List<MyTaskInfo.DataMonths> showMonths = new ArrayList<>();
        for (MyTaskInfo myTaskInfo : myTaskInfos) {
            if (myTaskInfo.getMonths() != null && myTaskInfo.getMonths().size() > 0) {
                for (MyTaskInfo.DataMonths showMonth : myTaskInfo.getMonths()) {
                    showMonth.setYear(myTaskInfo.getYear());
                    showMonths.add(showMonth);
                }
            }
        }
        return showMonths;
    }
}
