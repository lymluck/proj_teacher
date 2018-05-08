package com.smartstudy.counselor_t.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.mvp.contract.OtherTeacherTagContract;
import com.smartstudy.counselor_t.mvp.presenter.OtherTeacherTagPresenter;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.ui.widget.TagsLayout;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author yqy
 * @date on 2018/5/4
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class OtherTeacherTagActivity extends BaseActivity<OtherTeacherTagContract.Presenter> implements OtherTeacherTagContract.View {
    private String studentId;
    private TagFlowLayout tagFlowLayout;//所有标签的TagFlowLayout
    private List<String> all_label_List = new ArrayList<>();//所有标签列表
    private TagAdapter<String> tagAdapter;//标签适配器
    private LinearLayout llytErr;

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
        tagFlowLayout = findViewById(R.id.id_flowlayout_two);
        llytErr = findViewById(R.id.llyt_err);
        initAllLeblLayout();
        presenter.getOtherTeacherTag(studentId);


    }

    @Override
    public void getOtherTeacherTagSueccess(List<String> list) {
        if (list != null && list.size() > 0) {
            all_label_List.clear();
            all_label_List.addAll(list);
            tagAdapter.notifyDataChanged();
            llytErr.setVisibility(View.GONE);
        } else {
            llytErr.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 初始化所有标签列表
     */
    private void initAllLeblLayout() {
        //初始化适配器
        tagAdapter = new TagAdapter<String>(all_label_List) {
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
