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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.opennet.nix.opennetmvp.CommentsActivity;
import ru.opennet.nix.opennetmvp.R;
import ru.opennet.nix.opennetmvp.utils.BottomBarBehaviour;
import ru.opennet.nix.opennetmvp.utils.Links;

public class ArticleFragment extends MvpAppCompatFragment implements ArticleView, ArticlePartAdapter.OnItemClickListener,
        View.OnClickListener{

    @InjectPresenter
    ArticlePresenter mArticlePresenter;

    @BindView(R.id.article_recyclerview)
    RecyclerView mRecyclerView;

    @BindView(R.id.progressbar_article)
    ProgressBar mProgressBar;

    @BindView(R.id.article_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.bottom_bar)
    ConstraintLayout mBottomBar;

    @BindView(R.id.share_button)
    ImageButton mShareButton;

    @BindView(R.id.save_button)
    ImageButton mSaveButton;

    @BindView(R.id.comments_button)
    ImageButton mCommentsButton;

    @BindView(R.id.com_prog)
    ProgressBar mComProgressBar;

    @BindView(R.id.loading_com_textview)
    TextView mLoadingComTextView;

    private static final String ARG_ARTICLE_LINK = "article_link";
    private static final String ARG_ARTICLE_TITLE = "article_title";
    private static final String ARG_ARTICLE_DATE = "article_date";
    private static final String ARG_ARTICLE_CATEGORY = "article_category";

    private ArticlePartAdapter mArticlePartAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private String mCat;
    private Article mArticle;

    private boolean mAdded = false;

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
        mArticlePresenter.setArticle(mArticle);
        mArticlePartAdapter = new ArticlePartAdapter();
        mArticlePartAdapter.setOnItemClickListener(this);
        mArticlePartAdapter.setTitleAndDate(mArticle.getTitle(), mArticle.getDate());
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mArticlePartAdapter);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mBottomBar.getLayoutParams();
        layoutParams.setBehavior(new BottomBarBehaviour());
        mShareButton.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);
        mCommentsButton.setOnClickListener(this);
        if (mAdded){
            mSaveButton.setImageResource(R.drawable.ic_delete_button);
        }else{
            mSaveButton.setImageResource(R.drawable.ic_fav_button);
        }
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            String link = (String)getArguments().getSerializable(ARG_ARTICLE_LINK);
            String title = (String)getArguments().getSerializable(ARG_ARTICLE_TITLE);
            String date = (String)getArguments().getSerializable(ARG_ARTICLE_DATE);
            mCat = (String)getArguments().getSerializable(ARG_ARTICLE_CATEGORY);
            mArticle = new Article(date, title, link);
            setHasOptionsMenu(true);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.share_button:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, mArticle.getLink());
                i.putExtra(Intent.EXTRA_SUBJECT, mArticle.getTitle());
                startActivity(Intent.createChooser(i, getString(R.string.share_link_hint)));
                break;
            case R.id.save_button:
                if (mAdded){
                    mSaveButton.setImageResource(R.drawable.ic_fav_button);
                    mArticlePresenter.deleteArticleFromRealm();
                }else{
                    mSaveButton.setImageResource(R.drawable.ic_delete_button);
                    mArticlePresenter.saveArticleToRealm();
                }
                mAdded = !mAdded;
                break;
            case R.id.comments_button:
                showCommentsLinkLoading(true);
                mArticlePresenter.loadCommentsLink();
                break;
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
            mBottomBar.setVisibility(View.INVISIBLE);
        }else{
            mProgressBar.setVisibility(View.GONE);
            mBottomBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showArticle(List<ArticlePart> articleParts) {
        setUpdating(true);
        mArticlePartAdapter.setParts(articleParts);
        mArticlePartAdapter.notifyDataSetChanged();
        setUpdating(false);
    }

    @Override
    @StateStrategyType(SkipStrategy.class)
    public void startCommentsActivity(String link) {
        showCommentsLinkLoading(false);
        Intent intent = CommentsActivity.newInstance(getContext(), link);
        startActivity(intent);
    }

    @Override
    public void showCommentsLinkLoading(boolean isLoading) {
        if(isLoading){
            mLoadingComTextView.setVisibility(View.VISIBLE);
            mComProgressBar.setVisibility(View.VISIBLE);

            mSaveButton.setVisibility(View.INVISIBLE);
            mShareButton.setVisibility(View.INVISIBLE);
            mCommentsButton.setVisibility(View.INVISIBLE);
        }else{
            mLoadingComTextView.setVisibility(View.INVISIBLE);
            mComProgressBar.setVisibility(View.INVISIBLE);

            mSaveButton.setVisibility(View.VISIBLE);
            mShareButton.setVisibility(View.VISIBLE);
            mCommentsButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showSavingIcon(boolean isShowing) {
        if(isShowing){
            mSaveButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_delete_button));
            mAdded = true;
        }else{
            mSaveButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav_button));
            mAdded = false;
        }
    }

    @Override
    public void onVideoItemClicked(String link) {
        Intent intent = YouTubeStandalonePlayer.createVideoIntent(getActivity(),
                Links.YOUTUBE_API_KEY, link);
        startActivity(intent);
    }
}
