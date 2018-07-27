package study.smart.transfer_management.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import study.smart.baselib.ui.adapter.CommonAdapter;
import study.smart.baselib.ui.adapter.MultiItemTypeAdapter;
import study.smart.baselib.ui.adapter.base.ViewHolder;
import study.smart.baselib.ui.base.UIFragment;
import study.smart.baselib.ui.widget.HorizontalDividerItemDecoration;
import study.smart.baselib.ui.widget.NoScrollLinearLayoutManager;
import study.smart.baselib.utils.DensityUtils;
import study.smart.baselib.utils.DisplayImageUtils;
import study.smart.baselib.utils.ParameterUtils;
import study.smart.baselib.utils.ToastUtils;
import study.smart.transfer_management.R;
import study.smart.transfer_management.entity.MessageInfo;
import study.smart.transfer_management.mvp.contract.TransferManagerMessageContract;
import study.smart.transfer_management.mvp.presenter.TransferManagerMessagePresenter;
import study.smart.transfer_management.ui.activity.CommonSearchActivity;
import study.smart.transfer_management.ui.activity.TransferManagerMessageDetailActivity;

/**
 * @author yqy
 * @date on 2018/7/6
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MessageFragment extends UIFragment<TransferManagerMessageContract.Presenter> implements TransferManagerMessageContract.View {
    private LinearLayout llSearch;
    private RecyclerView rvMessage;
    private NoScrollLinearLayoutManager mLayoutManager;
    private List<MessageInfo> messageInfos;
    private CommonAdapter<MessageInfo> mAdapter;

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        presenter.getMessageInfos();
    }

    @Override
    protected View getLayoutView() {
        return mActivity.getLayoutInflater().inflate(R.layout.fragment_message_center, null);
    }

    @Override
    protected void initView(View rootView) {
        llSearch = rootView.findViewById(R.id.ll_search);
        rvMessage = rootView.findViewById(R.id.rv_message);
        rvMessage.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(mActivity);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvMessage.setLayoutManager(mLayoutManager);
        rvMessage.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mActivity)
            .size(DensityUtils.dip2px(0.5f)).margin(DensityUtils.dip2px(68f), DensityUtils.dip2px(16f)).colorResId(R.color.horizontal_line_color).build());
        initAdapter();
    }

    @Override
    protected void initEvent() {
        llSearch.setOnClickListener(this);
    }

    private void initAdapter() {
        messageInfos = new ArrayList<>();
        mAdapter = new CommonAdapter<MessageInfo>(mActivity, R.layout.item_message_info, messageInfos) {
            @Override
            protected void convert(ViewHolder holder, MessageInfo messageInfo, int position) {
                holder.setText(R.id.tv_title, messageInfo.getTitle());
                holder.setText(R.id.tv_content, messageInfo.getContent());
                holder.setText(R.id.tv_time, messageInfo.getCreatedAtText());
                TextView tvCount = holder.getView(R.id.tv_count);
                ImageView ivLogo = holder.getView(R.id.iv_logo);
                if ("TASK_TRAINING".equals(messageInfo.getType())) {
                    DisplayImageUtils.displayCircleImage(mActivity, R.drawable.transfer_task_manager, ivLogo);
                } else if ("TRANSFER_CASE".equals(messageInfo.getType())) {
                    DisplayImageUtils.displayCircleImage(mActivity, R.drawable.transfer_icon_manager, ivLogo);
                } else {
                    DisplayImageUtils.displayCircleImage(mActivity, R.drawable.transfer_student_manager, ivLogo);
                }
                if (messageInfo.getUnReadCount() > 99) {
                    tvCount.setText("99+");
                    tvCount.setBackgroundResource(R.drawable.bg_message_count_border9);
                } else {
                    tvCount.setText(messageInfo.getUnReadCount() + "");
                    tvCount.setBackgroundResource(R.drawable.bg_message_unread);
                }
            }
        };
        rvMessage.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                startActivity(new Intent(mActivity, TransferManagerMessageDetailActivity.class).putExtra("type", messageInfos.get(position).getType()));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    public TransferManagerMessageContract.Presenter initPresenter() {
        return new TransferManagerMessagePresenter(this);
    }

    @Override
    public void showTip(String message) {
        ToastUtils.shortToast(message);
    }

    @Override
    public void getMessageInfoSuccess(List<MessageInfo> messageInfos) {
        if (messageInfos != null) {
            this.messageInfos.clear();
            this.messageInfos.addAll(messageInfos);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.ll_search) {
            Intent toSearch = new Intent(mActivity, CommonSearchActivity.class);
            toSearch.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.MSG_DETAIL);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(toSearch, ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity,
                    llSearch, "btn_tr").toBundle());
            } else {
                startActivity(toSearch);
            }
        }
    }
}
