package com.smartstudy.counselor_t.ui.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.Answerer;
import com.smartstudy.counselor_t.entity.ItemOnClick;
import com.smartstudy.counselor_t.ui.widget.HorizontalDividerItemDecoration;
import com.smartstudy.counselor_t.ui.widget.RatingBar;
import com.smartstudy.counselor_t.ui.widget.audio.AudioRecorder;
import com.smartstudy.counselor_t.util.DensityUtils;
import com.smartstudy.counselor_t.util.DisplayImageUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    private String askName;

    AnimationDrawable animationDrawable;

    private AudioRecorder audioRecorder;

    private ImageView iv_voice;

    int positionOnclick = -1;

    private boolean isPlaying = false;

    private Uri isPlayingUri;

    private ImageView imageView;

    public void setAnswers(List<Answerer> answerers, String askName) {
        if (this.answerers != null) {
            this.answerers.clear();
        }
        audioRecorder = AudioRecorder.getInstance();
        this.answerers = answerers;
        this.askName = askName;
        this.notifyDataSetChanged();
    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        EventBus.getDefault().register(this);//订阅
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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Answerer entity = (Answerer) answerers.get(position);
        holder.tv_answer_time.setText(entity.getCreateTimeText());
        DisplayImageUtils.formatPersonImgUrl(mContext, entity.getCommenter().getAvatar(), holder.ivAssignee);
        holder.tv_assignee.setText(entity.getCommenter().getName());
        holder.tvAnswer.setText(entity.getContent());

        if (TextUtils.isEmpty(entity.getRatingScore())) {
            holder.llCommentDetail.setVisibility(View.GONE);
        } else {
            holder.llCommentDetail.setVisibility(View.VISIBLE);
            holder.rbCourseRate.setStar(TextUtils.isEmpty(entity.getRatingScore()) ? 0f : Float.parseFloat(entity.getRatingScore()));
            if (TextUtils.isEmpty(entity.getRatingComment())) {
                holder.tvComment.setVisibility(View.GONE);
            } else {
                holder.tvComment.setVisibility(View.VISIBLE);
                holder.tvComment.setText(entity.getRatingComment());
            }
        }

        if (entity.getVoiceUrl() != null) {
            holder.ll_voice.setVisibility(View.VISIBLE);
            holder.tv_voice_time.setText(entity.getVoiceDuration());
            holder.tvAnswer.setVisibility(View.GONE);
        } else {
            holder.ll_voice.setVisibility(View.GONE);
            holder.tvAnswer.setVisibility(View.VISIBLE);
        }

        holder.ll_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ItemOnClick("firstItem"));
                animationDrawable = (AnimationDrawable) mContext.getResources().getDrawable(R.drawable.bg_voice_receive);
                holder.iv_voice.setImageDrawable(animationDrawable);
                if (audioRecorder != null) {
                    if (isPlayingUri == null) {
                        isPlaying = true;
                        if (animationDrawable != null && !audioRecorder.isPlaying()) {
                            animationDrawable.start();
                            audioRecorder.playByUri(mContext, entity.getVoiceUrl());
                        } else {
                            isPlaying = false;
                            animationDrawable.stop();
                            audioRecorder.playStop();
                            if (imageView != null) {
                                imageView.setImageResource(R.drawable.sound_icon);
                            }
                        }

                    } else {
                        if (positionOnclick == position) {
                            if (isPlaying) {
                                isPlaying = false;
                                animationDrawable.stop();
                                audioRecorder.playStop();
                                if (imageView != null) {
                                    imageView.setImageResource(R.drawable.sound_icon);
                                }
                            } else {
                                isPlaying = true;
                                audioRecorder.playByUri(mContext, entity.getVoiceUrl());
                                holder.iv_voice.setImageDrawable(animationDrawable);
                                animationDrawable.start();
                            }

                        } else {
                            isPlaying = true;
                            animationDrawable.stop();
                            audioRecorder.playStop();
                            if (imageView != null) {
                                imageView.setImageResource(R.drawable.sound_icon);
                            }
                            animationDrawable.start();
                            audioRecorder.playByUri(mContext, entity.getVoiceUrl());
                        }

                    }
                    audioRecorder.playComplete(new AudioRecorder.PlayComplete() {
                        @Override
                        public void playComplete() {
                            if (audioRecorder != null) {
                                animationDrawable.stop();
                                audioRecorder.playStop();
                                holder.iv_voice.setImageResource(R.drawable.sound_icon);
                            }
                        }
                    });
                }
                positionOnclick = position;
                isPlayingUri = entity.getVoiceUrl();
                imageView = v.findViewById(R.id.iv_voice);
            }
        });
        // 关键代码
        //////////////////////////////////////////////////
        QadetailAnswerItemAdapter qadetailAnswerItemAdapter = new QadetailAnswerItemAdapter(mContext);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        holder.rvDetailAnswer.setLayoutManager(linearLayoutManager);

        holder.rvDetailAnswer.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
            .size(DensityUtils.dip2px(0.5f)).colorResId(R.color.bg_home_search).margin(DensityUtils.dip2px(64), 0).build());

        holder.rvDetailAnswer.setAdapter(qadetailAnswerItemAdapter);
        if (entity.getComments() != null && entity.getComments().size() > 0) {
            qadetailAnswerItemAdapter.setComments(entity.getComments(), entity.getCommenter().getName(), askName);
            holder.v_line.setVisibility(View.VISIBLE);
        } else {
            holder.v_line.setVisibility(View.GONE);
        }

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
        TextView tv_voice_time;
        LinearLayout ll_voice;
        ImageView iv_voice;
        View v_line;
        private LinearLayout llCommentDetail;
        private RatingBar rbCourseRate;
        private TextView tvComment;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivAssignee = (ImageView) itemView.findViewById(R.id.iv_assignee);
            tv_answer_time = (TextView) itemView.findViewById(R.id.tv_answer_time);
            tv_assignee = (TextView) itemView.findViewById(R.id.tv_assignee);
            tvAnswer = (TextView) itemView.findViewById(R.id.tv_answer);
            rvDetailAnswer = itemView.findViewById(R.id.rv_answer_detail);
            tv_voice_time = itemView.findViewById(R.id.tv_voice_time);
            iv_voice = itemView.findViewById(R.id.iv_voice);
            ll_voice = itemView.findViewById(R.id.ll_voice);
            v_line = itemView.findViewById(R.id.v_line);
            llCommentDetail = itemView.findViewById(R.id.ll_comment_detail);
            rbCourseRate = itemView.findViewById(R.id.rb_course_rate);
            tvComment = itemView.findViewById(R.id.tv_comment);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(ItemOnClick event) {
        if (event.getItemWhere().equals("secondItem") || event.getItemWhere().equals("Qa")) {
            isPlayingUri = null;
            isPlaying = false;
            if (animationDrawable != null) {
                animationDrawable.stop();
            }
            if (audioRecorder != null) {
                if (audioRecorder.isPlaying()) {
                    audioRecorder.playStop();
                }
            }
            if (imageView != null) {
                imageView.setImageResource(R.drawable.sound_icon);
            }
        }
    }


    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        EventBus.getDefault().unregister(this);
    }
}
