package ru.opennet.nix.opennetmvp.topics;

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
import ru.opennet.nix.opennetmvp.utils.Links;

public class TopicsBottomAdapter extends RecyclerView.Adapter<TopicsBottomAdapter.TopicsViewHolder> {

    private List<TopicItem> mTopicItems;
    private OnTopicItemClicked mCallback;

    public interface OnTopicItemClicked{
        void TopicItemClicked(String title, String url);
    }

    public void setOnTopicItemClickedListener(OnTopicItemClicked callback) {
        mCallback = callback;
    }

    public TopicsBottomAdapter() {
        super();
        mTopicItems = new ArrayList<>();
    }

    public void setTopics(List<TopicItem> topics){
        mTopicItems = topics;
    }

    @NonNull
    @Override
    public TopicsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_sheet_news_item, parent, false);
        return new TopicsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicsViewHolder holder, int position) {
        holder.bindView(mTopicItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mTopicItems.size();
    }

    class TopicsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.topic_title)
        TextView mTextView;
        TopicItem mTopicItem;
        TopicsViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }
        public void bindView(TopicItem item){
            mTopicItem = item;
            mTextView.setText(mTopicItem.getTitle());
        }

        @Override
        public void onClick(View view) {
            mCallback.TopicItemClicked(mTopicItem.getTitle(), mTopicItem.getLink());
        }
    }
}
