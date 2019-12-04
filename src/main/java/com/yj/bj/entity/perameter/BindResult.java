package com.yj.bj.entity.perameter;

/**
 * Created by bin on 2018/4/9.
 */
public class BindResult implements java.io.Serializable{
    //业务结果
    private String state;
    //业务结果描述
    private String description;
    //通道进件ID
    private String aisleMerId;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAisleMerId() {
        return aisleMerId;
    }

    public void setAisleMerId(String aisleMerId) {
        this.aisleMerId = aisleMerId;
    }
}
