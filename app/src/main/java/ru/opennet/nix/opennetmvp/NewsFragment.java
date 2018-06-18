package ru.opennet.nix.opennetmvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsFragment extends Fragment implements TopicsBottomAdapter.OnTopicItemClicked{

    @BindView(R.id.news_bottomsheet)
    LinearLayout mllBottomSheet;
    @BindView(R.id.visible_sheet)
    ConstraintLayout mVisibleLayout;
    @BindView(R.id.topics_recyclerview)
    RecyclerView mTopicsRecyclerView;
    @BindView(R.id.news_recyclerview)
    RecyclerView mNewsRecyclerView;
    @BindView(R.id.choosen_topic)
    TextView mChosenTopicTextView;

    private TopicsBottomAdapter mTopicsBottomAdapter;
    private NewsAdapter mNewsAdapter;
    private LinearLayoutManager mTopicsLinearLayoutManager;
    private LinearLayoutManager mNewsLinearLayoutManager;
    private BottomSheetBehavior mBottomSheetBehaviour;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.news_fragment_layout, container, false);
        ButterKnife.bind(this, v);
        mBottomSheetBehaviour = BottomSheetBehavior.from(mllBottomSheet);
        mVisibleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBottomSheetBehaviour.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                    mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if (mBottomSheetBehaviour.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
        mTopicsLinearLayoutManager = new LinearLayoutManager(getContext());
        mNewsLinearLayoutManager = new LinearLayoutManager(getContext());
        mTopicsBottomAdapter = new TopicsBottomAdapter();
        mNewsAdapter = new NewsAdapter(initTestNews());
        mTopicsBottomAdapter.setOnTopicItemClickedListener(this);
        mTopicsRecyclerView.setLayoutManager(mTopicsLinearLayoutManager);
        mNewsRecyclerView.setLayoutManager(mNewsLinearLayoutManager);
        mTopicsRecyclerView.setAdapter(mTopicsBottomAdapter);
        mNewsRecyclerView.setAdapter(mNewsAdapter);

        mNewsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0){
                    mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
                if (dy < 0){
                    mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        return v;
    }

    @Override
    public void TopicItemClicked(String title, String url) {
        mChosenTopicTextView.setText(title);
        mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
        //start loading news with using url parameter
    }

    private List<NewsItem> initTestNews(){
        List<NewsItem> items = new ArrayList<>();
        for (int i = 0; i < 7; i++){
            items.add(new NewsItem(getString(R.string.dateview), getString(R.string.titleview),
                    getString(R.string.newsview), "1234"));
        }
        return items;
    }
}
