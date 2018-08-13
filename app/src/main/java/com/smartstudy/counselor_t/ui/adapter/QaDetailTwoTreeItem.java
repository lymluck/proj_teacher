package com.smartstudy.counselor_t.ui.adapter;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartstudy.counselor_t.R;

import study.smart.baselib.ui.adapter.base.ViewHolder;
import study.smart.baselib.ui.widget.RatingBar;
import study.smart.baselib.ui.widget.audio.AudioRecorder;
import study.smart.baselib.ui.widget.treeview.TreeItem;

import com.smartstudy.counselor_t.ui.activity.StudentInfoActivity;
import com.smartstudy.counselor_t.util.TextBrHandle;

import study.smart.baselib.BaseApplication;
import study.smart.baselib.entity.Answerer;
import study.smart.baselib.utils.DensityUtils;
import study.smart.baselib.utils.SPCacheUtils;

/**
 * @author yqy
 * @date on 2018/6/14
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class QaDetailTwoTreeItem extends TreeItem<Answerer.Comments> {
    AnimationDrawable animationDrawable;
    private AudioRecorder audioRecorder;
    private ImageView imageView;
    private boolean isPlaying = false;

    @Override
    protected int initLayoutId() {
        audioRecorder = AudioRecorder.getInstance();
        return R.layout.item_qa_detail_answer;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder) {
        String askName = SPCacheUtils.get("askName", "").toString();
        final String askId = SPCacheUtils.get("askId", "").toString();
        if (data.getCommentType().equals("subQuestion")) {
            String question = "";
            if (!"complete_request_info".equals(data.getActionType())) {
                question = "<font color='#FF9C08'>" + "追问 @" + data.getAnswerName() + "</font>" + ": " + (data.getContent() == null ? "" : data.getContent());
                ((TextView) viewHolder.getView(R.id.tv_detail_answer)).setText(Html.fromHtml(TextBrHandle.parseContent(question)));
                viewHolder.getView(R.id.ll_more_info).setBackgroundResource(0);
                viewHolder.getView(R.id.tv_more_info).setVisibility(View.GONE);
            } else {
                question = data.getContent();
                ((TextView) viewHolder.getView(R.id.tv_detail_answer)).setText(question);
                viewHolder.getView(R.id.tv_more_info).setVisibility(View.VISIBLE);
                viewHolder.getView(R.id.ll_more_info).setBackgroundResource(R.drawable.bg_remind_blue);
            }
            viewHolder.getView(R.id.ll_voice).setVisibility(View.GONE);
        } else {
            if (data.getVoiceUrl() != null) {
                String answer = "回复<font color='#078CF1'>" + " @" + askName + "</font>" + ": ";
                viewHolder.setText(R.id.tv_detail_answer, Html.fromHtml(TextBrHandle.parseContent(answer)));
                viewHolder.getView(R.id.ll_voice).setVisibility(View.VISIBLE);
                viewHolder.setText(R.id.tv_voice_time, data.getVoiceDuration());
            } else {
                String answer = "回复<font color='#078CF1'>" + " @" + askName + "</font>" + ": " + (data.getContent() == null ? "" : data.getContent());
                viewHolder.setText(R.id.tv_detail_answer, Html.fromHtml(TextBrHandle.parseContent(answer)));
                viewHolder.getView(R.id.ll_voice).setVisibility(View.GONE);
            }

            if ("request_info".equals(data.getActionType())) {
                ((TextView) viewHolder.getView(R.id.tv_detail_answer)).setText(data.getContent());
                viewHolder.getView(R.id.tv_more_info).setVisibility(View.VISIBLE);
                viewHolder.getView(R.id.ll_more_info).setBackgroundResource(R.drawable.bg_answer_request_info);
                ((TextView) viewHolder.getView(R.id.tv_more_info)).setText("点击查看基本信息");
                viewHolder.getView(R.id.ll_more_info).setPadding(DensityUtils.dip2px(12f), DensityUtils.dip2px(12f), DensityUtils.dip2px(12f), DensityUtils.dip2px(12f));
            } else {
                viewHolder.getView(R.id.tv_more_info).setVisibility(View.GONE);
                viewHolder.getView(R.id.tv_more_info).setBackgroundResource(0);
                if (data.getVoiceUrl() != null) {
                    String answer = "回复<font color='#078CF1'>" + " @" + askName + "</font>" + ": ";
                    ((TextView) viewHolder.getView(R.id.tv_detail_answer)).setText(Html.fromHtml(TextBrHandle.parseContent(answer)));
                    viewHolder.getView(R.id.ll_voice).setVisibility(View.VISIBLE);
                    ((TextView) viewHolder.getView(R.id.tv_voice_time)).setText(data.getVoiceDuration());
                } else {
                    String answer = "回复<font color='#078CF1'>" + " @" + askName + "</font>" + ": " + (data.getContent() == null ? "" : data.getContent());
                    ((TextView) viewHolder.getView(R.id.tv_detail_answer)).setText(Html.fromHtml(TextBrHandle.parseContent(answer)));
                    viewHolder.getView(R.id.ll_voice).setVisibility(View.GONE);
                }
            }
        }
        viewHolder.setText(R.id.tv_time, data.getCreateTimeText());

        viewHolder.getView(R.id.tv_more_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentStudent = new Intent();
                intentStudent.putExtra("ids", askId);
                intentStudent.putExtra("from", "more_info");
                intentStudent.setClass(BaseApplication.appContext, StudentInfoActivity.class);
                BaseApplication.appContext.startActivity(intentStudent);
            }
        });

        if (getItemManager() != null && getItemManager().getItemPosition(this)
            == getItemManager().getItemPosition(getParentItem()) + getParentItem().getChildCount()) {
            viewHolder.getView(R.id.ll_comment_detail).setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(data.getRatingScore())) {
                viewHolder.getView(R.id.ll_comment_detail).setVisibility(View.GONE);
            } else {
                viewHolder.getView(R.id.ll_comment_detail).setVisibility(View.VISIBLE);
                RatingBar rbCourseRate = viewHolder.getView(R.id.rb_course_rate);
                rbCourseRate.setStar(TextUtils.isEmpty(data.getRatingScore())
                    ? 0f : Float.parseFloat(data.getRatingScore()));
                if (TextUtils.isEmpty(data.getRatingComment())) {
                    viewHolder.getView(R.id.tv_comment).setVisibility(View.GONE);
                } else {
                    viewHolder.getView(R.id.tv_comment).setVisibility(View.VISIBLE);
                    viewHolder.setText(R.id.tv_comment, data.getRatingComment());
                }
            }
            if (viewHolder.getView(R.id.ll_comment_detail).getVisibility() == View.GONE) {
                viewHolder.getView(R.id.v_last).setVisibility(View.GONE);
            } else {
                viewHolder.getView(R.id.v_last).setVisibility(View.VISIBLE);
            }
        } else {
            viewHolder.getView(R.id.ll_comment_detail).setVisibility(View.GONE);
        }
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


    //点击了条目后，重置上一条目录的播放状态
    public void setVoiceState() {
        if (isPlaying) {
            if (animationDrawable != null) {
                animationDrawable.stop();
            }
            if (audioRecorder != null) {
                if (audioRecorder.isPlaying()) {
                    audioRecorder.playStop();
                }
            }
            isPlaying = false;
            if (imageView != null) {
                imageView.setImageResource(R.drawable.sound_icon);
            }
        }
    }

    public boolean getIsPlaying() {
        return isPlaying;
    }

}

