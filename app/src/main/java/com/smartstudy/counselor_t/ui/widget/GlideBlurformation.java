package com.smartstudy.counselor_t.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.smartstudy.counselor_t.util.BlurBitmapUtil;

import java.security.MessageDigest;

/**
 * @author louis
 * @date on 2018/5/16
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class GlideBlurformation extends BitmapTransformation {
    private Context context;

    public GlideBlurformation(Context context) {
        this.context = context;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return BlurBitmapUtil.instance().blurBitmap(context, toTransform, 25, outWidth, outHeight);
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
    }
}
