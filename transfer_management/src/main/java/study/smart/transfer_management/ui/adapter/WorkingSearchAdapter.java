package study.smart.transfer_management.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import study.smart.baselib.ui.adapter.base.SectionedRecyclerViewAdapter;
import study.smart.baselib.utils.ParameterUtils;
import study.smart.baselib.utils.TimeUtil;
import study.smart.transfer_management.R;
import study.smart.baselib.entity.WorkingSearchInfo;
import study.smart.transfer_management.entity.WorkingSearchListInfo;
import study.smart.transfer_management.ui.activity.CommonSearchActivity;
import study.smart.transfer_management.ui.activity.MyTaskListActivity;
import study.smart.transfer_management.ui.activity.PersonTalkRecordDetailActivity;
import study.smart.transfer_management.ui.activity.StudentDetailActivity;
import study.smart.transfer_management.ui.activity.StudentDetailReportActivity;
import study.smart.transfer_management.ui.activity.StudentDetailTalkActivity;
import study.smart.transfer_management.ui.activity.TaskDetailActivity;
import study.smart.transfer_management.ui.adapter.workingsearch.WorkingSearchContentHolder;
import study.smart.transfer_management.ui.adapter.workingsearch.WorkingSearchFooterHolder;
import study.smart.transfer_management.ui.adapter.workingsearch.WorkingSearchHeaderHolder;

/**
 * @author yqy
 * @date on 2018/7/30
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class WorkingSearchAdapter extends SectionedRecyclerViewAdapter<WorkingSearchHeaderHolder, WorkingSearchContentHolder, WorkingSearchFooterHolder> {
    public List<WorkingSearchListInfo> mDatas;
    private Activity mContext;
    private LayoutInflater mInflater;
    private String from;

    public WorkingSearchAdapter(Activity context, List<WorkingSearchListInfo> listInfos, String from) {
        mContext = context;
        this.mDatas = listInfos;
        this.from = from;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    protected int getSectionCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    @Override
    protected int getItemCountForSection(int section) {
        List<WorkingSearchInfo> datas = mDatas.get(section).getWorkingSearchInfos();
        return datas != null ? datas.size() : 0;
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        WorkingSearchListInfo info = mDatas.get(section);
        int total = Integer.parseInt(info.getTypeTotal(), 10);
        if (total > 5) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected WorkingSearchHeaderHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        return new WorkingSearchHeaderHolder(mInflater.inflate(R.layout.item_working_search_title, parent, false));
    }

    @Override
    protected WorkingSearchFooterHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return new WorkingSearchFooterHolder(mInflater.inflate(R.layout.item_working_search_footer, parent, false));
    }

    @Override
    protected WorkingSearchContentHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new WorkingSearchContentHolder(mInflater.inflate(R.layout.item_working_search_list, parent, false));
    }

    @Override
    protected void onBindSectionHeaderViewHolder(WorkingSearchHeaderHolder holder, int section) {
        holder.titleView.setText(mDatas.get(section).getTypeName());
    }


    @Override
    protected void onBindSectionFooterViewHolder(WorkingSearchFooterHolder holder, int section) {
        final WorkingSearchListInfo info = mDatas.get(section);
        holder.tvYypeMore.setText(String.format(mContext.getString(R.string.home_search_foot), info.getTypeName(), info.getTypeTotal()));
        holder.itemView.findViewById(R.id.llyt_search_footer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSearch = new Intent(mContext, CommonSearchActivity.class);
                switch (info.getViewType()) {
                    case WorkingSearchListInfo.STUDENT_TYPE:
                        toSearch.putExtra(ParameterUtils.TRANSITION_FLAG, from);
                        break;
                    case WorkingSearchListInfo.TASK_TYPE:
                        toSearch.putExtra(ParameterUtils.TRANSITION_FLAG, from);
                        break;
                    case WorkingSearchListInfo.REPORT_TYPE:
                        toSearch.putExtra(ParameterUtils.TRANSITION_FLAG, from);
                        break;
                    case WorkingSearchListInfo.TALK_TYPE:
                        toSearch.putExtra(ParameterUtils.TRANSITION_FLAG, from);
                        break;
                    default:
                        break;
                }
                toSearch.putExtra("keyword", info.getKeyword());
                toSearch.putExtra("typeName", info.getViewType());
                mContext.startActivity(toSearch);
            }
        });
    }

    @Override
    protected void onBindItemViewHolder(WorkingSearchContentHolder holder, int section, int position) {
        final WorkingSearchListInfo info = mDatas.get(section);
        switch (info.getViewType()) {
            case WorkingSearchListInfo.STUDENT_TYPE:
                holder.itemStudent.setVisibility(View.VISIBLE);
                holder.itemTask.setVisibility(View.GONE);
                holder.itemReport.setVisibility(View.GONE);
                holder.itemTalk.setVisibility(View.GONE);
                holder.tvTargetYearSeason.setText(info.getWorkingSearchInfos().get(position).getTargetApplicationYearSeason() + "/" + info.getWorkingSearchInfos().get(position).getTargetDegreeName());
                holder.tvName.setText(info.getWorkingSearchInfos().get(position).getName());
                clickStudent(holder, info.getWorkingSearchInfos().get(position));
                break;
            case WorkingSearchListInfo.TASK_TYPE:
                holder.itemStudent.setVisibility(View.GONE);
                holder.itemTask.setVisibility(View.VISIBLE);
                holder.itemReport.setVisibility(View.GONE);
                holder.itemTalk.setVisibility(View.GONE);
                if (TextUtils.isEmpty(info.getWorkingSearchInfos().get(position).getName())) {
                    holder.tvContent.setText(info.getWorkingSearchInfos().get(position).getTypeText());
                } else {
                    holder.tvContent.setText(info.getWorkingSearchInfos().get(position).getTypeText() + ":" + info.getWorkingSearchInfos().get(position).getName());
                }
                holder.tvcenterName.setText(info.getWorkingSearchInfos().get(position).getCenterName() + "-" + info.getWorkingSearchInfos().get(position).getUserName());
                holder.taskTime.setText(TimeUtil.getStrTime(info.getWorkingSearchInfos().get(position).getEndTime(), "MM-dd") + "截止");

                if ("ALERT".equals(info.getWorkingSearchInfos().get(position).getStatus())) {
                    //临期
                    holder.vBackgroup.setBackgroundColor(Color.parseColor("#FAAD14"));
                } else if ("EXPIRED".equals(info.getWorkingSearchInfos().get(position).getStatus())) {
                    //过期
                    holder.vBackgroup.setBackgroundColor(Color.parseColor("#7f000000"));
                } else if ("PENDING".equals(info.getWorkingSearchInfos().get(position).getStatus())) {
                    //进行中
                    holder.vBackgroup.setBackgroundColor(Color.parseColor("#1890FF"));
                } else {
                    //已完成
                    holder.vBackgroup.setBackgroundColor(Color.parseColor("#52C41A"));
                }
                clickTask(holder, info.getWorkingSearchInfos().get(position));
                break;
            case WorkingSearchListInfo.REPORT_TYPE:
                holder.itemStudent.setVisibility(View.GONE);
                holder.itemTask.setVisibility(View.GONE);
                holder.itemReport.setVisibility(View.VISIBLE);
                holder.itemTalk.setVisibility(View.GONE);
                holder.reportName.setText(String.format(mContext.getString(R.string.name_report), info.getWorkingSearchInfos().get(position).getUserName(), info.getWorkingSearchInfos().get(position).getTime()));
                holder.reportCenterName.setText(info.getWorkingSearchInfos().get(position).getCenterName());
                holder.tvPublishTime.setText(TimeUtil.getStrTime(info.getWorkingSearchInfos().get(position).getPublishTime()));
                clickReport(holder, info.getWorkingSearchInfos().get(position));
                break;

            case WorkingSearchListInfo.TALK_TYPE:
                holder.itemStudent.setVisibility(View.GONE);
                holder.itemTask.setVisibility(View.GONE);
                holder.itemReport.setVisibility(View.GONE);
                holder.itemTalk.setVisibility(View.VISIBLE);
                holder.talkName.setText(info.getWorkingSearchInfos().get(position).getUserName());
                holder.tvTalkContent.setText(info.getWorkingSearchInfos().get(position).getName());
                holder.tvTalkTime.setText(TimeUtil.getStrTime(info.getWorkingSearchInfos().get(position).getTime()));
                holder.tvTalkCenterName.setText(info.getWorkingSearchInfos().get(position).getCenterName());
                clickTalk(holder, info.getWorkingSearchInfos().get(position));
                break;
            default:
                break;
        }

    }

    private void clickStudent(WorkingSearchContentHolder holder, final WorkingSearchInfo info) {
        holder.itemView.findViewById(R.id.item_student).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ParameterUtils.TASK_TRANSFER_MANAGER.equals(from)) {
                    mContext.startActivity(new Intent(mContext, MyTaskListActivity.class).putExtra("id", info.getId())
                        .putExtra("name", info.getName()));
                } else if (ParameterUtils.REPORT_TRANSFER_MANAGER.equals(from)) {
                    mContext.startActivity(new Intent(mContext, StudentDetailReportActivity.class).putExtra("id", info.getUserId())
                        .putExtra("name", info.getName()));
                } else if (ParameterUtils.TALK_TRANSFER_MANAGER.equals(from)) {
                    mContext.startActivity(new Intent(mContext, StudentDetailTalkActivity.class)
                        .putExtra("id", info.getUserId())
                        .putExtra("name", info.getName()));
                } else {
                    mContext.startActivity(new Intent(mContext, StudentDetailActivity.class).putExtra("id", info.getId())
                        .putExtra("name", info.getName()));
                }
            }
        });
    }

    private void clickTask(WorkingSearchContentHolder holder, final WorkingSearchInfo info) {
        holder.itemView.findViewById(R.id.item_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, TaskDetailActivity.class).putExtra("working_search_info", info));
            }
        });
    }

    private void clickReport(WorkingSearchContentHolder holder, final WorkingSearchInfo info) {
        holder.itemView.findViewById(R.id.item_report).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, StudentDetailReportActivity.class).putExtra("id", info.getUserId())
                    .putExtra("name", info.getUserName()));
            }
        });
    }

    private void clickTalk(WorkingSearchContentHolder holder, final WorkingSearchInfo info) {
        holder.itemView.findViewById(R.id.item_talk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, PersonTalkRecordDetailActivity.class).putExtra("working_search", info));
            }
        });
    }
}
