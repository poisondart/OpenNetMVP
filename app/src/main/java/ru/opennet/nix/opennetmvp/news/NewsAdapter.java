package ru.opennet.nix.opennetmvp.news;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.opennet.nix.opennetmvp.R;
import ru.opennet.nix.opennetmvp.utils.ClickableMovementMethod;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder>{

    private List<NewsItem> mNewsItems;
    private OnNewsItemClicked mCallback;

    public interface OnNewsItemClicked{
        void onNewsItemClicked(String url);
    }

    public void setOnNewsItemClickedListener(OnNewsItemClicked callback){
        mCallback = callback;
    }

    public NewsAdapter() {
        super();
        mNewsItems = new ArrayList<>();
    }

    public void setNews(List<NewsItem> items){
        mNewsItems = items;
    }

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_card_layout, parent, false);
        return new NewsHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder holder, int position) {
        holder.bindItem(mNewsItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mNewsItems.size();
    }

    class NewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.dateview)
        TextView mDateView;
        @BindView(R.id.titleview)
        TextView mTitleView;
        @BindView(R.id.descview)
        TextView mDescView;
        NewsItem mNewsItem;

        public NewsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mDescView.setMovementMethod(ClickableMovementMethod.getInstance());
            itemView.setOnClickListener(this);
            mDescView.setClickable(false);
            mDescView.setLongClickable(false);
        }
        public void bindItem(NewsItem item){
            mNewsItem = item;
            mDateView.setText(mNewsItem.getDate());
            mTitleView.setText(mNewsItem.getTitle());
            Spanned spanned = Html.fromHtml(mNewsItem.getDesc().replaceAll("<img.+?>", ""));
            mDescView.setText(spanned);
        }

        @Override
        public void onClick(View view) {
            mCallback.onNewsItemClicked(mNewsItem.getLink());
        }
    }
}
