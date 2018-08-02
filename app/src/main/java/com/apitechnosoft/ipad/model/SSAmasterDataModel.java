package com.apitechnosoft.ipad.model;

/**
 * Created by AST on 20-03-2017.
 */

public class SSAmasterDataModel {

    String SSAname;
    String SSAid;
    String circleId;
    String lastUpdatedDate;

    public String getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getSSAname() {
        return SSAname;
    }

    public void setSSAname(String SSAname) {
        this.SSAname = SSAname;
    }

    public String getSSAid() {
        return SSAid;
    }

    public void setSSAid(String SSAid) {
        this.SSAid = SSAid;
    }

    public String getCircleId() {
        return circleId;
    }

    public void setCircleId(String circleId) {
        this.circleId = circleId;
    }
}
