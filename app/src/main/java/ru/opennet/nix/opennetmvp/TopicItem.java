package ru.opennet.nix.opennetmvp;

public class TopicItem {
    private String Title;
    private String Link;

    public TopicItem(String title, String link) {
        Title = title;
        Link = link;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }
}
