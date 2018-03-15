package com.smartstudy.counselor_t.ui;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.TeacherInfo;
import com.smartstudy.counselor_t.mvp.contract.MainActivityContract;
import com.smartstudy.counselor_t.mvp.presenter.MainActivityPresenter;
import com.smartstudy.counselor_t.ui.activity.FillPersonActivity;
import com.smartstudy.counselor_t.ui.activity.LoginActivity;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.ui.fragment.MyFragment;
import com.smartstudy.counselor_t.ui.fragment.QaFragment;
import com.smartstudy.counselor_t.ui.widget.DragPointView;
import com.smartstudy.counselor_t.util.ConstantUtils;
import com.smartstudy.counselor_t.util.IMUtils;
import com.smartstudy.counselor_t.util.ParameterUtils;
import com.smartstudy.counselor_t.util.SPCacheUtils;
import com.smartstudy.counselor_t.util.ToastUtils;
import com.smartstudy.counselor_t.util.Utils;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

public class MainActivity extends BaseActivity<MainActivityContract.Presenter> implements DragPointView.OnDragListencer,
        MainActivityContract.View, ViewPager.OnPageChangeListener {
    private FragmentManager mfragmentManager;
    private ConversationListFragment mConversationListFragment = null;
    public static ViewPager mViewPager;
    private List<Fragment> mFragment = new ArrayList<>();
    private ImageView xxdChatImage, xxdQaImage, xxdMeImage;
    private TextView tvXxdChat, tvXxdQa, tvXxdMe;

    private RelativeLayout chatRLayout;
    private RelativeLayout answerRLayout;
    private RelativeLayout meRLayout;
    long firstClick = 0;
    long secondClick = 0;
    private DragPointView mUnreadNumView;
    private Conversation.ConversationType[] mConversationsTypes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTopLineVisibility(View.VISIBLE);
    }

    @Override
    public MainActivityContract.Presenter initPresenter() {
        return new MainActivityPresenter(this);
    }

    @Override
    public void initView() {
        Fragment conversationList = initConversationList();
        chatRLayout = (RelativeLayout) findViewById(R.id.xxd_chat);
        answerRLayout = (RelativeLayout) findViewById(R.id.xxd_answer_list);
        meRLayout = (RelativeLayout) findViewById(R.id.xxd_me);
        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);
        xxdChatImage = findViewById(R.id.tab_img_chats);
        tvXxdChat = findViewById(R.id.tab_text_chats);
        xxdQaImage = findViewById(R.id.tab_img_qa);
        tvXxdQa = findViewById(R.id.tab_text_qa);
        xxdMeImage = findViewById(R.id.tab_img_me);
        tvXxdMe = findViewById(R.id.tab_text_me);
        mUnreadNumView = findViewById(R.id.xxd_num);
        mFragment.add(conversationList);
        mFragment.add(new QaFragment());
        mFragment.add(new MyFragment());
        changeTextViewColor();
        changeSelectedTabState(0);
        String ticket = (String) SPCacheUtils.get("ticket", ConstantUtils.CACHE_NULL);
        if (!TextUtils.isEmpty(ticket) && ConstantUtils.CACHE_NULL.equals(ticket)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            presenter.getAuditResult();
        }

        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }
        };
        mViewPager.setAdapter(fragmentPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setOnPageChangeListener(this);

        RongIM.getInstance().addUnReadMessageCountChangedObserver(new IUnReadMessageObserver() {
            @Override
            public void onCountChanged(int i) {
                if (mViewPager.getCurrentItem() == 0) {
                    if (i > 0) {
                        setTitle(String.format(getString(R.string.msg_unread), i + ""));
                    } else {
                        setTitle(getString(R.string.msg_name));
                    }

                    if (i == 0) {
                        mUnreadNumView.setVisibility(View.GONE);
                    } else if (i > 0 && i < 100) {
                        mUnreadNumView.setVisibility(View.VISIBLE);
                        mUnreadNumView.setText(String.valueOf(i));
                    } else {
                        mUnreadNumView.setVisibility(View.VISIBLE);
                        mUnreadNumView.setText("...");
                    }
                }
            }
        }, Conversation.ConversationType.PRIVATE);
    }


    @Override
    public void initEvent() {
        chatRLayout.setOnClickListener(this);
        answerRLayout.setOnClickListener(this);
        meRLayout.setOnClickListener(this);
        topdefaultLefttext.setOnClickListener(this);
        mUnreadNumView.setOnClickListener(this);
        mUnreadNumView.setDragListencer(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.xxd_chat:
                if (mViewPager.getCurrentItem() == 0) {
                    if (firstClick == 0) {
                        firstClick = System.currentTimeMillis();
                    } else {
                        secondClick = System.currentTimeMillis();
                    }
                    if (secondClick - firstClick > 0 && secondClick - firstClick <= 800) {
                        mConversationListFragment.focusUnreadItem();
                        firstClick = 0;
                        secondClick = 0;
                    } else if (firstClick != 0 && secondClick != 0) {
                        firstClick = 0;
                        secondClick = 0;
                    }
                }
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.xxd_answer_list:
                mViewPager.setCurrentItem(1, false);
                break;
            case R.id.xxd_me:
                mViewPager.setCurrentItem(2, false);
                break;

            case R.id.topdefault_lefttext:
                presenter.getLogOut();
                break;
            default:
                break;

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        imConnect();
    }

    private void imConnect() {
        if (!RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
            if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
                //登录融云IM
                String cacheToken = (String) SPCacheUtils.get("imToken", "");
                if (!TextUtils.isEmpty(cacheToken)) {
                    RongIM.connect(cacheToken, IMUtils.getConnectCallback());
                }
            }
            if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.TOKEN_INCORRECT)) {
                IMUtils.reGetToken();
            }
        }
    }

    private void unReadMessage() {
        RongIM.getInstance().getTotalUnreadCount(new RongIMClient.ResultCallback<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                setTitle(String.format(getString(R.string.msg_unread), integer + ""));
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        });
    }

    private ConversationListFragment initConversationList() {
        if (mConversationListFragment == null) {
            ConversationListFragment listFragment = new ConversationListFragment();
            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")
                    .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")
                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")
                    .build();
            mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
                    Conversation.ConversationType.GROUP,
                    Conversation.ConversationType.DISCUSSION,
                    Conversation.ConversationType.SYSTEM
            };
            listFragment.setUri(uri);
            mConversationListFragment = listFragment;
            return listFragment;

        } else {
            return mConversationListFragment;
        }
    }


    @Override
    public void getAuditResult(TeacherInfo teacherInfo) {
        if (teacherInfo != null) {
            if (teacherInfo.getStatus() != 2) {
                startActivity(new Intent(this, FillPersonActivity.class));
                finish();
            }
        }
    }

    private void changeTextViewColor() {
        xxdChatImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_message_gray));
        xxdQaImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_qa_gray));
        xxdMeImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_me_gray));
        tvXxdChat.setTextColor(Color.parseColor("#949BA1"));
        tvXxdQa.setTextColor(Color.parseColor("#949BA1"));
        tvXxdMe.setTextColor(Color.parseColor("#949BA1"));
    }

    private void changeSelectedTabState(int position) {
        setMainTitle(position);
        switch (position) {
            case 0:
                unReadMessage();
                tvXxdChat.setTextColor(Color.parseColor("#078CF1"));
                xxdChatImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_message_blue));
                break;
            case 1:
                tvXxdQa.setTextColor(Color.parseColor("#078CF1"));
                xxdQaImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_qa_blue));
                break;
            case 2:
                tvXxdMe.setTextColor(Color.parseColor("#078CF1"));
                xxdMeImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_me_blue));
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        changeTextViewColor();
        changeSelectedTabState(position);
        setMainTitle(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setMainTitle(int position) {
        if (position == 0) {
            unReadMessage();
            setLeftImgVisible(View.GONE);
            setTopdefaultLefttextVisible(View.GONE);
            setTitle(getString(R.string.msg_name));
        } else if (position == 1) {
            setLeftImgVisible(View.GONE);
            setTopdefaultLefttextVisible(View.GONE);
            setTitle("答疑");
        } else {
            setLeftTxt("注销");
            setTopdefaultLefttextVisible(View.VISIBLE);
            setLeftImgVisible(View.GONE);
            setTopdefaultLefttextColor("#949BA1");
            setTitle("个人信息");
        }
    }

    @Override
    public void getLogOutSuccess() {
        RongIM.getInstance().logout();
        SPCacheUtils.put("phone", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("name", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("avatar", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("ticket", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("orgId", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("title", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("imToken", "");
        SPCacheUtils.put("imUserId", "");
        Utils.removeCookie(this);
        Intent to_login = new Intent(this, LoginActivity.class);
        to_login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(to_login);
    }


    @Override
    public void onDragOut() {
        mUnreadNumView.setVisibility(View.GONE);
        ToastUtils.shortToast(this, "清除成功");
        RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                if (conversations != null && conversations.size() > 0) {
                    for (Conversation c : conversations) {
                        RongIM.getInstance().clearMessagesUnreadStatus(c.getConversationType(), c.getTargetId(), null);
                    }
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode e) {

            }
        }, mConversationsTypes);
    }
}
