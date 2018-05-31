package com.smartstudy.counselor_t.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.SplashInfo;
import com.smartstudy.counselor_t.handler.WeakHandler;
import com.smartstudy.counselor_t.mvp.contract.SplashContract;
import com.smartstudy.counselor_t.mvp.presenter.SplashPresenter;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.util.DensityUtils;
import com.smartstudy.counselor_t.util.DisplayImageUtils;
import com.smartstudy.counselor_t.util.ParameterUtils;
import com.smartstudy.counselor_t.util.SPCacheUtils;
import com.smartstudy.counselor_t.util.ScreenUtils;


/**
 * @author louis
 * @desc 启动页
 * @time created at 17/3/30 下午4:50
 * @company www.smartstudy.com
 */
public class SplashActivity extends BaseActivity<SplashContract.Presenter> implements SplashContract.View {

    private TextView tv_go;

    private WeakHandler mHandler;
    private boolean needGo = false;
    private boolean isFirst = true;
    private boolean hasGo = false;
    private CountDownTimer countDownTimer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 首页禁用滑动返回
        setSlideable(false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setHeadVisible(View.GONE);
    }

    @Override
    protected void onDestroy() {
        releaseRes();
        super.onDestroy();
    }

    private void releaseRes() {
        if (mHandler != null) {
            mHandler = null;
        }
        if (countDownTimer != null) {
            countDownTimer.onFinish();
            countDownTimer = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hasBasePer) {
            presenter.getAdInfo();
        }
        if (isFirst) {
            isFirst = false;
            String adStr = (String) SPCacheUtils.get("adInfo", "");
            if (!TextUtils.isEmpty(adStr)) {
                //有广告时延迟时间增加
                mHandler.sendEmptyMessageDelayed(ParameterUtils.EMPTY_WHAT, 4000);
            } else {
                mHandler.sendEmptyMessageDelayed(ParameterUtils.EMPTY_WHAT, 2500);
            }
        } else {
            if (needGo && hasBasePer) {
                goIndex();
            }
        }
    }

    @Override
    public void initEvent() {
        mHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case ParameterUtils.EMPTY_WHAT:
                        needGo = true;
                        if (hasBasePer) {
                            if (!hasGo) {
                                goIndex();
                            }
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public SplashContract.Presenter initPresenter() {
        return new SplashPresenter(this);
    }

    @Override
    public void initView() {
        String adStr = (String) SPCacheUtils.get("adInfo", "");
        if (!TextUtils.isEmpty(adStr)) {
            SplashInfo info = JSON.parseObject(adStr, SplashInfo.class);
            ((ViewStub) findViewById(R.id.vs_adv)).inflate();
            ImageView ivAdv = findViewById(R.id.iv_adv);
            int screenWidth = ScreenUtils.getScreenWidth();
            int realWidth = screenWidth;
            if ("BEST_TEACHER".equals(info.getType())) {
                handTeacherUI(realWidth, screenWidth, ivAdv, info);
            }
            tv_go = findViewById(R.id.tv_go);
            findViewById(R.id.iv_splash).setVisibility(View.GONE);
            initAdvEvent();
            startTimer();
        }
        /**在应用的入口activity加入以下代码，解决首次安装应用，点击应用图标打开应用，点击home健回到桌面，再次点击应用图标，进入应用时多次初始化SplashActivity的问题*/
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        if (!isTaskRoot()) {
            finish();
            return;
        }
    }

    private void initAdvEvent() {
        tv_go.setOnClickListener(this);
    }

    private void handTeacherUI(int realWidth, int screenWidth, ImageView ivAdv, SplashInfo info) {
        // 教师图片实际宽度
        realWidth = screenWidth - 2 * DensityUtils.dip2px(12f);
        int padding = DensityUtils.dip2px(12f);
        ivAdv.setPadding(padding, padding, padding, 0);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ivAdv.getLayoutParams();
        params.width = screenWidth;
        params.height = (realWidth * 3) / 2;
        ivAdv.setLayoutParams(params);
        DisplayImageUtils.formatImgUrlNoHolder(this, info.getData().getPhotoUrl(), ivAdv);
        View viewTop = findViewById(R.id.view_top);
        LinearLayout.LayoutParams toplayout = (LinearLayout.LayoutParams) viewTop.getLayoutParams();
        toplayout.height = params.height;
        viewTop.setLayoutParams(toplayout);
        viewTop.setBackgroundColor(Color.parseColor(info.getData().getBackgroundGradientTop()));
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
            new int[]{Color.parseColor(info.getData().getBackgroundGradientBottom()),
                Color.parseColor(info.getData().getBackgroundGradientTop())});
        findViewById(R.id.view_bottom).setBackgroundDrawable(gradientDrawable);
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(info.getData().getTitle());
        tvTitle.setTextColor(Color.parseColor(info.getData().getTitleColor()));
        tvTitle.setVisibility(View.VISIBLE);
        TextView tvEgTitle = findViewById(R.id.tv_eg_title);
        tvEgTitle.setText(info.getData().getSubtitle());
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/splash_en.ttf");
        // 应用字体
        tvEgTitle.setTypeface(typeFace);
        tvEgTitle.setTextColor(Color.parseColor(info.getData().getSubtitleColor()));
        tvEgTitle.setAlpha(info.getData().getSubtitleAlpha());
        tvEgTitle.setVisibility(View.VISIBLE);
        TextView tvName = findViewById(R.id.tv_name);
        tvName.setText(info.getData().getName());
        tvName.setVisibility(View.VISIBLE);
        final LinearLayout llBottom = findViewById(R.id.ll_bottom);
        final FrameLayout flBottom = findViewById(R.id.fl_bottom);
        final LinearLayout.LayoutParams flBtmParams = (LinearLayout.LayoutParams) flBottom.getLayoutParams();
        ViewTreeObserver vto = flBottom.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                int gag = llBottom.getHeight() - flBottom.getHeight();
                int botgGap = gag * 2 / 3;
                int topGGap = gag - botgGap;
                flBtmParams.bottomMargin = DensityUtils.px2dip(botgGap);
                flBtmParams.topMargin = DensityUtils.px2dip(topGGap);
                flBottom.setLayoutParams(flBtmParams);
                ViewTreeObserver obs = flBottom.getViewTreeObserver();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    obs.removeOnGlobalLayoutListener(this);
                } else {
                    obs.removeGlobalOnLayoutListener(this);
                }
            }
        });
        findViewById(R.id.ll_teacher_bg).setVisibility(View.VISIBLE);
    }

    //跳转到首页
    private void goIndex() {
        String ticket = (String) SPCacheUtils.get("ticket", "");
        Intent it;
        if (!TextUtils.isEmpty(ticket)) {
            it = new Intent(SplashActivity.this, MyQaActivity.class);
        } else {
            it = new Intent(SplashActivity.this, LoginActivity.class);
            it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            it.putExtra("toMain", true);
        }
        //执行
        startActivity(it);
        finish();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_go:
                hasGo = true;
                //跳过
                goIndex();
                break;
            default:
                break;
        }
    }

    @Override
    public void hasRequestPermission() {
        //权限授予成功
        if (needGo) {
            goIndex();
        }
    }

    @Override
    public void onBackPressed() {
        if (mHandler != null) {
            mHandler.removeMessages(ParameterUtils.EMPTY_WHAT);
            mHandler = null;
        }
        super.onBackPressed();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    protected void startTimer() {
        countDownTimer = new CountDownTimer(3500, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                tv_go.setText(String.format(getString(R.string.ad_loop), millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                tv_go.setText(String.format(getString(R.string.ad_loop), 0));
            }
        };
        countDownTimer.start();
    }

    @Override
    public void getAdSuccess(String result) {
        if (TextUtils.isEmpty(result)) {
            SPCacheUtils.put("adInfo", "");
        } else {
            SPCacheUtils.put("adInfo", result);
        }
    }
}
