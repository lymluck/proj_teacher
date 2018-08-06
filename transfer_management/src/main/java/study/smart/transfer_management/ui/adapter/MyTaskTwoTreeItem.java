package study.smart.transfer_management.ui.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;

import study.smart.baselib.BaseApplication;
import study.smart.baselib.entity.TaskDetailInfo;
import study.smart.baselib.ui.adapter.base.ViewHolder;
import study.smart.baselib.ui.widget.treeview.TreeItem;
import study.smart.baselib.utils.TimeUtil;
import study.smart.transfer_management.R;
import study.smart.baselib.entity.MyTaskInfo;
import study.smart.transfer_management.ui.activity.TaskDetailActivity;

/**
 * @author yqy
 * @date on 2018/7/25
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyTaskTwoTreeItem extends TreeItem<TaskDetailInfo> {

    @Override
    protected int initLayoutId() {
        return R.layout.item_task_two;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder) {
        if (TextUtils.isEmpty(data.getName())) {
            viewHolder.setText(R.id.tv_content, data.getTypeText());
        } else {
            viewHolder.setText(R.id.tv_content, data.getTypeText() + "：" + data.getName());
        }
        viewHolder.setText(R.id.tv_center_name, data.getCenterName() + "-" + data.getUserName());
        viewHolder.setText(R.id.tv_time, TimeUtil.getStrTime(data.getEndTime(), "MM-dd") + "截止");
        if (getItemManager() != null && getItemManager().getItemPosition(this)
            == getItemManager().getItemPosition(getParentItem()) + getParentItem().getChildCount()) {
            viewHolder.getView(R.id.v_line).setVisibility(View.GONE);
        } else {
            viewHolder.getView(R.id.v_line).setVisibility(View.VISIBLE);
        }

        if ("ALERT".equals(data.getStatus())) {
            //临期
            viewHolder.getView(R.id.v_status).setBackgroundColor(Color.parseColor("#FAAD14"));
        } else if ("EXPIRED".equals(data.getStatus())) {
            //过期
            viewHolder.getView(R.id.v_status).setBackgroundColor(Color.parseColor("#7f000000"));
        } else if ("PENDING".equals(data.getStatus())) {
            //进行中
            viewHolder.getView(R.id.v_status).setBackgroundColor(Color.parseColor("#1890FF"));

        } else {
            //已完成
            viewHolder.getView(R.id.v_status).setBackgroundColor(Color.parseColor("#52C41A"));
        }

        viewHolder.getView(R.id.ll_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseApplication.appContext.startActivity(new Intent(BaseApplication.appContext, TaskDetailActivity.class)
                    .putExtra("taskDetail", data));
            }
        });
    }
}

