package com.smartstudy.counselor_t.ui.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
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
import com.smartstudy.counselor_t.ui.widget.audio.AudioRecorder;
import com.smartstudy.counselor_t.util.TextBrHandle;

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
public class QadetailAnswerItemAdapter extends RecyclerView.Adapter<QadetailAnswerItemAdapter.MyViewHolder> {

    private Context mContext;

    private List<Answerer.Comments> mDatas;

    AnimationDrawable animationDrawable;

    private AudioRecorder audioRecorder;

    private Uri isPlayingUri;

    private String answerName;

    private String askName;


    private ImageView imageView;


    public void setComments(List<Answerer.Comments> comments, String answerName, String askName) {
        if (this.mDatas != null) {
            this.mDatas.clear();
        }
        this.mDatas = comments;
        audioRecorder = AudioRecorder.getInstance();
        this.answerName = answerName;
        this.askName = askName;
        this.notifyDataSetChanged();
    }

    public QadetailAnswerItemAdapter(Context context) {
        EventBus.getDefault().register(this);//订阅
        this.mContext = context;
    }

    @NonNull
    @Override
    public QadetailAnswerItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_qa_detail_answer, parent, false);
        MyViewHolder myViewHolderw = new MyViewHolder(view);
        return myViewHolderw;

    }

    @Override
    public void onBindViewHolder(@NonNull final QadetailAnswerItemAdapter.MyViewHolder holder, int position) {
        final Answerer.Comments comments = mDatas.get(position);
        if (comments.getCommentType().equals("subQuestion")) {
            String question = "<font color='#FF9C08'>" + "追问 @" + answerName + "</font>" + ": " + comments.getContent();
            holder.tv_detail_answer.setText(Html.fromHtml(TextBrHandle.parseContent(question)));
            holder.ll_voice.setVisibility(View.GONE);
        } else {
            if (comments.getVoiceUrl() != null) {
                String answer = "回复<font color='#078CF1'>" + " @" + askName + "</font>" + ": ";
                holder.tv_detail_answer.setText(Html.fromHtml(TextBrHandle.parseContent(answer)));
                holder.ll_voice.setVisibility(View.VISIBLE);
                holder.tv_voice_time.setText(comments.getVoiceDuration());
            } else {
                String answer = "回复<font color='#078CF1'>" + " @" + askName + "</font>" + ": "+comments.getContent();
                holder.tv_detail_answer.setText(Html.fromHtml(TextBrHandle.parseContent(answer)));
                holder.ll_voice.setVisibility(View.GONE);
            }
        }
        holder.tv_time.setText(comments.getCreateTimeText());

        holder.iv_voice.setScaleType(ImageView.ScaleType.FIT_START);
        holder.ll_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ItemOnClick("secondItem"));
                animationDrawable = (AnimationDrawable) mContext.getResources().getDrawable(R.drawable.bg_voice_receive);
                holder.iv_voice.setImageDrawable(animationDrawable);
                if (isPlayingUri == null) {
                    if (audioRecorder != null) {
                        if (animationDrawable != null && !audioRecorder.isPlaying()) {
                            animationDrawable.start();
                            audioRecorder.playByUri(mContext, comments.getVoiceUrl());
                        } else {
                            animationDrawable.stop();
                            audioRecorder.playStop();
                            if (imageView != null) {
                                imageView.setImageResource(R.drawable.sound_icon);
                            }
                        }
                    }
                } else {
                    animationDrawable.stop();
                    audioRecorder.playStop();
                    if (imageView != null) {
                        imageView.setImageResource(R.drawable.sound_icon);
                    }
                    animationDrawable.start();
                    audioRecorder.playByUri(mContext, comments.getVoiceUrl());

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
                isPlayingUri = comments.getVoiceUrl();
                imageView = v.findViewById(R.id.iv_voice);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_detail_answer;

        TextView tv_time;

        LinearLayout ll_voice;

        TextView tv_voice_time;

        ImageView iv_voice;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_detail_answer = (TextView) itemView.findViewById(R.id.tv_detail_answer);
            tv_time = itemView.findViewById(R.id.tv_time);
            ll_voice = itemView.findViewById(R.id.ll_voice);
            tv_voice_time = itemView.findViewById(R.id.tv_voice_time);
            iv_voice = itemView.findViewById(R.id.iv_voice);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(ItemOnClick event) {
        if (event.getItemWhere().equals("firstItem") || event.getItemWhere().equals("Qa")) {
            isPlayingUri = null;
            if (animationDrawable != null) {
                animationDrawable.stop();
            }
            audioRecorder.playStop();
            if (imageView != null) {
                imageView.setImageResource(R.drawable.sound_icon);
            }
        }
    }

}
