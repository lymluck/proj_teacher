package com.smartstudy.counselor_t.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.ui.widget.audio.RecordStrategy;

/**
 * @author yqy
 * @date on 2018/2/7
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class AudioRecordView extends LinearLayout {
    private TextView tv_title;
    private TextView tv_again_audio;
    private ImageView iv_audio;
    private TextView tv_send;
    private SendOnClickListener listener;
    private RecordStrategy mAudioRecorder;
    private static final int RECORD_OFF = 0; // 不在录音
    private static final int RECORD_ON = 1; // 正在录音
    private static final int RECORD_COMPLETE = 2;//录完的状态
    private static final int RECORD_PLAY = 3;//正在播放的状态
    private int recordState = 0; // 录音状态


    public AudioRecordView(Context context) {
        super(context);
    }

    public AudioRecordView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_audio_record, this, true);
        tv_title = view.findViewById(R.id.tv_title);
        tv_again_audio = view.findViewById(R.id.tv_again_audio);
        iv_audio = view.findViewById(R.id.iv_audio);
        tv_send = view.findViewById(R.id.tv_send);


        iv_audio.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recordState == RECORD_OFF) {
                    //不在录音的状态
                    startRecord();
                } else if (recordState == RECORD_ON) {
                    mAudioRecorder.stop();
                    recordState=RECORD_COMPLETE;
                }else if(recordState==RECORD_COMPLETE){
                    
                }
            }
        });

        tv_again_audio.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //重新录音
                mAudioRecorder.deleteOldFile();
                startRecord();
            }
        });

        tv_send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送
                if (listener != null) {
                    listener.sendOnClick();
                }
            }
        });

    }

    /**
     * 更新标题
     *
     * @param title
     */
    public void updateTitle(String title) {
        tv_title.setText(title);
    }


    public void setsendOnClickListener(SendOnClickListener listener) {
        this.listener = listener;
    }


    interface SendOnClickListener {
        public void sendOnClick();
    }


    public void setAudioRecord(RecordStrategy record) {
        this.mAudioRecorder = record;
    }


    private void startRecord() {
        iv_audio.setImageResource(R.drawable.icon_audio_play);
        recordState = RECORD_ON;
        if (mAudioRecorder != null) {
            mAudioRecorder.ready();
            recordState = RECORD_ON;
            mAudioRecorder.start();
//                        callRecordTimeThread();
        }
    }
}
