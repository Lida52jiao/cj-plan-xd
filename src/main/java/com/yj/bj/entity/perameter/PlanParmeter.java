package com.yj.bj.entity.perameter;





import com.yj.bj.entity.PlanDetailEntity;
import com.yj.bj.entity.PlanEntity;

import java.util.List;

/**
 * Created by bin on 2017/11/6.
 */
public class PlanParmeter implements java.io.Serializable{
    private String planJsonKey;
    private PlanEntity plan;
    private List<PlanDetailEntity> list;

    public String getPlanJsonKey() {
        return planJsonKey;
    }

    public void setPlanJsonKey(String planJsonKey) {
        this.planJsonKey = planJsonKey;
    }

    public PlanEntity getPlan() {
        return plan;
    }

    public void setPlan(PlanEntity plan) {
        this.plan = plan;
    }

    public List<PlanDetailEntity> getList() {
        return list;
    }

    public void setList(List<PlanDetailEntity> list) {
        this.list = list;
    }
}
