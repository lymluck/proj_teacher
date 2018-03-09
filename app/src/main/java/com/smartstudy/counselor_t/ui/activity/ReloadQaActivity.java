package com.smartstudy.counselor_t.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reqa);
        setHeadVisible(View.GONE);
        questionId = getIntent().getStringExtra("question_id");
        Log.w("kim", "questionId--->" + questionId);
    }

    @Override
    public void initView() {
        audioRecordView = findViewById(R.id.arv);
        setFinishOnTouchOutside(false);
        audioRecordView.startRecord();

        audioRecordView.setsendOnClickListener(new AudioRecordView.SendOnClickListener() {
            @Override
            public void sendOnClick(String path) {
                File file = new File(path);
                if (file.exists()) {
                    pdDialog = new ProgressDialog(ReloadQaActivity.this);
                    pdDialog.setMessage("正在加载中");
                    pdDialog.show();
                    ToastUtils.shortToast(ReloadQaActivity.this, "发送成功");
                    presenter.postAnswerVoice(questionId, file);

                }
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
        finish();

    }
}
