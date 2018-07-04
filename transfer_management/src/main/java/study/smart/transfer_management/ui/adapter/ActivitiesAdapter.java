package study.smart.transfer_management.ui.adapter;

import android.content.Context;

import java.util.List;

import study.smart.baselib.ui.adapter.CommonAdapter;
import study.smart.baselib.ui.adapter.base.ViewHolder;
import study.smart.baselib.utils.TimeUtil;
import study.smart.transfer_management.R;
import study.smart.transfer_management.entity.TransferDetailentity;

/**
 * @author yqy
 * @date on 2018/6/29
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class ActivitiesAdapter extends CommonAdapter<TransferDetailentity.Activities> {
    public ActivitiesAdapter(Context context, int layoutId, List<TransferDetailentity.Activities> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, TransferDetailentity.Activities activities, int position) {
        holder.setText(R.id.tv_type, activities.getName());
        holder.setText(R.id.tv_number, position + 1 + "");
        holder.setText(R.id.tv_time, TimeUtil.getStrTime(activities.getStartTime()) + " ~ " + TimeUtil.getStrTime(activities.getEndTime()));
        holder.setText(R.id.tv_duty, activities.getRole());
        holder.setText(R.id.tv_content, activities.getContent());
        holder.setText(R.id.tv_result, activities.getContribution());
    }
}
