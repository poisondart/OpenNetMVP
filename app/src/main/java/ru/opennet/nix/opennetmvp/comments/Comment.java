package ru.opennet.nix.opennetmvp.comments;

import java.util.Comparator;

public class Comment {
    private String mAuthor;
    private int mPosition;
    private String mDate;
    private String mContent;

    public Comment() {
    }

    public Comment(String author, int position, String date, String content) {
        mAuthor = author;
        mPosition = position;
        mDate = date;
        mContent = content;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public static final Comparator<Comment> COMPARE_BY_POSITION = new Comparator<Comment>() {
        @Override
        public int compare(Comment comment, Comment t1) {
            return comment.getPosition() - t1.getPosition();
        }
    };
}
