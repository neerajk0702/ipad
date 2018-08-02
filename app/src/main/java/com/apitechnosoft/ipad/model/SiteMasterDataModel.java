package com.apitechnosoft.ipad.model;

/**
 * Created by AST on 03/04/2017.
 */

public class SiteMasterDataModel {

    String siteId;
    String siteName;
    String customerSiteId;
    String circleId;
    String circleName;
    String ssaName;
    String ssaId;
    String lastUpdatedDate;



    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getCustomerSiteId() {
        return customerSiteId;
    }

    public void setCustomerSiteId(String customerSiteId) {
        this.customerSiteId = customerSiteId;
    }

    public String getCircleId() {
        return circleId;
    }

    public void setCircleId(String circleId) {
        this.circleId = circleId;
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }

    public String getSsaName() {
        return ssaName;
    }

    public void setSsaName(String ssaName) {
        this.ssaName = ssaName;
    }

    public String getSsaId() {
        return ssaId;
    }

    public void setSsaId(String ssaId) {
        this.ssaId = ssaId;
    }
}
