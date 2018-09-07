package com.apitechnosoft.ipad.model;

public class Emaildata {
    private long itemSno;

    private String emailId;

    private long bytes;

    private long sno;

    private long kiloByte;

    private long shareSno;

    private long gigaByte;

    private long megaByte;

    private long size;

    public long getItemSno() {
        return itemSno;
    }

    public void setItemSno(long itemSno) {
        this.itemSno = itemSno;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
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
        return "ClassPojo [itemSno = "+itemSno+", emailId = "+emailId+", bytes = "+bytes+", sno = "+sno+", kiloByte = "+kiloByte+", shareSno = "+shareSno+", gigaByte = "+gigaByte+", megaByte = "+megaByte+", size = "+size+"]";
    }
}

