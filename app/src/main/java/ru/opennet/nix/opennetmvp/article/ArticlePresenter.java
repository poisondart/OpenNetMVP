package ru.opennet.nix.opennetmvp.article;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import java.util.List;

@InjectViewState
public class ArticlePresenter extends MvpPresenter<ArticleView> {

    private ArticleModel mArticleModel;

    public ArticlePresenter(){
        mArticleModel = new ArticleModel();
    }

    public void loadArticle() {
        mArticleModel.loadNews(new ArticleModel.LoadArticleCallback() {
            @Override
            public void onLoad(List<ArticlePart> articleParts) {
                getViewState().showArticle(articleParts);
            }
        });
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadArticle();
    }

    public void loadCommentsLink(){
        mArticleModel.loadCommentsLink(new ArticleModel.LoadCommentsLinkCallback() {
            @Override
            public void onLoad(String commentsLink) {
                getViewState().startCommentsActivity(commentsLink);
            }
        });
    }

    /*public void showLoading(boolean state){
        getViewState().setUpdating(state);
    }*/

    /*public void showCommentsLinkLoading(boolean state){
        getViewState().showCommentsLinkLoading(state);
    }*/

    public void setLink(String link){
        mArticleModel.setData(link);
    }
}
