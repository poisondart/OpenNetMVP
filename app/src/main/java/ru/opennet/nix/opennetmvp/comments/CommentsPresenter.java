package ru.opennet.nix.opennetmvp.comments;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import java.util.List;

@InjectViewState
public class CommentsPresenter extends MvpPresenter<CommentsView> {

    private CommentsModel mCommentsModel;

    public CommentsPresenter(){
        mCommentsModel = new CommentsModel();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        showLoading(true);
        loadComments();
    }

    public void loadComments(){
        mCommentsModel.loadComments(new CommentsModel.LoadCommentsCallback() {
            @Override
            public void onLoad(List<Comment> comments) {
                getViewState().showComments(comments);
            }
        });
    }

    public void showLoading(boolean state){
        getViewState().setUpdating(state);
    }

    public void setLink(String link){
        mCommentsModel.setLink(link);
    }
}
