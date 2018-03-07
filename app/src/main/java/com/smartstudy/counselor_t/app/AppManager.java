package com.smartstudy.counselor_t.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.manager.StudentInfoManager;
import com.smartstudy.counselor_t.ui.activity.MsgShareActivity;
import com.smartstudy.counselor_t.ui.activity.MyInfoActivity;
import com.smartstudy.counselor_t.ui.activity.ReloginActivity;
import com.smartstudy.counselor_t.ui.activity.StudentInfoActivity;
import com.smartstudy.counselor_t.util.BitmapUtils;
import com.smartstudy.counselor_t.util.DisplayImageUtils;
import com.smartstudy.counselor_t.util.SDCardUtils;
import com.smartstudy.counselor_t.util.SPCacheUtils;
import com.smartstudy.counselor_t.util.ToastUtils;

import java.io.File;
import java.util.List;

import io.rong.imageloader.core.ImageLoader;
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
import io.rong.message.InformationNotificationMessage;
import io.rong.message.TextMessage;
import me.kareluo.imaging.IMGEditActivity;

/**
 * @author louis
 * @date on 2018/2/24
 * @describe Application管理工具类
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */

public class AppManager implements RongIMClient.ConnectionStatusListener, RongIM.ConversationClickListener,
        RongIMClient.OnReceiveMessageListener {

    private static AppManager mInstance;
    //application context
    private Context mContext;

    public AppManager(Context mContext) {
        this.mContext = mContext;
        initListener();
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String s) {
                if (!TextUtils.isEmpty(s)) {
                    UserInfo info = RongUserInfoManager.getInstance().getUserInfo(s);
                    if (info == null) {
                        StudentInfoManager.getInstance().getStudentInfo(s);
                    }
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
        RongIM.setOnReceiveMessageListener(this);
    }

    @Override
    public void onChanged(ConnectionStatus connectionStatus) {
        if (ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT.equals(connectionStatus)) {
            RongIM.getInstance().logout();
            mContext.startActivity(new Intent(mContext, ReloginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        if (ConnectionStatus.CONN_USER_BLOCKED.equals(connectionStatus)) {
            RongIM.getInstance().logout();
            mContext.startActivity(new Intent(mContext, ReloginActivity.class)
                    .putExtra("content", mContext.getString(R.string.blocked_msg))
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo, String s) {
        String myId = (String) SPCacheUtils.get("imUserId", "");
        String userId = userInfo.getUserId();
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (userId.equals(myId)) {
//            intent.setClass(mContext, MyInfoActivity.class);
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
        final MessageContent msgContent = clickMsg.getContent();
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
                        context.startActivity(new Intent(context, MsgShareActivity.class).putExtra("msg", message.getMessage()));
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
                    public boolean onMessageItemLongClick(Context context, final UIMessage message) {
                        //编辑图片
                        ImageMessage msg = (ImageMessage) message.getContent();
                        if (msg.getLocalPath() == null && msg.getRemoteUri() == null) {
                            ToastUtils.shortToast(mContext, "图片已被清理");
                        } else {
                            if (msg.getLocalPath() != null) {
                                if (msg.getLocalPath().toString().startsWith("file")) {
                                    context.startActivity(new Intent(context, IMGEditActivity.class)
                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            .putExtra("msg", message.getMessage())
                                            .putExtra("uri", msg.getLocalPath()));
                                } else {
                                    File file = ImageLoader.getInstance().getDiskCache().get(msg.getLocalPath().toString());
                                    if (file != null && file.exists()) {
                                        Uri uri = Uri.parse("file://" + file.getAbsolutePath());
                                        context.startActivity(new Intent(context, IMGEditActivity.class)
                                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                .putExtra("msg", message.getMessage())
                                                .putExtra("uri", uri));
                                    } else {
                                        DisplayImageUtils.displayImage(mContext, msg.getLocalPath().toString(), new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                String savePath = SDCardUtils.getFileDirPath("Xxd_im" + File.separator + "pictures").getAbsolutePath();
                                                String fileName = "im_" + System.currentTimeMillis() + ".png";
                                                //临时存储文件
                                                if (BitmapUtils.saveBitmap(resource, fileName, savePath)) {
                                                    Uri uri = Uri.parse("file://" + savePath + File.separator + fileName);
                                                    mContext.startActivity(new Intent(mContext, IMGEditActivity.class)
                                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                            .putExtra("msg", message.getMessage())
                                                            .putExtra("uri", uri)
                                                            .putExtra("path", savePath + File.separator + fileName));
                                                } else {
                                                    ToastUtils.shortToast(mContext, "获取图片失败！");
                                                }
                                            }
                                        });
                                    }
                                }
                            } else {
                                if (msg.getRemoteUri() != null) {
                                    File file = ImageLoader.getInstance().getDiskCache().get(msg.getRemoteUri().toString());
                                    if (file != null && file.exists()) {
                                        Uri uri = Uri.parse("file://" + file.getAbsolutePath());
                                        context.startActivity(new Intent(context, IMGEditActivity.class)
                                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                .putExtra("msg", message.getMessage())
                                                .putExtra("uri", uri));
                                    } else {
                                        DisplayImageUtils.displayImage(mContext, msg.getRemoteUri().toString(), new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                String savePath = SDCardUtils.getFileDirPath("Xxd_im" + File.separator + "pictures").getAbsolutePath();
                                                String fileName = "im_" + System.currentTimeMillis() + ".png";
                                                //临时存储文件
                                                if (BitmapUtils.saveBitmap(resource, fileName, savePath)) {
                                                    Uri uri = Uri.parse("file://" + savePath + File.separator + fileName);
                                                    mContext.startActivity(new Intent(mContext, IMGEditActivity.class)
                                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                            .putExtra("msg", message.getMessage())
                                                            .putExtra("uri", uri)
                                                            .putExtra("path", savePath + File.separator + fileName));
                                                } else {
                                                    ToastUtils.shortToast(mContext, "获取图片失败！");
                                                }
                                            }
                                        });
                                    }
                                }
                            }
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

    @Override
    public boolean onReceived(Message message, int i) {
        if (InformationNotificationMessage.class.isAssignableFrom(message.getContent().getClass())) {
            RongIM.getInstance().deleteMessages(new int[]{message.getMessageId()}, null);
        }
        return false;
    }
}
