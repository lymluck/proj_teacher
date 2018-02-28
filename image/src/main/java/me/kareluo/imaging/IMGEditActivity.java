package me.kareluo.imaging;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;

import com.smartstudy.router.Router;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import io.rong.imlib.model.Message;
import io.rong.message.ImageMessage;
import me.kareluo.imaging.core.IMGMode;
import me.kareluo.imaging.core.IMGText;
import me.kareluo.imaging.core.file.IMGAssetFileDecoder;
import me.kareluo.imaging.core.file.IMGDecoder;
import me.kareluo.imaging.core.file.IMGFileDecoder;
import me.kareluo.imaging.core.util.IMGUtils;

public class IMGEditActivity extends IMGEditBaseActivity {

    private static final int MAX_WIDTH = 1024;

    private static final int MAX_HEIGHT = 1024;

    public static final String EXTRA_IMAGE_URI = "IMAGE_URI";

    public static final String EXTRA_IMAGE_SAVE_PATH = "IMAGE_SAVE_PATH";

    private String[] items = {"发送给朋友", "保存"};

    private File mImageFile = null;

    Message message;

    ImageMessage imageMessage;

    @Override
    public void onCreated() {

    }

    @Override
    public Bitmap getBitmap() {
//        Intent intent = getIntent();
//        if (intent == null) {
//            return null;
//        }
//
//        Uri uri = intent.getParcelableExtra(EXTRA_IMAGE_URI);
//        if (uri == null) {
//            return null;
//        }
        message = getIntent().getParcelableExtra("msg");

        if (message != null) {
            imageMessage = (ImageMessage) message.getContent();
        }

        Uri uri = imageMessage.getLocalUri();
        if (uri == null) {
            return null;
        } else {
            mImageFile = new File(this.getCacheDir(), UUID.randomUUID().toString() + ".jpg");
        }
        IMGDecoder decoder = null;

        String path = uri.getPath();
        if (!TextUtils.isEmpty(path)) {
            switch (uri.getScheme()) {
                case "asset":
                    decoder = new IMGAssetFileDecoder(this, uri);
                    break;
                case "file":
                    decoder = new IMGFileDecoder(uri);
                    break;
            }
        }

        if (decoder == null) {
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        options.inJustDecodeBounds = true;

        decoder.decode(options);

        if (options.outWidth > MAX_WIDTH) {
            options.inSampleSize = IMGUtils.inSampleSize(Math.round(1f * options.outWidth / MAX_WIDTH));
        }

        if (options.outHeight > MAX_HEIGHT) {
            options.inSampleSize = Math.max(options.inSampleSize,
                    IMGUtils.inSampleSize(Math.round(1f * options.outHeight / MAX_HEIGHT)));
        }

        options.inJustDecodeBounds = false;

        Bitmap bitmap = decoder.decode(options);
        if (bitmap == null) {
            return null;
        }

        return bitmap;
    }

    @Override
    public void onText(IMGText text) {
        mImgView.addStickerText(text);
    }

    @Override
    public void onModeClick(IMGMode mode) {
        IMGMode cm = mImgView.getMode();
        if (cm == mode) {
            mode = IMGMode.NONE;
        }
        mImgView.setMode(mode);
        updateModeUI();

        if (mode == IMGMode.CLIP) {
            setOpDisplay(OP_CLIP);
        }
    }

    @Override
    public void onUndoClick() {
        IMGMode mode = mImgView.getMode();
        if (mode == IMGMode.DOODLE) {
            mImgView.undoDoodle();
        } else if (mode == IMGMode.MOSAIC) {
            mImgView.undoMosaic();
        }
    }

    @Override
    public void onCancelClick() {
        finish();
    }

    @Override
    public void onDoneClick() {
        showOptDialog();
    }

    @Override
    public void onCancelClipClick() {
        mImgView.cancelClip();
        setOpDisplay(mImgView.getMode() == IMGMode.CLIP ? OP_CLIP : OP_NORMAL);
    }

    @Override
    public void onDoneClipClick() {
        mImgView.doClip();
        setOpDisplay(mImgView.getMode() == IMGMode.CLIP ? OP_CLIP : OP_NORMAL);
    }

    @Override
    public void onResetClipClick() {
        mImgView.resetClip();
    }

    @Override
    public void onRotateClipClick() {
        mImgView.doRotate();
    }

    @Override
    public void onColorChanged(int checkedColor) {
        mImgView.setPenColor(checkedColor);
    }


    public void showOptDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String path = mImageFile.getAbsolutePath();
                        if (!TextUtils.isEmpty(path)) {
                            Bitmap bitmap = mImgView.saveBitmap();
                            if (bitmap != null) {
                                FileOutputStream fout = null;
                                try {
                                    fout = new FileOutputStream(path);
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fout);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } finally {
                                    if (fout != null) {
                                        try {
                                            fout.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }

                        if (items[which].equals("发送给朋友")) {
                            Router.build("MsgShareActivity").with("msg", message).go(IMGEditActivity.this);
                        }
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                }).create();
        dialog.show();
    }
}
