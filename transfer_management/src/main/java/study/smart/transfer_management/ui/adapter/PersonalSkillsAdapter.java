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
public class PersonalSkillsAdapter extends CommonAdapter<TransferDetailentity.PersonalSkills> {
    public PersonalSkillsAdapter(Context context, int layoutId, List<TransferDetailentity.PersonalSkills> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, TransferDetailentity.PersonalSkills personalSkills, int position) {
        holder.setText(R.id.tv_type, personalSkills.getName());
        holder.setText(R.id.tv_number, position + 1 + "");
        holder.setText(R.id.tv_start_time, TimeUtil.getStrTime(personalSkills.getStartTime()));
        holder.setText(R.id.tv_end_time, TimeUtil.getStrTime(personalSkills.getEndTime()));
        holder.setText(R.id.tv_achievement, personalSkills.getContribution());
        holder.setText(R.id.tv_content, personalSkills.getContent());
        holder.setText(R.id.tv_other, personalSkills.getOthers());
    }
}
