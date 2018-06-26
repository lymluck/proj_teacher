package study.smart.baselib.ui.activity;

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

import com.smartstudy.medialib.ijkplayer.WeakHandler;

import study.smart.baselib.entity.MediaInfo;
import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.ui.adapter.SelectMyMediaAdapter;
import study.smart.baselib.ui.base.BaseActivity;
import study.smart.baselib.utils.DensityUtils;
import study.smart.baselib.utils.ParameterUtils;
import study.smart.baselib.utils.ScreenUtils;
import study.smart.baselib.utils.Utils;
import study.smart.baselib.entity.FloderInfo;
import study.smart.baselib.ui.adapter.MultiItemTypeAdapter;
import study.smart.baselib.ui.widget.popupwindow.ListDirPopupWindow;
import study.smart.baselib.ui.widget.DividerGridItemDecoration;
import study.smart.baselib.R;

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
    private WeakHandler myHandler = null;

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
        mChooseDir.setText(ParameterUtils.ALL_VIDEOS);
        mChooseDir.setOnClickListener(this);
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
                    String photo_path = mVideos.get(position).getPath();
                    if (!TextUtils.isEmpty(photo_path)) {
                        // 选择视频
                        setResult(RESULT_OK, new Intent().putExtra("path", photo_path));
                        finish();
                    } else {
                        showTip(getString(R.string.picture_load_failure));
                    }
                } else {
                    // 选择视频
                    String photo_path = mDirVideos.get(position).getPath();
                    setResult(RESULT_OK, new Intent().putExtra("path", photo_path));
                    finish();
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

                FloderInfo mAllVideos = new FloderInfo();
                mAllVideos.setIsSelected(true);
                mAllVideos.setDir(null);
                if (mCursor != null) {
                    mAllVideos.setCount(mCursor.getCount());
                    while (mCursor.moveToNext()) {
                        // 获取图片的路径
                        String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Video.Media.DATA));
                        long size = mCursor.getLong(mCursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                        mVideos.add(new MediaInfo(path, Utils.getFormatSize(size), "video"));
                        // 拿到第一张图片的路径
                        if (firstVideo == null) {
                            firstVideo = path;
                            mAllVideos.setFirstPath(firstVideo);
                            mAllVideos.setName(ParameterUtils.ALL_VIDEOS);
                            mAllVideos.setType("video");
                            mVideoFloders.add(mAllVideos);
                            mAllVideos = null;
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
                        imageFloder.setType("video");
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
            List<String> videoNames = Arrays.asList(mVideoDir.list(new FilenameFilter() {
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
            for (String name : videoNames) {
                String path = mVideoDir + "/" + name;
                String size = Utils.getFormatSize(new File(path).length());
                mDirVideos.add(new MediaInfo(path, size, "video"));
            }
            mAdapter = new SelectMyMediaAdapter(SelectMyVideoActivity.this, mDirVideos, R.layout.item_my_select_pic_grid);
            initItemClick();
            mGirdView.setAdapter(mAdapter);

            mChooseDir.setText(floder.getName());
        } else {
            mDirPaths = new HashSet<>();
            mChooseDir.setText(ParameterUtils.ALL_VIDEOS);
            dataToView();
        }
        mListDirPopupWindow.dismiss();

    }
}
