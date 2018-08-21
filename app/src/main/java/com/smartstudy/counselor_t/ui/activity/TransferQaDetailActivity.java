package com.smartstudy.counselor_t.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.DistributionInfo;
import com.smartstudy.counselor_t.mvp.contract.TransferQaDetailContract;
import com.smartstudy.counselor_t.mvp.presenter.TransferQaDetailPresenter;

import study.smart.baselib.ui.base.BaseActivity;
import study.smart.baselib.utils.DisplayImageUtils;

/**
 * @author yqy
 * @date on 2018/8/14
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TransferQaDetailActivity extends BaseActivity<TransferQaDetailContract.Presenter> implements TransferQaDetailContract.View {
    private DistributionInfo distributionInfo;
    private String title;
    private ImageView ivAsk;
    private TextView tvAskName;
    private TextView tvAskTime;
    private TextView tvQa;
    private TextView tvLocation;
    private TextView tvSchoolName;
    private TextView tvAnswerCount;
    private ImageView ivTransfer;
    private TextView tvTransferName;
    private TextView tvContent;
    private View vCricle;
    private TextView tvType;
    private TextView tvReceive;
    private TextView tvContinueAnswer;
    private TextView tvCreateTime;
    private TextView tvCenterName;
    private LinearLayout llContactUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_qa);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        tvContinueAnswer.setOnClickListener(this);
        llContactUser.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_receive:
                presenter.postReceive(distributionInfo.getQuestionId(), distributionInfo.getSenderId());
                break;
            case R.id.tv_continue_answer:
                startActivity(new Intent(this, QaDetailActivity.class).putExtra("id", distributionInfo.getQuestionId()));
                break;

            case R.id.ll_contact_user:
                if (!TextUtils.isEmpty(distributionInfo.getQuestion().getAsker().getPhone())) {
                    Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + distributionInfo.getQuestion().getAsker().getPhone()));//跳转到拨号界面，同时传递电话号码
                    startActivity(dialIntent);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public TransferQaDetailContract.Presenter initPresenter() {
        return new TransferQaDetailPresenter(this);
    }

    @Override
    public void initView() {
        distributionInfo = (DistributionInfo) getIntent().getSerializableExtra("detail_info");
        title = getIntent().getStringExtra("title");
        setTitle(title);
        ivAsk = findViewById(R.id.iv_asker);
        tvAskName = findViewById(R.id.tv_ask_name);
        tvAskTime = findViewById(R.id.tv_ask_time);
        tvQa = findViewById(R.id.tv_qa);
        tvLocation = findViewById(R.id.tv_location);
        tvSchoolName = findViewById(R.id.tv_schoolName);
        tvAnswerCount = findViewById(R.id.tv_answer_count);
        ivTransfer = findViewById(R.id.iv_transfer);
        tvTransferName = findViewById(R.id.tv_transfer_name);
        tvContent = findViewById(R.id.tv_content);
        llContactUser = findViewById(R.id.ll_contact_user);
        tvReceive = findViewById(R.id.tv_receive);
        vCricle = findViewById(R.id.v_cricle);
        tvCenterName = findViewById(R.id.tv_center_name);
        tvCreateTime = findViewById(R.id.tv_create_time);
        tvType = findViewById(R.id.tv_type);
        tvContinueAnswer = findViewById(R.id.tv_continue_answer);
        if (distributionInfo != null) {
            DisplayImageUtils.displayPersonImage(this, distributionInfo.getQuestion().getAsker().getAvatar(), ivAsk);
            tvAskName.setText(distributionInfo.getQuestion().getAsker().getName());
            tvAskTime.setText(distributionInfo.getQuestion().getCreateTimeText());
            tvQa.setText(distributionInfo.getQuestion().getContent());
            tvCreateTime.setText(distributionInfo.getCreateTimeText());
//            tvCenterName.setText(distributionInfo.get);
            if (distributionInfo.getQuestion().getAnswerCount() == 0) {
                tvAnswerCount.setText("暂无人回答");
                tvAnswerCount.setTextColor(Color.parseColor("#078CF1"));
                vCricle.setVisibility(View.GONE);
            } else {
                if (distributionInfo.getQuestion().getSubQuestionCount() != 0) {
                    tvAnswerCount.setText("对你有 " + distributionInfo.getQuestion().getSubQuestionCount() + " 追问");
                    tvAnswerCount.setTextColor(Color.parseColor("#F6611D"));
                    vCricle.setVisibility(View.VISIBLE);
                } else {
                    tvAnswerCount.setText(distributionInfo.getQuestion().getAnswerCount() + " 回答");
                    tvAnswerCount.setTextColor(Color.parseColor("#949BA1"));
                    vCricle.setVisibility(View.GONE);
                }
            }

            if (TextUtils.isEmpty(distributionInfo.getQuestion().getUserLocation())) {
                tvLocation.setVisibility(View.GONE);
            } else {
                tvLocation.setVisibility(View.VISIBLE);
                tvLocation.setText(distributionInfo.getQuestion().getUserLocation());
            }

            if (TextUtils.isEmpty(distributionInfo.getQuestion().getSchoolName())) {
                tvSchoolName.setVisibility(View.GONE);
            } else {
                tvSchoolName.setVisibility(View.VISIBLE);
                tvSchoolName.setText(distributionInfo.getQuestion().getSchoolName());
            }

            DisplayImageUtils.displayPersonImage(this, distributionInfo.getReceiver().getAvatar(), ivTransfer);
            tvTransferName.setText(distributionInfo.getReceiver().getName());
            if (TextUtils.isEmpty(distributionInfo.getNote())) {
                tvContent.setVisibility(View.GONE);
            } else {
                tvContent.setVisibility(View.VISIBLE);
                if ("我转出的".equals(title)) {
                    tvContent.setText("我：" + distributionInfo.getNote());
                } else {
                    tvContent.setText(distributionInfo.getNote());
                }
            }

            if ("我转出的".equals(title)) {
                tvType.setText("接收人");
                tvCenterName.setText(distributionInfo.getReceiver().getGroup());
                tvType.setBackgroundResource(R.drawable.bg_sent);
                if (!distributionInfo.isReceived()) {
                    tvReceive.setText("未接收");
                    tvReceive.setTextColor(Color.parseColor("#58646E"));
                } else {
                    tvReceive.setText("已接收");
                    tvReceive.setTextColor(Color.parseColor("#949BA1"));
                }
                tvReceive.setOnClickListener(null);
                tvReceive.setBackgroundResource(R.drawable.bg_receiver_gray);
            } else {
                tvType.setText("转发人");
                tvCenterName.setText(distributionInfo.getSender().getGroup());
                tvType.setBackgroundResource(R.drawable.bg_receiver);
                tvReceive.setBackgroundResource(R.drawable.bg_receive_blue);
                if (distributionInfo.isReceived()) {
                    tvReceive.setText("已接收");
                    tvReceive.setOnClickListener(null);
                    tvReceive.setTextColor(Color.parseColor("#949BA1"));
                    tvReceive.setBackgroundResource(R.drawable.bg_receiver_gray);
                } else {
                    tvReceive.setText("接 收");
                    tvReceive.setOnClickListener(this);
                    tvReceive.setTextColor(Color.parseColor("#078CF1"));
                    tvReceive.setBackgroundResource(R.drawable.bg_receive_blue);
                }
            }
        }
    }

    @Override
    public void postReceiveSuccess() {
        tvReceive.setText("已接收");
        tvReceive.setOnClickListener(null);
        tvReceive.setTextColor(Color.parseColor("#949BA1"));
        tvReceive.setBackgroundResource(R.drawable.bg_receiver_gray);
    }
}
