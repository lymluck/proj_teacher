package com.smartstudy.counselor_t.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.mvp.contract.ChooseTeacherContract;
import com.smartstudy.counselor_t.mvp.presenter.ChooseTeacherPresenter;

import java.util.ArrayList;
import java.util.List;

import study.smart.baselib.entity.CityTeacherInfo;
import study.smart.baselib.entity.Teacher;
import study.smart.baselib.listener.OnSendMsgDialogClickListener;
import study.smart.baselib.ui.adapter.CommonAdapter;
import study.smart.baselib.ui.adapter.MultiItemTypeAdapter;
import study.smart.baselib.ui.adapter.base.ViewHolder;
import study.smart.baselib.ui.base.BaseActivity;
import study.smart.baselib.ui.widget.dialog.AppBasicDialog;
import study.smart.baselib.ui.widget.dialog.DialogCreator;
import study.smart.baselib.utils.ParameterUtils;
import study.smart.baselib.utils.ToastUtils;
import study.smart.transfer_management.ui.activity.CommonSearchActivity;

/**
 * @author yqy
 * @date on 2018/8/14
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class ChooseTeacherActivity extends BaseActivity<ChooseTeacherContract.Presenter> implements ChooseTeacherContract.View {
    private RecyclerView recyclerCenter;
    private RecyclerView recyclerTeacher;
    private List<CityTeacherInfo> cityTeacherInfoList;
    private CommonAdapter<CityTeacherInfo> centerAdapter;
    private boolean isFirst = true;
    private CommonAdapter<Teacher> teacherCommonAdapter;
    private List<Teacher> teachers;
    private AppBasicDialog teacherDialog;
    private String askName;
    private String question;
    private String questionId;
    private View viewSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_teacher);
    }

    @Override
    public ChooseTeacherContract.Presenter initPresenter() {
        return new ChooseTeacherPresenter(this);
    }

    @Override
    public void initView() {
        setTitle("选择老师");
        setLeftTxt("取消");
        askName = getIntent().getStringExtra("askName");
        question = getIntent().getStringExtra("question");
        questionId = getIntent().getStringExtra("questionId");
        setTopdefaultLefttextVisible(View.VISIBLE);
        setLeftImgVisible(View.GONE);
        viewSearch = findViewById(R.id.view_search);
        recyclerCenter = findViewById(R.id.rv_center);
        recyclerTeacher = findViewById(R.id.rv_teacher);
        //拉取中心列表
        initCenter();
        //拉取老师列表
        initTeacher();
        presenter.getTeacherList();
    }

    @Override
    public void getTeacherListSuccess(List<CityTeacherInfo> cityTeacherInfo) {
        if (cityTeacherInfo != null && cityTeacherInfo.size() > 0) {
            cityTeacherInfoList.clear();
            cityTeacherInfoList.addAll(cityTeacherInfo);
            centerAdapter.notifyDataSetChanged();
            //默认第一个
            teachers.addAll(cityTeacherInfoList.get(0).getList());
            teacherCommonAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void shareQuestionSuccess() {
        ToastUtils.shortToast("发送成功");
        if (teacherDialog != null) {
            teacherDialog.dismiss();
        }
        finish();
    }

    @Override
    public void shareQuestionFail(String msg) {
        ToastUtils.shortToast(msg);
        if (teacherDialog != null) {
            teacherDialog.dismiss();
        }
    }

    private void initTeacher() {
        recyclerTeacher.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerTeacher.setLayoutManager(mLayoutManager);
        teachers = new ArrayList<>();
        teacherCommonAdapter = new CommonAdapter<Teacher>(this, R.layout.layout_teacher_item, teachers) {
            @Override
            protected void convert(ViewHolder holder, Teacher teacher, int position) {
                holder.setPersonImageUrl(R.id.iv_avatar, teacher.getAvatar(), true);
                holder.setText(R.id.tv_name, teacher.getRealName());
            }
        };
        recyclerTeacher.setAdapter(teacherCommonAdapter);
        teacherCommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, final int position) {
                teacherDialog = DialogCreator.createTranferTeacherDialog(ChooseTeacherActivity.this, teachers.get(position), askName, question, new OnSendMsgDialogClickListener() {
                    @Override
                    public void onPositive(String word) {
                        presenter.shareQuestion(questionId, teachers.get(position).getId(), word);
                    }

                    @Override
                    public void onNegative() {
                        teacherDialog.dismiss();
                    }
                });
                teacherDialog.show();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    private void initCenter() {
        recyclerCenter.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerCenter.setLayoutManager(mLayoutManager);
        cityTeacherInfoList = new ArrayList<>();
        centerAdapter = new CommonAdapter<CityTeacherInfo>(this, R.layout.layout_center_item, cityTeacherInfoList) {
            @Override
            protected void convert(ViewHolder holder, CityTeacherInfo cityTeacherInfo, int position) {
                holder.setText(R.id.tv_center_name, cityTeacherInfo.getGroup());
                //没有点击之前，默认第一个位置
                if (isFirst) {
                    if (position == 0) {
                        holder.getView(R.id.v_blue).setVisibility(View.VISIBLE);
                        holder.getView(R.id.tv_center_name).setBackgroundColor(Color.parseColor("#ffffff"));
                        ((TextView) holder.getView(R.id.tv_center_name)).setTextColor(Color.parseColor("#26343F"));
                    } else {
                        holder.getView(R.id.v_blue).setVisibility(View.INVISIBLE);
                        holder.getView(R.id.tv_center_name).setBackgroundColor(Color.parseColor("#F5F6F7"));
                        ((TextView) holder.getView(R.id.tv_center_name)).setTextColor(Color.parseColor("#949BA1"));
                    }
                } else {
                    //不是第一次进来的话，根据数据状态来显示
                    if (cityTeacherInfoList.get(position).isSelect()) {
                        holder.getView(R.id.v_blue).setVisibility(View.VISIBLE);
                        holder.getView(R.id.tv_center_name).setBackgroundColor(Color.parseColor("#ffffff"));
                        ((TextView) holder.getView(R.id.tv_center_name)).setTextColor(Color.parseColor("#26343F"));
                    } else {
                        holder.getView(R.id.v_blue).setVisibility(View.INVISIBLE);
                        holder.getView(R.id.tv_center_name).setBackgroundColor(Color.parseColor("#F5F6F7"));
                        ((TextView) holder.getView(R.id.tv_center_name)).setTextColor(Color.parseColor("#949BA1"));
                    }
                }
            }
        };
        recyclerCenter.setAdapter(centerAdapter);
        centerAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                //更新状态
                handleTeacherInfo(cityTeacherInfoList.get(position).getGroup());
                centerAdapter.notifyDataSetChanged();
                if (teachers != null) {
                    teachers.clear();
                    teachers.addAll(cityTeacherInfoList.get(position).getList());
                    teacherCommonAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }


    @Override
    public void initEvent() {
        super.initEvent();
        topdefaultLefttext.setOnClickListener(this);
        viewSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.topdefault_lefttext:
                finish();
                break;
            case R.id.view_search:
                Intent toSearch = new Intent(ChooseTeacherActivity.this, CommonSearchActivity.class);
                toSearch.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.SEARCH_TEACHER);
                toSearch.putExtra("askName", askName)
                    .putExtra("questionId", questionId)
                    .putExtra("question", question);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(toSearch, ActivityOptionsCompat.makeSceneTransitionAnimation(ChooseTeacherActivity.this,
                        viewSearch, "btn_tr").toBundle());
                } else {
                    startActivity(toSearch);
                }
                break;
            default:
                break;
        }
    }


    private void handleTeacherInfo(String groupName) {
        isFirst = false;
        for (CityTeacherInfo cityTeacherInfo : cityTeacherInfoList) {
            if (groupName.equals(cityTeacherInfo.getGroup())) {
                cityTeacherInfo.setSelect(true);
            } else {
                cityTeacherInfo.setSelect(false);
            }
        }
    }
}
