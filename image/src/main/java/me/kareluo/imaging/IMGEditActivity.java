package me.kareluo.imaging;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.smartstudy.router.Router;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.rong.imlib.model.Message;
import io.rong.message.ImageMessage;
import me.kareluo.imaging.core.IMGMode;
import me.kareluo.imaging.core.IMGText;
import me.kareluo.imaging.core.util.IMGUtils;
import me.kareluo.imaging.file.IMGAssetFileDecoder;
import me.kareluo.imaging.file.IMGDecoder;
import me.kareluo.imaging.file.IMGFileDecoder;
import me.kareluo.imaging.util.ImgUtils;
import me.kareluo.imaging.view.OptionsPopupDialog;

import static android.os.Environment.DIRECTORY_PICTURES;

public class IMGEditActivity extends IMGEditBaseActivity {

    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 1024;

    private String[] items = {"发送给朋友", "保存图片"};

    private File mImageFile = null;

    Message message;

    @Override
    public void onCreated() {

    }

    @Override
    public Bitmap getBitmap() {
        Intent intent = getIntent();
        if (intent == null) {
            return null;
        }
        message = intent.getParcelableExtra("msg");
        Uri uri = intent.getParcelableExtra("uri");
        String filePath = intent.getStringExtra("path");
        if (uri == null) {
            return null;
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
                default:
                    break;
            }
        }

        if (decoder == null) {
            return null;
        } else {
            if (!TextUtils.isEmpty(filePath)) {
                mImageFile = new File(filePath);
            } else {
                mImageFile = new File(this.getExternalFilesDir(DIRECTORY_PICTURES).getAbsolutePath(), System.currentTimeMillis() + ".png");
            }
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
        OptionsPopupDialog dialog = new OptionsPopupDialog(this, items).setOptionsPopupDialogListener(new OptionsPopupDialog.OnOptionsItemClickedListener() {
            @Override
            public void onOptionsItemClicked(int var1) {
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

                if (mImageFile != null) {
                    if (items[var1].equals("发送给朋友")) {
                        if (Uri.fromFile(mImageFile) != null) {
                            ((ImageMessage) message.getContent()).setThumUri(Uri.fromFile(mImageFile));
                            ((ImageMessage) message.getContent()).setLocalUri(Uri.fromFile(mImageFile));
                        }
                        Router.build("MsgShareActivity").with("msg", message).go(IMGEditActivity.this);
                    } else {
                        Bitmap bitmap = BitmapFactory.decodeFile(mImageFile.getPath());
                        boolean isSaveSuccess = ImgUtils.saveImageToGallery(IMGEditActivity.this, bitmap);
                        if (isSaveSuccess) {
                            Toast.makeText(IMGEditActivity.this, "保存图片成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(IMGEditActivity.this, "保存图片失败，请稍后重试", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                setResult(RESULT_CANCELED);
                finish();
            }

        });
        dialog.show();
    }

}
