package com.apitechnosoft.ipad.model;

public class ContentData {

    String tag;

    boolean status;

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

    private Folderdata[] folderdata;

    private String[] audioist;

    private Photolist[] photolist;

    private Videolist[] videolist;

    public Folderdata[] getFolderdata ()
    {
        return folderdata;
    }

    public void setFolderdata (Folderdata[] folderdata)
    {
        this.folderdata = folderdata;
    }

    public String[] getAudioist ()
    {
        return audioist;
    }

    public void setAudioist (String[] audioist)
    {
        this.audioist = audioist;
    }

    public Photolist[] getPhotolist ()
    {
        return photolist;
    }

    public void setPhotolist (Photolist[] photolist)
    {
        this.photolist = photolist;
    }

    public Videolist[] getVideolist ()
    {
        return videolist;
    }

    public void setVideolist (Videolist[] videolist)
    {
        this.videolist = videolist;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [folderdata = "+folderdata+", audioist = "+audioist+", photolist = "+photolist+", videolist = "+videolist+"]";
    }
}

