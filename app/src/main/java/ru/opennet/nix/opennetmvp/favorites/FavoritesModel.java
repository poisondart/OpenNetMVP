package ru.opennet.nix.opennetmvp.favorites;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import io.realm.Realm;
import ru.opennet.nix.opennetmvp.article.Article;
import ru.opennet.nix.opennetmvp.article.ArticlePart;

public class FavoritesModel {

    public List<Article> getFavs(Realm realm){
        List<Article> articles;
        articles =  realm.where(Article.class).findAll();
        return articles;
    }
    @ParametersAreNonnullByDefault
    public void deleteArticle(Realm realm, final String link){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Article.class).equalTo(Article.LINK, link).findAll().deleteAllFromRealm();
                realm.where(ArticlePart.class).equalTo(ArticlePart.ARTICLE_LINK, link)
                        .findAll().deleteAllFromRealm();
            }
        });
    }

    @ParametersAreNonnullByDefault
    public void deleteAllArticles(Realm realm){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Article.class).findAll().deleteAllFromRealm();
                realm.where(ArticlePart.class).findAll().deleteAllFromRealm();
            }
        });
    }
}
