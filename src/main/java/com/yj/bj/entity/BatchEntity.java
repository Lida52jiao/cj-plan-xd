package com.yj.bj.entity;

import javax.persistence.*;

/**
 * Created by bin on 2018/9/28.
 */
@Table(name = "plan_batch")
public class BatchEntity implements java.io.Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column(name = "batch")
    private String batch;
    @Column(name = "aisleCode")
    private String aisleCode;
    @Column(name = "executeDate")
    private Long executeDate;
    @Column(name = "number")
    private Long number;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getAisleCode() {
        return aisleCode;
    }

    public void setAisleCode(String aisleCode) {
        this.aisleCode = aisleCode;
    }

    public Long getExecuteDate() {
        return executeDate;
    }

    public void setExecuteDate(Long executeDate) {
        this.executeDate = executeDate;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }
}
