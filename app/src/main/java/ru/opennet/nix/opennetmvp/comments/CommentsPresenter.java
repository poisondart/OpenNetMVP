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
        loadComments();
    }

    public void loadComments(){
        getViewState().setUpdating(true);
        mCommentsModel.loadComments(new CommentsModel.LoadCommentsCallback() {
            @Override
            public void onLoad(List<Comment> comments) {
                if (comments == null) {
                    getViewState().showError();
                }else{
                    getViewState().showComments(comments);
                }
                getViewState().setUpdating(false);
            }
        });
    }

    public void setLink(String link){
        mCommentsModel.setLink(link);
    }
}
