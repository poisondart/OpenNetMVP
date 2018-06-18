package ru.opennet.nix.opennetmvp;

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

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder>{

    private List<NewsItem> mNewsItems;

    public NewsAdapter(List<NewsItem> items) {
        super();
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

    class NewsHolder extends RecyclerView.ViewHolder{
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
        }
        public void bindItem(NewsItem item){
            mNewsItem = item;
            mDateView.setText(mNewsItem.getDate());
            mTitleView.setText(mNewsItem.getTitle());
            mDescView.setText(mNewsItem.getDesc());
        }
    }
}
