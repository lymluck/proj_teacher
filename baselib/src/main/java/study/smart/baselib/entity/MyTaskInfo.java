package study.smart.baselib.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author yqy
 * @date on 2018/7/25
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyTaskInfo implements Serializable {
    private String year;
    private List<DataMonths> months;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<DataMonths> getMonths() {
        return months;
    }

    public void setMonths(List<DataMonths> months) {
        this.months = months;
    }

    public class DataMonths implements Serializable {
        private String key;
        private String year;
        private List<TaskDetailInfo> list;
        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<TaskDetailInfo> getList() {
            return list;
        }

        public void setList(List<TaskDetailInfo> list) {
            this.list = list;
        }
    }
}
