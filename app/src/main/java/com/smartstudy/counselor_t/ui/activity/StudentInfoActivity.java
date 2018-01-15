package com.smartstudy.counselor_t.ui.activity;

import com.smartstudy.counselor_t.entity.StudentInfo;
import com.smartstudy.counselor_t.mvp.contract.MainActivityContract;
import com.smartstudy.counselor_t.ui.base.BaseActivity;

/**
 * @author yqy
 * @date on 2018/1/15
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class StudentInfoActivity extends BaseActivity<MainActivityContract.Presenter> implements MainActivityContract.View {
    @Override
    public void getStudentInfoSuccess(String mUserId, StudentInfo studentInfo) {

    }

    @Override
    public MainActivityContract.Presenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {

    }
}
