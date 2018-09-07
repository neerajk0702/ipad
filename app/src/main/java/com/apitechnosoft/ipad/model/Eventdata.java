package com.apitechnosoft.ipad.model;

public class Eventdata {
    private String reminder;

    private long sno;

    private long bytes;

    private String toDateTime;

    private String fromDateTime;

    private String todate;

    private String type;

    private long shareSno;

    private long size;

    private long megaByte;

    private long itemSno;

    private String eventDescription;

    private String fromdate;

    private String kiloByte;

    private long gigaByte;

    private String eventname;

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public long getSno() {
        return sno;
    }

    public void setSno(long sno) {
        this.sno = sno;
    }

    public long getBytes() {
        return bytes;
    }

    public void setBytes(long bytes) {
        this.bytes = bytes;
    }

    public String getToDateTime() {
        return toDateTime;
    }

    public void setToDateTime(String toDateTime) {
        this.toDateTime = toDateTime;
    }

    public String getFromDateTime() {
        return fromDateTime;
    }

    public void setFromDateTime(String fromDateTime) {
        this.fromDateTime = fromDateTime;
    }

    public String getTodate() {
        return todate;
    }

    public void setTodate(String todate) {
        this.todate = todate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getShareSno() {
        return shareSno;
    }

    public void setShareSno(long shareSno) {
        this.shareSno = shareSno;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getMegaByte() {
        return megaByte;
    }

    public void setMegaByte(long megaByte) {
        this.megaByte = megaByte;
    }

    public long getItemSno() {
        return itemSno;
    }

    public void setItemSno(long itemSno) {
        this.itemSno = itemSno;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getFromdate() {
        return fromdate;
    }

    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    public String getKiloByte() {
        return kiloByte;
    }

    public void setKiloByte(String kiloByte) {
        this.kiloByte = kiloByte;
    }

    public long getGigaByte() {
        return gigaByte;
    }

    public void setGigaByte(long gigaByte) {
        this.gigaByte = gigaByte;
    }

    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [reminder = "+reminder+", sno = "+sno+", bytes = "+bytes+", toDateTime = "+toDateTime+", fromDateTime = "+fromDateTime+", todate = "+todate+", type = "+type+", shareSno = "+shareSno+", size = "+size+", megaByte = "+megaByte+", itemSno = "+itemSno+", eventDescription = "+eventDescription+", fromdate = "+fromdate+", kiloByte = "+kiloByte+", gigaByte = "+gigaByte+", eventname = "+eventname+"]";
    }
}
