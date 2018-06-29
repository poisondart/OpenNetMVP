package ru.opennet.nix.opennetmvp.favorites;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                mFavoritesPresenter.deleteArticleFromRealm(mAdapter.getFavLink(pos));
                mAdapter.notifyItemRemoved(pos);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
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
}
