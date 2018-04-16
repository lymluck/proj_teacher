package com.smartstudy.counselor_t.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.smartstudy.counselor_t.ui.activity.MyInfoActivity;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.ui.dialog.AppBasicDialog;
import com.smartstudy.counselor_t.ui.dialog.DialogCreator;
import com.smartstudy.counselor_t.util.ConstantUtils;
import com.smartstudy.counselor_t.util.IMUtils;
import com.smartstudy.counselor_t.util.ParameterUtils;
import com.smartstudy.counselor_t.util.SPCacheUtils;
import com.smartstudy.counselor_t.util.ScreenUtils;
import com.smartstudy.counselor_t.util.Utils;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

public class MainActivity extends BaseActivity<MainActivityContract.Presenter> implements MainActivityContract.View {
    private FragmentManager mfragmentManager;
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
    private String mLastVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mfragmentManager = getSupportFragmentManager();
        setTopLineVisibility(View.VISIBLE);
        showChatList(mfragmentManager.beginTransaction());
    }

    @Override
    public MainActivityContract.Presenter initPresenter() {
        return new MainActivityPresenter(this);
    }

    @Override
    public void initView() {
        String ticket = (String) SPCacheUtils.get("ticket", ConstantUtils.CACHE_NULL);
        if (!TextUtils.isEmpty(ticket) && ConstantUtils.CACHE_NULL.equals(ticket)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            presenter.getAuditResult();
        }
        setLeftImgVisible(View.GONE);
        setTitle(getString(R.string.msg_name));
        setRightImgVisible(View.VISIBLE);
        setRightImg(R.drawable.ic_my_info);
        topdefaultRightbutton.setOnClickListener(this);
        RongIM.getInstance().addUnReadMessageCountChangedObserver(new IUnReadMessageObserver() {
            @Override
            public void onCountChanged(int i) {
                if (i > 0) {
                    setTitle(String.format(getString(R.string.msg_unread), i + ""));
                } else {
                    setTitle(getString(R.string.msg_name));
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_rightbutton:
                startActivity(new Intent(this, MyInfoActivity.class));
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
            listFragment.setUri(uri);
            mConversationListFragment = listFragment;
            return listFragment;
        } else {
            return mConversationListFragment;
        }
    }


    public void showChatList(FragmentTransaction ft) {
        /**
         * 如果Fragment为空，就新建一个实例
         * 如果不为空，就将它从栈中显示出来
         */
        if (mConversationListFragment == null) {
            mConversationListFragment = initConversationList();
            ft.add(R.id.conversation, mConversationListFragment);
        } else {
            ft.show(mConversationListFragment);
        }
        mConversationListFragment.setUserVisibleHint(true);
        ft.commitAllowingStateLoss();
        ft = null;
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
                    public void onStart() {
                    }

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
}
