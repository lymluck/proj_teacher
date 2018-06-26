package study.smart.baselib.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import study.smart.baselib.R;
import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.ui.base.BaseActivity;
import study.smart.baselib.utils.ParameterUtils;
import study.smart.baselib.utils.SPCacheUtils;
import study.smart.baselib.utils.ScreenUtils;

public class ReloginActivity extends BaseActivity<BasePresenter> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 首页禁用滑动返回
        setSlideable(false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relogin);
        setHeadVisible(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        findViewById(R.id.btn_relogin).setOnClickListener(this);
        SPCacheUtils.put("name", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("avatar", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("ticket", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("orgId", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("title", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("imToken", "");
        SPCacheUtils.put("imUserId", "");
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        TextView tv_content = findViewById(R.id.tv_content);
        Intent intent = getIntent();
        if (intent.hasExtra("content")) {
            tv_content.setText(getIntent().getStringExtra("content"));
        }
        setFinishOnTouchOutside(false);
        LinearLayout llyt_dialog = findViewById(R.id.llyt_dialog);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) llyt_dialog.getLayoutParams();
        lp.width = (int) (ScreenUtils.getScreenWidth() * 0.85);
        llyt_dialog.setLayoutParams(lp);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_relogin) {
            Intent to_login = new Intent(this, LoginActivity.class);
            to_login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(to_login);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
    }
}
