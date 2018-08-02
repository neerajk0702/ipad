package com.apitechnosoft.ipad.model;

/**
 * Created by AST on 15-04-2017.
 */

public class SiteOnBatteryBankDataModel {

    String dischargeCurrent;
    String dischargeVoltage;
    String siteId;
    String userId;

    public String getDischargeCurrent() {
        return dischargeCurrent;
    }

    public void setDischargeCurrent(String dischargeCurrent) {
        this.dischargeCurrent = dischargeCurrent;
    }

    public String getDischargeVoltage() {
        return dischargeVoltage;
    }

    public void setDischargeVoltage(String dischargeVoltage) {
        this.dischargeVoltage = dischargeVoltage;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
