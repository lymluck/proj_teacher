package com.smartstudy.counselor_t.entity;

import android.text.TextUtils;

/**
 * Created by louis on 2017/3/2.
 */

public class ResponseInfo {

    private String code;
    private String data;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return TextUtils.equals(code, "0");
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"code\":\"")
                .append(code).append('\"');
        sb.append(",\"data\":\"")
                .append(data).append('\"');
        sb.append(",\"msg\":\"")
                .append(msg).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
