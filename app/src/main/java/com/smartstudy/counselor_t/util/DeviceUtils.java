package com.smartstudy.counselor_t.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import com.smartstudy.counselor_t.app.BaseApplication;


/**
 * 获取设备信息
 * Created by louis on 2017/3/4.
 */
public class DeviceUtils {

    /**
     * 获取系统版本号
     *
     * @param context
     * @return
     */
    public static String getSystemName(Context context) {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取系统版本号
     *
     * @param context
     * @return
     */
    public static int getSystemVersion(Context context) {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取系统唯一标识
     *
     * @return
     */
    public static String getIdentifier() {
        String id;
        TelephonyManager mTelephony = (TelephonyManager) BaseApplication.appContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(BaseApplication.appContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            id = "";
        }
        if (mTelephony.getDeviceId() != null) {
            id = mTelephony.getDeviceId();
        } else {
            //android.provider.Settings;
            id = Settings.Secure.getString(BaseApplication.appContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return id;
    }

    /**
     * 获取设备型号
     */
    public static String getDeviceModel(Context context) {
        return Build.DEVICE + ":" + Build.MODEL;
    }
}
