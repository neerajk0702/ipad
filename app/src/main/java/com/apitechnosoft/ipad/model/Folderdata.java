package com.apitechnosoft.ipad.model;

public class Folderdata {
    private int sno;

    private String filePath;

    private String fullFilePath;

    private String fileName;

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFullFilePath() {
        return fullFilePath;
    }

    public void setFullFilePath(String fullFilePath) {
        this.fullFilePath = fullFilePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private String limitFilename;
    private long size;
    private String type;
    private String enteredDate;
    private int shareSno;
    private int itemSno;
    private long bytes;
    private long kiloByte;
    private long megaByte;
    private long gigaByte;
    private String eventname;
    private String limitFilename1;
    private String uploadFilePath;
    private String folderlocation;

    public String getLimitFilename() {
        return limitFilename;
    }

    public void setLimitFilename(String limitFilename) {
        this.limitFilename = limitFilename;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEnteredDate() {
        return enteredDate;
    }

    public void setEnteredDate(String enteredDate) {
        this.enteredDate = enteredDate;
    }

    public int getShareSno() {
        return shareSno;
    }

    public void setShareSno(int shareSno) {
        this.shareSno = shareSno;
    }

    public int getItemSno() {
        return itemSno;
    }

    public void setItemSno(int itemSno) {
        this.itemSno = itemSno;
    }

    public long getBytes() {
        return bytes;
    }

    public void setBytes(long bytes) {
        this.bytes = bytes;
    }

    public long getKiloByte() {
        return kiloByte;
    }

    public void setKiloByte(long kiloByte) {
        this.kiloByte = kiloByte;
    }

    public long getMegaByte() {
        return megaByte;
    }

    public void setMegaByte(long megaByte) {
        this.megaByte = megaByte;
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

    public String getLimitFilename1() {
        return limitFilename1;
    }

    public void setLimitFilename1(String limitFilename1) {
        this.limitFilename1 = limitFilename1;
    }

    public String getUploadFilePath() {
        return uploadFilePath;
    }

    public void setUploadFilePath(String uploadFilePath) {
        this.uploadFilePath = uploadFilePath;
    }

    public String getFolderlocation() {
        return folderlocation;
    }

    public void setFolderlocation(String folderlocation) {
        this.folderlocation = folderlocation;
    }

    private String extension;

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    private String thamblingImage;

    public String getThamblingImage() {
        return thamblingImage;
    }

    public void setThamblingImage(String thamblingImage) {
        this.thamblingImage = thamblingImage;
    }
    @Override
    public String toString() {
        return "ClassPojo [sno = " + sno + ", filePath = " + filePath + ", fullFilePath = " + fullFilePath + ", fileName = " + fileName + "]";
    }
}


