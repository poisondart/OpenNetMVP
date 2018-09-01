package ru.opennet.nix.opennetmvp.news;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import java.util.List;

@InjectViewState
public class NewsPresenter extends MvpPresenter<NewsView> {

    private NewsModel mNewsModel;

    public NewsPresenter(){
        mNewsModel = new NewsModel(NewsModel.RequestCount.ALL);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadNews();
    }

    public void loadNews(){
        getViewState().setUpdating(true);
        mNewsModel.loadNews(new NewsModel.LoadNewsCallback() {
            @Override
            public void onLoad(List<NewsItem> items) {
                if(items == null){
                    getViewState().showError();
                }else{
                    getViewState().showNews(items);
                }
                getViewState().setUpdating(false);
            }
        });
    }

    public void setLink(String link){
        mNewsModel.setLink(link);
    }
}
