package com.smartstudy.counselor_t.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.app.BaseApplication;
import com.smartstudy.counselor_t.entity.TeacherInfo;
import com.smartstudy.counselor_t.handler.WeakHandler;
import com.smartstudy.counselor_t.listener.OnProgressListener;
import com.smartstudy.counselor_t.mvp.contract.MainActivityContract;
import com.smartstudy.counselor_t.mvp.presenter.MainActivityPresenter;
import com.smartstudy.counselor_t.service.VersionUpdateService;
import com.smartstudy.counselor_t.ui.activity.FillPersonActivity;
import com.smartstudy.counselor_t.ui.activity.LoginActivity;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.ui.dialog.AppBasicDialog;
import com.smartstudy.counselor_t.ui.dialog.DialogCreator;
import com.smartstudy.counselor_t.ui.fragment.MyFragment;
import com.smartstudy.counselor_t.ui.fragment.QaFragment;
import com.smartstudy.counselor_t.ui.widget.DragPointView;
import com.smartstudy.counselor_t.util.ConstantUtils;
import com.smartstudy.counselor_t.util.IMUtils;
import com.smartstudy.counselor_t.util.ParameterUtils;
import com.smartstudy.counselor_t.util.SPCacheUtils;
import com.smartstudy.counselor_t.util.ScreenUtils;
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
    private ConversationListFragment mConversationListFragment = null;
    private int update_type;
    private AppBasicDialog updateDialog;
    private String apk_path;
    private WeakHandler myHandler;
    private boolean isBinded;
    private VersionUpdateService.DownloadBinder binder;
    private Button updateBtn;
    private TextView updateTitle;

    private ProgressBar progressbar;
    private TextView tv_progress;
    private boolean isDestroy = true;
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
    private String mLastVersion;

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
        if (!BaseApplication.getInstance().isDownload()) {
            presenter.checkVersion();
        }
    }

    @Override
    public void initEvent() {
        super.initEvent();
        myHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case ParameterUtils.FLAG_UPDATE:
                        progressbar.setProgress(msg.arg1);
                        tv_progress.setText(msg.arg1 + "%");
                        break;
                    case ParameterUtils.MSG_WHAT_FINISH:
                        progressbar.setProgress(100);
                        tv_progress.setText("100%");
                        updateBtn.setVisibility(View.VISIBLE);
                        updateBtn.setText("安装");
                        updateTitle.setText("立即安装");
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
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
        //继续下载apk
        if (isDestroy && BaseApplication.getInstance().isDownload()) {
            Intent it = new Intent(MainActivity.this, VersionUpdateService.class);
            it.putExtra("version", mLastVersion);
            startService(it);
            bindService(it, conn, Context.BIND_AUTO_CREATE);
        }
        imConnect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isDestroy = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (updateDialog != null) {
            updateDialog.dismiss();
            updateDialog = null;
        }
        if (myHandler != null) {
            myHandler = null;
        }
        if (isBinded) {
            unbindService(conn);
        }
        if (binder != null && binder.isCanceled()) {
            Intent it = new Intent(this, VersionUpdateService.class);
            it.putExtra("version", mLastVersion);
            stopService(it);
            binder = null;
        }
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

    /**
     * 连接版本下载service
     */
    ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBinded = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (VersionUpdateService.DownloadBinder) service;
            // 开始下载
            isBinded = true;
            binder.start(update_type);
            if (update_type == ParameterUtils.FLAG_UPDATE_NOW) {
                updateBtn = updateDialog.findViewById(R.id.dialog_base_confirm_btn);
                updateTitle = updateDialog.findViewById(R.id.dialog_base_title_tv);
                updateBtn.setVisibility(View.GONE);
                updateDialog.findViewById(R.id.dialog_base_text_tv).setVisibility(View.GONE);
                updateDialog.findViewById(R.id.llyt_progress).setVisibility(View.VISIBLE);
                updateTitle.setText("开始更新");
                binder.setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(int progress) {
                        if (progress < 100) {
                            Message msg = Message.obtain();
                            msg.what = ParameterUtils.FLAG_UPDATE;
                            msg.arg1 = progress;
                            myHandler.sendMessage(msg);
                        }
                    }

                    @Override
                    public void onFinish(final String path) {
                        apk_path = path;
                        Message msg = Message.obtain();
                        msg.what = ParameterUtils.MSG_WHAT_FINISH;
                        myHandler.sendMessage(msg);
                    }
                });
            }
        }
    };

    @Override
    public void updateable(final String downUrl, String lastVersion, String des) {
        this.mLastVersion = lastVersion;
        update_type = ParameterUtils.FLAG_UPDATE;
        //提示当前有版本更新
        String isToUpdate = (String) SPCacheUtils.get("isToUpdate", "");
        if ("".equals(isToUpdate) || "yes".equals(isToUpdate)) {
            updateDialog = DialogCreator.createAppBasicDialog(this, getString(R.string.version_update), des,
                getString(R.string.update_vs_now), getString(R.string.not_update), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.positive_btn:
                                BaseApplication.getInstance().setDownload(true);
                                BaseApplication.getInstance().setDownLoadUrl(downUrl);
                                //开始下载
                                Intent it = new Intent(MainActivity.this, VersionUpdateService.class);
                                it.putExtra("version", mLastVersion);
                                startService(it);
                                bindService(it, conn, Context.BIND_AUTO_CREATE);
                                updateDialog.dismiss();
                                break;
                            case R.id.negative_btn:
                                SPCacheUtils.put("isToUpdate", "no");
                                updateDialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                });
            ((TextView) updateDialog.findViewById(R.id.dialog_info)).setGravity(Gravity.CENTER_VERTICAL);
            WindowManager.LayoutParams p = updateDialog.getWindow().getAttributes();
            p.width = (int) (ScreenUtils.getScreenWidth() * 0.85);
            updateDialog.getWindow().setAttributes(p);
            updateDialog.show();
        }
    }

    @Override
    public void forceUpdate(final String downUrl, String lastVersion, String des) {
        this.mLastVersion = lastVersion;
        update_type = ParameterUtils.FLAG_UPDATE_NOW;
        String title = getString(R.string.update_vs_now);
        updateDialog = DialogCreator.createBaseCustomDialog(MainActivity.this, title, des, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (apk_path != null) {
                    Utils.installApk(getApplicationContext(), apk_path);
                } else {
                    BaseApplication.getInstance().setDownload(true);
                    BaseApplication.getInstance().setDownLoadUrl(downUrl);
                    //开始下载
                    Intent it = new Intent(MainActivity.this, VersionUpdateService.class);
                    it.putExtra("version", mLastVersion);
                    startService(it);
                    bindService(it, conn, Context.BIND_AUTO_CREATE);
                }
            }
        });
        progressbar = updateDialog.findViewById(R.id.progressbar);
        tv_progress = updateDialog.findViewById(R.id.tv_progress);
        updateDialog.findViewById(R.id.dialog_base_confirm_btn).setBackgroundResource(R.drawable.bg_btn_wm_lrb_radius);
        ((TextView) updateDialog.findViewById(R.id.dialog_base_text_tv)).setGravity(Gravity.CENTER_VERTICAL);
        WindowManager.LayoutParams p = updateDialog.getWindow().getAttributes();
        p.width = (int) (ScreenUtils.getScreenWidth() * 0.85);
        updateDialog.getWindow().setAttributes(p);
        updateDialog.show();
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

}
