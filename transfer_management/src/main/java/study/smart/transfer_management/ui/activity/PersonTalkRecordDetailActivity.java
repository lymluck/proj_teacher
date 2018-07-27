package study.smart.transfer_management.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import study.smart.baselib.entity.MyTalkRecordInfo;
import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.ui.adapter.CommonAdapter;
import study.smart.baselib.ui.adapter.base.ViewHolder;
import study.smart.baselib.ui.adapter.wrapper.HeaderAndFooterWrapper;
import study.smart.baselib.ui.base.BaseActivity;
import study.smart.baselib.ui.widget.NoScrollLinearLayoutManager;
import study.smart.transfer_management.R;

/**
 * @author yqy
 * @date on 2018/7/25
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class PersonTalkRecordDetailActivity extends BaseActivity {
    private RecyclerView rvTalk;
    private HeaderAndFooterWrapper mHeader;
    private View headView;
    private MyTalkRecordInfo.ShowMonth.DataListInfo dataListInfo;
    private NoScrollLinearLayoutManager mLayoutManager;
    private CommonAdapter<MyTalkRecordInfo.ShowMonth.DataListInfo.CommunicationList> mAdapter;
    private List<MyTalkRecordInfo.ShowMonth.DataListInfo.CommunicationList> communicationListList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_talk_record);
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        dataListInfo = (MyTalkRecordInfo.ShowMonth.DataListInfo) getIntent().getSerializableExtra("talk_detail");
        rvTalk = findViewById(R.id.rv_talk);
        rvTalk.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvTalk.setLayoutManager(mLayoutManager);
        initAdapter();
        initHeaderAndFooter();
    }

    private void initAdapter() {
        communicationListList = new ArrayList<>();
        mAdapter = new CommonAdapter<MyTalkRecordInfo.ShowMonth.DataListInfo.CommunicationList>(this, R.layout.item_person_talk_record, communicationListList) {
            @Override
            protected void convert(ViewHolder holder, MyTalkRecordInfo.ShowMonth.DataListInfo.CommunicationList communicationList, int position) {
                holder.setText(R.id.tv_title, communicationList.getType());
                holder.setText(R.id.tv_feedback, communicationList.getFeedback());
                holder.setText(R.id.tv_question, communicationList.getQuestion());
                holder.setText(R.id.tv_evaluation, communicationList.getEvaluation());
            }
        };
        mHeader = new HeaderAndFooterWrapper(mAdapter);
    }

    private void initHeaderAndFooter() {
        headView = mInflater.inflate(R.layout.layout_person_talk_record, null, false);
        if (dataListInfo != null) {
            setTitle(dataListInfo.getUserName() + "的沟通记录");
            TextView tvRccordName = headView.findViewById(R.id.tv_rccord_name);
            TextView tvUserType = headView.findViewById(R.id.tv_user_type);
            TextView tvMethod = headView.findViewById(R.id.tv_method);
            TextView tvContactType = headView.findViewById(R.id.tv_contact_type);
            TextView tvReason = headView.findViewById(R.id.tv_reason);
            tvRccordName.setText(dataListInfo.getName());
            tvUserType.setText(dataListInfo.getUserTypeText());
            tvMethod.setText(dataListInfo.getMethodText());
            tvContactType.setText(dataListInfo.getTypeText());
            tvReason.setText(dataListInfo.getReason());

            if (dataListInfo.getCommunicationList() != null) {
                this.communicationListList.clear();
                this.communicationListList.addAll(dataListInfo.getCommunicationList());
                mHeader.notifyDataSetChanged();
            }
        }
        mHeader = new HeaderAndFooterWrapper(mAdapter);
        mHeader.addHeaderView(headView);
        rvTalk.setAdapter(mHeader);
    }
}
