package ru.opennet.nix.opennetmvp.article;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.opennet.nix.opennetmvp.R;

public class ArticleFragment extends MvpAppCompatFragment implements ArticleView {

    @InjectPresenter
    ArticlePresenter mArticlePresenter;

    @BindView(R.id.article_recyclerview)
    RecyclerView mRecyclerView;

    @BindView(R.id.progressbar_article)
    ProgressBar mProgressBar;

    @BindView(R.id.article_toolbar)
    Toolbar mToolbar;

    private static final String ARG_ARTICLE_LINK = "article_link";
    private static final String ARG_ARTICLE_TITLE = "article_title";
    private static final String ARG_ARTICLE_DATE = "article_date";

    private ArticlePartAdapter mArticlePartAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private String mTitle, mDate;

    public static ArticleFragment newInstance(String link, String title, String date){
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_ARTICLE_LINK, link);
        bundle.putSerializable(ARG_ARTICLE_TITLE, title);
        bundle.putSerializable(ARG_ARTICLE_DATE, date);
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
        mArticlePartAdapter = new ArticlePartAdapter();
        mArticlePartAdapter.setTitleAndDate(mTitle, mDate);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mArticlePartAdapter);
        mArticlePresenter.loadArticle();
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            String link = (String)getArguments().getSerializable(ARG_ARTICLE_LINK);
            mTitle = (String)getArguments().getSerializable(ARG_ARTICLE_TITLE);
            mDate = (String)getArguments().getSerializable(ARG_ARTICLE_DATE);
            mArticlePresenter.setLink(link);
        }

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
}