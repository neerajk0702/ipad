package com.apitechnosoft.ipad.model;

public class ContentResponce {
    private String tag;
    private boolean status;
    private String error_msg;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }


    private long notificationcount;

    private Notificationlist[] notificationlist;

    public long getNotificationcount() {
        return notificationcount;
    }

    public void setNotificationcount(long notificationcount) {
        this.notificationcount = notificationcount;
    }

    public Notificationlist[] getNotificationlist() {
        return notificationlist;
    }

    public void setNotificationlist(Notificationlist[] notificationlist) {
        this.notificationlist = notificationlist;
    }

    private String notificationstatus;
    private long singlenotificationdatacount;
    private String allnotificationstatus;
    private long allnotificationdatacount;

    public String getNotificationstatus() {
        return notificationstatus;
    }

    public void setNotificationstatus(String notificationstatus) {
        this.notificationstatus = notificationstatus;
    }

    public long getSinglenotificationdatacount() {
        return singlenotificationdatacount;
    }

    public void setSinglenotificationdatacount(long singlenotificationdatacount) {
        this.singlenotificationdatacount = singlenotificationdatacount;
    }

    public String getAllnotificationstatus() {
        return allnotificationstatus;
    }

    public void setAllnotificationstatus(String allnotificationstatus) {
        this.allnotificationstatus = allnotificationstatus;
    }

    public long getAllnotificationdatacount() {
        return allnotificationdatacount;
    }

    public void setAllnotificationdatacount(long allnotificationdatacount) {
        this.allnotificationdatacount = allnotificationdatacount;
    }

    private User user;

    private Userprofile userprofile;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Userprofile getUserprofile() {
        return userprofile;
    }

    public void setUserprofile(Userprofile userprofile) {
        this.userprofile = userprofile;
    }

    @Override
    public String toString() {
        return "ClassPojo [user = " + user + ", userprofile = " + userprofile + "]";
    }
}
