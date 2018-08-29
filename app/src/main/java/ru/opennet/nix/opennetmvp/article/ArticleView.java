package ru.opennet.nix.opennetmvp.article;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

public interface ArticleView extends MvpView {
    void setUpdating(boolean isLoading);
    void showArticle(List<ArticlePart> articleParts);
    @StateStrategyType(SkipStrategy.class)
    void startCommentsActivity(String link);
    void showCommentsLinkLoading(boolean isLoading);
    void showSavingIcon(boolean isShowing);
}
