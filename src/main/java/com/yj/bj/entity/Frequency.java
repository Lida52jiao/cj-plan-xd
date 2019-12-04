package com.yj.bj.entity;

/**
 * Created by 61968 on 2018/10/12.
 */
public class Frequency {

    private Long time;

    private int consume;

    private int repayment;

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public int getConsume() {
        return consume;
    }

    public void setConsume(int consume) {
        this.consume = consume;
    }

    public int getRepayment() {
        return repayment;
    }

    public void setRepayment(int repayment) {
        this.repayment = repayment;
    }
}
