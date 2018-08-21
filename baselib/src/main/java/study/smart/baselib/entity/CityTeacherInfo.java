package study.smart.baselib.entity;

import java.util.List;

/**
 * @author yqy
 * @date on 2018/8/15
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class CityTeacherInfo {
    private String group;
    private boolean isSelect;
    private List<Teacher> list;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public List<Teacher> getList() {
        return list;
    }

    public void setList(List<Teacher> list) {
        this.list = list;
    }

}
