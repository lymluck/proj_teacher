package com.smartstudy.counselor_t.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.smartstudy.annotation.Route;
import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.ChatUserInfo;
import com.smartstudy.counselor_t.handler.WeakHandler;
import com.smartstudy.counselor_t.listener.OnSendMsgDialogClickListener;
import com.smartstudy.counselor_t.mvp.contract.MsgShareContract;
import com.smartstudy.counselor_t.mvp.presenter.MsgSharePresenter;
import com.smartstudy.counselor_t.ui.adapter.CommonAdapter;
import com.smartstudy.counselor_t.ui.adapter.MultiItemTypeAdapter;
import com.smartstudy.counselor_t.ui.adapter.base.ViewHolder;
import com.smartstudy.counselor_t.ui.adapter.wrapper.HeaderAndFooterWrapper;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.ui.dialog.DialogCreator;
import com.smartstudy.counselor_t.util.BitmapUtils;
import com.smartstudy.counselor_t.util.DisplayImageUtils;
import com.smartstudy.counselor_t.util.ParameterUtils;
import com.smartstudy.counselor_t.util.SDCardUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.rong.imageloader.core.ImageLoader;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;

@Route("MsgShareActivity")
public class MsgShareActivity extends BaseActivity<MsgShareContract.Presenter> implements MsgShareContract.View {

    private HeaderAndFooterWrapper<ChatUserInfo> mHeaderAdapter;
    private RecyclerView rclv_recent;
    private CommonAdapter<ChatUserInfo> chatUserAdapter;
    private View searchView;

    private List<ChatUserInfo> chatUserInfoList;
    private WeakHandler mHandler;
    private io.rong.imlib.model.Message sendMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_share);
    }

    @Override
    public MsgShareContract.Presenter initPresenter() {
        return new MsgSharePresenter(this);
    }

    @Override
    public void initView() {
        setTitle("选择");
        rclv_recent = findViewById(R.id.rclv_recent);
        rclv_recent.setHasFixedSize(true);
        rclv_recent.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        presenter.getChatUsers();
        sendMsg = getIntent().getParcelableExtra("msg");
    }

    private void initAdapter() {
        chatUserInfoList = new ArrayList<>();
        chatUserAdapter = new CommonAdapter<ChatUserInfo>(this, R.layout.item_chat_user_list, chatUserInfoList) {
            @Override
            protected void convert(ViewHolder holder, ChatUserInfo chatUserInfo, int position) {
                DisplayImageUtils.displayPersonImage(MsgShareActivity.this, chatUserInfo.getAvatar(), (ImageView) holder.getView(R.id.iv_avatar));
                holder.setText(R.id.tv_name, chatUserInfo.getName());
            }
        };
        mHeaderAdapter = new HeaderAndFooterWrapper<>(chatUserAdapter);
        View headView = mInflater.inflate(R.layout.header_rencent_user, null, false);
        mHeaderAdapter.addHeaderView(headView);
        searchView = headView.findViewById(R.id.searchView);
        rclv_recent.setAdapter(mHeaderAdapter);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        mHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case ParameterUtils.MSG_WHAT_REFRESH:
                        rclv_recent.scrollBy(0, searchView.getHeight());
                        break;

                    default:
                        break;
                }
                return false;
            }
        });
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击跳转搜索
                mHandler.sendEmptyMessageAtTime(ParameterUtils.MSG_WHAT_REFRESH, 600);
                Intent toSearch = new Intent(MsgShareActivity.this, CommonSearchActivity.class);
                toSearch.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.RECENTUSER_FLAG);
                Pair<View, String> searchTop = Pair.create(searchView, "search_top");
                ActivityOptionsCompat compat = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(MsgShareActivity.this, searchTop);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(toSearch, compat.toBundle());
                } else {
                    startActivity(toSearch);
                }
            }
        });
        chatUserAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                //头部view占去一个position
                final ChatUserInfo chatUserInfo = chatUserInfoList.get(position - 1);
                if (chatUserInfo != null) {
                    MessageContent messageContent = sendMsg.getContent();
                    if (TextMessage.class.isAssignableFrom(messageContent.getClass())) {
                        final String content = ((TextMessage) messageContent).getContent();
                        DialogCreator.createSendTextMsgDialog(MsgShareActivity.this, chatUserInfo.getAvatar(), chatUserInfo.getName(), content, new OnSendMsgDialogClickListener() {
                            @Override
                            public void onPositive(String word) {
                                //转发内容
                                sendTextMsg(chatUserInfo.getId(), content);
                                if (!TextUtils.isEmpty(word)) {
                                    //转发留言
                                    sendTextMsg(chatUserInfo.getId(), word);
                                }
                            }

                            @Override
                            public void onNegative() {
                            }
                        });
                    } else if (ImageMessage.class.isAssignableFrom(messageContent.getClass())) {
                        final ImageMessage imageMessage = (ImageMessage) messageContent;
                        DialogCreator.createSendImgMsgDialog(MsgShareActivity.this, chatUserInfo.getAvatar(), chatUserInfo.getName(),
                                imageMessage.getLocalPath() != null ? imageMessage.getLocalPath().toString() : imageMessage.getRemoteUri().toString(), new OnSendMsgDialogClickListener() {
                                    @Override
                                    public void onPositive(String word) {
                                        //转发内容
                                        handleMsg(chatUserInfo.getId(), imageMessage, imageMessage.isFull());
                                        if (!TextUtils.isEmpty(word)) {
                                            //转发留言
                                            sendTextMsg(chatUserInfo.getId(), word);
                                        }
                                    }

                                    @Override
                                    public void onNegative() {
                                    }
                                });
                    }
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    public void showUsers(List<ChatUserInfo> data) {
        chatUserInfoList.clear();
        chatUserInfoList.addAll(data);
        mHeaderAdapter.notifyDataSetChanged();
        searchView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                searchView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                rclv_recent.scrollBy(0, searchView.getHeight());
            }
        });
    }

    private void sendTextMsg(final String userId, String content) {
        TextMessage myTextMessage = TextMessage.obtain(content);
        Message myMessage = Message.obtain(userId, Conversation.ConversationType.PRIVATE, myTextMessage);
        RongIM.getInstance().sendMessage(myMessage, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
                //消息本地数据库存储成功的回调
            }

            @Override
            public void onSuccess(Message message) {
                //消息通过网络发送成功的回调
                showTip("已发送");
                finish();
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                //消息发送失败的回调
                showTip("消息发送失败！");
                finish();
            }
        });
    }

    private void handleMsg(final String userId, final ImageMessage msg, final boolean isFull) {
        if (msg.getLocalPath() != null) {
            if (msg.getLocalPath().toString().startsWith("file")) {
                sendImageMsg(userId, msg.getThumUri(), msg.getLocalPath(), isFull, null);
            } else {
                File file = ImageLoader.getInstance().getDiskCache().get(msg.getLocalPath().toString());
                if (file != null && file.exists()) {
                    Uri uri = Uri.parse("file://" + file.getAbsolutePath());
                    sendImageMsg(userId, msg.getThumUri(), uri, isFull, null);
                } else {
                    DisplayImageUtils.displayImage(this, msg.getLocalPath().toString(), new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            String savePath = SDCardUtils.getFileDirPath("Xxd_im" + File.separator + "pictures").getAbsolutePath();
                            String fileName = "im_" + System.currentTimeMillis() + ".png";
                            if (BitmapUtils.saveBitmap(resource, fileName, savePath)) {
                                Uri uri = Uri.parse("file://" + savePath + File.separator + fileName);
                                sendImageMsg(userId, msg.getThumUri(), uri, isFull, savePath + File.separator + fileName);
                            } else {
                                showTip("消息已发送失败！");
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
                    sendImageMsg(userId, msg.getThumUri(), uri, isFull, null);
                } else {
                    DisplayImageUtils.displayImage(this, msg.getRemoteUri().toString(), new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            String savePath = SDCardUtils.getFileDirPath("Xxd_im" + File.separator + "pictures").getAbsolutePath();
                            String fileName = "im_" + System.currentTimeMillis() + ".png";
                            //临时存储文件
                            if (BitmapUtils.saveBitmap(resource, fileName, savePath)) {
                                Uri uri = Uri.parse("file://" + savePath + File.separator + fileName);
                                sendImageMsg(userId, msg.getThumUri(), uri, isFull, savePath + File.separator + fileName);
                            } else {
                                showTip("消息已发送失败！");
                            }
                        }
                    });
                }
            }
        }
    }

    private void sendImageMsg(String userId, Uri thumUri, Uri localUri, boolean isFull, final String filePath) {
        ImageMessage sendImgMsg = ImageMessage.obtain(thumUri, localUri, isFull);
        RongIM.getInstance().sendImageMessage(Conversation.ConversationType.PRIVATE, userId, sendImgMsg, null, null, new RongIMClient.SendImageMessageCallback() {
            @Override
            public void onAttached(Message message) {

            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                showTip("消息发送失败！");
                finish();
            }

            @Override
            public void onSuccess(Message message) {
                if (filePath != null) {
                    BitmapUtils.deleteFile(filePath);
                }
                showTip("消息已发送");
                finish();
            }

            @Override
            public void onProgress(Message message, int i) {

            }
        });

    }
}
