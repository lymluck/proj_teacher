package study.smart.transfer_management.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.ui.base.BaseActivity;
import study.smart.transfer_management.R;

/**
 * @author yqy
 * @date on 2018/7/16
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class WorkingDetailActivity extends BaseActivity {
    private RecyclerView rvWorking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_working_detail);
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        rvWorking = findViewById(R.id.rv_working);

    }
}
