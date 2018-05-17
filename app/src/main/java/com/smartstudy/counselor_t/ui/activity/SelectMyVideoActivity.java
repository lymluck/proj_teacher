package com.smartstudy.counselor_t.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler.Callback;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.FloderInfo;
import com.smartstudy.counselor_t.entity.MediaInfo;
import com.smartstudy.counselor_t.handler.WeakHandler;
import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.ui.adapter.MultiItemTypeAdapter;
import com.smartstudy.counselor_t.ui.adapter.SelectMyMediaAdapter;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.ui.popupwindow.ListDirPopupWindow;
import com.smartstudy.counselor_t.ui.widget.ClipImageLayout;
import com.smartstudy.counselor_t.ui.widget.DividerGridItemDecoration;
import com.smartstudy.counselor_t.util.DensityUtils;
import com.smartstudy.counselor_t.util.ParameterUtils;
import com.smartstudy.counselor_t.util.SDCardUtils;
import com.smartstudy.counselor_t.util.ScreenUtils;
import com.smartstudy.counselor_t.util.Utils;
import com.smartstudy.permissions.Permission;
import com.smartstudy.permissions.PermissionUtil;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * @author yqy
 */
public class SelectMyVideoActivity extends BaseActivity<BasePresenter> implements ListDirPopupWindow.OnImageDirSelected {

    private TextView topdefault_centertitle;
    private RecyclerView mGirdView;
    private TextView mChooseDir;
    private TextView mImageCount;
    private RelativeLayout id_bottom_ly;

    private ProgressDialog mProgressDialog;

    /**
     * 存储文件夹中的视频数量
     */
    private int mVideosSize;

    /**
     * 当前选择的文件夹
     */
    private File mVideoDir;

    /**
     * 所有的视频
     */
    private LinkedList<MediaInfo> mVideos = null;
    /**
     * 当前文件夹中的所有视频
     */
    private List<MediaInfo> mDirVideos = new ArrayList<>();

    private SelectMyMediaAdapter mAdapter;

    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<String>();

    /**
     * 扫描拿到所有的视频文件夹
     */
    private List<FloderInfo> mVideoFloders = new ArrayList<>();

    int totalCount;

    private int mScreenHeight;

    private ListDirPopupWindow mListDirPopupWindow;

    private String firstVideo = null;
    private Uri imageUri = null;
    private WeakHandler myHandler = null;
    private File saveFile;// 保存文件夹
    private String saveName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_select_my_photo);
    }

    @Override
    protected void onDestroy() {
        if (mGirdView != null) {
            mGirdView.removeAllViews();
            mGirdView = null;
        }
        if (mVideos != null) {
            mVideos.clear();
            mVideos = null;
        }
        if (mDirVideos != null) {
            mDirVideos.clear();
            mDirVideos = null;
        }
        if (mDirPaths != null) {
            mDirPaths.clear();
            mDirPaths = null;
        }
        if (mVideoFloders != null) {
            mVideoFloders.clear();
            mVideoFloders = null;
        }
        super.onDestroy();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
    }


    @Override
    public void initEvent() {
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        TextView tvChooseDir = findViewById(R.id.id_choose_dir);
        tvChooseDir.setText(ParameterUtils.ALL_VIDEOS);
        tvChooseDir.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.topdefault_leftbutton) {
            finish();
        } else if (id == R.id.id_choose_dir) {
            mListDirPopupWindow.showAsDropDown(id_bottom_ly, 0, 0);
            Utils.convertActivityFromTranslucent(SelectMyVideoActivity.this);
            // 设置背景颜色变暗
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = .3f;
            getWindow().setAttributes(lp);
        }
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        topdefault_centertitle = (TextView) findViewById(R.id.topdefault_centertitle);
        mGirdView = (RecyclerView) findViewById(R.id.id_gridView);
        mGirdView.setHasFixedSize(true);
        int spanCount = 3;
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, spanCount);
        mGirdView.addItemDecoration(new DividerGridItemDecoration(spanCount, DensityUtils.dip2px(8), true));
        mGirdView.setLayoutManager(mLayoutManager);
        mChooseDir = (TextView) findViewById(R.id.id_choose_dir);
        mImageCount = (TextView) findViewById(R.id.id_total);
        id_bottom_ly = (RelativeLayout) findViewById(R.id.id_bottom_ly);
        myHandler = new WeakHandler(new Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                mProgressDialog.dismiss();
                // 为View绑定数据
                dataToView();
                // 初始化展示文件夹的popupWindw
                initListDirPopupWindw();
                return false;
            }
        });
        topdefault_centertitle.setText(getResources().getString(R.string.choose_video));
        mScreenHeight = ScreenUtils.getScreenHeight();
        getImages();
    }

    private void initItemClick() {
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (ParameterUtils.ALL_VIDEOS.equals(mChooseDir.getText().toString())) {
                    if (position == 0 && mVideos.get(position) == null) {
                        if (PermissionUtil.hasPermissions(SelectMyVideoActivity.this, Permission.CAMERA)) {
                            toVideo();
                        } else {
                            PermissionUtil.requestPermissions(SelectMyVideoActivity.this, Permission.getPermissionContent(Arrays.asList(Permission.CAMERA)),
                                ParameterUtils.REQUEST_CODE_CAMERA, Permission.CAMERA);
                        }
                    } else {
                        String photo_path = mVideos.get(position).getPath();
                        if (!TextUtils.isEmpty(photo_path)) {
                            // 选择视频


//                            Intent toClipImage = new Intent(SelectMyVideoActivity.this, ClipPictureActivity.class);
//                            toClipImage.putExtra("path", photo_path);
//                            toClipImage.putExtra("clipType", ClipImageLayout.SQUARE);
//                            startActivityForResult(toClipImage, ParameterUtils.REQUEST_CODE_CLIP_OVER);
                        } else {
                            showTip(getString(R.string.picture_load_failure));
                        }
                    }
                } else {
                    // 选择视频


//                    String photo_path = mDirImgs.get(position);
//                    Intent toClipImage = new Intent(SelectMyVideoActivity.this, ClipPictureActivity.class);
//                    toClipImage.putExtra("path", photo_path);
//                    toClipImage.putExtra("clipType", ClipImageLayout.SQUARE);
//                    startActivityForResult(toClipImage, ParameterUtils.REQUEST_CODE_CLIP_OVER);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void getImages() {
        totalCount = 0;
        mChooseDir.setText(ParameterUtils.ALL_VIDEOS);
        mVideos = new LinkedList<>();
        mVideos.addFirst(null);
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, getString(R.string.no_external_storage), Toast.LENGTH_SHORT).show();
            return;
        }
        // 显示进度条
        mProgressDialog = ProgressDialog.show(this, null, getString(R.string.loading));

        new Thread(new Runnable() {
            @Override
            public void run() {

                Uri mImageUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = SelectMyVideoActivity.this.getContentResolver();

                // 只查询mpeg、mp4和3gpp的视频
                Cursor mCursor = mContentResolver.query(mImageUri, null, MediaStore.Video.Media.MIME_TYPE + "=? or "
                        + MediaStore.Video.Media.MIME_TYPE + "=? or " + MediaStore.Video.Media.MIME_TYPE + "=?",
                    new String[]{"video/mpeg", "video/mp4", "video/3gpp"}, MediaStore.Video.Media.DATE_MODIFIED + " DESC");

                FloderInfo mAllPics = new FloderInfo();
                mAllPics.setIsSelected(true);
                mAllPics.setDir(null);
                if (mCursor != null) {
                    mAllPics.setCount(mCursor.getCount());
                    while (mCursor.moveToNext()) {
                        // 获取图片的路径
                        String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Video.Media.DATA));
                        long duration = mCursor.getLong(mCursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                        mVideos.add(new MediaInfo(path, duration));
                        // 拿到第一张图片的路径
                        if (firstVideo == null) {
                            firstVideo = path;
                            mAllPics.setFirstPath(firstVideo);
                            mVideoFloders.add(mAllPics);
                        }

                        // 获取该图片的父路径名
                        File parentFile = new File(path).getParentFile();
                        if (parentFile == null) {
                            continue;
                        }

                        String dirPath = parentFile.getAbsolutePath();
                        FloderInfo imageFloder = null;
                        // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
                        if (mDirPaths.contains(dirPath)) {
                            continue;
                        } else {
                            mDirPaths.add(dirPath);
                            // 初始化imageFloder
                            imageFloder = new FloderInfo();
                            imageFloder.setDir(dirPath);
                            imageFloder.setFirstPath(path);
                            imageFloder.setIsSelected(false);
                        }
                        String[] file_filter = parentFile.list(new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String filename) {
                                return filename.endsWith(".mpeg") || filename.endsWith(".mp4")
                                    || filename.endsWith(".3gpp");
                            }
                        });
                        int picSize = 0;
                        if (file_filter != null) {
                            picSize = file_filter.length;
                        }
                        totalCount += picSize;

                        imageFloder.setCount(picSize);
                        mVideoFloders.add(imageFloder);

                        if (picSize > mVideosSize) {
                            mVideosSize = picSize;
                            mVideoDir = parentFile;
                        }
                    }
                    mCursor.close();
                }

                // 扫描完成，辅助的HashSet也就可以释放内存了
                mDirPaths = null;

                // 通知Handler扫描图片完成
                myHandler.sendEmptyMessage(0x110);
            }
        }).start();
    }

    /**
     * 为View绑定数据
     */
    private void dataToView() {
        if (mVideoDir == null) {
            Toast.makeText(getApplicationContext(), getString(R.string.scan_no_video), Toast.LENGTH_SHORT).show();
//            return;
        }
        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
        mImageCount.setText(totalCount + getString(R.string.video_unit));
        mAdapter = new SelectMyMediaAdapter(SelectMyVideoActivity.this, mVideos, R.layout.item_my_select_pic_grid);
        initItemClick();
        mGirdView.setAdapter(mAdapter);
    }

    /**
     * 初始化展示文件夹的popupWindw
     */
    private void initListDirPopupWindw() {
        mListDirPopupWindow = new ListDirPopupWindow(LayoutParams.MATCH_PARENT, (int) (mScreenHeight * 0.6),
            mVideoFloders, LayoutInflater.from(getApplicationContext())
            .inflate(R.layout.popupwindow_pic_list, null));

        mListDirPopupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                // 设置背景颜色变暗
                Utils.convertActivityToTranslucent(SelectMyVideoActivity.this);
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        // 设置选择文件夹的回调
        mListDirPopupWindow.setOnImageDirSelected(this);
    }

    /**
     * 利用ContentProvider扫描手机中的视频，此方法在运行在子线程中 完成视频的扫描，最终获得视频最多的那个文件夹
     */
    @Override
    public void selected(FloderInfo floder) {
        if (floder.getDir() != null) {
            mVideoDir = new File(floder.getDir());
            List<String> Imgs_name = Arrays.asList(mVideoDir.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    return filename.endsWith(".mpeg") || filename.endsWith(".mp4")
                        || filename.endsWith(".3gpp");
                }
            }));
            /**
             * 可以看到文件夹的路径和视频的路径分开保存，极大的减少了内存的消耗；
             */
            mImageCount.setText(floder.getCount() + getString(R.string.picture_unit));
            mDirVideos.clear();
            for (String name : Imgs_name) {
                mDirVideos.add(new MediaInfo(mVideoDir + "/" + name, 0));
            }
            mAdapter = new SelectMyMediaAdapter(SelectMyVideoActivity.this, mDirVideos, R.layout.item_my_select_pic_grid);
            initItemClick();
            mGirdView.setAdapter(mAdapter);

            mChooseDir.setText(floder.getName());
        } else {
            mDirPaths = new HashSet<>();
            getImages();
        }
        mListDirPopupWindow.dismiss();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ParameterUtils.REQUEST_CODE_CAMERA:
                if (imageUri != null) {
                    Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imageUri);
                    sendBroadcast(localIntent);
                    mVideos.removeFirst();
                    mVideos.addFirst(new MediaInfo(imageUri.getPath(), 0));
                    mVideos.addFirst(null);
                    mAdapter.notifyDataSetChanged();
                    imageUri = null;
                } else {
                    String path_capture = saveFile.getAbsolutePath() + "/" + saveName;
                    Intent toClipImage = new Intent(SelectMyVideoActivity.this, ClipPictureActivity.class);
                    toClipImage.putExtra("path", path_capture);
                    toClipImage.putExtra("clipType", ClipImageLayout.SQUARE);
                    startActivityForResult(toClipImage, ParameterUtils.REQUEST_CODE_CLIP_OVER);
                }
                break;
            case ParameterUtils.REQUEST_CODE_CLIP_OVER:
                setResult(RESULT_OK, data);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        switch (requestCode) {
            case ParameterUtils.REQUEST_CODE_CAMERA:
                toVideo();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        verifyPermission(perms);
    }

    private void toVideo() {
        if (getIntent().getBooleanExtra("singlePic", true)) {
            saveName = System.currentTimeMillis() + ".png";
            // 存放照片的文件夹
            saveFile = SDCardUtils.getFileDirPath("Xxd_im" + File.separator + "videos");
            Utils.startActionCapture(SelectMyVideoActivity.this, new File(saveFile.getAbsolutePath(), saveName), ParameterUtils.REQUEST_CODE_CAMERA);
        } else {
            saveName = System.currentTimeMillis() + ".png";
            // 存放照片的文件夹
            saveFile = SDCardUtils.getFileDirPath("Xxd_im" + File.separator + "videos");
            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (saveFile != null) {
                imageUri = Uri.fromFile(new File(saveFile.getAbsolutePath(), saveName));
            }
            openCameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(openCameraIntent, ParameterUtils.REQUEST_CODE_CAMERA);
        }
    }
}
