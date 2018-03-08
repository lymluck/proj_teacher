package com.smartstudy.counselor_t.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
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

import java.util.Timer;
import java.util.TimerTask;

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
    private long lastClickTime;
    private SendOnClickListener listener;
    private RecordStrategy mAudioRecorder;
    private static final int RECORD_OFF = 0; // 不在录音
    private static final int RECORD_ON = 1; // 正在录音
    private static final int RECORD_COMPLETE = 2;//录完的状态
    private static final int RECORD_PLAY = 3;//正在播放的状态
    private int recordState = 0; // 录音状态

    private Timer timer = null;//计时器
    private int i = 0;
    private TimerTask timerTask = null;

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
                if (isFastDoubleClick()) {
                    return;
                }
                if (recordState == RECORD_OFF) {
                    //不在录音的状态
                    startRecord();
                } else if (recordState == RECORD_ON) {
                    mAudioRecorder.stop();
                    stopTime();
                    recordState = RECORD_COMPLETE;
                    tv_again_audio.setVisibility(VISIBLE);
                    tv_send.setVisibility(VISIBLE);
                    iv_audio.setImageResource(R.drawable.icon_audio_play);
                } else if (recordState == RECORD_COMPLETE) {
                    mAudioRecorder.play(mAudioRecorder.getFilePath());
                    recordState = RECORD_PLAY;
                    i = 0;
                    startTime();
                    iv_audio.setImageResource(R.drawable.icon_audio_stop);
                    mAudioRecorder.playComplete(new AudioRecorder.PlayComplete() {
                        @Override
                        public void playComplete() {
                            recordState = RECORD_COMPLETE;
                            iv_audio.setImageResource(R.drawable.icon_audio_play);
                            tv_again_audio.setVisibility(VISIBLE);
                            tv_send.setVisibility(VISIBLE);
                            mAudioRecorder.playStop();
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
                mAudioRecorder.stop();
                mAudioRecorder.deleteOldFile();
                tv_again_audio.setVisibility(GONE);
                tv_send.setVisibility(GONE);
                recordState = RECORD_OFF;
                i = 0;
                updateTitle("点击开始录音");
                iv_audio.setImageResource(R.drawable.icon_audio_speak);
            }
        });

        tv_send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送
                if (listener != null) {
                    listener.sendOnClick(mAudioRecorder.getFilePath());
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


    public interface SendOnClickListener {
        void sendOnClick(String path);
    }


    public void setAudioRecord(RecordStrategy record) {
        this.mAudioRecorder = record;
    }


    private void startRecord() {
        recordState = RECORD_ON;
        iv_audio.setImageResource(R.drawable.icon_audio_stop);
        if (mAudioRecorder != null) {
            mAudioRecorder.ready();
            recordState = RECORD_ON;
            mAudioRecorder.start();
            startTime();
        }
    }


    @SuppressLint("HandlerLeak")
    private Handler recordHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RECORD_ON:
                    if (170 <= i) {
                        tv_title.setTextColor(Color.parseColor("#F6611D"));
                        tv_title.setTextSize(17);
                    } else {
                        tv_title.setTextColor(Color.parseColor("#949BA1"));
                        tv_title.setTextSize(15);
                    }
                    tv_title.setText(getTime(i));
                    startTime();
                    break;
                case RECORD_COMPLETE:
                    mAudioRecorder.stop();
                    stopTime();
                    recordState = RECORD_COMPLETE;
                    tv_title.setText("");
                    tv_again_audio.setVisibility(VISIBLE);
                    tv_send.setVisibility(VISIBLE);
                    iv_audio.setImageResource(R.drawable.icon_audio_play);
                    break;
            }

        }
    };


    private String getTime(int time) {
        if (0 <= time && time < 10) {
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


    /**
     * 开始自动减时
     */
    private void startTime() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (recordState == RECORD_ON || recordState == RECORD_PLAY) {
                    if (0 <= i && i < 180) {
                        Message message = Message.obtain();
                        message.what = RECORD_ON;
                        i++;//自动加1
                        recordHandler.sendMessage(message);
                    }
                    if (i >= 180) {
                        Message message = Message.obtain();
                        message.what = RECORD_COMPLETE;
                        recordHandler.sendMessage(message);
                    }
                }
            }
        };
        timer.schedule(timerTask, 1000);//1000ms执行一次
    }

    /**
     * 停止自动减时
     */
    private void stopTime() {
        if (timer != null) {
            timer.cancel();
        }
    }


    public boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1000) {       //1000毫秒内按钮无效，这样可以控制快速点击，自己调整频率
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
