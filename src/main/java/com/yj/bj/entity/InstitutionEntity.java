package com.yj.bj.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by bin on 2018/3/14.
 */
@Table(name = "api_institution")
public class InstitutionEntity implements java.io.Serializable{
    @Id
    @Column(name = "institutionId", unique = true, nullable = false)
    private String institutionId;
    @Column(name = "institutionName")
    private String institutionName;
    @Column(name = "apiHost")
    private String apiHost;
    @Column(name = "merHost")
    private String merHost;

    public String getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(String institutionId) {
        this.institutionId = institutionId;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getApiHost() {
        return apiHost;
    }

    public void setApiHost(String apiHost) {
        this.apiHost = apiHost;
    }

    public String getMerHost() {
        return merHost;
    }

    public void setMerHost(String merHost) {
        this.merHost = merHost;
    }
}
