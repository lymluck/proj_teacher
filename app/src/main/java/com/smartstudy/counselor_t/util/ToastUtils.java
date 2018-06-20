package com.smartstudy.counselor_t.util;

import android.widget.Toast;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.app.BaseApplication;

public class ToastUtils {

    private static Toast mToast;

    /**
     * 非阻塞试显示Toast,防止出现连续点击Toast时的显示问题
     */
    public static void showToast(CharSequence text, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(BaseApplication.appContext, text, duration);
        } else {
            mToast.setText(text);
            mToast.setDuration(duration);
        }
        mToast.show();
    }

    public static void shortToast(CharSequence text) {
        showToast(text, Toast.LENGTH_SHORT);
    }

    public static void longToast(CharSequence text) {
        showToast(text, Toast.LENGTH_LONG);
    }

    public static void showNotNull(CharSequence text) {
        showToast(String.format(BaseApplication.appContext.getString(R.string.not_null), text), Toast.LENGTH_SHORT);
    }
}
