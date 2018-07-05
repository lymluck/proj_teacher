package com.smartstudy.counselor_t.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import study.smart.baselib.entity.TeacherInfo;
import study.smart.baselib.ui.base.BaseActivity;
import study.smart.baselib.utils.DensityUtils;
import study.smart.baselib.utils.KeyBoardUtils;
import study.smart.baselib.utils.ParameterUtils;
import study.smart.baselib.utils.ToastUtils;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.mvp.contract.CommonEditNameContract;
import com.smartstudy.counselor_t.mvp.presenter.CommonEditNamePresenter;
import com.smartstudy.counselor_t.util.CheckUtil;

public class CommonEditNameActivity extends BaseActivity<CommonEditNameContract.Presenter> implements CommonEditNameContract.View {
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
        if (ParameterUtils.EDIT_WORK_EXPERIENCE.equals(flag)) {
            etname.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else {
            etname.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        if (ParameterUtils.EDIT_REMARK.equals(flag)) {
            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) etname.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20
            linearParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            linearParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            etname.setPadding(DensityUtils.dip2px(20), DensityUtils.dip2px(16), DensityUtils.dip2px(20), DensityUtils.dip2px(16));
            etname.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
            etname.setMaxLines(200);
            etname.setSingleLine(false);
            etname.setSelection(etname.getText().toString().length());
        } else {
            InputFilter[] filters = {new InputFilter.LengthFilter(30)};
            etname.setFilters(filters);
        }
    }

    @Override
    public void initEvent() {
        topdefaultLeftbutton.setOnClickListener(this);
        topdefaultRighttext.setOnClickListener(this);
    }

    @Override
    public CommonEditNameContract.Presenter initPresenter() {
        return new CommonEditNamePresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                KeyBoardUtils.closeKeybord(etname, this);
                finish();
                break;
            case R.id.topdefault_righttext:
                TeacherInfo teacherInfo = new TeacherInfo();
                String content = etname.getText().toString().replaceAll(" ", "");
                if (TextUtils.isEmpty(content)) {
                    ToastUtils.shortToast("输入的内容不能为空！");
                    return;
                }

                if (flag.equals(ParameterUtils.EDIT_EMAIL)) {
                    if (!CheckUtil.checkEmail(content)) {
                        ToastUtils.shortToast("邮箱不合法");
                        return;
                    }
                }
                if (flag.equals(ParameterUtils.EDIT_NAME)) {
                    teacherInfo.setName(content);
                } else if (flag.equals(ParameterUtils.EDIT_WORK_NAME)) {
                    teacherInfo.setTitle(content);
                } else if (flag.equals(ParameterUtils.EDIT_WORK_EXPERIENCE)) {
                    teacherInfo.setYearsOfWorking(content);
                } else if (flag.equals(ParameterUtils.EDIT_GRADUATED_SCHOOL)) {
                    teacherInfo.setSchool(content);
                } else if (flag.equals(ParameterUtils.EDIT_REAL_NAME)) {
                    teacherInfo.setRealName(content);
                } else if (flag.equals(ParameterUtils.EDIT_EMAIL)) {
                    teacherInfo.setEmail(content);
                } else {
                    teacherInfo.setIntroduction(content);
                }
                presenter.updateMyInfo(teacherInfo);
                break;
        }
    }

    @Override
    public void updateMyInfoSuccesee(TeacherInfo teacherInfo) {
        Intent data = new Intent();
        data.putExtra(ParameterUtils.TRANSITION_FLAG, flag);
        data.putExtra("new_value", etname.getText().toString());
        data.putExtra("TEACHERINFO", teacherInfo);
        //保存成功后销毁页面隐藏软键盘
        KeyBoardUtils.closeKeybord(etname, this);
        setResult(RESULT_OK, data);
        finish();

    }
}
