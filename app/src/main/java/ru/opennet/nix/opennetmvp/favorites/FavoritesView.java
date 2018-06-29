package ru.opennet.nix.opennetmvp.favorites;

import com.arellomobile.mvp.MvpView;
import java.util.List;
import ru.opennet.nix.opennetmvp.article.Article;

public interface FavoritesView extends MvpView {
    void showFavorites(List<Article> articles);
    void showHint(boolean result);
}
