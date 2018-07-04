package ru.opennet.nix.opennetmvp.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.opennet.nix.opennetmvp.ArticleActivity;
import ru.opennet.nix.opennetmvp.OpenNetService;
import ru.opennet.nix.opennetmvp.R;
import ru.opennet.nix.opennetmvp.topics.TopicItem;
import ru.opennet.nix.opennetmvp.topics.TopicsBottomAdapter;
import ru.opennet.nix.opennetmvp.utils.Links;
import ru.opennet.nix.opennetmvp.utils.OpenNetPreferences;

public class NewsFragment extends MvpAppCompatFragment implements TopicsBottomAdapter.OnTopicItemClicked, NewsView,
    NewsAdapter.OnNewsItemClicked{

    @InjectPresenter
    NewsPresenter mNewsPresenter;

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
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private TopicsBottomAdapter mTopicsBottomAdapter;
    private NewsAdapter mNewsAdapter;
    private LinearLayoutManager mTopicsLinearLayoutManager;
    private LinearLayoutManager mNewsLinearLayoutManager;
    private BottomSheetBehavior mBottomSheetBehaviour;
    private OpenNetPreferences mOpenNetPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.news_fragment_layout, container, false);
        ButterKnife.bind(this, v);
        mOpenNetPreferences = new OpenNetPreferences(getContext());
        mChosenTopicTextView.setText(mOpenNetPreferences.getTopicTitle(getContext()));
        mNewsPresenter.setLink(mOpenNetPreferences.getTopicLink());
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
        mTopicsBottomAdapter.setTopics(initTopics());
        mNewsAdapter = new NewsAdapter();
        mTopicsBottomAdapter.setOnTopicItemClickedListener(this);
        mNewsAdapter.setOnNewsItemClickedListener(this);
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

        OpenNetService.setServiceAlarm(getActivity(), OpenNetService.isServiceAlarmOn(getActivity()));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setUpdating(true);
                mNewsPresenter.loadNews();
            }
        });
        return v;
    }


    @Override
    public void TopicItemClicked(String title, String url) {
        mChosenTopicTextView.setText(title);
        mOpenNetPreferences.setTopicTitle(title);
        mOpenNetPreferences.setTopicLink(url);
        mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mNewsPresenter.showLoading(true);
        mNewsPresenter.setLink(url);
        mNewsPresenter.loadNews();
        mNewsRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onNewsItemClicked(String url, String title, String date) {
        Intent intent = ArticleActivity.newIntent(getContext(), url, title, date, mOpenNetPreferences.getTopicTitle(getContext()));
        startActivity(intent);
    }

    @Override
    public void setUpdating(boolean isLoading) {
        mSwipeRefreshLayout.setRefreshing(isLoading);
    }

    @Override
    public void showNews(List<NewsItem> items) {
        mNewsAdapter.setNews(items);
        mNewsAdapter.notifyDataSetChanged();
        setUpdating(false);
    }

    private List<TopicItem> initTopics(){
        List<TopicItem> topicItems = new ArrayList<>();
        topicItems.add(new TopicItem(getString(R.string.main_news), Links.MAIN_NEWS_RSS_LINK));
        topicItems.add(new TopicItem(getString(R.string.updates), Links.MAIN_NEW_SOFT_UPDATE_RSS_LINK));
        topicItems.add(new TopicItem(getString(R.string.safety), Links.MAIN_SECURITY_PROB_RSS_LINK));
        topicItems.add(new TopicItem(getString(R.string.Ubuntu), Links.UBUNTU_NEWS_RSS_LINK));
        topicItems.add(new TopicItem(getString(R.string.linux), Links.MAIN_LINUX_RSS_LINK));
        topicItems.add(new TopicItem(getString(R.string.Fedora), Links.MAIN_FEDORA_RSS_LINK));
        topicItems.add(new TopicItem(getString(R.string.BSD), Links.MAIN_BSD_RSS_LINK));
        topicItems.add(new TopicItem(getString(R.string.mozilla), Links.MAIN_MOZILLA_FIREFOX_RSS_LINK));
        return topicItems;
    }
}
