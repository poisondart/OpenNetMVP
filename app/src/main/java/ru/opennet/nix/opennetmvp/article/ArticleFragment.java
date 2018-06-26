package ru.opennet.nix.opennetmvp.article;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.opennet.nix.opennetmvp.R;
import ru.opennet.nix.opennetmvp.utils.BottomBarBehaviour;
import ru.opennet.nix.opennetmvp.utils.Links;

public class ArticleFragment extends MvpAppCompatFragment implements ArticleView, ArticlePartAdapter.OnItemClickListener {

    @InjectPresenter
    ArticlePresenter mArticlePresenter;

    @BindView(R.id.article_recyclerview)
    RecyclerView mRecyclerView;

    @BindView(R.id.progressbar_article)
    ProgressBar mProgressBar;

    @BindView(R.id.article_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.bottom_bar)
    ConstraintLayout mBotttomBar;

    private static final String ARG_ARTICLE_LINK = "article_link";
    private static final String ARG_ARTICLE_TITLE = "article_title";
    private static final String ARG_ARTICLE_DATE = "article_date";
    private static final String ARG_ARTICLE_CATEGORY = "article_category";

    private ArticlePartAdapter mArticlePartAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private String mTitle, mDate, mCat;

    public static ArticleFragment newInstance(String link, String title, String date, String cat){
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_ARTICLE_LINK, link);
        bundle.putSerializable(ARG_ARTICLE_TITLE, title);
        bundle.putSerializable(ARG_ARTICLE_DATE, date);
        bundle.putSerializable(ARG_ARTICLE_CATEGORY, cat);
        ArticleFragment articleFragment = new ArticleFragment();
        articleFragment.setArguments(bundle);
        return articleFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.article_fragment_layout, container, false);
        ButterKnife.bind(this, v);
        AppCompatActivity actionBar = (AppCompatActivity) getActivity();
        actionBar.setSupportActionBar(mToolbar);
        actionBar.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBar.getSupportActionBar().setDisplayShowHomeEnabled(true);
        actionBar.getSupportActionBar().setTitle(mCat);
        mArticlePartAdapter = new ArticlePartAdapter();
        mArticlePartAdapter.setOnItemClickListener(this);
        mArticlePartAdapter.setTitleAndDate(mTitle, mDate);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mArticlePartAdapter);
        mArticlePresenter.loadArticle();
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mBotttomBar.getLayoutParams();
        layoutParams.setBehavior(new BottomBarBehaviour());
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            String link = (String)getArguments().getSerializable(ARG_ARTICLE_LINK);
            mTitle = (String)getArguments().getSerializable(ARG_ARTICLE_TITLE);
            mDate = (String)getArguments().getSerializable(ARG_ARTICLE_DATE);
            mCat = (String)getArguments().getSerializable(ARG_ARTICLE_CATEGORY);
            mArticlePresenter.setLink(link);
            setHasOptionsMenu(true);
        }

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
    public void setUpdating(boolean isLoading) {
        if(isLoading){
            mProgressBar.setVisibility(View.VISIBLE);
        }else{
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showArticle(List<ArticlePart> articleParts) {
        mArticlePresenter.showLoading(true);
        mArticlePartAdapter.setParts(articleParts);
        mArticlePartAdapter.notifyDataSetChanged();
        mArticlePresenter.showLoading(false);
    }

    @Override
    public void onVideoItemClicked(String link) {
        Intent intent = YouTubeStandalonePlayer.createVideoIntent(getActivity(),
                Links.YOUTUBE_API_KEY, link);
        startActivity(intent);
    }
}
