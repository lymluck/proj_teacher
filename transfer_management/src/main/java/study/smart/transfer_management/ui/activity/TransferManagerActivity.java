package study.smart.transfer_management.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.ui.base.BaseActivity;
import study.smart.transfer_management.R;
import study.smart.transfer_management.ui.fragment.TransferManagerFragment;

/**
 * @author yqy
 * @date on 2018/7/27
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TransferManagerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_manager);
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        setHeadVisible(View.GONE);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        TransferManagerFragment transferManagerFragment = new TransferManagerFragment();
        transferManagerFragment.setUserVisibleHint(true);
        transaction.replace(R.id.flyt_transfer, transferManagerFragment);
        transaction.commit();
    }
}
