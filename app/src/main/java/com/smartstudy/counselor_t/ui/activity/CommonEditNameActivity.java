package com.smartstudy.counselor_t.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.TeacherInfo;
import com.smartstudy.counselor_t.mvp.contract.MyInfoContract;
import com.smartstudy.counselor_t.mvp.presenter.MyInfoActivityPresenter;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.util.KeyBoardUtils;
import com.smartstudy.counselor_t.util.ParameterUtils;

public class CommonEditNameActivity extends BaseActivity<MyInfoContract.Presenter> implements MyInfoContract.View {

    private EditText etname;

    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);
    }


    @Override
    public void initView() {
        Intent data = getIntent();
        setRightTxt("保存");
        setTopdefaultRighttextVisible(View.VISIBLE);
        setTitle(data.getStringExtra("title"));
        setLeftImgVisible(View.VISIBLE);
        this.etname = (EditText) findViewById(R.id.et_name);
        etname.setText(data.getStringExtra("value"));
        etname.requestFocus();
        KeyBoardUtils.openKeybord(etname, this);
        flag = data.getStringExtra(ParameterUtils.TRANSITION_FLAG);
//        new EditMyInfoPresenter(this);
    }

    @Override
    public void initEvent() {
        topdefaultLeftbutton.setOnClickListener(this);
        topdefaultRighttext.setOnClickListener(this);
    }

    @Override
    public MyInfoContract.Presenter initPresenter() {
        return new MyInfoActivityPresenter(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                KeyBoardUtils.closeKeybord(etname, this);
                finish();
                break;
            case R.id.topdefault_righttext:
                if (flag.equals(ParameterUtils.EDIT_NAME)) {
//                    presenter.updateMyInfo(etname,null,);
                }
//                PersonalParamsInfo paramsModel = new PersonalParamsInfo();
//                String content = etname.getText().toString().replaceAll(" ", "");
//                if (TextUtils.isEmpty(content)) {
//                    ToastUtils.showToast(this, "输入的内容不能为空！");
//                    return;
//                }
//                if (ParameterUtils.EDIT_NAME.equals(flag)) {
//                    paramsModel.setName(content);
//                } else if (ParameterUtils.EDIT_GZ.equals(flag) || ParameterUtils.EDIT_BK.equals(flag) || ParameterUtils.EDIT_CZ.equals(flag)) {
//                    paramsModel.setCurrentSchool(content);
//                }
//                editP.editMyInfo(paramsModel);
                break;
        }
    }


//    @Override
//    public void editMyInfoSuccess(String jsonObject) {
//        Intent data = new Intent();
//        if (ParameterUtils.EDIT_NAME.equals(flag)) {
//            //更新缓存
//            SPCacheUtils.put("user_name", etname.getText().toString());
//        }
//        data.putExtra(ParameterUtils.TRANSITION_FLAG, flag);
//        data.putExtra("new_value", etname.getText().toString());
//        //保存成功后销毁页面隐藏软键盘
//        KeyBoardUtils.closeKeybord(etname, this);
//        setResult(RESULT_OK, data);
//        finish();
//    }

    @Override
    public void getMyInfoSuccess(TeacherInfo teacherInfo) {

    }

    @Override
    public void updateMyInfoSuccesee() {

    }
}
