package com.apitechnosoft.ipad.model;

public class EventotdataList {

    private long itemSno;

    private long bytes;

    private long sno;

    private String fromdate;

    private long kiloByte;

    private long shareSno;

    private long gigaByte;

    private String eventname;

    private long megaByte;

    private long size;

    public long getItemSno() {
        return itemSno;
    }

    public void setItemSno(long itemSno) {
        this.itemSno = itemSno;
    }

    public long getBytes() {
        return bytes;
    }

    public void setBytes(long bytes) {
        this.bytes = bytes;
    }

    public long getSno() {
        return sno;
    }

    public void setSno(long sno) {
        this.sno = sno;
    }

    public String getFromdate() {
        return fromdate;
    }

    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    public long getKiloByte() {
        return kiloByte;
    }

    public void setKiloByte(long kiloByte) {
        this.kiloByte = kiloByte;
    }

    public long getShareSno() {
        return shareSno;
    }

    public void setShareSno(long shareSno) {
        this.shareSno = shareSno;
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

    public long getMegaByte() {
        return megaByte;
    }

    public void setMegaByte(long megaByte) {
        this.megaByte = megaByte;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [itemSno = "+itemSno+", bytes = "+bytes+", sno = "+sno+", fromdate = "+fromdate+", kiloByte = "+kiloByte+", shareSno = "+shareSno+", gigaByte = "+gigaByte+", eventname = "+eventname+", megaByte = "+megaByte+", size = "+size+"]";
    }

}
