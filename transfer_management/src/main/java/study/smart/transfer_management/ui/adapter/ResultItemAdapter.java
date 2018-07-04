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
public class ResultItemAdapter extends CommonAdapter<TransferDetailentity.Results> {

    public ResultItemAdapter(Context context, int layoutId, List<TransferDetailentity.Results> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, TransferDetailentity.Results results, int position) {
        holder.setText(R.id.tv_all_result, results.getScore());
        holder.setText(R.id.tv_date, TimeUtil.getStrTime(results.getExamTime()));
        holder.setText(R.id.tv_notes, results.getNotes());
        holder.setText(R.id.tv_type, results.getExamType());
        holder.setText(R.id.tv_number, position + 1 + "");
    }
}
