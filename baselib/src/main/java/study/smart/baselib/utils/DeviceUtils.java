package study.smart.baselib.utils;

import android.content.Context;
import android.os.Build;

import java.util.UUID;


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
    public static String getUniquePsuedoID() {
        String serial = null;
        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 位
        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial";
        }
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    /**
     * 获取设备型号
     */
    public static String getDeviceModel(Context context) {
        return Build.DEVICE + ":" + Build.MODEL;
    }
}
