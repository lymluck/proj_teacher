package com.smartstudy.counselor_t.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.handler.WeakHandler;
import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.ui.fragment.MyConversationFragment;
import com.smartstudy.counselor_t.util.RongUtils;
import com.smartstudy.counselor_t.util.SPCacheUtils;

import java.util.List;
import java.util.Locale;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
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
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void initEvent() {
        this.findViewById(R.id.topdefault_rightbutton2).setOnClickListener(this);
        this.findViewById(R.id.topdefault_leftbutton2).setOnClickListener(this);
        RongIM.getInstance().setSendMessageListener(this);
    }

    private void enterFragment(final Conversation.ConversationType mConversationType, final String mTargetId) {
        setTvTitleTag();
        if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
            new WeakHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //登录融云IM
                    String cacheToken = (String) SPCacheUtils.get("imToken", "");
                    if (!TextUtils.isEmpty(cacheToken)) {
                        RongIM.connect(cacheToken, new RongIMClient.ConnectCallback() {
                            @Override
                            public void onTokenIncorrect() {
                            }

                            @Override
                            public void onSuccess(String s) {
                                enterFragment(mConversationType, mTargetId);
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                                enterFragment(mConversationType, mTargetId);
                            }
                        });
                    } else {
                        startActivity(new Intent(ConversationActivity.this, LoginActivity.class));
                    }
                }
            }, 300);
        } else {
            MyConversationFragment fragment = new MyConversationFragment();

            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                    .appendQueryParameter("targetId", mTargetId).build();

            fragment.setUri(uri);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            //xxx 为你要加载的 id
            transaction.add(R.id.rong_content, fragment);
            transaction.commitAllowingStateLoss();
        }
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        tvTitle = findViewById(R.id.topdefault_centertitle2);
        tvTitleTag = findViewById(R.id.tv_title_tag);
        Uri uri = getIntent().getData();
        tvTitle.setText(uri.getQueryParameter("title"));
        targeId = uri.getQueryParameter("targetId");
        Conversation.ConversationType mConversationType = Conversation.ConversationType.valueOf(uri
                .getLastPathSegment().toUpperCase(Locale.US));
        enterFragment(mConversationType, targeId);
    }

    private void setTvTitleTag() {
        RongIM.getInstance().getHistoryMessages(Conversation.ConversationType.PRIVATE, targeId, 0, 100, new RongIMClient.ResultCallback<List<Message>>() {
            @Override
            public void onSuccess(List<Message> messages) {
                for (Message message : messages) {
                    String extra = RongUtils.getMsgExtra(message.getContent());
                    JSONObject obj_msg = JSON.parseObject(extra);
                    if (obj_msg != null && obj_msg.containsKey("abroadyear")) {
                        String tag = RongUtils.getTitleTag(extra);
                        if (TextUtils.isEmpty(tag)) {
                            tvTitleTag.setVisibility(View.GONE);
                        } else {
                            tvTitleTag.setText(RongUtils.getTitleTag(extra));
                            tvTitleTag.setVisibility(View.VISIBLE);
                        }
                        break;
                    }
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        });
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

    private Message setExtra(Message message) {
        //按消息类型添加extra
        JSONObject object = new JSONObject();
        object.put("title", SPCacheUtils.get("title", ""));
        object.put("workyear", SPCacheUtils.get("year", ""));
        object.put("company", SPCacheUtils.get("company", ""));
        MessageContent messageContent = message.getContent();
        //按消息类型添加extra
        if (TextMessage.class.isAssignableFrom(messageContent.getClass())) {
            ((TextMessage) messageContent).setExtra(object.toJSONString());
        }
        if (ImageMessage.class.isAssignableFrom(messageContent.getClass())) {
            ((ImageMessage) messageContent).setExtra(object.toJSONString());
        }
        if (LocationMessage.class.isAssignableFrom(messageContent.getClass())) {
            ((LocationMessage) messageContent).setExtra(object.toJSONString());
        }
        if (FileMessage.class.isAssignableFrom(messageContent.getClass())) {
            ((FileMessage) messageContent).setExtra(object.toJSONString());
        }
        if (VoiceMessage.class.isAssignableFrom(messageContent.getClass())) {
            ((VoiceMessage) messageContent).setExtra(object.toJSONString());
        }
        return message;
    }
}