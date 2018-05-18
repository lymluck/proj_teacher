package com.smartstudy.counselor_t.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.QaDetailInfo;
import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.ui.widget.DrawableTextView;
import com.smartstudy.counselor_t.util.DisplayImageUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author yqy
 * @date on 2018/5/2
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class QaDetailGuideActivity extends BaseActivity {
    private ImageView ivAsker;
    private TextView tvAskerName;
    private TextView tvQuestion;
    private TextView tvAskerTime;
    private DrawableTextView dtvFocus;
    private LinearLayout llFocus;
    private int step = 1;
    private DrawableTextView dtvCallout;
    private ImageView ivStep1;
    private ImageView ivStep2;
    private ImageView ivStep3;
    private LinearLayout llImage;
    private DrawableTextView dtvTelephone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa_detail_guide);
        EventBus.getDefault().register(this);
        setHeadVisible(View.INVISIBLE);
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initEvent() {
        super.initEvent();
        llFocus.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_focus:
                if (step == 1) {
                    step = 2;
                    ivStep1.setVisibility(View.GONE);
                    dtvFocus.setVisibility(View.INVISIBLE);
                    dtvCallout.setVisibility(View.VISIBLE);
                    llImage.setGravity(Gravity.CENTER);
                    ivStep2.setVisibility(View.VISIBLE);
                    dtvCallout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    break;
                }
                if (step == 2) {
                    step = 3;
                    ivStep2.setVisibility(View.GONE);
                    dtvCallout.setVisibility(View.INVISIBLE);
                    dtvTelephone.setVisibility(View.VISIBLE);
                    llImage.setGravity(Gravity.RIGHT);
                    ivStep3.setVisibility(View.VISIBLE);
                    dtvTelephone.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    break;
                }
                if (step == 3) {
                    finish();
                    break;
                }
            default:
                finish();
                break;
        }
    }

    @Override
    public void initView() {
        ivAsker = findViewById(R.id.iv_asker);
        tvAskerName = findViewById(R.id.tv_asker_name);
        tvQuestion = findViewById(R.id.tv_question);
        tvAskerTime = findViewById(R.id.tv_ask_time);
        dtvFocus = findViewById(R.id.dtv_focus);
        llFocus = findViewById(R.id.ll_focus);
        dtvCallout = findViewById(R.id.dtv_callout);
        ivStep1 = findViewById(R.id.iv_step1);
        ivStep2 = findViewById(R.id.iv_step2);
        ivStep3 = findViewById(R.id.iv_step3);
        llImage = findViewById(R.id.ll_image);
        dtvTelephone = findViewById(R.id.dtv_telephone);
        ivStep1.setVisibility(View.VISIBLE);
        dtvFocus.setVisibility(View.VISIBLE);
        dtvFocus.setBackgroundColor(Color.parseColor("#FFFFFF"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(QaDetailInfo data) {
        if (data != null) {
            DisplayImageUtils.formatPersonImgUrl(getApplicationContext(), data.getAsker().getAvatar(), ivAsker);
            tvAskerName.setText(data.getAsker().getName());
            tvAskerTime.setText(data.getCreateTimeText());
            tvQuestion.setText(data.getContent());
        }
    }
}