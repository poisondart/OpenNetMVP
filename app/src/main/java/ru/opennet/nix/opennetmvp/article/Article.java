package ru.opennet.nix.opennetmvp.article;

public class Article {
    private String mDate;
    private String mTitle;
    private String mText;

    public Article(String date, String title, String text) {
        mDate = date;
        mTitle = title;
        mText = text;
    }

    public Article(){

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

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }
}
