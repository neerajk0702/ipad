package com.apitechnosoft.ipad.model;

public class Lastuploadpic {
    private String fileName;

    private String filePath;
    private String limitFilename;
    private int sno;
    private int size;
    private String type;
    private String enteredDate;
    private int shareSno;

    private int itemSno;
    private int bytes;
    private String extension;
    private String folderlocation;

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }



    public String getLimitFilename() {
        return this.limitFilename;
    }

    public void setLimitFilename(String limitFilename) {
        this.limitFilename = limitFilename;
    }



    public int getSno() {
        return this.sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }


    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }


    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getEnteredDate() {
        return this.enteredDate;
    }

    public void setEnteredDate(String enteredDate) {
        this.enteredDate = enteredDate;
    }

    public int getShareSno() {
        return this.shareSno;
    }

    public void setShareSno(int shareSno) {
        this.shareSno = shareSno;
    }


    public int getItemSno() {
        return this.itemSno;
    }

    public void setItemSno(int itemSno) {
        this.itemSno = itemSno;
    }


    public int getBytes() {
        return this.bytes;
    }

    public void setBytes(int bytes) {
        this.bytes = bytes;
    }

    private int kiloByte;

    public int getKiloByte() {
        return this.kiloByte;
    }

    public void setKiloByte(int kiloByte) {
        this.kiloByte = kiloByte;
    }

    private int megaByte;

    public int getMegaByte() {
        return this.megaByte;
    }

    public void setMegaByte(int megaByte) {
        this.megaByte = megaByte;
    }

    private int gigaByte;

    public int getGigaByte() {
        return this.gigaByte;
    }

    public void setGigaByte(int gigaByte) {
        this.gigaByte = gigaByte;
    }


    public String getExtension() {
        return this.extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }


    public String getFolderlocation() {
        return this.folderlocation;
    }

    public void setFolderlocation(String folderlocation) {
        this.folderlocation = folderlocation;
    }
}
