package ru.opennet.nix.opennetmvp.comments;

import com.arellomobile.mvp.MvpView;
import java.util.List;

public interface CommentsView extends MvpView {
    void setUpdating(boolean state);
    void showComments(List<Comment> comments);
}
