package ru.opennet.nix.opennetmvp;

public class NewsItem {
    private String mDate;
    private String mTitle;
    private String mDesc;
    private String mLink;

    public NewsItem(String date, String title, String desc, String link) {
        mDate = date;
        mTitle = title;
        mDesc = desc;
        mLink = link;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDesc() {
        return mDesc;
    }

    public void setDesc(String desc) {
        mDesc = desc;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }
}
