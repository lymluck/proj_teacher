package io.rong.imkit.plugin.image;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

    private int mCurrentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                .putExtra("uri", Uri.parse("file://" + PictureSelectorActivity.PicItemHolder.itemList.get(mCurrentIndex).uri)), REQUEST_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_EDIT:
                finish();
                break;
            default:
                break;
        }
    }
}
