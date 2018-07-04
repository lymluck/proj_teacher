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
public class WorkExpAdapter extends CommonAdapter<TransferDetailentity.WorkExp> {
    public WorkExpAdapter(Context context, int layoutId, List<TransferDetailentity.WorkExp> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, TransferDetailentity.WorkExp workExp, int position) {
        holder.setText(R.id.tv_type, workExp.getName());
        holder.setText(R.id.tv_number, position + 1 + "");
        holder.setText(R.id.tv_time, TimeUtil.getStrTime(workExp.getStartTime()) + " ~ " + TimeUtil.getStrTime(workExp.getEndTime()));
        holder.setText(R.id.tv_duty, workExp.getRole());
        holder.setText(R.id.tv_content, workExp.getContent());
        holder.setText(R.id.tv_result, workExp.getContribution());
    }
}
