package study.smart.transfer_management.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.ui.base.BaseActivity;
import study.smart.transfer_management.R;
import study.smart.transfer_management.entity.MyStudentInfo;

/**
 * @author yqy
 * @date on 2018/7/17
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class StudentDetailActivity extends BaseActivity {
    private MyStudentInfo myStudentInfo;
    private String from;

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        from = getIntent().getStringExtra("from");
        myStudentInfo = (MyStudentInfo) getIntent().getSerializableExtra("studentInfo");
        TextView tvName = findViewById(R.id.tv_name);
        TextView tvPhone = findViewById(R.id.tv_telephone);
        TextView tvServiceProject = findViewById(R.id.tv_service_project);
        TextView tvContractor = findViewById(R.id.tv_contractor);
        TextView tvSignedTime = findViewById(R.id.tv_signed_time);
        TextView tvYearSeason = findViewById(R.id.tv_year_season);
        TextView tvApplyProject = findViewById(R.id.tv_apply_project);
        TextView tvCenterName = findViewById(R.id.tv_center_name);
        TextView tvHardTeacher = findViewById(R.id.tv_hard_teacher);
        TextView tvSoftTeacher = findViewById(R.id.tv_soft_teacher);
        TextView tvStatus = findViewById(R.id.tv_status);
        TextView tvRemind = findViewById(R.id.tv_remind);
        TextView tvContact = findViewById(R.id.tv_contact);
        if (myStudentInfo != null) {
            tvName.setText(myStudentInfo.getName());
            tvPhone.setText(myStudentInfo.getPhone());
            tvServiceProject.setText(myStudentInfo.getServiceProductNames());
            tvContractor.setText(myStudentInfo.getContractor());
            tvSignedTime.setText(myStudentInfo.getSignedTime());
            tvYearSeason.setText(myStudentInfo.getTargetApplicationYearSeason());
            tvApplyProject.setText(myStudentInfo.getTargetDegreeName());
            tvCenterName.setText(myStudentInfo.getCenterName());
            tvHardTeacher.setText(myStudentInfo.getHardTeacherName());
            tvSoftTeacher.setText(myStudentInfo.getSoftTeacherName());
            tvStatus.setText(myStudentInfo.getStatusName());
        }
        if ("student".equals(from)) {
            tvRemind.setText("学员信息待完善，请前往电脑上完善");
            tvContact.setVisibility(View.VISIBLE);
        } else if ("report".equals(from)) {
            tvRemind.setText("请前往电脑上完成该学员的季度报告");
            tvContact.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);
    }

}
