package com.smartstudy.counselor_t.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.util.DisplayImageUtils;

import java.io.File;
import java.util.UUID;

import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
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
    private String imageUri;
    private static int REQ_IMAGE_EDIT = 1;
    private int REQ_IMAGE_CHOOSE = 2;
    private ImageView sdv_image;
    private ImageView sdv_image_edit;
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
//        sdv_image = findViewById(R.id.sdv_image);
//        sdv_image_edit = findViewById(R.id.sdv_image_edit);
//        DisplayImageUtils.downloadImageFile(this, Uri,imageMessage.getThumUri(), new SimpleTarget<File>() {
//            @Override
//            public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
//
//            }
//        });
        if (imageMessage != null) {
            onChooseImages(imageMessage.getLocalUri());
        }
    }


    private void onChooseImages(Uri uri) {
        if (uri != null) {
//            sdv_image.setImageURI(uri);
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

    private void onImageEditDone() {
        ((ImageMessage) message.getContent()).setThumUri(Uri.fromFile(mImageFile));
//        sdv_image_edit.setImageURI(Uri.fromFile(mImageFile));
//        if (imageMessage.getRemoteUri() != null && !imageMessage.getRemoteUri().toString().startsWith("file")) {
//            RongIM.getInstance().sendMessage(message, "", "", new IRongCallback.ISendMessageCallback() {
//                @Override
//                public void onAttached(Message message) {
//
//                }
//
//                @Override
//                public void onSuccess(Message message) {
//
//                }
//
//                @Override
//                public void onError(Message message, RongIMClient.ErrorCode errorCode) {
//
//                }
//            });
//        } else {


        RongIM.getInstance().sendImageMessage(message, "", "", new RongIMClient.SendImageMessageCallback() {

            @Override
            public void onAttached(Message message) {

                Log.w("kim", "-----");
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                Log.w("kim", "=====");
            }

            @Override
            public void onSuccess(Message message) {
                Log.w("kim", "11111");
            }

            @Override
            public void onProgress(Message message, int i) {
                Log.w("kim", "222222");

            }
        });
        finish();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_IMAGE_EDIT) {
            if (resultCode == RESULT_OK) {
                onImageEditDone();
            }
        }
    }
}
