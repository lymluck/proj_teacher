package com.smartstudy.counselor_t.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.smartstudy.annotation.Route;
import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.handler.WeakHandler;
import com.smartstudy.counselor_t.mvp.contract.MainActivityContract;
import com.smartstudy.counselor_t.mvp.presenter.MainActivityPresenter;
import com.smartstudy.counselor_t.service.VersionUpdateService;
import com.smartstudy.counselor_t.ui.activity.FillPersonActivity;
import com.smartstudy.counselor_t.ui.fragment.MyAllQaFragment;
import com.smartstudy.counselor_t.ui.fragment.MyInfoDetailFragment;
import com.smartstudy.counselor_t.ui.fragment.QaFragment;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;
import study.smart.baselib.BaseApplication;
import study.smart.baselib.entity.Privileges;
import study.smart.baselib.entity.TeacherInfo;
import study.smart.baselib.listener.OnProgressListener;
import study.smart.baselib.ui.activity.LoginActivity;
import study.smart.baselib.ui.base.BaseActivity;
import study.smart.baselib.ui.widget.NoScrollViewPager;
import study.smart.baselib.ui.widget.dialog.AppBasicDialog;
import study.smart.baselib.ui.widget.dialog.DialogCreator;
import study.smart.baselib.utils.ConstantUtils;
import study.smart.baselib.utils.ParameterUtils;
import study.smart.baselib.utils.SPCacheUtils;
import study.smart.baselib.utils.ScreenUtils;
import study.smart.baselib.utils.Utils;
import study.smart.transfer_management.ui.fragment.TransferManagerFragment;

@Route("MainActivity")
public class MainActivity extends BaseActivity<MainActivityContract.Presenter> implements MainActivityContract.View, ViewPager.OnPageChangeListener {
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
    public static NoScrollViewPager mViewPager;
    private List<Fragment> mFragment = new ArrayList<>();
    private ImageView xxdChatImage, xxdQaImage, xxdMeImage;
    private TextView tvXxdChat, tvXxdQa, tvXxdMe;

    private RelativeLayout chatRLayout;
    private RelativeLayout answerRLayout;
    private RelativeLayout meRLayout;
    private String mLastVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSlideable(false);
        setContentView(R.layout.activity_main);
        setHeadVisible(View.GONE);
    }

    @Override
    public MainActivityContract.Presenter initPresenter() {
        return new MainActivityPresenter(this);
    }

    @Override
    public void initView() {
        chatRLayout = findViewById(R.id.xxd_chat);
        //权限判断，转案管理是否显示
        String transferPerssion = (String) SPCacheUtils.get("privileges", "");
        if (TextUtils.isEmpty(transferPerssion)) {
            //没有权限，不加载转案模块
            chatRLayout.setVisibility(View.GONE);
            mFragment.add(new QaFragment());
            mFragment.add(new QaFragment());
        } else {
            //判断是否有权限
            Privileges privileges = JSONObject.parseObject(transferPerssion, Privileges.class);
            if (privileges.isTransferCase()) {
                chatRLayout.setVisibility(View.VISIBLE);
                mFragment.add(new TransferManagerFragment());
                mFragment.add(new MyAllQaFragment());
                mFragment.add(new MyInfoDetailFragment());
            } else {
                chatRLayout.setVisibility(View.GONE);
                mFragment.add(new MyAllQaFragment());
                mFragment.add(new MyInfoDetailFragment());
            }
        }
        answerRLayout = findViewById(R.id.xxd_answer_list);
        meRLayout = findViewById(R.id.xxd_me);
        mViewPager = findViewById(R.id.main_viewpager);
        mViewPager.setNoScroll(true);
        xxdChatImage = findViewById(R.id.tab_img_chats);
        tvXxdChat = findViewById(R.id.tab_text_chats);
        xxdQaImage = findViewById(R.id.tab_img_qa);
        tvXxdQa = findViewById(R.id.tab_text_qa);
        xxdMeImage = findViewById(R.id.tab_img_me);
        tvXxdMe = findViewById(R.id.tab_text_me);
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
        mViewPager.setOffscreenPageLimit(mFragment.size());
        mViewPager.setOnPageChangeListener(this);
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
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.xxd_chat:
                mViewPager.setCurrentItem(0, false);
                setHeadVisible(View.GONE);
                break;
            case R.id.xxd_answer_list:
                mViewPager.setCurrentItem(mFragment.size() - 2, false);
                setHeadVisible(View.GONE);
                break;
            case R.id.xxd_me:
                mViewPager.setCurrentItem(mFragment.size() - 1, false);
                setHeadVisible(View.VISIBLE);
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
        xxdChatImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.group_smart_gray));
        xxdQaImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_qa_gray));
        xxdMeImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_me_gray));
        tvXxdChat.setTextColor(Color.parseColor("#949BA1"));
        tvXxdQa.setTextColor(Color.parseColor("#949BA1"));
        tvXxdMe.setTextColor(Color.parseColor("#949BA1"));
    }

    private void changeSelectedTabState(int position) {
        if (mFragment.size() == 3) {
            switch (position) {
                case 0:
                    tvXxdChat.setTextColor(Color.parseColor("#078CF1"));
                    xxdChatImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.group_smart_blue));
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
        } else {
            switch (position) {
                case 0:
                    tvXxdQa.setTextColor(Color.parseColor("#078CF1"));
                    xxdQaImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_qa_blue));
                    break;
                case 1:
                    tvXxdMe.setTextColor(Color.parseColor("#078CF1"));
                    xxdMeImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_me_blue));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        changeTextViewColor();
        changeSelectedTabState(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void getLogOutSuccess() {
        RongIM.getInstance().logout();
        SPCacheUtils.put("phone", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("name", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("avatar", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("ticket", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("smart_ticket", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("privileges", ParameterUtils.CACHE_NULL);
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

}
