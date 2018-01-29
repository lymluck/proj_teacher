package com.smartstudy.counselor_t.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.util.SPCacheUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.FileMessage;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

//CallKit start 1
//CallKit end 1

/**
 * 会话页面
 * 1，设置 ActionBar title
 * 2，加载会话页面
 * 3，push 和 通知 判断
 */
public class ConversationActivity extends BaseActivity implements View.OnClickListener, RongIM.OnSendMessageListener {
    private TextView tvTitle;
    private String targeId;
    private TextView tvTitleTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        setHeadVisible(View.GONE);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initEvent() {
        this.findViewById(R.id.topdefault_rightbutton2).setOnClickListener(this);
        this.findViewById(R.id.topdefault_leftbutton2).setOnClickListener(this);
        RongIM.getInstance().setSendMessageListener(this);
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        tvTitle = findViewById(R.id.topdefault_centertitle2);
        tvTitleTag = findViewById(R.id.tv_title_tag);
        tvTitle.setText(getIntent().getData().getQueryParameter("title"));
        String titleTag = (String) SPCacheUtils.get("titleTag", "");
        tvTitleTag.setText(titleTag);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_rightbutton2:
                Intent intent = new Intent();
                intent.putExtra("ids", targeId);
                intent.setClass(this, StudentInfoActivity.class);
                startActivity(intent);
                break;

            case R.id.topdefault_leftbutton2:
                finish();
                break;
            default:
                break;

        }

    }

    @Override
    public Message onSend(Message message) {
        //获取个人信息，通过extra
        return setExtra(message);
    }

    @Override
    public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {
        return false;
    }

    @Subscribe(threadMode = org.greenrobot.eventbus.ThreadMode.MAIN)
    public void onMessageEvent(MessageContent messageContent) {
        String userName = messageContent.getUserInfo().getName();
        if (!TextUtils.isEmpty(userName)) {
            tvTitle.setText(userName);
        }
        tvTitleTag.setText(SPCacheUtils.get("titleTag", "").toString());
    }

    private Message setExtra(Message message) {
        //按消息类型添加extra
        JSONObject object = new JSONObject();
        object.put("title", SPCacheUtils.get("title", ""));
        object.put("workyear", SPCacheUtils.get("year", ""));
        object.put("company", SPCacheUtils.get("company", ""));

        //按消息类型添加extra
        if (message.getContent() instanceof TextMessage) {
            ((TextMessage) message.getContent()).setExtra(object.toJSONString());
        }
        if (message.getContent() instanceof ImageMessage) {
            ((ImageMessage) message.getContent()).setExtra(object.toJSONString());
        }
        if (message.getContent() instanceof LocationMessage) {
            ((LocationMessage) message.getContent()).setExtra(object.toJSONString());
        }
        if (message.getContent() instanceof FileMessage) {
            ((FileMessage) message.getContent()).setExtra(object.toJSONString());
        }
        if (message.getContent() instanceof VoiceMessage) {
            ((VoiceMessage) message.getContent()).setExtra(object.toJSONString());
        }
        return message;
    }
}