package study.smart.transfer_management.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import study.smart.baselib.ui.base.BaseActivity;
import study.smart.baselib.utils.TimeUtil;
import study.smart.transfer_management.R;
import study.smart.baselib.entity.MyStudentInfo;
import study.smart.transfer_management.mvp.contract.StudentDetailContract;
import study.smart.transfer_management.mvp.presenter.StudentDetailPresenter;

/**
 * @author yqy
 * @date on 2018/7/17
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class StudentDetailActivity extends BaseActivity<StudentDetailContract.Presenter> implements StudentDetailContract.View {
    private MyStudentInfo myStudentInfo;
    private String from;
    private String id;
    private TextView tvName;
    private TextView tvPhone;
    private TextView tvServiceProject;
    private TextView tvContractor;
    private TextView tvSignedTime;
    private TextView tvYearSeason;
    private TextView tvApplyProject;
    private TextView tvCenterName;
    private TextView tvHardTeacher;
    private TextView tvSoftTeacher;
    private TextView tvStatus;
    private TextView tvRemind;
    private TextView tvContact;
    private String type;

    @Override
    public StudentDetailContract.Presenter initPresenter() {
        return new StudentDetailPresenter(this);
    }

    @Override
    public void initView() {
        from = getIntent().getStringExtra("from");
        id = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
        myStudentInfo = (MyStudentInfo) getIntent().getSerializableExtra("studentInfo");
        tvName = findViewById(R.id.tv_name);
        tvPhone = findViewById(R.id.tv_telephone);
        tvServiceProject = findViewById(R.id.tv_service_project);
        tvContractor = findViewById(R.id.tv_contractor);
        tvSignedTime = findViewById(R.id.tv_signed_time);
        tvYearSeason = findViewById(R.id.tv_year_season);
        tvApplyProject = findViewById(R.id.tv_apply_project);
        tvCenterName = findViewById(R.id.tv_center_name);
        tvHardTeacher = findViewById(R.id.tv_hard_teacher);
        tvSoftTeacher = findViewById(R.id.tv_soft_teacher);
        tvStatus = findViewById(R.id.tv_status);
        tvRemind = findViewById(R.id.tv_remind);
        tvContact = findViewById(R.id.tv_contact);

        if (!TextUtils.isEmpty(id)) {
            presenter.getStudentDetail(id);
        } else {
            initData(myStudentInfo);
        }
        if ("compelete_student".equals(from) || "STUDENT_TRANSFER_MANAGER".equals(from)) {
            tvRemind.setText("学员信息待完善，请前往电脑上完善");
            tvContact.setVisibility(View.VISIBLE);
        } else if ("report".equals(from)) {
            if ("QUARTERLY".equals(type)) {
                tvRemind.setText("请前往电脑上完成该学员的季度报告");
            } else if ("SUMMARY".equals(type)) {
                tvRemind.setText("请前往电脑上完成该学员的总结报告");
            } else {
                tvRemind.setText("请前往电脑上完成该学员的结案报告");
            }
            tvContact.setVisibility(View.GONE);
        } else if ("message".equals(from)) {
            tvRemind.setText("更多操作请前往电脑上操作");
            tvContact.setVisibility(View.VISIBLE);
        } else {
            tvRemind.setText("请前往电脑上完成该学员的沟通记录");
            tvContact.setVisibility(View.GONE);
        }
    }

    @Override
    public void initEvent() {
        super.initEvent();
        findViewById(R.id.tv_contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(myStudentInfo.getPhone())) {
                    Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + myStudentInfo.getPhone()));//跳转到拨号界面，同时传递电话号码
                    startActivity(dialIntent);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);
    }


    private void initData(MyStudentInfo myStudentInfo) {
        if (myStudentInfo != null) {
            this.myStudentInfo = myStudentInfo;
            setTitle(myStudentInfo.getName());
            setTopLineVisibility(View.VISIBLE);
            tvName.setText(myStudentInfo.getName());
            tvPhone.setText(myStudentInfo.getPhone());
            tvServiceProject.setText(myStudentInfo.getServiceProductNames());
            tvContractor.setText(myStudentInfo.getContractor());
            tvSignedTime.setText(TimeUtil.getStrTime(myStudentInfo.getSignedTime()));
            tvYearSeason.setText(myStudentInfo.getTargetApplicationYearSeason());
            tvApplyProject.setText(myStudentInfo.getTargetDegreeName());
            tvCenterName.setText(myStudentInfo.getCenterName());
            tvHardTeacher.setText(myStudentInfo.getHardTeacherName());
            tvSoftTeacher.setText(myStudentInfo.getSoftTeacherName());
            tvStatus.setText(myStudentInfo.getStatusName());
        }
    }

    @Override
    public void getStudentDetailSuccess(MyStudentInfo myStudentInfo) {
        initData(myStudentInfo);
    }
}
