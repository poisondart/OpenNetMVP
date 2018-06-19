package ru.opennet.nix.opennetmvp.news;

import com.arellomobile.mvp.MvpView;

import java.util.List;

public interface NewsView extends MvpView {
    void setUpdating(boolean isLoading);
    void showNews(List<NewsItem> items);
}
