package com.apitechnosoft.ipad.model;

public class Audioist {
    private int sno;

    private String bytes;

    private String filePath;

    private String enteredDate;

    private String type;

    private int shareSno;

    private String size;

    private String megaByte;

    private String limitFilename;

    private int itemSno;

    private String limitFilename1;

    private String folderlocation;

    private String fileName;

    private String kiloByte;

    private String gigaByte;


    public String getBytes ()
    {
        return bytes;
    }

    public void setBytes (String bytes)
    {
        this.bytes = bytes;
    }

    public String getFilePath ()
    {
        return filePath;
    }

    public void setFilePath (String filePath)
    {
        this.filePath = filePath;
    }

    public String getEnteredDate ()
    {
        return enteredDate;
    }

    public void setEnteredDate (String enteredDate)
    {
        this.enteredDate = enteredDate;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }


    public void setSize (String size)
    {
        this.size = size;
    }

    public String getMegaByte ()
    {
        return megaByte;
    }

    public void setMegaByte (String megaByte)
    {
        this.megaByte = megaByte;
    }

    public String getLimitFilename ()
    {
        return limitFilename;
    }

    public void setLimitFilename (String limitFilename)
    {
        this.limitFilename = limitFilename;
    }

    public String getLimitFilename1 ()
    {
        return limitFilename1;
    }

    public void setLimitFilename1 (String limitFilename1)
    {
        this.limitFilename1 = limitFilename1;
    }

    public String getFolderlocation ()
    {
        return folderlocation;
    }

    public void setFolderlocation (String folderlocation)
    {
        this.folderlocation = folderlocation;
    }

    public String getFileName ()
    {
        return fileName;
    }

    public void setFileName (String fileName)
    {
        this.fileName = fileName;
    }

    public String getKiloByte ()
    {
        return kiloByte;
    }

    public void setKiloByte (String kiloByte)
    {
        this.kiloByte = kiloByte;
    }

    public String getGigaByte ()
    {
        return gigaByte;
    }

    public void setGigaByte (String gigaByte)
    {
        this.gigaByte = gigaByte;
    }

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    public int getShareSno() {
        return shareSno;
    }

    public void setShareSno(int shareSno) {
        this.shareSno = shareSno;
    }

    public String getSize() {
        return size;
    }

    public int getItemSno() {
        return itemSno;
    }

    public void setItemSno(int itemSno) {
        this.itemSno = itemSno;
    }
public  String fileExtension;

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [sno = "+sno+", bytes = "+bytes+", filePath = "+filePath+", enteredDate = "+enteredDate+", type = "+type+", shareSno = "+shareSno+", size = "+size+", megaByte = "+megaByte+", limitFilename = "+limitFilename+", itemSno = "+itemSno+", limitFilename1 = "+limitFilename1+", folderlocation = "+folderlocation+", fileName = "+fileName+", kiloByte = "+kiloByte+", gigaByte = "+gigaByte+"]";
    }
}


