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
public class WorkingSearchContentHolder extends RecyclerView.ViewHolder {
    public TextView tvName;
    public TextView tvTargetYearSeason;
    public View itemStudent;
    public View itemTask;
    public TextView tvContent;
    public TextView tvcenterName;
    public TextView taskTime;
    public View vBackgroup;
    public TextView reportName;
    public TextView reportCenterName;
    public TextView tvPublishTime;
    public View itemReport;
    public View itemTalk;
    public TextView talkName;
    public TextView tvTalkContent;
    public TextView tvTalkCenterName;
    public TextView tvTalkTime;

    public WorkingSearchContentHolder(View itemView) {
        super(itemView);
        initView();
    }

    private void initView() {
        itemReport = itemView.findViewById(R.id.item_report);
        tvName = itemView.findViewById(R.id.tv_name);
        tvTargetYearSeason = itemView.findViewById(R.id.tv_target_year_season);
        itemStudent = itemView.findViewById(R.id.item_student);
        itemTask = itemView.findViewById(R.id.item_task);
        tvContent = itemView.findViewById(R.id.tv_content);
        tvcenterName = itemView.findViewById(R.id.tv_center_name);
        taskTime = itemView.findViewById(R.id.tv_time);
        vBackgroup = itemView.findViewById(R.id.v_status);
        reportName = itemView.findViewById(R.id.tv_report_name);
        reportCenterName = itemView.findViewById(R.id.tv_report_center_name);
        tvPublishTime = itemView.findViewById(R.id.tv_publish_time);
        itemTalk = itemView.findViewById(R.id.item_talk);
        talkName = itemView.findViewById(R.id.tv_talk_name);
        tvTalkContent = itemView.findViewById(R.id.tv_talk_content);
        tvTalkCenterName = itemView.findViewById(R.id.tv_talk_center_name);
        tvTalkTime = itemView.findViewById(R.id.tv_talk_time);
    }
}

