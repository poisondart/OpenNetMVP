package ru.opennet.nix.opennetmvp.favorites;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.opennet.nix.opennetmvp.R;
import ru.opennet.nix.opennetmvp.article.Article;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder> {

    private List<Article> mArticles;
    private OnFavsItemClicked mCallback;

    public FavoritesAdapter() {
        super();
        mArticles = new ArrayList<>();
    }

    public String getFavLink(int pos){
        return mArticles.get(pos).getLink();
    }

    public interface OnFavsItemClicked{
        void onFavsItemClicked(String url, String title, String date);
    }

    public void setOnFavsItemClickedListener(OnFavsItemClicked callback){
        mCallback = callback;
    }

    public void setArticles(List<Article> articles){
        mArticles = articles;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_article_card, parent, false);
        return new FavoritesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
        holder.bindArticle(mArticles.get(position));
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    class FavoritesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.dateview_fav)
        TextView mDateView;

        @BindView(R.id.titleview_fav)
        TextView mTitleView;

        Article mArticle;

        public FavoritesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bindArticle(Article article){
            mArticle = article;
            mDateView.setText(article.getDate());
            mTitleView.setText(article.getTitle());
        }

        @Override
        public void onClick(View view) {
            mCallback.onFavsItemClicked(mArticle.getLink(), mArticle.getTitle(), mArticle.getDate());
        }
    }
}
