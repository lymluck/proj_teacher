package com.smartstudy.counselor_t.ui.fragment;

import android.view.View;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.ui.base.UIFragment;
import com.smartstudy.counselor_t.ui.widget.AudioRecordView;
import com.smartstudy.counselor_t.ui.widget.audio.AudioRecorder;
import com.smartstudy.counselor_t.ui.widget.audio.RecordStrategy;

import io.rong.imkit.fragment.BaseFragment;

/**
 * @author yqy
 * @date on 2018/2/6
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class QaFragment extends UIFragment {

    private AudioRecordView arv;

    @Override
    protected View getLayoutView() {
        return mActivity.getLayoutInflater().inflate(R.layout.fragment_qa_list, null);
    }

    @Override
    protected void initView(View rootView) {
        arv = rootView.findViewById(R.id.arv);
        arv.setAudioRecord(new AudioRecorder());

    }

    @Override
    protected void initEvent() {

    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }
}
