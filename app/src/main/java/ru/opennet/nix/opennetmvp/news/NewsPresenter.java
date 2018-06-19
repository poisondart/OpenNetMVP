package ru.opennet.nix.opennetmvp.news;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import java.util.List;

@InjectViewState
public class NewsPresenter extends MvpPresenter<NewsView> {

    private NewsModel mNewsModel;

    public NewsPresenter(){
        mNewsModel = new NewsModel();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        showLoading(true);
        loadNews();
    }

    public void loadNews(){
        mNewsModel.loadNews(new NewsModel.LoadNewsCallback() {
            @Override
            public void onLoad(List<NewsItem> items) {
                getViewState().showNews(items);
            }
        });
    }

    public void showLoading(boolean state){
        getViewState().setUpdating(state);
    }

    public void setLink(String link){
        mNewsModel.setLink(link);
    }
}