package com.smartstudy.counselor_t.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.mvp.contract.OtherTeacherTagContract;
import com.smartstudy.counselor_t.mvp.presenter.OtherTeacherTagPresenter;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.ui.widget.TagsLayout;

import java.util.List;

/**
 * @author yqy
 * @date on 2018/5/4
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class OtherTeacherTagActivity extends BaseActivity<OtherTeacherTagContract.Presenter> implements OtherTeacherTagContract.View {
    private TagsLayout tagsLayout;
    private String studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_teacher_tag);
    }

    @Override
    public OtherTeacherTagContract.Presenter initPresenter() {
        return new OtherTeacherTagPresenter(this);
    }

    @Override
    public void initView() {
        topdefaultCentertitle.setText("其他老师标注");
        setTopLineVisibility(View.VISIBLE);
        studentId = getIntent().getStringExtra("id");
        tagsLayout = findViewById(R.id.tl_tags);
        presenter.getOtherTeacherTag(studentId);
    }

    @Override
    public void getOtherTeacherTagSueccess(List<String> list) {
        if (list != null && list.size() > 0) {
            tagsLayout.setBackGroup(R.drawable.bg_good_bussienss_border);
            tagsLayout.createTab(this, list);
        }
    }
}
