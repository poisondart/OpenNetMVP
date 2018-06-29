package ru.opennet.nix.opennetmvp.favorites;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import java.util.List;
import io.realm.Realm;
import ru.opennet.nix.opennetmvp.article.Article;

@InjectViewState
public class FavoritesPresenter extends MvpPresenter<FavoritesView> {

    private FavoritesModel mFavoritesModel;
    private Realm mRealm;

    public FavoritesPresenter(){
        mFavoritesModel = new FavoritesModel();
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadFavs();
    }

    @Override
    public void attachView(FavoritesView view) {
        super.attachView(view);
        loadFavs();
    }

    public void loadFavs(){
        List<Article> articleList = mFavoritesModel.getFavs(mRealm);
        getViewState().showFavorites(articleList);
        getViewState().showHint(articleList.size() > 0);
    }

    public void deleteArticleFromRealm(String link){
        mFavoritesModel.deleteArticle(mRealm, link);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }
}
