package ru.opennet.nix.opennetmvp.article;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import java.util.List;
import io.realm.Realm;
import ru.opennet.nix.opennetmvp.R;

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
            getViewState().showSavingIconState(true);
            getViewState().enableSaveButton(true);
        }else{
            getViewState().setUpdating(true);
            mArticleModel.loadNews(new ArticleModel.LoadArticleCallback() {
                @Override
                public void onLoad(List<ArticlePart> articleParts) {
                    if(articleParts == null){
                        getViewState().showError(R.string.no_connection);
                        getViewState().enableSaveButton(false);
                    }else{
                        getViewState().showArticle(articleParts);
                        mArticleModel.setArticleParts(articleParts);
                        getViewState().showSavingIconState(false);
                        getViewState().enableSaveButton(true);
                    }
                    getViewState().setUpdating(false);
                }
            });
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
        getViewState().showCommentsLinkLoading(true);
        mArticleModel.loadCommentsLink(new ArticleModel.LoadCommentsLinkCallback() {
            @Override
            public void onLoad(String commentsLink) {
                if(commentsLink == null){
                    getViewState().showError(R.string.cannot_load_comments);
                }else if(commentsLink.equals(ArticleModel.NO_CONNECTION)){
                    getViewState().showError(R.string.no_connection);
                }else{
                    getViewState().startCommentsActivity(commentsLink);
                }
                getViewState().showCommentsLinkLoading(false);
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
