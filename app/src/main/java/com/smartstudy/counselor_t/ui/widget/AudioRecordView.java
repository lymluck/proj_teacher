package com.smartstudy.counselor_t.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.ui.widget.audio.AudioRecorder;
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

    private Thread mRecordThread;

    private int recodeTime = 0; // 录音时长，如果录音时间太短则录音失败
    private boolean isCanceled = false; // 是否关闭录音


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
        tv_again_audio.setVisibility(GONE);
        tv_send.setVisibility(GONE);

        iv_audio.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recordState == RECORD_OFF) {
                    //不在录音的状态
                    startRecord();
                } else if (recordState == RECORD_ON) {
                    mAudioRecorder.stop();
                    recordState = RECORD_COMPLETE;
                    tv_again_audio.setVisibility(VISIBLE);
                    tv_send.setVisibility(VISIBLE);
                    mRecordThread.interrupt();
                    iv_audio.setImageResource(R.drawable.icon_audio_play);
                } else if (recordState == RECORD_COMPLETE) {
                    mAudioRecorder.play(mAudioRecorder.getFilePath());
                    recordState = RECORD_PLAY;
                    iv_audio.setImageResource(R.drawable.icon_audio_stop);
                    callRecordTimeThread();
                    mAudioRecorder.playComplete(new AudioRecorder.PlayComplete() {
                        @Override
                        public void playComplete() {
                            recordState = RECORD_COMPLETE;
                            mRecordThread.interrupt();
                            iv_audio.setImageResource(R.drawable.icon_audio_play);
                        }
                    });

                } else if (recordState == RECORD_PLAY) {
                    mAudioRecorder.playStop();
                    recordState = RECORD_COMPLETE;
                    iv_audio.setImageResource(R.drawable.icon_audio_play);
                }
            }
        });

        tv_again_audio.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //重新录音
//                mAudioRecorder.stop();
                recodeTime = 0;
                mAudioRecorder.deleteOldFile();
                tv_again_audio.setVisibility(GONE);
                tv_send.setVisibility(GONE);
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
        void sendOnClick();
    }


    public void setAudioRecord(RecordStrategy record) {
        this.mAudioRecorder = record;
    }


    private void startRecord() {
        recordState = RECORD_ON;
        callRecordTimeThread();
        iv_audio.setImageResource(R.drawable.icon_audio_stop);
        if (mAudioRecorder != null) {
            mAudioRecorder.ready();
            recordState = RECORD_ON;
            mAudioRecorder.start();
//                        callRecordTimeThread();
        }
    }


    // 开启录音计时线程
    private void callRecordTimeThread() {
        mRecordThread = new Thread(recordThread);
        mRecordThread.start();
    }


    // 录音线程
    private Runnable recordThread = new Runnable() {

        @Override
        public void run() {
            recodeTime = 0;
            while (recordState == RECORD_ON||RECORD_PLAY==recordState) {
                {
                    try {
                        Thread.sleep(1000);
                        if (!isCanceled) {
                            Message message = new Message();
                            message.what = RECORD_ON;
                            recodeTime += 1;
                            recordHandler.sendMessage(message);
                        }

                        if (recodeTime >= 180) {
                            Message message = new Message();
                            message.what = RECORD_COMPLETE;
                            recordHandler.sendMessage(message);
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler recordHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RECORD_ON:
                    tv_title.setText(getTime(recodeTime));
                    break;
                case RECORD_COMPLETE:
                    mAudioRecorder.stop();
                    recordState = RECORD_COMPLETE;
                    tv_title.setText("");
                    mRecordThread.interrupt();
                    iv_audio.setImageResource(R.drawable.icon_audio_play);
                    break;
            }

        }
    };


    private String getTime(int time) {
        if (0 < time && time < 10) {
            return "0:0" + time;
        } else if (10 <= time && time < 60) {
            return "0:" + time;
        } else if (60 <= time && time < 70) {
            return "1:0" + (time - 60);
        } else if (70 <= time && time < 120) {
            return "1:" + (time - 60);
        } else if (120 <= time && time < 130) {
            return "2:0" + (time - 120);
        } else if (130 <= time && time < 170) {
            return "2:" + (time - 120);
        } else {
            return "倒计时 " + (180 - time);
        }
    }

}
