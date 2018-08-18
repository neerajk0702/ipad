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

    public String getFilePath ()
    {
        return filePath;
    }

    public void setFilePath (String filePath)
    {
        this.filePath = filePath;
    }

    public String getFullFilePath ()
    {
        return fullFilePath;
    }

    public void setFullFilePath (String fullFilePath)
    {
        this.fullFilePath = fullFilePath;
    }

    public String getFileName ()
    {
        return fileName;
    }

    public void setFileName (String fileName)
    {
        this.fileName = fileName;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [sno = "+sno+", filePath = "+filePath+", fullFilePath = "+fullFilePath+", fileName = "+fileName+"]";
    }
}


