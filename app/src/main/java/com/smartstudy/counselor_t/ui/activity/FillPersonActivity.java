package com.smartstudy.counselor_t.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.util.DisplayImageUtils;

/**
 * @author yqy
 * @date on 2018/1/16
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class FillPersonActivity extends BaseActivity {

    private ImageView ivAvatar;
    private ImageView ivPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_person_info);
        setTopLineVisibility(View.VISIBLE);
        setLeftImgVisible(View.INVISIBLE);
        setTitle("完善个人信息");

    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
//        DisplayImageUtils.displayCircleImage();
        ivAvatar = findViewById(R.id.iv_avatar);
        ivPhoto = findViewById(R.id.iv_photo);
        DisplayImageUtils.displayCircleImage(this, "77/fa/77fa2d9eb368d911e2ecb809212ea2d451fc.jpg", ivPhoto);
//        DisplayImageUtils.displayCircleImage(this,"https://bkd-media.smartstudy.com/user/avatar/default/77/fa/77fa2d9eb368d911e2ecb809212ea2d451fc.jpg",ivAvatar);
    }
}
