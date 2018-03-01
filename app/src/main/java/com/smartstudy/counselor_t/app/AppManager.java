package com.smartstudy.counselor_t.app;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.manager.StudentInfoManager;
import com.smartstudy.counselor_t.ui.activity.LoginActivity;
import com.smartstudy.counselor_t.ui.activity.MsgShareActivity;
import com.smartstudy.counselor_t.ui.activity.MyInfoActivity;
import com.smartstudy.counselor_t.ui.activity.StudentInfoActivity;
import com.smartstudy.counselor_t.util.SPCacheUtils;
import com.smartstudy.counselor_t.util.ToastUtils;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.RongMessageItemLongClickActionManager;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imkit.widget.provider.MessageItemLongClickAction;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;
import me.kareluo.imaging.IMGEditActivity;

/**
 * @author louis
 * @date on 2018/2/24
 * @describe Application管理工具类
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */

public class AppManager implements RongIMClient.ConnectionStatusListener, RongIM.ConversationClickListener {

    private static AppManager mInstance;
    //application context
    private Context mContext;
    private Message msg;


    public AppManager(Context mContext) {
        this.mContext = mContext;
        initListener();
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String s) {
                UserInfo info = RongUserInfoManager.getInstance().getUserInfo(s);
                if (info == null) {
                    StudentInfoManager.getInstance().getStudentInfo(s);
                }
                return null;

            }
        }, true);
    }

    public static void init(Context context) {
        if (mInstance == null) {
            synchronized (AppManager.class) {
                if (mInstance == null) {
                    mInstance = new AppManager(context);
                }
            }
        }
    }

    private void initListener() {
        RongIM.setConnectionStatusListener(this);
        RongIM.setConversationClickListener(this);
    }

    @Override
    public void onChanged(ConnectionStatus connectionStatus) {
        if (ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT.equals(connectionStatus)) {
            mContext.startActivity(new Intent(mContext, LoginActivity.class));
        }
        if (ConnectionStatus.CONN_USER_BLOCKED.equals(connectionStatus)) {

        }
    }

    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo, String s) {
        String myId = (String) SPCacheUtils.get("imUserId", "");
        String userId = userInfo.getUserId();
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (userId.equals(myId)) {
            intent.setClass(mContext, MyInfoActivity.class);
        } else {
            intent.putExtra("ids", s);
            intent.setClass(mContext, StudentInfoActivity.class);
        }
        mContext.startActivity(intent);
        return false;
    }

    @Override
    public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo, String s) {
        return false;
    }

    @Override
    public boolean onMessageClick(Context context, View view, Message message) {
        return false;
    }

    @Override
    public boolean onMessageLinkClick(Context context, String s, Message message) {
        return false;
    }

    @Override
    public boolean onMessageLongClick(Context context, View view, Message message) {
        this.msg = message;
        addMsgAction();
        return false;
    }


    private void addMsgAction() {
        List<MessageItemLongClickAction> messageItemLongClickActions = RongMessageItemLongClickActionManager.getInstance().getMessageItemLongClickActions();
        MessageItemLongClickAction shareAction = null;
        MessageItemLongClickAction imgAction = null;
        for (MessageItemLongClickAction clickAction : messageItemLongClickActions) {
            if (mContext.getString(R.string.rc_dialog_item_message_share).equals(clickAction.getTitle(mContext))) {
                shareAction = clickAction;
            }
            if (mContext.getString(R.string.rc_dialog_item_message_edit).equals(clickAction.getTitle(mContext))) {
                imgAction = clickAction;
            }
        }
        final MessageContent msgContent = msg.getContent();
        if (shareAction == null) {
            if (ImageMessage.class.isAssignableFrom(msgContent.getClass()) || TextMessage.class.isAssignableFrom(msgContent.getClass())) {
                shareAction = (new MessageItemLongClickAction.Builder()).titleResId(io.rong.imkit.R.string.rc_dialog_item_message_share).actionListener(new MessageItemLongClickAction.MessageItemLongClickListener() {
                    @Override
                    public boolean onMessageItemLongClick(Context context, UIMessage message) {
                        //转发消息
                        if (ImageMessage.class.isAssignableFrom(msgContent.getClass())) {
                            if (((ImageMessage) msgContent).getLocalPath() == null && ((ImageMessage) msgContent).getRemoteUri() == null) {
                                ToastUtils.shortToast(mContext, "图片已被清理");
                                return true;
                            }
                        }
                        context.startActivity(new Intent(context, MsgShareActivity.class).putExtra("msg", msg));
                        return true;
                    }
                }).build();
                RongMessageItemLongClickActionManager.getInstance().addMessageItemLongClickAction(shareAction, 0);
            }
        } else {
            if (!ImageMessage.class.isAssignableFrom(msgContent.getClass()) && !TextMessage.class.isAssignableFrom(msgContent.getClass())) {
                RongMessageItemLongClickActionManager.getInstance().removeMessageItemLongClickAction(shareAction);
            }
        }
        if (imgAction == null) {
            if (ImageMessage.class.isAssignableFrom(msgContent.getClass())) {
                imgAction = (new MessageItemLongClickAction.Builder()).titleResId(io.rong.imkit.R.string.rc_dialog_item_message_edit).actionListener(new MessageItemLongClickAction.MessageItemLongClickListener() {
                    @Override
                    public boolean onMessageItemLongClick(Context context, UIMessage message) {
                        //编辑图片
                        if (((ImageMessage) msgContent).getLocalPath() == null && ((ImageMessage) msgContent).getRemoteUri() == null) {
                            ToastUtils.shortToast(mContext, "图片已被清理");
                        } else {
                            context.startActivity(new Intent(context, IMGEditActivity.class).putExtra("msg", msg));
                        }
                        return true;
                    }
                }).build();
                RongMessageItemLongClickActionManager.getInstance().addMessageItemLongClickAction(imgAction, 1);
            }
        } else {
            if (!ImageMessage.class.isAssignableFrom(msgContent.getClass())) {
                RongMessageItemLongClickActionManager.getInstance().removeMessageItemLongClickAction(imgAction);
            }
        }
    }
}
