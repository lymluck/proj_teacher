package com.smartstudy.counselor_t.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.smartstudy.annotation.Route;
import study.smart.baselib.listener.OnSendMsgDialogClickListener;
import study.smart.baselib.ui.base.BaseActivity;
import study.smart.baselib.utils.DisplayImageUtils;
import study.smart.baselib.utils.ParameterUtils;
import com.smartstudy.counselor_t.R;
import study.smart.baselib.entity.ChatUserInfo;
import com.smartstudy.counselor_t.handler.WeakHandler;
import com.smartstudy.counselor_t.mvp.contract.MsgShareContract;
import com.smartstudy.counselor_t.mvp.presenter.MsgSharePresenter;
import study.smart.baselib.ui.adapter.CommonAdapter;
import study.smart.baselib.ui.adapter.MultiItemTypeAdapter;
import study.smart.baselib.ui.adapter.base.ViewHolder;
import study.smart.baselib.ui.adapter.wrapper.HeaderAndFooterWrapper;
import study.smart.baselib.ui.widget.dialog.DialogCreator;
import study.smart.transfer_management.ui.activity.CommonSearchActivity;

import java.util.ArrayList;
import java.util.List;

@Route("MsgShareActivity")
public class MsgShareActivity extends BaseActivity<MsgShareContract.Presenter> implements MsgShareContract.View {

    private HeaderAndFooterWrapper<ChatUserInfo> mHeaderAdapter;
    private RecyclerView rclv_recent;
    private CommonAdapter<ChatUserInfo> chatUserAdapter;
    private View searchView;

    private List<ChatUserInfo> chatUserInfoList;
    private WeakHandler mHandler;

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
                final String msgType = getIntent().getStringExtra("type");
                if (chatUserInfo != null) {
                    if ("text".equals(msgType)) {
                        final String content = getIntent().getStringExtra("content");
                        DialogCreator.createSendTextMsgDialog(MsgShareActivity.this, chatUserInfo.getAvatar(), chatUserInfo.getName(), content, new OnSendMsgDialogClickListener() {
                            @Override
                            public void onPositive(String word) {
                                //转发内容
                                setResult(RESULT_OK, new Intent()
                                        .putExtra("targetId", chatUserInfo.getId())
                                        .putExtra("type", msgType)
                                        .putExtra("content", content)
                                        .putExtra("word", word));
                                finish();
                            }

                            @Override
                            public void onNegative() {
                            }
                        });
                    } else if ("image".equals(msgType)) {
                        final Uri uri = getIntent().getParcelableExtra("uri");
                        DialogCreator.createSendImgMsgDialog(MsgShareActivity.this, chatUserInfo.getAvatar(), chatUserInfo.getName(),
                                uri.toString(), new OnSendMsgDialogClickListener() {
                                    @Override
                                    public void onPositive(String word) {
                                        //转发内容
                                        Intent data = new Intent();
                                        String path = getIntent().getStringExtra("path");
                                        if (!TextUtils.isEmpty(path)) {
                                            data.putExtra("path", path);
                                        }
                                        data.putExtra("targetId", chatUserInfo.getId());
                                        data.putExtra("type", msgType);
                                        data.putExtra("uri", uri);
                                        data.putExtra("word", word);
                                        setResult(RESULT_OK, data);
                                        finish();
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

}
