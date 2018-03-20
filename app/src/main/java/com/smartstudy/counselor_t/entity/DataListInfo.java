package com.smartstudy.counselor_t.entity;

/**
 * Created by louis on 2017/3/2.
 */

public class DataListInfo {

    private PaginationInfo pagination;
    private String data;
    private Meta meta;

    public PaginationInfo getPagination() {
        return pagination;
    }

    public void setPagination(PaginationInfo pagination) {
        this.pagination = pagination;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public class Meta {
        private int totalSubQuestionCountToMe;


        public int getTotalSubQuestionCountToMe() {
            return totalSubQuestionCountToMe;
        }

        public void setTotalSubQuestionCountToMe(int totalSubQuestionCountToMe) {
            this.totalSubQuestionCountToMe = totalSubQuestionCountToMe;
        }
    }

    @Override
    public String toString() {
        return "DataListInfo{" +
                "paginationInfo=" + pagination +
                ", data='" + data + '\'' +
                ", meta='" + meta + '\'' +
                '}';
    }
}