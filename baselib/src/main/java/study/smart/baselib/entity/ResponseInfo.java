package study.smart.baselib.entity;

import android.text.TextUtils;

/**
 * Created by louis on 2017/3/2.
 */

public class ResponseInfo<T> {

    private String code;
    private T data;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
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
