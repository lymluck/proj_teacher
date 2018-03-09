package com.smartstudy.counselor_t.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.ui.base.BaseActivity;

/**
 * @author yqy
 * @date on 2018/3/9
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class ReloadQaActivity extends BaseActivity<BasePresenter> {
    @Override
    public BasePresenter initPresenter() {
        return null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reqa);
        setHeadVisible(View.GONE);
    }

    @Override
    public void initView() {
        setFinishOnTouchOutside(false);
    }
}
