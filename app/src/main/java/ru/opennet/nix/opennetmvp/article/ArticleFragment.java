package ru.opennet.nix.opennetmvp.article;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.opennet.nix.opennetmvp.R;

public class ArticleFragment extends MvpAppCompatFragment implements ArticleView {

    @InjectPresenter
    ArticlePresenter mArticlePresenter;

    @BindView(R.id.dateview_article)
    TextView mDateTextView;
    @BindView(R.id.titleview_article)
    TextView mTitleTextView;
    @BindView(R.id.descview_article)
    TextView mDescTextView;
    /*@BindView(R.id.swiperefresh_article)
    SwipeRefreshLayout mSwipeRefreshLayout;*/

    private static final String ARG_ARTICLE_LINK = "article_link";

    public static ArticleFragment newInstance(String link){
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_ARTICLE_LINK, link);
        ArticleFragment articleFragment = new ArticleFragment();
        articleFragment.setArguments(bundle);
        return articleFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.article_fragment_layout, container, false);
        ButterKnife.bind(this, v);
        mArticlePresenter.loadArticle();
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            String link = (String)getArguments().getSerializable(ARG_ARTICLE_LINK);
            mArticlePresenter.setLink(link);
        }

    }

    @Override
    public void setUpdating(boolean isLoading) {
        //mSwipeRefreshLayout.setRefreshing(isLoading);
    }

    @Override
    public void showArticle(Article article) {
        mDateTextView.setText(article.getDate());
        mTitleTextView.setText(article.getTitle());
        Spanned spanned = Html.fromHtml(article.getText());
        mDescTextView.setText(spanned);
        mDescTextView.setMovementMethod(LinkMovementMethod.getInstance());
        mArticlePresenter.showLoading(false);
    }
}
