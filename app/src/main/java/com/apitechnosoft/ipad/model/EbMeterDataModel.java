package com.apitechnosoft.ipad.model;

/**
 * Created by AST on 15-04-2017.
 */

public class EbMeterDataModel {

    String meterReading;
    String meterNumber;
    String availableHours;
    String photo1;
    String photo2;
    String photo1Name;
    String photo2Name;
    String supplySinglePhase;
    String supplyThreePhase;
    String userId;
    String siteId;

    public String getPhoto1Name() {
        return photo1Name;
    }

    public void setPhoto1Name(String photo1Name) {
        this.photo1Name = photo1Name;
    }

    public String getPhoto2Name() {
        return photo2Name;
    }

    public void setPhoto2Name(String photo2Name) {
        this.photo2Name = photo2Name;
    }

    public String getMeterReading() {
        return meterReading;
    }

    public void setMeterReading(String meterReading) {
        this.meterReading = meterReading;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    public String getAvailableHours() {
        return availableHours;
    }

    public void setAvailableHours(String availableHours) {
        this.availableHours = availableHours;
    }

    public String getPhoto1() {
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public String getPhoto2() {
        return photo2;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

    public String getSupplySinglePhase() {
        return supplySinglePhase;
    }

    public void setSupplySinglePhase(String supplySinglePhase) {
        this.supplySinglePhase = supplySinglePhase;
    }

    public String getSupplyThreePhase() {
        return supplyThreePhase;
    }

    public void setSupplyThreePhase(String supplyThreePhase) {
        this.supplyThreePhase = supplyThreePhase;
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
