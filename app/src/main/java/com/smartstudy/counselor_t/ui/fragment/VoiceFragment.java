package com.smartstudy.counselor_t.ui.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.ui.base.UIFragment;
import com.smartstudy.counselor_t.ui.widget.AudioRecordView;
import com.smartstudy.counselor_t.ui.widget.audio.AudioRecorder;

import io.rong.imlib.model.Message;
import io.rong.message.ImageMessage;

/**
 * @author yqy
 * @date on 2018/2/27
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class VoiceFragment extends UIFragment {
    private AudioRecordView audioRecordView;
    private ImageView imageView;

    @Override
    protected View getLayoutView() {
        return mActivity.getLayoutInflater().inflate(
                R.layout.activity_voice, null);
    }

    @Override
    protected void initView(View rootView) {
        audioRecordView = rootView.findViewById(R.id.ar);
//        audioRecordView.setAudioRecord(new AudioRecorder());
        imageView = rootView.findViewById(R.id.iv_voice);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationDrawable animationDrawable = (AnimationDrawable) getActivity().getResources().getDrawable(io.rong.imkit.R.drawable.rc_an_voice_receive);
                imageView.setImageDrawable(animationDrawable);
                if (animationDrawable != null) {
                    animationDrawable.start();
                }
            }
        });
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }


}
