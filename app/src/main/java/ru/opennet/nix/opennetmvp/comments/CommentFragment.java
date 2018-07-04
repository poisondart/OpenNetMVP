package ru.opennet.nix.opennetmvp.comments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.opennet.nix.opennetmvp.R;

public class CommentFragment extends MvpAppCompatFragment implements CommentsView{

    @InjectPresenter
    CommentsPresenter mCommentsPresenter;

    @BindView(R.id.comments_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.swiperefresh_comments)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.comments_recyclerview)
    RecyclerView mRecyclerView;

    private CommentsAdapter mCommentsAdapter;
    private LinearLayoutManager mLinearLayout;


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
        ButterKnife.bind(this, v);
        AppCompatActivity actionBar = (AppCompatActivity) getActivity();
        actionBar.setSupportActionBar(mToolbar);
        actionBar.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBar.getSupportActionBar().setDisplayShowHomeEnabled(true);
        actionBar.getSupportActionBar().setTitle(R.string.comments_title);
        mCommentsAdapter = new CommentsAdapter();
        mLinearLayout = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayout);
        mRecyclerView.setAdapter(mCommentsAdapter);
        mCommentsPresenter.setLink(mLink);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setUpdating(true);
                mCommentsPresenter.loadComments();
            }
        });
        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setUpdating(boolean state) {
        mSwipeRefreshLayout.setRefreshing(state);
    }

    @Override
    public void showComments(List<Comment> comments) {
        mCommentsAdapter.setComments(comments);
        mCommentsAdapter.notifyDataSetChanged();
        setUpdating(false);
    }
}
