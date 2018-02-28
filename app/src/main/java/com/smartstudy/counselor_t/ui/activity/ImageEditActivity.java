package com.smartstudy.counselor_t.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.ui.base.BaseActivity;

import java.io.File;
import java.util.UUID;

import io.rong.imlib.model.Message;
import io.rong.message.ImageMessage;
import me.kareluo.imaging.IMGEditActivity;

/**
 * @author yqy
 * @date on 2018/2/26
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class ImageEditActivity extends BaseActivity {
    private File mImageFile = null;
    private static int REQ_IMAGE_EDIT = 1;
    Message message;
    ImageMessage imageMessage;

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_edit_sample);
    }

    @Override
    public void initView() {
        message = getIntent().getParcelableExtra("msg");
        imageMessage = (ImageMessage) message.getContent();
        if (imageMessage != null) {
            onChooseImages(imageMessage.getLocalUri());
        }
    }


    private void onChooseImages(Uri uri) {
        if (uri != null) {
            mImageFile = new File(this.getCacheDir(), UUID.randomUUID().toString() + ".jpg");
            startActivityForResult(
                    new Intent(this, IMGEditActivity.class)
                            .putExtra(IMGEditActivity.EXTRA_IMAGE_URI, uri)
                            .putExtra(IMGEditActivity.EXTRA_IMAGE_SAVE_PATH, mImageFile.getAbsolutePath()),
                    REQ_IMAGE_EDIT
            );
            finish();
        }
    }
}
