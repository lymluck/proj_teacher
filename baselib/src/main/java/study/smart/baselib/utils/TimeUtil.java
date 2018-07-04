package study.smart.baselib.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yqy
 * @date on 2018/7/3
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TimeUtil {
    /**
     * 时间戳转为时间(年月日，时分秒)
     *
     * @param cc_time 时间戳
     * @return
     */
    public static String getStrTime(String cc_time) {
        Date date = null;
        try {
            if (TextUtils.isEmpty(cc_time)) {
                return "";
            }
            date = new SimpleDateFormat("yyyy-MM-dd").parse(cc_time);
            String str = (new SimpleDateFormat("yyyy-MM-dd")).format(date);
            return str;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}