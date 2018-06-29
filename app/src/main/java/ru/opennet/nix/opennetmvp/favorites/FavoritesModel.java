package ru.opennet.nix.opennetmvp.favorites;

import java.util.List;
import io.realm.Realm;
import ru.opennet.nix.opennetmvp.article.Article;

public class FavoritesModel {

    public List<Article> getFavs(Realm realm){
        List<Article> articles;
        articles =  realm.where(Article.class).findAll();
        return articles;
    }
}
