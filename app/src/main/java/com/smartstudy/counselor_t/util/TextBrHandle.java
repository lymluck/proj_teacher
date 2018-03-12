package com.smartstudy.counselor_t.util;

import android.text.TextUtils;
import android.util.Log;

/**
 * @author yqy
 * @date on 2018/3/10
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TextBrHandle {
    public static String parseContent(String content) {
        if (!TextUtils.isEmpty(content)) {
            content = content.replace("\n", "<br />");
        }
        return content;
    }
}
