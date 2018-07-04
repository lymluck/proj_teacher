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
public class AwarderExpAdapter extends CommonAdapter<TransferDetailentity.AwarderExp> {
    public AwarderExpAdapter(Context context, int layoutId, List<TransferDetailentity.AwarderExp> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, TransferDetailentity.AwarderExp awarderExp, int position) {
        holder.setText(R.id.tv_type, awarderExp.getName());
        holder.setText(R.id.tv_number, position + 1 + "");
        holder.setText(R.id.tv_time, TimeUtil.getStrTime(awarderExp.getTime()));
        holder.setText(R.id.tv_company, awarderExp.getOrganization());
        holder.setText(R.id.tv_grade, awarderExp.getLevel());
        holder.setText(R.id.tv_content, awarderExp.getContribution());
        holder.setText(R.id.tv_other, awarderExp.getOthers());
    }
}
