package com.smartstudy.counselor_t;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by louis on 2017/12/14.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private FrameLayout contentView;
    private RelativeLayout rlytTop;
    protected ImageView topdefaultLeftbutton;
    protected ImageView topdefaultRightbutton;
    protected TextView topdefaultLefttext;
    protected TextView topdefaultCentertitle;
    protected TextView topdefaultRighttext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        initView(savedInstanceState);
    }

    @Override
    public void setContentView(View view) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        contentView.addView(view, lp);
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = LayoutInflater.from(this).inflate(layoutResID, null);
        setContentView(view);
    }

    private void initView(Bundle savedInstanceState) {
        contentView = findViewById(R.id.content_view);
        rlytTop = findViewById(R.id.rlyt_top);
        topdefaultLeftbutton = findViewById(R.id.topdefault_leftbutton);
        topdefaultRightbutton = findViewById(R.id.topdefault_rightbutton);
        topdefaultLefttext = findViewById(R.id.topdefault_lefttext);
        topdefaultCentertitle = findViewById(R.id.topdefault_centertitle);
        topdefaultRighttext = findViewById(R.id.topdefault_righttext);
        initEvent(savedInstanceState);
    }

    /**
     * 初始化事件
     *
     * @param savedInstanceState
     */
    public abstract void initEvent(Bundle savedInstanceState);

    public void setHeadVisible(int visible) {
        rlytTop.setVisibility(visible);
    }

    public void setLeftImgVisible(int visible) {
        topdefaultLeftbutton.setVisibility(visible);
    }

    public void setRightImgVisible(int visible) {
        topdefaultRightbutton.setVisibility(visible);
    }

    public void setLeftImg(int resId) {
        topdefaultLeftbutton.setBackgroundResource(resId);
    }

    public void setRightImg(int resId) {
        topdefaultRightbutton.setBackgroundResource(resId);
    }

    public void setLeftTxt(String leftTxt) {
        topdefaultLefttext.setText(leftTxt);
    }

    public void setRightTxt(String rightTxt) {
        topdefaultRighttext.setText(rightTxt);
    }

    public void setTitle(String title) {
        topdefaultCentertitle.setText(title);
    }
}