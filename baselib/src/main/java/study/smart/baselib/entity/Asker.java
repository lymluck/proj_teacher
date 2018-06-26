package study.smart.baselib.entity;

import java.io.Serializable;

/**
 * Created by yqy on 2017/12/4.
 */

public class Asker implements Serializable {
    private String id;

    private String name;

    private String avatar;

    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}