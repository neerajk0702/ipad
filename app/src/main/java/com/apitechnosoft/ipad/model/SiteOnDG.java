package com.apitechnosoft.ipad.model;

/**
 * Created by AST on 15-04-2017.
 */

public class SiteOnDG {

    String dgCurrent;
    String dgFrequency;
    String dgVoltage;
    String batteryChargeCurrent;
    String batteryVoltage;
    String userId;
    String siteId;

    public String getDgCurrent() {
        return dgCurrent;
    }

    public void setDgCurrent(String dgCurrent) {
        this.dgCurrent = dgCurrent;
    }

    public String getDgFrequency() {
        return dgFrequency;
    }

    public void setDgFrequency(String dgFrequency) {
        this.dgFrequency = dgFrequency;
    }

    public String getDgVoltage() {
        return dgVoltage;
    }

    public void setDgVoltage(String dgVoltage) {
        this.dgVoltage = dgVoltage;
    }

    public String getBatteryChargeCurrent() {
        return batteryChargeCurrent;
    }

    public void setBatteryChargeCurrent(String batteryChargeCurrent) {
        this.batteryChargeCurrent = batteryChargeCurrent;
    }

    public String getBatteryVoltage() {
        return batteryVoltage;
    }

    public void setBatteryVoltage(String batteryVoltage) {
        this.batteryVoltage = batteryVoltage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
}
