package com.smartstudy.counselor_t.entity;

public class FloderInfo {
    /**
     * 文件夹路径
     */
    private String dir;

    /**
     * 第一张的路径
     */
    private String firstPath;

    /**
     * 文件夹的名称
     */
    private String name;

    /**
     * 数量
     */
    private int count;

    private boolean isSelected = false;

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
        if (dir == null) {
            this.name = "所有图片";
        } else {
            int lastIndexOf = this.dir.lastIndexOf("/");
            this.name = this.dir.substring(lastIndexOf + 1);
        }
    }

    public String getFirstPath() {
        return firstPath;
    }

    public void setFirstPath(String firstPath) {
        this.firstPath = firstPath;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

}
