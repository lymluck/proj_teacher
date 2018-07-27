package study.smart.transfer_management.ui.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import study.smart.baselib.BaseApplication;
import study.smart.baselib.entity.MyTalkRecordInfo;
import study.smart.baselib.ui.adapter.base.ViewHolder;
import study.smart.baselib.ui.widget.treeview.TreeItem;
import study.smart.baselib.utils.TimeUtil;
import study.smart.transfer_management.R;
import study.smart.transfer_management.ui.activity.PersonTalkRecordDetailActivity;

/**
 * @author yqy
 * @date on 2018/7/24
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TalkRecordTwoTreeItem extends TreeItem<MyTalkRecordInfo.ShowMonth.DataListInfo> {

    @Override
    protected int initLayoutId() {
        return R.layout.item_talk_record_two;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder) {
        viewHolder.setText(R.id.tv_name, data.getUserName());
        viewHolder.setText(R.id.tv_content, data.getName());
        viewHolder.setText(R.id.tv_time, TimeUtil.getStrTime(data.getTime()));
        Log.w("kim","---->"+ TimeUtil.getStrTime(data.getTime()));
        viewHolder.getView(R.id.ll_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseApplication.appContext.startActivity(new Intent(BaseApplication.appContext, PersonTalkRecordDetailActivity.class).putExtra("talk_detail", data));
            }
        });
        if (getItemManager() != null && getItemManager().getItemPosition(this)
            == getItemManager().getItemPosition(getParentItem()) + getParentItem().getChildCount()) {
            viewHolder.getView(R.id.v_line).setVisibility(View.GONE);
        } else {
            viewHolder.getView(R.id.v_line).setVisibility(View.VISIBLE);
        }
    }
}

