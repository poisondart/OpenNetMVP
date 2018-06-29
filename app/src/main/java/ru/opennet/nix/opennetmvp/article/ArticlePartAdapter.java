package ru.opennet.nix.opennetmvp.article;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import java.util.ArrayList;
import java.util.List;
import ru.opennet.nix.opennetmvp.R;
import ru.opennet.nix.opennetmvp.utils.ClickableMovementMethod;
import ru.opennet.nix.opennetmvp.utils.GlideApp;
import ru.opennet.nix.opennetmvp.utils.Links;

public class ArticlePartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM_TEXT = 1;
    private static final int TYPE_ITEM_IMAGE = 2;
    private static final int TYPE_ITEM_VIDEO = 3;
    private List<ArticlePart> mArticleParts;
    private String mArticleTitle, mArticleDate;
    private OnItemClickListener mCallback;

    public ArticlePartAdapter() {
        super();
        mArticleParts = new ArrayList<>();
    }

    public interface OnItemClickListener{
        void onVideoItemClicked(String link);
    }

    public void setOnItemClickListener(OnItemClickListener callback){
        mCallback = callback;
    }

    public void setParts(List<ArticlePart> parts){
        mArticleParts = parts;
    }

    public void setTitleAndDate(String title, String date){
        mArticleTitle = title;
        mArticleDate = date;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }else{
            if(mArticleParts.get(position - 1).getType() == ArticlePart.SIMPLE_TEXT){
                return TYPE_ITEM_TEXT;
            } else if(mArticleParts.get(position - 1).getType() == ArticlePart.IMAGE){
                return TYPE_ITEM_IMAGE;
            }else if(mArticleParts.get(position - 1).getType() == ArticlePart.VIDEO_ITEM){
                return TYPE_ITEM_VIDEO;
            }else{
                return 0;
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if(viewType == TYPE_HEADER){
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.article_part_header, parent, false);
            return new HeaderViewHolder(itemView);
        }else if(viewType == TYPE_ITEM_TEXT){
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.article_part_text, parent,false);
            return new TextPartViewHolder(itemView);
        }else if(viewType == TYPE_ITEM_IMAGE){
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.article_part_image, parent,false);
            return new ImagePartViewHolder(itemView);
        }else if(viewType == TYPE_ITEM_VIDEO){
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.article_part_video, parent,false);
            return new VideoPartViewHolder(itemView);
        }else return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            final HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.headerTitle.setText(mArticleTitle);
            headerHolder.headerDate.setText(mArticleDate);
        }else if (holder instanceof TextPartViewHolder) {
            final TextPartViewHolder textPartViewHolder = (TextPartViewHolder) holder;
            Spanned spanned = Html.fromHtml(mArticleParts.get(position - 1).getText().replaceAll("<img.+?>", ""));
            textPartViewHolder.textView.setText(spanned);
        }else if (holder instanceof VideoPartViewHolder) {
            final VideoPartViewHolder videoPartViewHolder = (VideoPartViewHolder) holder;
            videoPartViewHolder.bindTube(mArticleParts.get(position - 1));
        }else if (holder instanceof ImagePartViewHolder) {
            final ImagePartViewHolder imagePartViewHolder = (ImagePartViewHolder) holder;
            GlideApp.with(imagePartViewHolder.imageView.getContext())
                    .load(mArticleParts.get(position - 1)
                            .getContentLink())
                    .placeholder(R.drawable.preload)
                    .into(imagePartViewHolder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return mArticleParts.size() + 1;
    }
    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView headerTitle, headerDate;

        private HeaderViewHolder(View view) {
            super(view);
            headerTitle = view.findViewById(R.id.article_title);
            headerDate = view.findViewById(R.id.article_date);
        }
    }
    private class TextPartViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        private TextPartViewHolder(View view){
            super(view);
            textView = view.findViewById(R.id.text_part);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
    private class ImagePartViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private ImagePartViewHolder(View view){
            super(view);
            imageView = view.findViewById(R.id.image_part);
        }
    }
    private class VideoPartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private YouTubeThumbnailView mYouTubeThumbnailView;
        private RelativeLayout mRelativeLayout;
        private ImageView mImageView;
        private ArticlePart mPart;
        boolean isInit = false;

        public VideoPartViewHolder(View itemView) {
            super(itemView);
            mYouTubeThumbnailView = itemView.findViewById(R.id.video_view);
            mImageView = itemView.findViewById(R.id.btnYoutube_player);
            mRelativeLayout = itemView.findViewById(R.id.relative_youtube);
            mImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(isInit){
                mCallback.onVideoItemClicked(mPart.getContentLink());
            }
        }

        private void bindTube(final ArticlePart part){
            mPart = part;
            final YouTubeThumbnailLoader.OnThumbnailLoadedListener onThumbnailLoadedListener =
                    new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                        @Override
                        public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                            youTubeThumbnailView.setVisibility(View.VISIBLE);
                            mRelativeLayout.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                            //nothing
                        }
                    };
            mYouTubeThumbnailView.initialize(Links.YOUTUBE_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                    youTubeThumbnailLoader.setVideo(mPart.getContentLink());
                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
                    isInit = true;
                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                    //nothing
                }
            });
        }
    }
}
