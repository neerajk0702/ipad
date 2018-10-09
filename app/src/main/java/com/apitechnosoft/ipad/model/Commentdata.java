package com.apitechnosoft.ipad.model;

public class Commentdata {
    private String content;

    private long itemSno;

    private long bytes;

    private long sno;

    private String filePath;

    private String path;

    private long kiloByte;

    private long shareSno;

    private long gigaByte;

    private long megaByte;

    private long size;

    private String fName;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

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

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getFName() {
        return fName;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    private String emailId;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    @Override
    public String toString() {
        return "ClassPojo [content = " + content + ", itemSno = " + itemSno + ", bytes = " + bytes + ", sno = " + sno + ", filePath = " + filePath + ", path = " + path + ", kiloByte = " + kiloByte + ", shareSno = " + shareSno + ", gigaByte = " + gigaByte + ", megaByte = " + megaByte + ", size = " + size + ", fName = " + fName + "]";
    }
}


