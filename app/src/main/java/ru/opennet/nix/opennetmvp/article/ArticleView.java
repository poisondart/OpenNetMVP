package ru.opennet.nix.opennetmvp.article;

import com.arellomobile.mvp.MvpView;

public interface ArticleView extends MvpView {
    void setUpdating(boolean isLoading);
    void showArticle(Article article);
}
