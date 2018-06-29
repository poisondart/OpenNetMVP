package ru.opennet.nix.opennetmvp.article;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class ArticlePart extends RealmObject{
    @Required
    private String mArticleLink;
    private String mText;
    private String mContentLink;
    private int mType;

    public static final int SIMPLE_TEXT = 0;
    public static final int IMAGE = 1;
    public static final int VIDEO_ITEM = 2;

    public static String ARTICLE_LINK = "mArticleLink";

    public ArticlePart() {
    }

    public ArticlePart(int type, String text, String link) {
        mType = type;
        mArticleLink = link;
        if(type == SIMPLE_TEXT){
            mText = text;
            mContentLink = null;
        }else if(type == IMAGE || type == VIDEO_ITEM){
            mContentLink = text;
            mText = null;
        }
    }

    public String getArticleLink() {
        return mArticleLink;
    }

    public void setArticleLink(String articleLink) {
        mArticleLink = articleLink;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public String getContentLink() {
        return mContentLink;
    }

    public void setContentLink(String contentLink) {
        mContentLink = contentLink;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public void initVideoID(){
        mContentLink = mContentLink.substring(30, 41);
    }
}
