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
import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsFragment extends Fragment implements TopicsBottomAdapter.OnTopicItemClicked{

    @BindView(R.id.news_bottomsheet)
    LinearLayout mllBottomSheet;
    @BindView(R.id.visible_sheet)
    ConstraintLayout mVisibleLayout;
    @BindView(R.id.topics_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.choosen_topic)
    TextView mChoosenTopicTextView;

    private TopicsBottomAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
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
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new TopicsBottomAdapter();
        mAdapter.setOnTopicItemClickedListener(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        return v;
    }

    @Override
    public void TopicItemClicked(String title, String url) {
        mChoosenTopicTextView.setText(title);
        mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
        //start loading news with using url parameter
    }
}
