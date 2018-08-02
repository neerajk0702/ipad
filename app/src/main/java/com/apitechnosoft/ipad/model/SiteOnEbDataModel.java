package com.apitechnosoft.ipad.model;

/**
 * Created by AST on 15-04-2017.
 */

public class SiteOnEbDataModel {

    String gridCurrent;
    String gridVoltage;
    String gridFrequency;
    String userId;
    String siteId;

    public String getGridCurrent() {
        return gridCurrent;
    }

    public void setGridCurrent(String gridCurrent) {
        this.gridCurrent = gridCurrent;
    }

    public String getGridVoltage() {
        return gridVoltage;
    }

    public void setGridVoltage(String gridVoltage) {
        this.gridVoltage = gridVoltage;
    }

    public String getGridFrequency() {
        return gridFrequency;
    }

    public void setGridFrequency(String gridFrequency) {
        this.gridFrequency = gridFrequency;
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
