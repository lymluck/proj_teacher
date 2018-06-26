package study.smart.baselib.entity;

import java.util.List;

/**
 * @author yqy
 * @date on 2018/1/12
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class CommonTarge extends CommonStudentInfo {

    private List<Options> options;


    public List<Options> getOptions() {
        return options;
    }

    public void setOptions(List<Options> options) {
        this.options = options;
    }
}
