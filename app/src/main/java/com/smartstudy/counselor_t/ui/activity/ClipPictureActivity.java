package com.smartstudy.counselor_t.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.ui.widget.ClipImageLayout;
import com.smartstudy.counselor_t.util.DisplayImageUtils;
import com.smartstudy.counselor_t.util.ToastUtils;

import java.io.File;

import static android.icu.lang.UCharacter.DecompositionType.CIRCLE;
import static android.text.TextUtils.isEmpty;
import static android.view.View.VISIBLE;
import static android.view.Window.FEATURE_NO_TITLE;
import static com.smartstudy.counselor_t.ui.widget.ClipImageLayout.SQUARE;

public class ClipPictureActivity extends BaseActivity {

    private TextView topdefault_centertitle;

    private ClipImageLayout mClipImageLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(FEATURE_NO_TITLE);// 去掉标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_picture);
    }

    @Override
    public void initView() {
        topdefault_centertitle = (TextView) findViewById(R.id.topdefault_centertitle);
        topdefault_centertitle.setText(getString(R.string.move_and_zoom));
        Intent intent = getIntent();
        String type = intent.getStringExtra("clipType");
        if (type.equals(CIRCLE)) {
            mClipImageLayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout_circle);
        } else if (type.equals(SQUARE)) {
            mClipImageLayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout_square);
        }
        mClipImageLayout.setVisibility(VISIBLE);
        String path = intent.getStringExtra("path");
        if (isEmpty(path) || !(new File(path).exists())) {
            ToastUtils.shortToast(this, getString(R.string.picture_load_failure));
            return;
        }
        DisplayImageUtils.displayImage(ClipPictureActivity.this, path, new SimpleTarget<Bitmap>(600, 600) {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                if (resource == null) {
                    ToastUtils.shortToast(ClipPictureActivity.this, getString(R.string.picture_load_failure));
                    return;
                }
                mClipImageLayout.setBitmap(resource);
            }
        });
    }

    @Override
    public void initEvent() {
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        findViewById(R.id.id_action_clip).setOnClickListener(this);
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.topdefault_leftbutton) {
            finish();
        } else if (id == R.id.id_action_clip) {
            Bitmap bitmap = mClipImageLayout.clip();
            DisplayImageUtils.displayImageFile(ClipPictureActivity.this, bitmap, new SimpleTarget<File>() {
                @Override
                public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                    Intent intent = new Intent();
                    intent.putExtra("path", resource.getAbsolutePath());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
    }
}
