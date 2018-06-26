package ru.opennet.nix.opennetmvp.article;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public class ArticlePresenter extends MvpPresenter<ArticleView> {

    private ArticleModel mArticleModel;

    public ArticlePresenter(){
        mArticleModel = new ArticleModel();
    }

    public void loadArticle(){
        mArticleModel.loadNews(new ArticleModel.LoadArticleCallback() {
            @Override
            public void onLoad(Article article) {
                getViewState().showArticle(article);
            }
        });
    }
    public void showLoading(boolean state){
        getViewState().setUpdating(state);
    }
    public void setLink(String link){
        mArticleModel.setNumLink(link);
    }
}
