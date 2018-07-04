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
public class EduGroudAdapter extends CommonAdapter<TransferDetailentity.EduBackground> {
    public EduGroudAdapter(Context context, int layoutId, List<TransferDetailentity.EduBackground> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, TransferDetailentity.EduBackground eduBackground, int position) {
        holder.setText(R.id.tv_type, eduBackground.getStages());
        holder.setText(R.id.tv_number, position + 1 + "");
        holder.setText(R.id.tv_school_name, eduBackground.getSchoolName());
        holder.setText(R.id.tv_time, TimeUtil.getStrTime(eduBackground.getStartTime())+" ~ "+TimeUtil.getStrTime(eduBackground.getEndTime()));
        holder.setText(R.id.tv_subject, eduBackground.getSubject());
        holder.setText(R.id.tv_major, eduBackground.getMajor());
        holder.setText(R.id.tv_bachelor_science, eduBackground.getDegree());
        holder.setText(R.id.tv_score, eduBackground.getGpa());

    }
}
