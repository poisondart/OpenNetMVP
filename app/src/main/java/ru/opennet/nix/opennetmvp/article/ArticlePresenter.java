package ru.opennet.nix.opennetmvp.article;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import java.util.List;
import io.realm.Realm;

@InjectViewState
public class ArticlePresenter extends MvpPresenter<ArticleView> {

    private ArticleModel mArticleModel;
    private Realm mRealm;

    public ArticlePresenter(){
        mArticleModel = new ArticleModel();
        mRealm = Realm.getDefaultInstance();
    }

    public void loadArticle() {
        if(mArticleModel.checkArticleInRealm(mRealm)){
            getViewState().showArticle(mArticleModel.getArticleParts());
            getViewState().showSavingIcon(true);
        }else{
            mArticleModel.loadNews(new ArticleModel.LoadArticleCallback() {
                @Override
                public void onLoad(List<ArticlePart> articleParts) {
                    getViewState().showArticle(articleParts);
                    mArticleModel.setArticleParts(articleParts);
                }
            });
            getViewState().showSavingIcon(false);
        }
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadArticle();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    public void loadCommentsLink(){
        mArticleModel.loadCommentsLink(new ArticleModel.LoadCommentsLinkCallback() {
            @Override
            public void onLoad(String commentsLink) {
                getViewState().startCommentsActivity(commentsLink);
            }
        });
    }

    public void setArticle(Article article){
        mArticleModel.setArticle(article);
    }

    public void saveArticleToRealm(){
        mArticleModel.saveArticle(mRealm);
    }

    public void deleteArticleFromRealm(){
        mArticleModel.deleteArticle(mRealm);
    }
}
