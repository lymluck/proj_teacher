package study.smart.transfer_management.ui.adapter.workingsearch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import study.smart.transfer_management.R;

/**
 * @author yqy
 * @date on 2018/7/30
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class WorkingSearchHeaderHolder extends RecyclerView.ViewHolder {
    public TextView titleView;

    public WorkingSearchHeaderHolder(View itemView) {
        super(itemView);
        initView();
    }

    private void initView() {
        titleView= itemView.findViewById(R.id.tv_title);
    }
}

