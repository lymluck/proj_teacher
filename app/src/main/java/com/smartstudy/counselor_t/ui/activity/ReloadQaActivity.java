package com.smartstudy.counselor_t.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.mvp.contract.ReloadQaContract;
import com.smartstudy.counselor_t.mvp.presenter.ReloadQaPresenter;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.ui.widget.AudioRecordView;
import com.smartstudy.counselor_t.ui.widget.audio.AudioRecorder;
import com.smartstudy.counselor_t.util.ToastUtils;

import java.io.File;

/**
 * @author yqy
 * @date on 2018/3/9
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class ReloadQaActivity extends BaseActivity<ReloadQaContract.Presenter> implements ReloadQaContract.View {
    private AudioRecordView audioRecordView;
    private String questionId;
    ProgressDialog pdDialog;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reqa);
        setHeadVisible(View.GONE);
        questionId = getIntent().getStringExtra("question_id");
    }

    @Override
    public void initView() {
        audioRecordView = findViewById(R.id.arv);
        setFinishOnTouchOutside(false);
        audioRecordView.startRecord();
        audioRecordView.setsendOnClickListener(new AudioRecordView.SendOnClickListener() {
            @Override
            public void sendOnClick(String path) {
                file = new File(path);
                if (file.exists()) {
                    pdDialog = new ProgressDialog(ReloadQaActivity.this);
                    pdDialog.setMessage("正在上传中");
                    pdDialog.show();
                    presenter.postAnswerVoice(questionId, file);
                }
            }
        });
        audioRecordView.setAgainRecordOnclick(new AudioRecordView.AgainRecordOnclick() {
            @Override
            public void againRecordOnclick() {
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        overridePendingTransition(0, 0);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (presenter != null) {
            presenter = null;
        }
        if (AudioRecorder.getInstance() != null) {
            AudioRecorder.getInstance().setReset();
        }
        super.onDestroy();
    }

    @Override
    public ReloadQaContract.Presenter initPresenter() {
        return new ReloadQaPresenter(this);
    }

    @Override
    public void postAnswerSuccess() {
        if (pdDialog != null && pdDialog.isShowing()) {
            pdDialog.dismiss();
        }
        ToastUtils.shortToast("发送成功");
        if (file.exists()) {
            file.delete();
        }
        finish();
    }

    @Override
    public void postAnswerFail(String message) {
        ToastUtils.shortToast(message);
        if (pdDialog != null && pdDialog.isShowing()) {
            pdDialog.dismiss();
        }
    }
}
