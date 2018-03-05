package com.smartstudy.counselor_t.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.Answerer;
import com.smartstudy.counselor_t.ui.widget.HorizontalDividerItemDecoration;
import com.smartstudy.counselor_t.util.DensityUtils;
import com.smartstudy.counselor_t.util.DisplayImageUtils;

import java.util.List;

/**
 * @author yqy
 * @date on 2018/3/5
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class QaDetailAdapter extends RecyclerView.Adapter<QaDetailAdapter.MyViewHolder> {

    private Context mContext;

    private List<Answerer> answerers;

    public void setAnswers(List<Answerer> answerers) {
        if (this.answerers != null) {
            this.answerers.clear();
        }
        this.answerers = answerers;
        this.notifyDataSetChanged();
    }

    public QaDetailAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_qa_detail, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Answerer entity = (Answerer) answerers.get(position);

        holder.tv_answer_time.setText(entity.getCreateTime());
        DisplayImageUtils.displayCircleImage(mContext, entity.getCommenter().getAvatar(), holder.ivAssignee);
        holder.tv_assignee.setText(entity.getCommenter().getName());
        holder.tvAnswer.setText(entity.getContent());

        // 关键代码
        //////////////////////////////////////////////////
        QadetailAnswerItemAdapter qadetailAnswerItemAdapter = new QadetailAnswerItemAdapter(mContext);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        holder.rvDetailAnswer.setLayoutManager(linearLayoutManager);

//        holder.rvDetailAnswer.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
//                .size(DensityUtils.dip2px(0.5f)).colorResId(R.color.bg_recent_user).margin(DensityUtils.dip2px(64),0).build());

        holder.rvDetailAnswer.setAdapter(qadetailAnswerItemAdapter);
        qadetailAnswerItemAdapter.setComments(entity.getComments());
        holder.rvDetailAnswer.setVisibility(View.VISIBLE);
        /////////////////////////////////////////////////////

    }

    @Override
    public int getItemCount() {
        return answerers == null ? 0 : answerers.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivAssignee;
        TextView tv_answer_time;
        TextView tv_assignee;
        TextView tvAnswer;
        RecyclerView rvDetailAnswer;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivAssignee = (ImageView) itemView.findViewById(R.id.iv_assignee);
            tv_answer_time = (TextView) itemView.findViewById(R.id.tv_answer_time);
            tv_assignee = (TextView) itemView.findViewById(R.id.tv_assignee);
            tvAnswer = (TextView) itemView.findViewById(R.id.tv_answer);
            rvDetailAnswer = itemView.findViewById(R.id.rv_answer_detail);
        }
    }

}
