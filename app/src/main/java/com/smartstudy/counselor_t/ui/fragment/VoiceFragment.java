package com.smartstudy.counselor_t.ui.fragment;

import android.view.View;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.ui.base.UIFragment;
import com.smartstudy.counselor_t.ui.widget.AudioRecordView;
import com.smartstudy.counselor_t.ui.widget.audio.AudioRecorder;

/**
 * @author yqy
 * @date on 2018/2/27
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class VoiceFragment extends UIFragment {
    private AudioRecordView audioRecordView;
    @Override
    protected View getLayoutView() {
        return mActivity.getLayoutInflater().inflate(
                R.layout.activity_voice, null);
    }

    @Override
    protected void initView(View rootView) {
        audioRecordView=rootView.findViewById(R.id.ar);
        audioRecordView.setAudioRecord(new AudioRecorder());
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }
}
