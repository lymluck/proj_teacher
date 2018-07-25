package study.smart.transfer_management.ui.adapter;

import study.smart.baselib.entity.MyTalkRecordInfo;
import study.smart.baselib.ui.adapter.base.ViewHolder;
import study.smart.baselib.ui.widget.treeview.TreeItem;
import study.smart.transfer_management.R;

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
        viewHolder.setText(R.id.tv_time, data.getTimeText());

    }
}

