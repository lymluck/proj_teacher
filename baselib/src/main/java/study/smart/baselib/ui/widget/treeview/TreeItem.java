package study.smart.baselib.ui.widget.treeview;

import android.support.annotation.Nullable;

import study.smart.baselib.ui.adapter.base.BaseItem;


/**
 * TreeRecyclerAdapter的item
 */
public abstract class TreeItem<D> extends BaseItem<D> {
    private TreeItemGroup parentItem;

    public void setParentItem(TreeItemGroup parentItem) {
        this.parentItem = parentItem;
    }

    /**
     * 获取当前item的父级
     *
     * @return
     */
    @Nullable
    public TreeItemGroup getParentItem() {
        return parentItem;
    }


}
