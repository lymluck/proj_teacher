package study.smart.transfer_management.ui.adapter;

import android.widget.ImageView;

import java.util.List;

import study.smart.baselib.entity.MyTalkRecordInfo;
import study.smart.baselib.ui.adapter.base.ItemFactory;
import study.smart.baselib.ui.adapter.base.ViewHolder;
import study.smart.baselib.ui.widget.treeview.TreeItem;
import study.smart.baselib.ui.widget.treeview.TreeItemGroup;
import study.smart.transfer_management.R;

/**
 * @author yqy
 * @date on 2018/7/24
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TalkRecordTreeItemParent extends TreeItemGroup<MyTalkRecordInfo.ShowMonth> {

    @Override
    public List<? extends TreeItem> initChildsList(MyTalkRecordInfo.ShowMonth showMonth) {
        return ItemFactory.createTreeItemList(showMonth.getList(), TalkRecordTwoTreeItem.class, this);
    }

    @Override
    public int initLayoutId() {
        return R.layout.item_talk_record_one;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder) {
        if ("其他".equals(data.getYear())) {
            holder.setText(R.id.tv_month, data.getYear());
        } else {
            holder.setText(R.id.tv_month, data.getYear() + "年" + data.getKey());
        }
    }

    @Override
    public void onExpand(boolean isCollapseOthers) {
        isCollapseOthers = true;
        super.onExpand(isCollapseOthers);
    }
}

