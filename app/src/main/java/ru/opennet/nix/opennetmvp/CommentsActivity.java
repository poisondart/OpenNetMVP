package ru.opennet.nix.opennetmvp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import ru.opennet.nix.opennetmvp.comments.CommentFragment;

public class CommentsActivity extends AppCompatActivity {

    private static final String EXTRA_LINK_COMMENTS = "ru.opennet.nix.opennetclient.comments";

    public static Intent newInstance(Context context, String commentsLink){
        Intent intent = new Intent(context, CommentsActivity.class);
        intent.putExtra(EXTRA_LINK_COMMENTS, commentsLink);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        if(savedInstanceState == null){
            String link = getIntent().getStringExtra(EXTRA_LINK_COMMENTS);
            FragmentManager fragmentManager = getSupportFragmentManager();
            CommentFragment fragment = CommentFragment.newInstance(link);
            fragmentManager.beginTransaction().add(R.id.comments_fragment_host, fragment).commit();
        }
    }
}
