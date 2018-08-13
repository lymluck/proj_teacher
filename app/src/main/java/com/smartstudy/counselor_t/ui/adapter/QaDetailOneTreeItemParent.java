package com.smartstudy.counselor_t.ui.adapter;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.ui.activity.StudentInfoActivity;

import study.smart.baselib.ui.adapter.base.BaseItem;
import study.smart.baselib.ui.adapter.base.ItemFactory;
import study.smart.baselib.ui.adapter.base.ViewHolder;
import study.smart.baselib.ui.widget.RatingBar;
import study.smart.baselib.ui.widget.audio.AudioRecorder;
import study.smart.baselib.ui.widget.treeview.TreeItemGroup;

import java.util.List;

import study.smart.baselib.BaseApplication;
import study.smart.baselib.entity.Answerer;
import study.smart.baselib.utils.DensityUtils;
import study.smart.baselib.utils.DisplayImageUtils;
import study.smart.baselib.utils.SPCacheUtils;

/**
 * @author yqy
 * @date on 2018/6/14
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class QaDetailOneTreeItemParent extends TreeItemGroup<Answerer> {
    private AnimationDrawable animationDrawable;
    private AudioRecorder audioRecorder;
    private boolean isPlaying = false;
    private ImageView imageView;

    @Override
    protected int initLayoutId() {
        audioRecorder = AudioRecorder.getInstance();
        return R.layout.item_qa_detail;
    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder) {
        final String askId = SPCacheUtils.get("askId", "").toString();
        viewHolder.setText(R.id.tv_answer_time, data.getCreateTimeText());
        DisplayImageUtils.formatPersonImgUrl(BaseApplication.appContext, data.getCommenter().getAvatar(), (ImageView) viewHolder.getView(R.id.iv_assignee));
        viewHolder.setText(R.id.tv_assignee, data.getCommenter().getName());
        viewHolder.setText(R.id.tv_answer, data.getContent());

        if ("request_info".equals(data.getActionType())) {
            viewHolder.getView(R.id.tv_compelete_info).setVisibility(View.VISIBLE);
            viewHolder.getView(R.id.ll_answer).setBackgroundResource(R.drawable.bg_answer_request_info);
            viewHolder.getView(R.id.ll_answer).setPadding(DensityUtils.dip2px(12f), DensityUtils.dip2px(12f), DensityUtils.dip2px(12f), DensityUtils.dip2px(12f));
        } else {
            viewHolder.getView(R.id.tv_compelete_info).setVisibility(View.GONE);
            viewHolder.getView(R.id.ll_answer).setBackgroundResource(0);
            viewHolder.getView(R.id.ll_answer).setPadding(0, 0, 0, 0);
        }


        if (data.getVoiceUrl() != null) {
            viewHolder.getView(R.id.ll_voice).setVisibility(View.VISIBLE);
            viewHolder.setText(R.id.tv_voice_time, data.getVoiceDuration());
            viewHolder.getView(R.id.tv_answer).setVisibility(View.GONE);
        } else {
            viewHolder.getView(R.id.ll_voice).setVisibility(View.GONE);
            viewHolder.getView(R.id.tv_answer).setVisibility(View.VISIBLE);
        }
        if (data.getComments() == null || data.getComments().size() == 0) {
            if (TextUtils.isEmpty(data.getRatingScore())) {
                viewHolder.getView(R.id.ll_comment_detail).setVisibility(View.GONE);
            } else {
                viewHolder.getView(R.id.ll_comment_detail).setVisibility(View.VISIBLE);
                RatingBar rbCourseRate = viewHolder.getView(R.id.rb_course_rate);
                rbCourseRate.setStar(TextUtils.isEmpty(data.getRatingScore()) ? 0f : Float.parseFloat(data.getRatingScore()));
                if (TextUtils.isEmpty(data.getRatingComment())) {
                    viewHolder.getView(R.id.tv_comment).setVisibility(View.GONE);
                } else {
                    viewHolder.getView(R.id.tv_comment).setVisibility(View.VISIBLE);
                    viewHolder.setText(R.id.tv_comment, data.getRatingComment());
                }
            }
        } else {
            viewHolder.getView(R.id.ll_comment_detail).setVisibility(View.GONE);
        }

        if (viewHolder.getView(R.id.ll_comment_detail).getVisibility() == View.GONE) {
            viewHolder.getView(R.id.view_last).setVisibility(View.GONE);
        } else {
            viewHolder.getView(R.id.view_last).setVisibility(View.VISIBLE);
        }

        viewHolder.getView(R.id.tv_compelete_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentStudent = new Intent();
                intentStudent.putExtra("ids", askId);
                intentStudent.putExtra("from", "more_info");
                intentStudent.setClass(BaseApplication.appContext, StudentInfoActivity.class);
                BaseApplication.appContext.startActivity(intentStudent);
            }
        });
    }

    @Override
    protected List<? extends BaseItem> initChildsList(Answerer data) {
        return ItemFactory.createTreeItemList(handleAnswerName(data), QaDetailTwoTreeItem.class, this);
    }


    @Override
    public void onExpand(boolean isCollapseOthers) {
        isCollapseOthers = true;
        super.onExpand(isCollapseOthers);
    }

    public void voiceOnclick(ViewHolder viewHolder) {
        if (data.getVoiceUrl() == null) {
            return;
        }
        animationDrawable = (AnimationDrawable) BaseApplication.appContext.getResources().getDrawable(R.drawable.bg_voice_receive);
        final ImageView ivVoice = viewHolder.getView(R.id.iv_voice);
        ivVoice.setImageDrawable(animationDrawable);
        if (animationDrawable != null && !audioRecorder.isPlaying()) {
            isPlaying = true;
            animationDrawable.start();
            audioRecorder.playByUri(BaseApplication.appContext, data.getVoiceUrl());
        }
        audioRecorder.playComplete(new AudioRecorder.PlayComplete() {
            @Override
            public void playComplete() {
                if (audioRecorder != null) {
                    isPlaying = false;
                    animationDrawable.stop();
                    audioRecorder.playStop();
                    ivVoice.setImageResource(R.drawable.sound_icon);
                }
            }
        });
        imageView = viewHolder.getView(R.id.iv_voice);
    }

    public void setVoiceState() {
        if (isPlaying) {
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


    private List<Answerer.Comments> handleAnswerName(Answerer answerer) {
        if (answerer != null) {
            if (answerer.getComments() != null && answerer.getComments().size() > 0) {
                for (Answerer.Comments comments : answerer.getComments()) {
                    comments.setAnswerName(answerer.getCommenter().getName());
//                    comments.setAnsewerId(answerer.getId());
                    comments.setRatingScore(answerer.getRatingScore());
                    comments.setRatingComment(answerer.getRatingComment());
                }
            }
        }
        return answerer.getComments();
    }

    public boolean getIsPlaying() {
        return isPlaying;
    }
}
