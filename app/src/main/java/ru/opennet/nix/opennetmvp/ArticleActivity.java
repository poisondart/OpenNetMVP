package ru.opennet.nix.opennetmvp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import ru.opennet.nix.opennetmvp.article.ArticleFragment;

public class ArticleActivity extends AppCompatActivity {

    private static final String EXTRA_LINK = "ru.opennet.nix.opennetclient.link";

    public static Intent newIntent(Context context, String link){
        Intent intent = new Intent(context, ArticleActivity.class);
        intent.putExtra(EXTRA_LINK, link);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        String link = getIntent().getStringExtra(EXTRA_LINK);

        FragmentManager fragmentManager = getSupportFragmentManager();
        ArticleFragment fragment = ArticleFragment.newInstance(link);
        fragmentManager.beginTransaction().add(R.id.article_fragment_host, fragment).commit();
    }
}
