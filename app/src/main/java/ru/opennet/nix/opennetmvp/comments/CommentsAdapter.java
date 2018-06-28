package ru.opennet.nix.opennetmvp.comments;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.opennet.nix.opennetmvp.R;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsHolder> {

    private List<Comment> mCommentList;

    public CommentsAdapter() {
        super();
        mCommentList = new ArrayList<>();
    }

    public void setComments(List<Comment> comments){
        mCommentList = comments;
    }

    @NonNull
    @Override
    public CommentsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item_layout, parent, false);
        return new CommentsHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsHolder holder, int position) {
        holder.bindItem(mCommentList.get(position));
    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }

    class CommentsHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.comment_author_textview)
        TextView mAuthorView;

        @BindView(R.id.comment_date_textbox)
        TextView mDateView;

        @BindView(R.id.comment_desc_textbox)
        TextView mDescView;

        @BindView(R.id.comment_holder)
        ConstraintLayout mConstraintLayout;

        Comment mComment;

        public CommentsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindItem(Comment c){
            mComment = c;
            mAuthorView.setText(mComment.getAuthor());
            mDateView.setText(mComment.getDate());
            Spanned spanned = Html.fromHtml(mComment.getContent());
            mDescView.setText(spanned);
            /*int pos = (mComment.getPosition() - 1) * 10 + 6;
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mConstraintLayout.getLayoutParams();
            params.setMargins(pos,0,0,0);
            mConstraintLayout.setLayoutParams(params);
            itemView.requestLayout();*/
        }
    }
}