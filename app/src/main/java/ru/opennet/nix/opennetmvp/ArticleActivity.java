package ru.opennet.nix.opennetmvp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import ru.opennet.nix.opennetmvp.article.ArticleFragment;

public class ArticleActivity extends AppCompatActivity {

    private static final String EXTRA_LINK = "ru.opennet.nix.opennetclient.link";
    private static final String EXTRA_TITLE = "ru.opennet.nix.opennetclient.title";
    private static final String EXTRA_DATE = "ru.opennet.nix.opennetclient.date";
    private static final String EXTRA_CATEGORY = "ru.opennet.nix.opennetclient.category";

    public static Intent newIntent(Context context, String link, String title, String date, String cat){
        Intent intent = new Intent(context, ArticleActivity.class);
        intent.putExtra(EXTRA_LINK, link);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_DATE, date);
        intent.putExtra(EXTRA_CATEGORY, cat);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        String link = getIntent().getStringExtra(EXTRA_LINK);
        String title = getIntent().getStringExtra(EXTRA_TITLE);
        String date = getIntent().getStringExtra(EXTRA_DATE);
        String cat = getIntent().getStringExtra(EXTRA_CATEGORY);

        FragmentManager fragmentManager = getSupportFragmentManager();
        ArticleFragment fragment = ArticleFragment.newInstance(link, title, date, cat);
        fragmentManager.beginTransaction().add(R.id.article_fragment_host, fragment).commit();
    }
}
