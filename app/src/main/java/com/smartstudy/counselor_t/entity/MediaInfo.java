package com.smartstudy.counselor_t.entity;

/**
 * @author louis
 * @date on 2018/5/17
 * @describe 视频信息
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class MediaInfo {

    public MediaInfo(String path, long duration) {
        this.path = path;
        this.duration = duration;
    }

    private String path;
    private long duration;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
