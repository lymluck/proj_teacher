package com.smartstudy.counselor_t.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.app.BaseApplication;
import com.smartstudy.counselor_t.util.ToastUtils;
import com.smartstudy.zbar.zxing.QRCodeDecoder;

import java.io.File;
import java.lang.ref.WeakReference;

import io.rong.common.FileUtils;
import io.rong.imageloader.core.ImageLoader;
import io.rong.imkit.activity.PicturePagerActivity;
import io.rong.imkit.tools.RongWebviewActivity;
import io.rong.imkit.utilities.OptionsPopupDialog;

/**
 * @author louis
 * @date on 2018/3/2
 * @describe 图片浏览
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */

public class ImPicPagerActivity extends PicturePagerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onPictureLongClick(View v, Uri thumbUri, Uri largeImageUri) {
        if (largeImageUri == null) {
            return false;
        }

        final File file;
        if (!largeImageUri.getScheme().startsWith("http") && !largeImageUri.getScheme().startsWith("https")) {
            file = new File(largeImageUri.getPath());
        } else {
            file = ImageLoader.getInstance().getDiskCache().get(largeImageUri.toString());
        }
        String[] items = new String[]{this.getString(io.rong.imkit.R.string.rc_save_picture)};
        OptionsPopupDialog dialog = OptionsPopupDialog.newInstance(this, items).setOptionsPopupDialogListener(new OptionsPopupDialog.OnOptionsItemClickedListener() {
            @Override
            public void onOptionsItemClicked(int which) {
                if (which == 0) {
                    savePic(file);
                }
            }
        });
        dialog.show();
        new MyTask(this, file, dialog).execute();
        return true;
    }

    public static void savePic(File file) {
        File path = Environment.getExternalStorageDirectory();
        String defaultPath = BaseApplication.appContext.getString(io.rong.imkit.R.string.rc_image_default_saved_path);
        File dir = new File(path, defaultPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        if (file != null && file.exists()) {
            String name = System.currentTimeMillis() + ".jpg";
            FileUtils.copyFile(file, dir.getPath() + File.separator, name);
            MediaScannerConnection.scanFile(BaseApplication.appContext, new String[]{dir.getPath() + File.separator + name}, (String[]) null, (MediaScannerConnection.OnScanCompletedListener) null);
            ToastUtils.shortToast(BaseApplication.appContext, String.format(BaseApplication.appContext.getString(io.rong.imkit.R.string.rc_save_picture_at), new Object[]{dir.getPath() + File.separator + name}));
        } else {
            ToastUtils.shortToast(BaseApplication.appContext, BaseApplication.appContext.getString(io.rong.imkit.R.string.rc_src_file_not_found));
        }
    }

    static class MyTask extends AsyncTask<String, Integer, String> {
        private WeakReference<Activity> weakAty;
        private File mFile;
        private OptionsPopupDialog mDialog;

        public MyTask(Activity activity, File file, OptionsPopupDialog dialog) {
            weakAty = new WeakReference<>(activity);
            mFile = file;
            mDialog = dialog;
        }

        @Override
        protected String doInBackground(String... params) {
            return QRCodeDecoder.syncDecodeQRCode(mFile.getPath());
        }

        @Override
        protected void onPostExecute(final String result) {
            if (!TextUtils.isEmpty(result)) {
                if (mDialog != null && mDialog.isShowing()) {
                    String[] items = new String[]{weakAty.get().getString(io.rong.imkit.R.string.rc_save_picture), weakAty.get().getString(R.string.open_qrcode)};
                    OptionsPopupDialog.newInstance(weakAty.get(), items).setOptionsPopupDialogListener(new OptionsPopupDialog.OnOptionsItemClickedListener() {
                        @Override
                        public void onOptionsItemClicked(int which) {
                            switch (which) {
                                case 0:
                                    savePic(mFile);
                                    break;
                                case 1:
                                    //跳转到result这个url
                                    weakAty.get().startActivity(new Intent(weakAty.get(), RongWebviewActivity.class)
                                            .putExtra("url", result));
                                    break;
                                default:
                                    break;
                            }
                        }
                    }).show();
                    mDialog.dismiss();
                }
            }
        }
    }
}
