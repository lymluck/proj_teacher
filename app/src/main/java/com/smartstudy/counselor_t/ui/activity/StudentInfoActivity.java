package com.smartstudy.counselor_t.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.StudentPageInfo;
import com.smartstudy.counselor_t.mvp.contract.StudentActivityContract;
import com.smartstudy.counselor_t.mvp.presenter.StudentInfoActivityPresenter;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.util.DisplayImageUtils;

/**
 * @author yqy
 * @date on 2018/1/15
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class StudentInfoActivity extends BaseActivity<StudentActivityContract.Presenter> implements StudentActivityContract.View {

    private String targeId;

    private ImageView ivAvatar;

    private TextView tvName;

    private ImageView ivSex;

    private TextView tvGrade;

    private TextView tvLocation;

    private View vLine;

    private LinearLayout llInfoDetail;

    private TextView tvTime;

    private LinearLayout llIntentionInformation;

    private TextView tvTargeCountry;

    private TextView tvTargetDegree;

    private TextView tvTargetMajorDirection;

    private LinearLayout llBaseInfo;

    private TextView tvCurrentSchool;

    private TextView tvScore;

    private TextView tvCurrentScore;

    private TextView tvSchool;

    private TextView tvScoreLanguage;

    private TextView tvGreGmat;

    private TextView tvActivityInternshi;

    private TextView tvActivityResearch;

    private TextView tvActivityCommunity;

    private TextView tvActivityExchangey;

    private LinearLayout ll_top_school;

    private LinearLayout ll_bg;

    private LinearLayout ll_target_direction;

    private LinearLayout ll_gre;

    private TextView tv_gre;

    private TextView tv_shehui_event;

    private LinearLayout llyt_activity_research;

    private LinearLayout llyt_activity_community;

    private TextView tv_targe_school;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail_info);
        ivAvatar = findViewById(R.id.iv_avatar);
        tvName = findViewById(R.id.tv_name);
        ivSex = findViewById(R.id.iv_sex);
        tvGrade = findViewById(R.id.tv_grade);
        tvLocation = findViewById(R.id.tv_location);
        vLine = findViewById(R.id.v_line);
        tv_targe_school = findViewById(R.id.tv_targe_school);
        llInfoDetail = findViewById(R.id.ll_info_detail);
        llIntentionInformation = findViewById(R.id.ll_intention_information);
        tvTime = findViewById(R.id.tv_time);
        tv_gre = findViewById(R.id.tv_gre);
        tvTargeCountry = findViewById(R.id.tv_targe_country);
        tvTargetDegree = findViewById(R.id.tv_target_degree);
        tvTargetMajorDirection = findViewById(R.id.tv_target_direction);
        ll_top_school = findViewById(R.id.ll_top_school);
        llBaseInfo = findViewById(R.id.ll_base_info);
        ll_gre = findViewById(R.id.ll_gre);
        tvCurrentScore = findViewById(R.id.tv_current_score);
        tvSchool = findViewById(R.id.tv_school);
        tv_shehui_event = findViewById(R.id.tv_shehui_event);
        ll_target_direction = findViewById(R.id.ll_target_direction);
        tvCurrentSchool = findViewById(R.id.tv_current_school);
        ll_bg = findViewById(R.id.ll_bg);
        tvScore = findViewById(R.id.tv_score);
        tvScoreLanguage = findViewById(R.id.tv_score_language);
        tvGreGmat = findViewById(R.id.tv_gre_gmat);
        tvActivityInternshi = findViewById(R.id.tv_activity_internshi);
        tvActivityResearch = findViewById(R.id.tv_activity_research);
        tvActivityCommunity = findViewById(R.id.tv_activity_community);
        tvActivityExchangey = findViewById(R.id.tv_activity_exchangey);
        llyt_activity_research = findViewById(R.id.llyt_activity_research);
        llyt_activity_community = findViewById(R.id.llyt_activity_community);
    }

    @Override
    public StudentActivityContract.Presenter initPresenter() {
        return new StudentInfoActivityPresenter(this);
    }

    @Override
    public void initView() {
        targeId = getIntent().getStringExtra("ids");
        if (!TextUtils.isEmpty(targeId)) {
            presenter.getStudentDetailInfo(targeId);
        }
    }

    @Override
    public void getStudentInfoDetailSuccess(StudentPageInfo studentInfo) {
        /**
         * 个人信息头像等基本信息设置
         */
        DisplayImageUtils.formatPersonImgUrl(this, studentInfo.getAvatar(), ivAvatar);
        tvName.setText(studentInfo.getName());
        if (TextUtils.isEmpty(studentInfo.getGenderId())) {
            ivSex.setVisibility(View.GONE);
        } else {
            if (studentInfo.getGenderId().equals("GENDER_MALE")) {
                ivSex.setImageResource(R.drawable.ic_man);
            } else {
                ivSex.setImageResource(R.drawable.ic_woman);
            }
        }

        if (TextUtils.isEmpty(studentInfo.getCurrentGrade())) {
            tvGrade.setVisibility(View.GONE);
        } else {
            tvGrade.setText(studentInfo.getCurrentGrade());
        }

        if (TextUtils.isEmpty(studentInfo.getLocation())) {
            tvLocation.setVisibility(View.GONE);
        } else {
            tvLocation.setText(studentInfo.getLocation());
        }

        if (TextUtils.isEmpty(studentInfo.getLocation()) && TextUtils.isEmpty(studentInfo.getCurrentGrade())) {
            llInfoDetail.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(studentInfo.getLocation()) || TextUtils.isEmpty(studentInfo.getCurrentGrade())) {
            vLine.setVisibility(View.GONE);
        } else {
            vLine.setVisibility(View.VISIBLE);
        }

        /**
         *意向信息设置
         */
        if (studentInfo.getTargetSection() == null) {
            llIntentionInformation.setVisibility(View.GONE);
        } else {

            StudentPageInfo.TargetSection.AdmissionTime admissionTime = studentInfo.getTargetSection().getAdmissionTime();
            if (admissionTime != null) {
                tvTime.setText(admissionTime.getName());
            }

            StudentPageInfo.TargetSection.TargetCountry targetCountry = studentInfo.getTargetSection().getTargetCountry();
            if (targetCountry != null) {
                if ("美国".equals(targetCountry.getName())) {
                    ll_top_school.setVisibility(View.VISIBLE);
                    StudentPageInfo.TargetSection.TargetSchoolRank targetSchoolRank = studentInfo.getTargetSection().getTargetSchoolRank();
                    if (targetSchoolRank != null) {
                        tv_targe_school.setText(targetSchoolRank.getName());
                    }
                } else {
                    ll_top_school.setVisibility(View.GONE);
                }
                tvTargeCountry.setText(targetCountry.getName());
            }

            StudentPageInfo.TargetSection.TargetDegree targetDegree = studentInfo.getTargetSection().getTargetDegree();

            if (targetDegree != null) {
                if ("高中".equals(targetDegree.getName())) {
                    ll_top_school.setVisibility(View.GONE);
                    ll_bg.setVisibility(View.GONE);
                    tvSchool.setText("初中学校");
                    tvScore.setText("初中成绩");
                    ll_target_direction.setVisibility(View.GONE);
                    ll_gre.setVisibility(View.GONE);
                } else if ("本科".equals(targetDegree.getName())) {
                    ll_bg.setVisibility(View.VISIBLE);
                    tvSchool.setText("高中学校");
                    tvScore.setText("高中成绩");
                    llyt_activity_research.setVisibility(View.GONE);
                    llyt_activity_community.setVisibility(View.GONE);
                    ll_target_direction.setVisibility(View.GONE);
                    findViewById(R.id.llyt_bk_event).setVisibility(View.VISIBLE);
                    findViewById(R.id.llyt_yjs_event).setVisibility(View.GONE);
                    tv_gre.setText("SAT/ACT");
                } else {
                    ll_bg.setVisibility(View.VISIBLE);
                    tvSchool.setText("本科学校");
                    tvScore.setText("本科成绩");
                    ll_target_direction.setVisibility(View.VISIBLE);
                    tv_gre.setText("GRE/GMAT");
                    findViewById(R.id.llyt_bk_event).setVisibility(View.GONE);
                    findViewById(R.id.llyt_yjs_event).setVisibility(View.VISIBLE);
                    if ("其他".equals(targetDegree.getName())) {
                        ll_gre.setVisibility(View.GONE);
                        tvSchool.setText("在读学校");
                        tvScore.setText("在读成绩");
                    }
                }
                tvTargetDegree.setText(targetDegree.getName());
            }

            StudentPageInfo.TargetSection.TargetMajorDirection targetMajorDirection = studentInfo.getTargetSection().getTargetMajorDirection();
            if (targetMajorDirection != null) {
                tvTargetMajorDirection.setText(targetMajorDirection.getName());
            }
        }

        /**
         * 基本信息设置
         */
        if (studentInfo.getBackgroundSection() == null)

        {
            llBaseInfo.setVisibility(View.GONE);
        } else

        {
            if (studentInfo.getBackgroundSection().getCurrentSchool() != null) {
                tvCurrentSchool.setText(studentInfo.getBackgroundSection().getCurrentSchool().getName());
            }

            if (studentInfo.getBackgroundSection().getScore() != null) {
                tvCurrentScore.setText(studentInfo.getBackgroundSection().getScore().getName());
            }

            if (studentInfo.getBackgroundSection().getScoreLanguage() != null) {
                tvScoreLanguage.setText(studentInfo.getBackgroundSection().getScoreLanguage().getName());
            }


            /**
             * GRE/GMAT
             */

            if (studentInfo.getBackgroundSection().getScoreStandard() != null) {
                tvGreGmat.setText(studentInfo.getBackgroundSection().getScoreStandard().getName());
            } else {
                ll_gre.setVisibility(View.GONE);
            }

            if (studentInfo.getBackgroundSection().getActivityInternship() != null) {
                tvActivityInternshi.setText(studentInfo.getBackgroundSection().getActivityInternship().getName());
            }


            if (studentInfo.getBackgroundSection().getActivityResearch() != null) {
                tvActivityResearch.setText(studentInfo.getBackgroundSection().getActivityResearch().getName());
            }


            if (studentInfo.getBackgroundSection().getActivityCommunity() != null) {
                tvActivityCommunity.setText(studentInfo.getBackgroundSection().getActivityCommunity().getName());
            }

            if (studentInfo.getBackgroundSection().getActivityExchange() != null) {
                tvActivityExchangey.setText(studentInfo.getBackgroundSection().getActivityExchange().getName());
            }

            if (studentInfo.getBackgroundSection().getActivitySocial() != null) {
                tv_shehui_event.setText(studentInfo.getBackgroundSection().getActivitySocial().getName());
            }
        }
    }
}
