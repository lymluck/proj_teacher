package com.smartstudy.counselor_t.ui.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.mvp.contract.AddLabelContract;
import com.smartstudy.counselor_t.mvp.presenter.AddLabelPresenter;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.util.DensityUtils;
import com.smartstudy.counselor_t.util.KeyBoardUtils;
import com.smartstudy.counselor_t.util.ToastUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author yqy
 * @date on 2018/5/3
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class AddLabelActivity extends BaseActivity<AddLabelContract.Presenter> implements AddLabelContract.View {
    private FlowLayout flowLayout;//上面的flowLayout
    private TagFlowLayout allFlowLayout;//所有标签的TagFlowLayout
    private List<String> label_list = new ArrayList<>();//上面的标签列表
    private LinearLayout.LayoutParams params;
    final List<Boolean> labelStates = new ArrayList<>();//存放标签状态
    private List<String> all_label_List = new ArrayList<>();//所有标签列表
    final List<TextView> labels = new ArrayList<>();//存放标签
    final Set<Integer> set = new HashSet<>();//存放选中的
    private TagAdapter<String> tagAdapter;//标签适配器
    private EditText editText;
    private TextView tvSubmit;
    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_label);
    }


    @Override
    public AddLabelContract.Presenter initPresenter() {
        return new AddLabelPresenter(this);
    }

    @Override
    public void initView() {
        id = getIntent().getStringExtra("id");
        setTopLineVisibility(View.VISIBLE);
        topdefaultCentertitle.setText("添加标签");
        tvSubmit = findViewById(R.id.tv_submit);
        flowLayout = (FlowLayout) findViewById(R.id.id_flowlayout);
        allFlowLayout = (TagFlowLayout) findViewById(R.id.id_flowlayout_two);
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 16, 16, 8);
        presenter.getMyStudentTag(id);
    }


    public void initOnclick() {
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String editTextContent = editText.getText().toString();
                if (TextUtils.isEmpty(editTextContent)) {
//                    tagNormal();
                    return false;
                } else {
                    label_list.add(editTextContent);
                    addLabel(editText);
                    addTest(editTextContent);
                    editText.clearFocus();
                    KeyBoardUtils.closeKeybord(editText,AddLabelActivity.this);
                }
                return false;
            }
        });

        topdefaultLeftbutton.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit:
                presenter.submitMyStudentTag(id, label_list);
                break;

            case R.id.topdefault_leftbutton:
                finish();
                break;
            default:
                break;
        }
    }


    /**
     * 标签恢复到正常状态
     */
    private void tagNormal() {
        //输入文字时取消已经选中的标签
        for (int i = 0; i < labelStates.size(); i++) {
            if (labelStates.get(i)) {
                if (labels.size() < i) {
                    TextView tmp = labels.get(i);
                    tmp.setCompoundDrawables(null, null, null, null);
                    labelStates.set(i, false);
                }
            }
        }
    }

    /**
     * 初始化默认的添加标签
     */
    private void initEdittext() {
        editText = new EditText(getApplicationContext());
        editText.setHint("新增标签");
        //设置固定宽度
        editText.setMinEms(1);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});
        editText.setTextSize(15);
        editText.setSingleLine();
        try {
            java.lang.reflect.Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(editText, R.drawable.edit_cursor_color);
        } catch (Exception e) {
        }
        Drawable leftDrawable = getResources().getDrawable(R.drawable.ic_add_tag);
        leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
        editText.setCompoundDrawables(null, null, leftDrawable, null);
        editText.setCompoundDrawablePadding(DensityUtils.dip2px(3));
        editText.setBackgroundResource(R.drawable.label_add);
        editText.setHintTextColor(Color.parseColor("#078CF1"));
        editText.setTextColor(Color.parseColor("#078CF1"));
        editText.setLayoutParams(params);
        //添加到layout中
        flowLayout.addView(editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                tagNormal();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    /**
     * 初始化所有标签列表
     */
    private void initAllLeblLayout() {
        //初始化适配器
        tagAdapter = new TagAdapter<String>(all_label_List) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) getLayoutInflater().inflate(R.layout.layout_flag_adapter,
                    allFlowLayout, false);

                ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.setMargins(DensityUtils.dip2px(3),
                    DensityUtils.dip2px(3),
                    DensityUtils.dip2px(3),
                    DensityUtils.dip2px(3));
                tv.setLayoutParams(lp);
                tv.setText(s);
                return tv;
            }
        };

        allFlowLayout.setAdapter(tagAdapter);

        //根据上面标签来判断下面的标签是否含有上面的标签
        for (int i = 0; i < label_list.size(); i++) {
            for (int j = 0; j < all_label_List.size(); j++) {
                if (label_list.get(i).equals(
                    all_label_List.get(j))) {
                    tagAdapter.setSelectedList(j);//设为选中
                    set.add(i);
                }
            }
        }
        tagAdapter.notifyDataChanged();

        //给下面的标签添加监听
        allFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (labels.size() == 0) {
                    editText.setText(all_label_List.get(position));
                    label_list.add(all_label_List.get(position));
                    addLabel(editText);
                    return false;
                }
                List<String> list = new ArrayList<>();
                for (int i = 0; i < labels.size(); i++) {
                    list.add(labels.get(i).getText().toString());
                }
                //如果上面包含点击的标签就删除
                if (list.contains(all_label_List.get(position))) {
                    for (int i = 0; i < list.size(); i++) {
                        if (all_label_List.get(position).equals(list.get(i))) {
                            flowLayout.removeView(labels.get(i));
                            labels.remove(i);
                            label_list.remove(i);
                        }
                    }
                } else {
                    editText.setText(all_label_List.get(position));
                    label_list.add(all_label_List.get(position));
                    addLabel(editText);
                }
                return false;
            }
        });

        //已经选中的监听
        allFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {

            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                set.clear();
                set.addAll(selectPosSet);
                tagAdapter.setSelectedList(set);
            }
        });
    }


    /**
     * 添加标签
     *
     * @param editText
     * @return
     */
    private boolean addLabel(EditText editText) {
        String editTextContent = editText.getText().toString();
        //判断输入是否为空
        if (editTextContent.equals("")) {
            return true;
        }
        //判断是否重复
        for (TextView tag : labels) {
            String tempStr = tag.getText().toString();
            if (tempStr.equals(editTextContent)) {
                editText.setText("");
                editText.requestFocus();
                return true;
            }
        }
        //添加标签
        final TextView temp = getTag(editText.getText().toString());
        labels.add(temp);
        labelStates.add(false);
        //添加点击事件，点击变成选中状态，选中状态下被点击则删除
        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curIndex = labels.indexOf(temp);
                if (!labelStates.get(curIndex)) {
                    //显示 ×号删除
                    temp.setText(temp.getText());
                    Drawable leftDrawable = getResources().getDrawable(R.drawable.ic_detele_tags);
                    leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
                    temp.setCompoundDrawables(null, null, leftDrawable, null);
                    temp.setTextColor(Color.parseColor("#ffffff"));
                    temp.setCompoundDrawablePadding(DensityUtils.dip2px(4));
                    //修改选中状态
                    labelStates.set(curIndex, true);
                } else {
                    delByTest(temp.getText().toString());
                    flowLayout.removeView(temp);
                    labels.remove(curIndex);
                    label_list.remove(curIndex);
                    labelStates.remove(curIndex);
                }
            }
        });
        flowLayout.addView(temp);
        //让输入框在最后一个位置上
        editText.bringToFront();
        //清空编辑框
        editText.setText("");
        return true;

    }


    /**
     * 根据字符删除标签
     *
     * @param text
     */
    private void delByTest(String text) {
        for (int i = 0; i < all_label_List.size(); i++) {
            String a = all_label_List.get(i);
            if (a.equals(text)) {
                set.remove(i);
            }
        }
        tagAdapter.setSelectedList(set);//重置选中的标签/*/
    }


    /**
     * 增加新的标签
     */

    private void addTest(String text) {
        for (int i = 0; i < all_label_List.size(); i++) {
            String a = all_label_List.get(i);
            if (a.equals(text)) {
                tagAdapter.setSelectedList(i);//重置选中的标签/*/
            }
        }
    }

    /**
     * 创建一个正常状态的标签
     *
     * @param label
     * @return
     */
    private TextView getTag(String label) {
        TextView textView = new TextView(getApplicationContext());
        textView.setTextSize(15);
        textView.setSingleLine(true);
        textView.setBackgroundResource(R.drawable.label_normal);
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        textView.setText(label);
        textView.setLayoutParams(params);
        return textView;
    }

    @Override
    public void getMyTagSuccess(List<String> list) {
        presenter.getHisToryTag();
        if (list != null && list.size() > 0) {
            label_list.clear();
            label_list.addAll(list);
        }
    }

    @Override
    public void subitMyStudentSuccess() {
        ToastUtils.shortToast(this, "标签更新成功");
        finish();
    }

    @Override
    public void postHistoryTagSueccess() {
    }

    @Override
    public void getHistoryTagSueccess(List<String> tags) {
        if (tags != null) {
            all_label_List.clear();
            all_label_List.addAll(tags);
        }

        for (int i = 0; i < label_list.size(); i++) {
            editText = new EditText(getApplicationContext());//new 一个EditText
            editText.setText(label_list.get(i));
            addLabel(editText);//添加标签
        }
        initEdittext();
        initAllLeblLayout();
        initOnclick();
    }
}
