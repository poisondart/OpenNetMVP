package ru.opennet.nix.opennetmvp.article;

import com.arellomobile.mvp.MvpView;

import java.util.List;

public interface ArticleView extends MvpView {
    void setUpdating(boolean isLoading);
    void showArticle(List<ArticlePart> articleParts);
    void startCommentsActivity(String link);
    void showCommentsLinkLoading(boolean isLoading);
}
