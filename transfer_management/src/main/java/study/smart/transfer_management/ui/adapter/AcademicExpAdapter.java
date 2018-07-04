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
public class AcademicExpAdapter extends CommonAdapter<TransferDetailentity.AcademicExp> {
    public AcademicExpAdapter(Context context, int layoutId, List<TransferDetailentity.AcademicExp> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, TransferDetailentity.AcademicExp academicExp, int position) {
        holder.setText(R.id.tv_type, academicExp.getName());
        holder.setText(R.id.tv_number, position + 1 + "");
        holder.setText(R.id.tv_time, TimeUtil.getStrTime(academicExp.getStartTime()) + " ~ " + TimeUtil.getStrTime(academicExp.getEndTime()));
        holder.setText(R.id.tv_acticle, academicExp.getArticle());
        holder.setText(R.id.tv_main, academicExp.getContent());
        holder.setText(R.id.tv_other, academicExp.getOthers());
    }
}
