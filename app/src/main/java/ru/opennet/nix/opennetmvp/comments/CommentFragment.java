package ru.opennet.nix.opennetmvp.comments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.arellomobile.mvp.MvpAppCompatFragment;
import ru.opennet.nix.opennetmvp.R;

public class CommentFragment extends MvpAppCompatFragment {

    private static final String ARG_COMMENTS_LINK = "comments_link";
    private String mLink;

    public static CommentFragment newInstance(String link){
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_COMMENTS_LINK, link);
        CommentFragment commentFragment = new CommentFragment();
        commentFragment.setArguments(bundle);
        return commentFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mLink = (String)getArguments().getSerializable(ARG_COMMENTS_LINK);
            setHasOptionsMenu(true);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.comments_fragment_layout, container, false);
        return v;
    }
}
