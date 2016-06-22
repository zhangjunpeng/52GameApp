package com.view.accountsetting;

/**
 * Created by Administrator on 2016/2/24.
 */
public class CityInfo {
    private String id;
    private String name;
    private String upid;
    private String level;

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

    public String getUpid() {
        return upid;
    }

    public void setUpid(String upid) {
        this.upid = upid;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "CityInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", upid='" + upid + '\'' +
                ", level='" + level + '\'' +
                '}';
    }
}
