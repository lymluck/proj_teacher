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
public class SocialPracticeAdapter extends CommonAdapter<TransferDetailentity.SocialPractice> {
    public SocialPracticeAdapter(Context context, int layoutId, List<TransferDetailentity.SocialPractice> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, TransferDetailentity.SocialPractice socialPractice, int position) {
        holder.setText(R.id.tv_type, socialPractice.getName());
        holder.setText(R.id.tv_number, position + 1 + "");
        holder.setText(R.id.tv_activity_type, socialPractice.getType());
        holder.setText(R.id.tv_activity_grade, socialPractice.getLevel());
        holder.setText(R.id.tv_time, TimeUtil.getStrTime(socialPractice.getStartTime()) + " ~ " + TimeUtil.getStrTime(socialPractice.getEndTime()));
        holder.setText(R.id.tv_frequency, socialPractice.getFrequency());
        holder.setText(R.id.tv_role, socialPractice.getRole());
        holder.setText(R.id.tv_desc, socialPractice.getDesc());

    }
}
