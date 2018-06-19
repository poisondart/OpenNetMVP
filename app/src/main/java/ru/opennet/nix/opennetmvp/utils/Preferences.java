package ru.opennet.nix.opennetmvp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import ru.opennet.nix.opennetmvp.R;

public class Preferences {

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private final String PREF_TITLE = "opennet_prefs";
    private final String TOPIC_TITLE = "topic_title";
    private final String TOPIC_LINK = "topic_link";

    public Preferences(Context context){
        mSharedPreferences = context.getSharedPreferences(PREF_TITLE, Context.MODE_PRIVATE);
    }

    public String getTopicTitle(Context context){
        return mSharedPreferences.getString(TOPIC_TITLE, context.getString(R.string.main_news));
    }

    public String getTopicLink(){
        return mSharedPreferences.getString(TOPIC_LINK, Links.MAIN_NEWS_RSS_LINK);
    }

    public void setTopicTitle(String title){
        mEditor = mSharedPreferences.edit();
        mEditor.putString(TOPIC_TITLE, title);
        mEditor.apply();
    }
    public void setTopicLink(String link){
        mEditor = mSharedPreferences.edit();
        mEditor.putString(TOPIC_LINK, link);
        mEditor.apply();
    }
}
