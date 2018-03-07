package io.rong.imkit.plugin.image;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import io.rong.imkit.R;
import me.kareluo.imaging.IMGEditActivity;

import static me.kareluo.imaging.IMGEditActivity.REQUEST_EDIT;


/**
 * @author louis
 * @date on 2018/3/6
 * @describe 预览图片
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */

public class ImPicPreviewActivity extends PicturePreviewActivity {

    private ArrayList<PictureSelectorActivity.PicItem> mItemList;
    private int mCurrentIndex;
    private HackyViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mViewPager = findViewById(R.id.viewpager);
        TextView tv_edit = findViewById(R.id.tv_edit);
        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toEditImg();
            }
        });
        this.mCurrentIndex = this.getIntent().getIntExtra("index", 0);
    }

    private void toEditImg() {
        startActivityForResult(new Intent(this, IMGEditActivity.class)
                .putExtra("showDialog", false)
                .putExtra("path", PictureSelectorActivity.PicItemHolder.itemList.get(mCurrentIndex).uri), REQUEST_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_EDIT:
                PictureSelectorActivity.PicItemHolder.itemList.get(mCurrentIndex).uri = data.getStringExtra("path");
                if (this.mItemList == null) {
                    this.mItemList = PictureSelectorActivity.PicItemHolder.itemList;
                }
                mViewPager.setAdapter(mViewPager.getAdapter());
                mViewPager.setCurrentItem(mCurrentIndex);
                break;
            default:
                break;
        }
    }
}
