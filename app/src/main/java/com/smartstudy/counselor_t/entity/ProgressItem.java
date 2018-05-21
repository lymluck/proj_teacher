package com.smartstudy.counselor_t.entity;

/**
 * @author yqy
 * @date on 2018/5/21
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class ProgressItem {
    private int progress;

    private String voideoUrl;

    public String getVoideoUrl() {
        return voideoUrl;
    }

    public void setVoideoUrl(String voideoUrl) {
        this.voideoUrl = voideoUrl;
    }

    public ProgressItem(int progress, String voideoUrl) {
        this.progress = progress;
        this.voideoUrl = voideoUrl;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
