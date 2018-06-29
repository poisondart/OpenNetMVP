package ru.opennet.nix.opennetmvp.article;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Article extends RealmObject{
    @Required
    private String mDate;
    @PrimaryKey
    @Required
    private String mTitle;
    private String mText;

    @Required
    private String mLink;

    public static String LINK = "mLink";

    public Article(String date, String title, String link) {
        mDate = date;
        mTitle = title;
        mLink = link;
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

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }
}
