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
public class SelectMyPhotoActivity extends BaseActivity<BasePresenter> implements ListDirPopupWindow.OnImageDirSelected {

    private TextView topdefault_centertitle;
    private TextView topdefault_righttext;
    private RecyclerView mGirdView;
    private TextView mChooseDir;
    private TextView mImageCount;
    private RelativeLayout id_bottom_ly;

    private ProgressDialog mProgressDialog;

    /**
     * 存储文件夹中的图片数量
     */
    private int mPicsSize;

    /**
     * 当前选择的文件夹
     */
    private File mImgDir;

    /**
     * 所有的图片
     */
    private LinkedList<MediaInfo> mImgs = null;
    /**
     * 当前文件夹中的所有图片
     */
    private List<MediaInfo> mDirImgs = new ArrayList<>();

    private SelectMyMediaAdapter mAdapter;

    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<String>();

    /**
     * 扫描拿到所有的图片文件夹
     */
    private List<FloderInfo> mImageFloders = new ArrayList<>();

    int totalCount;

    private int mScreenHeight;

    private ListDirPopupWindow mListImageDirPopupWindow;

    private String firstImage = null;
    private Uri imageUri = null;
    private WeakHandler myHandler = null;
    private File photoSaveFile;// 保存文件夹
    private String photoSaveName = null;// 图片名

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_select_my_media);
    }

    @Override
    protected void onDestroy() {
        if (mGirdView != null) {
            mGirdView.removeAllViews();
            mGirdView = null;
        }
        if (mImgs != null) {
            mImgs.clear();
            mImgs = null;
        }
        if (mDirImgs != null) {
            mDirImgs.clear();
            mDirImgs = null;
        }
        if (mDirPaths != null) {
            mDirPaths.clear();
            mDirPaths = null;
        }
        if (mImageFloders != null) {
            mImageFloders.clear();
            mImageFloders = null;
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
        findViewById(R.id.topdefault_righttext).setOnClickListener(this);
        mChooseDir.setText(ParameterUtils.ALL_PICS);
        mChooseDir.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.topdefault_leftbutton) {
            finish();
        } else if (id == R.id.id_choose_dir) {
            mListImageDirPopupWindow.showAsDropDown(id_bottom_ly, 0, 0);
            Utils.convertActivityFromTranslucent(SelectMyPhotoActivity.this);
            // 设置背景颜色变暗
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = .3f;
            getWindow().setAttributes(lp);
        } else if (id == R.id.topdefault_righttext) {
            //存放选中图片的路径
            ArrayList<String> mPickedList = new ArrayList<>();
            //存放选中的图片的position
            List<Integer> positionList = mAdapter.getSelectItems();
            //拿到选中图片的路径
            for (int i = 0; i < positionList.size(); i++) {
                if (ParameterUtils.ALL_PICS.equals(mChooseDir.getText().toString())) {
                    mPickedList.add(mImgs.get(positionList.get(i)).getPath());
                } else {
                    mPickedList.add(mDirImgs.get(positionList.get(i)).getPath());
                }
            }
            if (mPickedList.size() < 1) {
                return;
            } else {
                //照片多选
                Intent toClipImage = new Intent();
                toClipImage.putStringArrayListExtra("pathList", mPickedList);
                toClipImage.putExtra("flag_from", "from_album");
                setResult(RESULT_OK, toClipImage);
                finish();
            }
        }
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        topdefault_centertitle = (TextView) findViewById(R.id.topdefault_centertitle);
        topdefault_righttext = (TextView) findViewById(R.id.topdefault_righttext);
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
        topdefault_centertitle.setText(getResources().getString(R.string.choose_pic));
        mScreenHeight = ScreenUtils.getScreenHeight();
        getImages();
    }

    private void initItemClick() {
        if (getIntent().getBooleanExtra("singlePic", true)) {
            topdefault_righttext.setVisibility(View.GONE);
            mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    if (ParameterUtils.ALL_PICS.equals(mChooseDir.getText().toString())) {
                        if (position == 0 && mImgs.get(position) == null) {
                            if (PermissionUtil.hasPermissions(SelectMyPhotoActivity.this, Permission.CAMERA)) {
                                toCamera();
                            } else {
                                PermissionUtil.requestPermissions(SelectMyPhotoActivity.this, Permission.getPermissionContent(Arrays.asList(Permission.CAMERA)),
                                    ParameterUtils.REQUEST_CODE_CAMERA, Permission.CAMERA);
                            }
                        } else {
                            String photo_path = mImgs.get(position).getPath();
                            if (!TextUtils.isEmpty(photo_path)) {
                                Intent toClipImage = new Intent(SelectMyPhotoActivity.this, ClipPictureActivity.class);
                                toClipImage.putExtra("path", photo_path);
                                toClipImage.putExtra("clipType", ClipImageLayout.SQUARE);
                                startActivityForResult(toClipImage, ParameterUtils.REQUEST_CODE_CLIP_OVER);
                            } else {
                                showTip(getString(R.string.picture_load_failure));
                            }
                        }
                    } else {
                        String photo_path = mDirImgs.get(position).getPath();
                        Intent toClipImage = new Intent(SelectMyPhotoActivity.this, ClipPictureActivity.class);
                        toClipImage.putExtra("path", photo_path);
                        toClipImage.putExtra("clipType", ClipImageLayout.SQUARE);
                        startActivityForResult(toClipImage, ParameterUtils.REQUEST_CODE_CLIP_OVER);
                    }
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
        } else {
            topdefault_righttext.setText(getString(R.string.sure));
            topdefault_righttext.setVisibility(View.VISIBLE);
            mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    if (ParameterUtils.ALL_PICS.equals(mChooseDir.getText().toString())) {
                        if (position == 0 && "".equals(mImgs.get(position))) {
                            if (PermissionUtil.hasPermissions(SelectMyPhotoActivity.this, Permission.CAMERA)) {
                                toCamera();
                            } else {
                                PermissionUtil.requestPermissions(SelectMyPhotoActivity.this, Permission.getPermissionContent(Arrays.asList(Permission.CAMERA)),
                                    ParameterUtils.REQUEST_CODE_CAMERA, Permission.CAMERA);
                            }
                        }
                    }
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void getImages() {
        totalCount = 0;
        mChooseDir.setText(ParameterUtils.ALL_PICS);
        mImgs = new LinkedList<>();
        mImgs.addFirst(null);
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, getString(R.string.no_external_storage), Toast.LENGTH_SHORT).show();
            return;
        }
        // 显示进度条
        mProgressDialog = ProgressDialog.show(this, null, getString(R.string.loading));

        new Thread(new Runnable() {
            @Override
            public void run() {

                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = SelectMyPhotoActivity.this.getContentResolver();

                // 只查询jpeg、jpg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null, MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                    new String[]{"image/jpeg", "image/jpg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED + " DESC");

                FloderInfo mAllPics = new FloderInfo();
                mAllPics.setIsSelected(true);
                mAllPics.setDir(null);
                if (mCursor != null) {
                    mAllPics.setCount(mCursor.getCount());
                    while (mCursor.moveToNext()) {
                        // 获取图片的路径
                        String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        long size = mCursor.getLong(mCursor.getColumnIndex(MediaStore.Images.Media.SIZE));
                        mImgs.add(new MediaInfo(path, Utils.getFormatSize(size), "image"));
                        // 拿到第一张图片的路径
                        if (firstImage == null) {
                            firstImage = path;
                            mAllPics.setFirstPath(firstImage);
                            mAllPics.setName(ParameterUtils.ALL_PICS);
                            mAllPics.setType("image");
                            mImageFloders.add(mAllPics);
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
                                return filename.endsWith(".jpg") || filename.endsWith(".png")
                                    || filename.endsWith(".jpeg");
                            }
                        });
                        int picSize = 0;
                        if (file_filter != null) {
                            picSize = file_filter.length;
                        }
                        totalCount += picSize;

                        imageFloder.setCount(picSize);
                        imageFloder.setType("image");
                        mImageFloders.add(imageFloder);

                        if (picSize > mPicsSize) {
                            mPicsSize = picSize;
                            mImgDir = parentFile;
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
        if (mImgDir == null) {
            Toast.makeText(getApplicationContext(), getString(R.string.scan_no_picture), Toast.LENGTH_SHORT).show();
//            return;
        }
        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
        mImageCount.setText(totalCount + getString(R.string.picture_unit));
        mAdapter = new SelectMyMediaAdapter(SelectMyPhotoActivity.this, mImgs, R.layout.item_my_select_pic_grid);
        initItemClick();
        mGirdView.setAdapter(mAdapter);
    }

    /**
     * 初始化展示文件夹的popupWindw
     */
    private void initListDirPopupWindw() {
        mListImageDirPopupWindow = new ListDirPopupWindow(LayoutParams.MATCH_PARENT, (int) (mScreenHeight * 0.6),
            mImageFloders, LayoutInflater.from(getApplicationContext())
            .inflate(R.layout.popupwindow_pic_list, null));

        mListImageDirPopupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                // 设置背景颜色变暗
                Utils.convertActivityToTranslucent(SelectMyPhotoActivity.this);
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        // 设置选择文件夹的回调
        mListImageDirPopupWindow.setOnImageDirSelected(this);
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */


    @Override
    public void selected(FloderInfo floder) {
        if (topdefault_righttext.getVisibility() == View.VISIBLE) {
            topdefault_righttext.setText(getString(R.string.sure));
        }
        if (floder.getDir() != null) {
            mImgDir = new File(floder.getDir());
            List<String> Imgs_name = Arrays.asList(mImgDir.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    return filename.endsWith(".jpg") || filename.endsWith(".png")
                        || filename.endsWith(".jpeg");
                }
            }));
            /**
             * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
             */
            mImageCount.setText(floder.getCount() + getString(R.string.picture_unit));
            mDirImgs.clear();
            for (String name : Imgs_name) {
                // size不显示故设置默认值
                mDirImgs.add(new MediaInfo(mImgDir + "/" + name, "0", "image"));
            }
            mAdapter = new SelectMyMediaAdapter(SelectMyPhotoActivity.this, mDirImgs, R.layout.item_my_select_pic_grid);
            initItemClick();
            mGirdView.setAdapter(mAdapter);
            // mAdapter.notifyDataSetChanged();

            mChooseDir.setText(floder.getName());
        } else {
            mDirPaths = new HashSet<>();
            mChooseDir.setText(ParameterUtils.ALL_PICS);
            dataToView();
        }
        mListImageDirPopupWindow.dismiss();

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
                    mImgs.removeFirst();
                    // size不显示故设置默认值
                    mImgs.addFirst(new MediaInfo(imageUri.getPath(), "0", "image"));
                    mImgs.addFirst(null);
                    mAdapter.notifyDataSetChanged();
                    imageUri = null;
                } else {
                    String path_capture = photoSaveFile.getAbsolutePath() + "/" + photoSaveName;
                    Intent toClipImage = new Intent(SelectMyPhotoActivity.this, ClipPictureActivity.class);
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
                toCamera();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        verifyPermission(perms);
    }

    private void toCamera() {
        if (getIntent().getBooleanExtra("singlePic", true)) {
            photoSaveName = System.currentTimeMillis() + ".png";
            // 存放照片的文件夹
            photoSaveFile = SDCardUtils.getFileDirPath("Xxd_im" + File.separator + "pictures");
            Utils.startActionCapture(SelectMyPhotoActivity.this, new File(photoSaveFile.getAbsolutePath(), photoSaveName), ParameterUtils.REQUEST_CODE_CAMERA);
        } else {
            photoSaveName = System.currentTimeMillis() + ".png";
            // 存放照片的文件夹
            photoSaveFile = SDCardUtils.getFileDirPath("Xxd_im" + File.separator + "pictures");
            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (photoSaveFile != null) {
                imageUri = Uri.fromFile(new File(photoSaveFile.getAbsolutePath(), photoSaveName));
            }
            openCameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(openCameraIntent, ParameterUtils.REQUEST_CODE_CAMERA);
        }
    }
}
