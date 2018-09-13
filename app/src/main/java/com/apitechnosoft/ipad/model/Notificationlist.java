package com.apitechnosoft.ipad.model;

public class Notificationlist {

    private long itemSno;

    private String emailId;

    private long count;

    private String commentl;

    private String folderlocation;

    private String path;

    private String userName;

    private String filename;

    private long shareSno;

    private long shareCommentsno;

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

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getCommentl() {
        return commentl;
    }

    public void setCommentl(String commentl) {
        this.commentl = commentl;
    }

    public String getFolderlocation() {
        return folderlocation;
    }

    public void setFolderlocation(String folderlocation) {
        this.folderlocation = folderlocation;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public long getShareSno() {
        return shareSno;
    }

    public void setShareSno(long shareSno) {
        this.shareSno = shareSno;
    }

    public long getShareCommentsno() {
        return shareCommentsno;
    }

    public void setShareCommentsno(long shareCommentsno) {
        this.shareCommentsno = shareCommentsno;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [itemSno = "+itemSno+", emailId = "+emailId+", count = "+count+", commentl = "+commentl+", folderlocation = "+folderlocation+", path = "+path+", userName = "+userName+", filename = "+filename+", shareSno = "+shareSno+", shareCommentsno = "+shareCommentsno+"]";
    }
}
