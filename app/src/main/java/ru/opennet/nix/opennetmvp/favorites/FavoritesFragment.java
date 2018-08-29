package ru.opennet.nix.opennetmvp.favorites;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.opennet.nix.opennetmvp.ArticleActivity;
import ru.opennet.nix.opennetmvp.R;
import ru.opennet.nix.opennetmvp.article.Article;

public class FavoritesFragment extends MvpAppCompatFragment implements FavoritesView, FavoritesAdapter.OnFavsItemClicked{

    @InjectPresenter
    FavoritesPresenter mFavoritesPresenter;

    @BindView(R.id.favs_recyclerview)
    RecyclerView mRecyclerView;

    @BindView(R.id.favs_hint)
    TextView mHint;

    private FavoritesAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.favorites_fragment_layout, container, false);
        ButterKnife.bind(this, v);
        mAdapter = new FavoritesAdapter();
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnFavsItemClickedListener(this);
        return v;
    }

    @Override
    public void showFavorites(List<Article> articles) {
        mAdapter.setArticles(articles);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showHint(boolean result) {
        if(result){
            mHint.setVisibility(View.INVISIBLE);
        }else{
            mHint.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFavsItemClicked(String url, String title, String date) {
        Intent intent = ArticleActivity.newIntent(getContext(), url, title, date, getString(R.string.saved));
        startActivity(intent);
    }

    @Override
    public void onFavsItemLongClicked(final String url, final int pos) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.delete_this_proof);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mFavoritesPresenter.deleteArticleFromRealm(url);
                mAdapter.notifyItemRemoved(pos);
                Toast.makeText(getContext(), getString(R.string.article_deleted), Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mFavoritesPresenter.loadFavs();
    }
}
