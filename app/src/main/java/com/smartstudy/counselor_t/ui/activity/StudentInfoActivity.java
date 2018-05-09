package com.smartstudy.counselor_t.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.StudentPageInfo;
import com.smartstudy.counselor_t.mvp.contract.StudentActivityContract;
import com.smartstudy.counselor_t.mvp.presenter.StudentInfoActivityPresenter;
import com.smartstudy.counselor_t.ui.adapter.CommonAdapter;
import com.smartstudy.counselor_t.ui.adapter.base.ViewHolder;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.ui.widget.HorizontalDividerItemDecoration;
import com.smartstudy.counselor_t.ui.widget.NoScrollLinearLayoutManager;
import com.smartstudy.counselor_t.util.DensityUtils;
import com.smartstudy.counselor_t.util.DisplayImageUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

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
    private LinearLayout llTopSchool;
    private LinearLayout llBg;
    private LinearLayout llTargetDirection;
    private LinearLayout llGre;
    private TextView tvGre;
    private TextView tvShehuiEvent;
    private LinearLayout llytActivityResearch;
    private LinearLayout llytActivityCommunity;
    private TextView tvTargeSchool;
    private RecyclerView rvSchool;
    private NoScrollLinearLayoutManager mLayoutManager;
    private LinearLayout llStudent;
    private View vBackline;
    private TextView tvCountSchool;
    private ScrollView slStudentInfo;
    private StudentPageInfo studentInfo;
    private TextView tvAddTags;
    private CommonAdapter<StudentPageInfo.WatchSchools.SchoolData> mAdapter;
    private List<StudentPageInfo.WatchSchools.SchoolData> schoolDataList;
    private TagFlowLayout tagFlowLayout;//所有标签的TagFlowLayout
    private List<String> allLabelList = new ArrayList<>();//所有标签列表
    private TagAdapter<String> tagAdapter;//标签适配器
    private LinearLayout llOtherTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail_info);
    }

    private void initAdapter() {
        schoolDataList = new ArrayList<>();
        mAdapter = new CommonAdapter<StudentPageInfo.WatchSchools.SchoolData>(this, R.layout.item_studetn_school, schoolDataList) {
            @Override
            protected void convert(ViewHolder holder, StudentPageInfo.WatchSchools.SchoolData schoolData, int position) {
                holder.setText(R.id.tv_school, schoolData.getSchool().getChineseName());
                if (schoolData.getMatchTypeId().equals("MS_MATCH_TYPE_TOP")) {
                    holder.setText(R.id.tv_status, "冲");
                    holder.setBackgroundRes(R.id.tv_status, R.drawable.bg_oval_red_size);
                } else if (schoolData.getMatchTypeId().equals("MS_MATCH_TYPE_MIDDLE")) {
                    holder.setText(R.id.tv_status, "核");
                    holder.setBackgroundRes(R.id.tv_status, R.drawable.bg_oval_blue_size);
                } else {
                    holder.setText(R.id.tv_status, "保");
                    holder.setBackgroundRes(R.id.tv_status, R.drawable.bg_oval_green_size);
                }

            }
        };
    }

    @Override
    public StudentActivityContract.Presenter initPresenter() {
        return new StudentInfoActivityPresenter(this);
    }

    @Override
    public void initView() {
        targeId = getIntent().getStringExtra("ids");
        ivAvatar = findViewById(R.id.iv_avatar);
        tvName = findViewById(R.id.tv_name);
        ivSex = findViewById(R.id.iv_sex);
        tvGrade = findViewById(R.id.tv_grade);
        tvLocation = findViewById(R.id.tv_location);
        vLine = findViewById(R.id.v_line);
        llOtherTag = findViewById(R.id.ll_other_tag);
        tvTargeSchool = findViewById(R.id.tv_targe_school);
        llInfoDetail = findViewById(R.id.ll_info_detail);
        llIntentionInformation = findViewById(R.id.ll_intention_information);
        tvTime = findViewById(R.id.tv_time);
        tvGre = findViewById(R.id.tv_gre);
        tvAddTags = findViewById(R.id.tv_add_tags);
        llStudent = findViewById(R.id.ll_student);
        tvTargeCountry = findViewById(R.id.tv_targe_country);
        tvTargetDegree = findViewById(R.id.tv_target_degree);
        tvTargetMajorDirection = findViewById(R.id.tv_target_direction);
        llTopSchool = findViewById(R.id.ll_top_school);
        llBaseInfo = findViewById(R.id.ll_base_info);
        llGre = findViewById(R.id.ll_gre);
        tvCurrentScore = findViewById(R.id.tv_current_score);
        tvSchool = findViewById(R.id.tv_school);
        tvShehuiEvent = findViewById(R.id.tv_shehui_event);
        llTargetDirection = findViewById(R.id.ll_target_direction);
        tvCurrentSchool = findViewById(R.id.tv_current_school);
        llBg = findViewById(R.id.ll_bg);
        tvScore = findViewById(R.id.tv_score);
        tvScoreLanguage = findViewById(R.id.tv_score_language);
        tvGreGmat = findViewById(R.id.tv_gre_gmat);
        tvActivityInternshi = findViewById(R.id.tv_activity_internshi);
        tvActivityResearch = findViewById(R.id.tv_activity_research);
        tvActivityCommunity = findViewById(R.id.tv_activity_community);
        tvActivityExchangey = findViewById(R.id.tv_activity_exchangey);
        llytActivityResearch = findViewById(R.id.llyt_activity_research);
        llytActivityCommunity = findViewById(R.id.llyt_activity_community);
        rvSchool = findViewById(R.id.rv_school);
        slStudentInfo = findViewById(R.id.sl_student_info);
        vBackline = findViewById(R.id.v_backline);
        tvCountSchool = findViewById(R.id.tv_count_school);
        rvSchool.setNestedScrollingEnabled(false);
        rvSchool.setHasFixedSize(true);
        tagFlowLayout = findViewById(R.id.id_flowlayout_two);
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvSchool.setLayoutManager(mLayoutManager);
        initAdapter();
        initAllLeblLayout();
        rvSchool.setAdapter(mAdapter);
        rvSchool.setNestedScrollingEnabled(false);
        rvSchool.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
            .size(DensityUtils.dip2px(0.5f)).colorResId(R.color.horizontal_line_color).build());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(targeId)) {
            presenter.getStudentDetailInfo(targeId);
        }
    }

    @Override
    public void initEvent() {
        super.initEvent();
        tvAddTags.setOnClickListener(this);
        llOtherTag.setOnClickListener(this);
        topdefaultLeftbutton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_other_tag:
                startActivity(new Intent(this, OtherTeacherTagActivity.class).putExtra("id", studentInfo.getId()));
                break;
            case R.id.tv_add_tags:
                startActivity(new Intent(this, AddLabelActivity.class).putExtra("id", studentInfo.getId()));
                break;
            case R.id.topdefault_leftbutton:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void getStudentInfoDetailSuccess(StudentPageInfo studentInfo) {
        /**
         * 个人信息头像等基本信息设置
         */
        this.studentInfo = studentInfo;
        slStudentInfo.setVisibility(View.VISIBLE);
        if (studentInfo.getTags() != null && studentInfo.getTags().size() > 0) {
            tagFlowLayout.setVisibility(View.VISIBLE);
            allLabelList.clear();
            allLabelList.addAll(studentInfo.getTags());
            tagAdapter.notifyDataChanged();
        } else {
            tagFlowLayout.setVisibility(View.GONE);
        }
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
                    llTopSchool.setVisibility(View.VISIBLE);
                    StudentPageInfo.TargetSection.TargetSchoolRank targetSchoolRank = studentInfo.getTargetSection().getTargetSchoolRank();
                    if (targetSchoolRank != null) {
                        tvTargeSchool.setText(targetSchoolRank.getName());
                    }
                } else {
                    llTopSchool.setVisibility(View.GONE);
                }
                tvTargeCountry.setText(targetCountry.getName());
            }
            StudentPageInfo.TargetSection.TargetDegree targetDegree = studentInfo.getTargetSection().getTargetDegree();
            if (targetDegree != null) {
                if ("高中".equals(targetDegree.getName())) {
                    llTopSchool.setVisibility(View.GONE);
                    llBg.setVisibility(View.GONE);
                    tvSchool.setText("初中学校");
                    tvScore.setText("初中成绩");
                    llTargetDirection.setVisibility(View.GONE);
                    llGre.setVisibility(View.GONE);
                } else if ("本科".equals(targetDegree.getName())) {
                    llBg.setVisibility(View.VISIBLE);
                    tvSchool.setText("高中学校");
                    tvScore.setText("高中成绩");
                    llytActivityResearch.setVisibility(View.GONE);
                    llytActivityCommunity.setVisibility(View.GONE);
                    llTargetDirection.setVisibility(View.GONE);
                    findViewById(R.id.llyt_bk_event).setVisibility(View.VISIBLE);
                    findViewById(R.id.llyt_yjs_event).setVisibility(View.GONE);
                    tvGre.setText("SAT/ACT");
                } else {
                    llBg.setVisibility(View.VISIBLE);
                    tvSchool.setText("本科学校");
                    tvScore.setText("本科成绩");
                    llTargetDirection.setVisibility(View.VISIBLE);
                    tvGre.setText("GRE/GMAT");
                    findViewById(R.id.llyt_bk_event).setVisibility(View.GONE);
                    findViewById(R.id.llyt_yjs_event).setVisibility(View.VISIBLE);
                    if ("其他".equals(targetDegree.getName())) {
                        llGre.setVisibility(View.GONE);
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
        if (studentInfo.getBackgroundSection() == null) {
            llBaseInfo.setVisibility(View.GONE);
        } else {
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
                llGre.setVisibility(View.GONE);
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
                tvShehuiEvent.setText(studentInfo.getBackgroundSection().getActivitySocial().getName());
            }
        }
        if (studentInfo.getWatchSchools() != null) {
            if (schoolDataList != null) {
                schoolDataList.clear();
            }
            if (studentInfo.getWatchSchools().getData() != null && studentInfo.getWatchSchools().getData().size() > 0) {
                llStudent.setVisibility(View.VISIBLE);
                vBackline.setVisibility(View.GONE);
                schoolDataList.addAll(studentInfo.getWatchSchools().getData());
                mAdapter.notifyDataSetChanged();
                tvCountSchool.setText("(" + schoolDataList.size() + "所)");
            } else {
                llStudent.setVisibility(View.GONE);
                vBackline.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 初始化所有标签列表
     */
    private void initAllLeblLayout() {
        //初始化适配器
        tagAdapter = new TagAdapter<String>(allLabelList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) getLayoutInflater().inflate(R.layout.layout_tag_other_item,
                    tagFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        };
        tagFlowLayout.setAdapter(tagAdapter);
    }
}