package com.smartstudy.counselor_t.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.ChatUserInfo;
import com.smartstudy.counselor_t.handler.WeakHandler;
import com.smartstudy.counselor_t.listener.OnSendMsgDialogClickListener;
import com.smartstudy.counselor_t.mvp.contract.MsgShareContract;
import com.smartstudy.counselor_t.mvp.presenter.MsgSharePresenter;
import com.smartstudy.counselor_t.ui.adapter.CommonAdapter;
import com.smartstudy.counselor_t.ui.adapter.MultiItemTypeAdapter;
import com.smartstudy.counselor_t.ui.adapter.base.ViewHolder;
import com.smartstudy.counselor_t.ui.adapter.wrapper.EmptyWrapper;
import com.smartstudy.counselor_t.ui.adapter.wrapper.HeaderAndFooterWrapper;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.ui.dialog.AppBasicDialog;
import com.smartstudy.counselor_t.ui.dialog.DialogCreator;
import com.smartstudy.counselor_t.util.DisplayImageUtils;
import com.smartstudy.counselor_t.util.ParameterUtils;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;

public class MsgShareActivity extends BaseActivity<MsgShareContract.Presenter> implements MsgShareContract.View {

    private HeaderAndFooterWrapper<ChatUserInfo> mHeaderAdapter;
    private RecyclerView rclv_recent;
    private EmptyWrapper<ChatUserInfo> emptyWrapper;
    private CommonAdapter<ChatUserInfo> chatUserAdapter;
    private View searchView;
    private View emptyView;

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
        emptyView = mInflater.inflate(R.layout.layout_empty, rclv_recent, false);
        presenter.showLoading(this, emptyView);
        presenter.getChatUsers();
        sendMsg = getIntent().getParcelableExtra("msg");
        Log.d("msg====", sendMsg.toString());
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
        emptyWrapper = new EmptyWrapper<>(mHeaderAdapter);
        searchView = headView.findViewById(R.id.searchView);
        emptyWrapper = new EmptyWrapper<>(mHeaderAdapter);
        rclv_recent.setAdapter(emptyWrapper);
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
                        final AppBasicDialog dialog = DialogCreator.createSendTextMsgDialog(MsgShareActivity.this, chatUserInfo.getAvatar(), chatUserInfo.getName(), content, new OnSendMsgDialogClickListener() {
                            @Override
                            public void onPositive(String word) {
                                //转发内容
                                sendTextMsg(chatUserInfo.getId(), chatUserInfo.getName(), content);
                                if (!TextUtils.isEmpty(word)) {
                                    //转发留言
                                    sendTextMsg(chatUserInfo.getId(), chatUserInfo.getName(), word);
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
        presenter.setEmptyView(emptyView);
        chatUserInfoList.clear();
        chatUserInfoList.addAll(data);
        chatUserAdapter.notifyDataSetChanged();
        searchView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                searchView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                rclv_recent.scrollBy(0, searchView.getHeight());
            }
        });
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        emptyWrapper.notifyDataSetChanged();
    }

    private void sendTextMsg(final String userId, final String userName, String content) {
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
                RongIM.getInstance().startPrivateChat(MsgShareActivity.this, userId, userName);
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                //消息发送失败的回调
                showTip("消息发送失败！");
            }
        });
    }
}
