package com.smartstudy.counselor_t.util;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import io.rong.imlib.model.Message;
import io.rong.message.FileMessage;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * @author louis
 * @date on 2018/1/29
 * @describe RongIM相关类
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */

public class RongUtils {

    public static String setTitleTag(Message message) {
        String extra = null;
        String titleTag = "";
        if (message.getContent() instanceof TextMessage) {
            extra = ((TextMessage) message.getContent()).getExtra();
        }
        if (message.getContent() instanceof ImageMessage) {
            extra = ((ImageMessage) message.getContent()).getExtra();
        }
        if (message.getContent() instanceof LocationMessage) {
            extra = ((LocationMessage) message.getContent()).getExtra();
        }
        if (message.getContent() instanceof FileMessage) {
            extra = ((FileMessage) message.getContent()).getExtra();
        }
        if (message.getContent() instanceof VoiceMessage) {
            extra = ((VoiceMessage) message.getContent()).getExtra();
        }
        if (!TextUtils.isEmpty(extra)) {
            JSONObject object = JSON.parseObject(extra);
            String year = Utils.getStringNum(object.getString("abroadyear"));
            if (!TextUtils.isEmpty(year)) {
                titleTag += year + " | ";
            }
            String country = object.getString("country");
            if (!TextUtils.isEmpty(country)) {
                titleTag += country + " | ";
            }
            String grade = object.getString("grade");
            if (!TextUtils.isEmpty(grade)) {
                titleTag += grade;
            }
            SPCacheUtils.put("titleTag", titleTag);
        }
        return titleTag;
    }
}
