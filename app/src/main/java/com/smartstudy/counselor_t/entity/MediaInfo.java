package com.smartstudy.counselor_t.entity;

import android.provider.MediaStore;

/**
 * @author louis
 * @date on 2018/5/17
 * @describe 视频信息
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class MediaInfo {

    public MediaInfo(String path, String size,String type) {
        this.path = path;
        this.size = size;
        this.type = type;
    }

    private String path;
    private String size;
    private String type;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
