package com.smartstudy.counselor_t.ui.activity;

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
import com.smartstudy.counselor_t.util.BitmapUtils;
import com.smartstudy.counselor_t.util.SPCacheUtils;
import com.smartstudy.counselor_t.util.ToastUtils;
import com.smartstudy.router.Router;
import com.smartstudy.zbar.zxing.QRCodeDecoder;

import java.io.File;
import java.lang.ref.WeakReference;

import io.rong.common.FileUtils;
import io.rong.imageloader.core.ImageLoader;
import io.rong.imkit.RongIM;
import io.rong.imkit.activity.PicturePagerActivity;
import io.rong.imkit.tools.RongWebviewActivity;
import io.rong.imkit.utilities.OptionsPopupDialog;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;
import me.kareluo.imaging.IMGEditActivity;

import static me.kareluo.imaging.IMGEditActivity.REQUEST_EDIT;
import static me.kareluo.imaging.IMGEditActivity.REQUEST_SHARE;

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
        String[] items = new String[]{"发送给朋友", this.getString(io.rong.imkit.R.string.rc_save_picture), "编辑图片"};
        OptionsPopupDialog dialog = OptionsPopupDialog.newInstance(this, items).setOptionsPopupDialogListener(new OptionsPopupDialog.OnOptionsItemClickedListener() {
            @Override
            public void onOptionsItemClicked(int which) {
                switch (which) {
                    case 0:
                        Router.build("MsgShareActivity").with("uri", Uri.fromFile(file))
                                .with("type", "image")
                                .requestCode(REQUEST_SHARE).go(ImPicPagerActivity.this);
                        break;
                    case 1:
                        savePic(file);
                        break;
                    case 2:
                        startActivityForResult(new Intent(ImPicPagerActivity.this, IMGEditActivity.class)
                                .putExtra("uri", Uri.parse("file://" + file.getAbsolutePath())), REQUEST_EDIT);
                        break;
                    default:
                        break;
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
        private WeakReference<ImPicPagerActivity> weakAty;
        private File mFile;
        private OptionsPopupDialog mDialog;

        public MyTask(ImPicPagerActivity activity, File file, OptionsPopupDialog dialog) {
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
                    final ImPicPagerActivity mActivity = weakAty.get();
                    String[] items = new String[]{"发送给朋友", mActivity.getString(io.rong.imkit.R.string.rc_save_picture), "编辑图片", mActivity.getString(R.string.open_qrcode)};
                    OptionsPopupDialog.newInstance(mActivity, items).setOptionsPopupDialogListener(new OptionsPopupDialog.OnOptionsItemClickedListener() {
                        @Override
                        public void onOptionsItemClicked(int which) {
                            switch (which) {
                                case 0:
                                    Router.build("MsgShareActivity").with("uri", Uri.fromFile(mFile))
                                            .with("type", "image")
                                            .requestCode(REQUEST_SHARE).go(mActivity);
                                    break;
                                case 1:
                                    savePic(mFile);
                                    break;
                                case 2:
                                    mActivity.startActivityForResult(new Intent(mActivity, IMGEditActivity.class)
                                            .putExtra("uri", Uri.parse("file://" + mFile.getAbsolutePath())), REQUEST_EDIT);
                                    break;
                                case 3:
                                    //跳转到result这个url
                                    mActivity.startActivity(new Intent(mActivity, RongWebviewActivity.class)
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_EDIT:
                handleResult(data);
                break;
            case REQUEST_SHARE:
                handleResult(data);
                break;
            default:
                break;
        }
    }


    private void handleResult(Intent data) {
        String targetId = data.getStringExtra("targetId");
        String msgType = data.getStringExtra("type");
        String word = data.getStringExtra("word");
        if ("text".equals(msgType)) {
            String content = data.getStringExtra("content");
            sendTextMsg(targetId, content);
        } else if ("image".equals(msgType)) {
            Uri uri = data.getParcelableExtra("uri");
            String path = data.getStringExtra("path");
            sendImageMsg(targetId, uri, path);
        }
        if (!TextUtils.isEmpty(word)) {
            sendTextMsg(targetId, word);
        }
        finish();
    }

    private void sendTextMsg(final String userId, String content) {
        TextMessage myTextMessage = TextMessage.obtain(content);
        Message myMessage = Message.obtain(userId, Conversation.ConversationType.PRIVATE, myTextMessage);
        RongIM.getInstance().sendMessage(myMessage, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
                //消息本地数据库存储成功的回调
            }

            @Override
            public void onSuccess(Message message) {
                //消息通过网络发送成功的回调
                String myId = (String) SPCacheUtils.get("imUserId", "");
                if (!userId.equals(myId)) {
                    ToastUtils.shortToast(ImPicPagerActivity.this, "已发送");
                }
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                //消息发送失败的回调
                String myId = (String) SPCacheUtils.get("imUserId", "");
                if (!userId.equals(myId)) {
                    ToastUtils.shortToast(ImPicPagerActivity.this, "消息发送失败！");
                }
            }
        });
    }

    private void sendImageMsg(final String userId, Uri uri, final String filePath) {
        ImageMessage sendImgMsg = ImageMessage.obtain(uri, uri, false);
        RongIM.getInstance().sendImageMessage(Conversation.ConversationType.PRIVATE, userId, sendImgMsg, null, null, new RongIMClient.SendImageMessageCallback() {
            @Override
            public void onAttached(Message message) {

            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                String myId = (String) SPCacheUtils.get("imUserId", "");
                if (!userId.equals(myId)) {
                    ToastUtils.shortToast(ImPicPagerActivity.this, "消息发送失败！");
                }
            }

            @Override
            public void onSuccess(Message message) {
                if (!TextUtils.isEmpty(filePath)) {
                    BitmapUtils.deleteFile(filePath);
                }
                String myId = (String) SPCacheUtils.get("imUserId", "");
                if (!userId.equals(myId)) {
                    ToastUtils.shortToast(ImPicPagerActivity.this, "消息已发送");
                }
            }

            @Override
            public void onProgress(Message message, int i) {
            }
        });

    }
}
