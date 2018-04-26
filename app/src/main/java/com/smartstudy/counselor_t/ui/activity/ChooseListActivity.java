package com.smartstudy.counselor_t.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.IdNameInfo;
import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.ui.adapter.CommonAdapter;
import com.smartstudy.counselor_t.ui.adapter.MultiItemTypeAdapter;
import com.smartstudy.counselor_t.ui.adapter.base.ViewHolder;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.util.ParameterUtils;
import com.smartstudy.counselor_t.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yqy
 * @date on 2018/4/19
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class ChooseListActivity extends BaseActivity {
    private String flag;
    private String from;
    private boolean isChange;
    private String value;
    private RecyclerView rcvList;
    private List<IdNameInfo> nameInfos;
    private CommonAdapter<IdNameInfo> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_list);
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }


    @Override
    public void initEvent() {
        topdefaultLeftbutton.setOnClickListener(this);
        topdefaultRighttext.setOnClickListener(this);
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (!ParameterUtils.WORK_CITY.equals(flag)) {
                    IdNameInfo nameInfo = nameInfos.get(position);
                    if (nameInfo.is_selected()) {
                        nameInfo.setIs_selected(false);
                        holder.itemView.findViewById(R.id.iv_choose).setVisibility(View.VISIBLE);
                        holder.itemView.findViewById(R.id.iv_choose).setBackgroundResource(R.drawable.unselect_white);
                    } else {
                        nameInfo.setIs_selected(true);
                        holder.itemView.findViewById(R.id.iv_choose).setVisibility(View.VISIBLE);
                        holder.itemView.findViewById(R.id.iv_choose).setBackgroundResource(R.drawable.select_blue);
                    }
                } else {
                    Intent data = new Intent();
                    data.putExtra("new_value", nameInfos.get(position).getId());
                    data.putExtra(ParameterUtils.TRANSITION_FLAG, flag);
                    setResult(RESULT_OK, data);
                    finish();
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.topdefault_leftbutton) {
            finish();
        } else if (id == R.id.topdefault_righttext) {
            String names = "";
            for (IdNameInfo nameInfo : nameInfos) {
                if (nameInfo.is_selected()) {
                    names += nameInfo.getId() + ",";
                }
            }
            if (TextUtils.isEmpty(names)) {
                ToastUtils.shortToast(this, "请至少选一项！");
            } else {
                Intent data = new Intent();
                data.putExtra("new_value", names);
                data.putExtra(ParameterUtils.TRANSITION_FLAG, flag);
                setResult(RESULT_OK, data);
                finish();
            }
        }
    }

    @Override
    public void initView() {
        Intent data = getIntent();
        flag = data.getStringExtra(ParameterUtils.TRANSITION_FLAG);
        from = data.getStringExtra("from");
        isChange = data.getBooleanExtra("ischange", false);
        value = data.getStringExtra("value");
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        topdefaultCentertitle.setText(data.getStringExtra("title"));
        rcvList = (RecyclerView) findViewById(R.id.rcv_list);
        rcvList.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvList.setLayoutManager(mLayoutManager);
        if (ParameterUtils.WORK_BUSSIENSS.equals(flag)) {
            topdefaultRighttext.setVisibility(View.VISIBLE);
            topdefaultRighttext.setText("确定");
            topdefaultRighttext.setTextColor(getResources().getColor(R.color.app_main_color));
            findViewById(R.id.tv_multi_tips).setVisibility(View.VISIBLE);
        }
        if (data.hasExtra("list")) {
            nameInfos = initData(value, data.<IdNameInfo>getParcelableArrayListExtra("list"));
        } else {
            nameInfos = new ArrayList<>();
        }
        mAdapter = new CommonAdapter<IdNameInfo>(ChooseListActivity.this, R.layout.item_choose_list, nameInfos, mInflater) {
            @Override
            protected void convert(ViewHolder holder, IdNameInfo idNameInfo, int position) {
                if (position == 0) {
                    holder.getView(R.id.view_first).setVisibility(View.VISIBLE);
                } else {
                    holder.getView(R.id.view_first).setVisibility(View.GONE);
                }
                if (ParameterUtils.WORK_CITY.equals(flag) || ParameterUtils.WORK_BUSSIENSS.equals(flag)) {
                    holder.setText(R.id.tv_list_name, idNameInfo.getValue());
                }

                if (idNameInfo.is_selected()) {
                    holder.getView(R.id.iv_choose).setVisibility(View.VISIBLE);
                    if (ParameterUtils.WORK_BUSSIENSS.equals(flag)) {
                        holder.getView(R.id.iv_choose).setBackgroundResource(R.drawable.select_blue);
                    } else {
                        holder.getView(R.id.iv_choose).setBackgroundResource(R.drawable.ic_square_select);
                    }

                } else {
                    if (ParameterUtils.WORK_BUSSIENSS.equals(flag)) {
                        holder.getView(R.id.iv_choose).setVisibility(View.VISIBLE);
                        holder.getView(R.id.iv_choose).setBackgroundResource(R.drawable.unselect_white);
                    } else {
                        holder.getView(R.id.iv_choose).setVisibility(View.INVISIBLE);
                    }
                }
                if (position == nameInfos.size() - 1) {
                    if (!ParameterUtils.EDIT_SHEHUI_EVENT.equals(flag)) {
                        holder.getView(R.id.view_last).setVisibility(View.VISIBLE);
                    }
                } else {
                    holder.getView(R.id.view_last).setVisibility(View.GONE);
                }
            }
        };
        rcvList.setAdapter(mAdapter);
    }


    private List<IdNameInfo> initData(String value, ArrayList<IdNameInfo> nameInfos) {
        if (nameInfos != null) {
            if (ParameterUtils.WORK_BUSSIENSS.equals(flag)) {
                String[] ids = value.split(",");
                for (IdNameInfo info : nameInfos) {
                    info.setIs_selected(false);
                    for (String id : ids) {
                        if (id.equals(info.getValue())) {
                            info.setIs_selected(true);
                        }
                    }
                }
            } else {
                for (IdNameInfo info : nameInfos) {
                    if (value.equals(info.getValue()) || value.equals(info.getGroup() + info.getValue())) {
                        info.setIs_selected(true);
                    } else {
                        info.setIs_selected(false);
                    }
                }
            }
        } else {
            nameInfos = new ArrayList<>();
        }
        return nameInfos;
    }
}
