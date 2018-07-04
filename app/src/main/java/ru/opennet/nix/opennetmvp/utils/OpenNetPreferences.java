package ru.opennet.nix.opennetmvp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import ru.opennet.nix.opennetmvp.R;

public class OpenNetPreferences {

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private final String PREF_TITLE = "opennet_prefs";
    private final String TOPIC_TITLE = "topic_title";
    private final String TOPIC_LINK = "topic_link";
    private final String LAST_NEWS_GUID = "last_result_guid";

    public OpenNetPreferences(Context context){
        mSharedPreferences = context.getSharedPreferences(PREF_TITLE, Context.MODE_PRIVATE);
    }

    public String getTopicTitle(Context context){
        return mSharedPreferences.getString(TOPIC_TITLE, context.getString(R.string.main_news));
    }

    public String getTopicLink(){
        return mSharedPreferences.getString(TOPIC_LINK, Links.MAIN_NEWS_RSS_LINK);
    }

    public String getLastNewsID(){
        return mSharedPreferences.getString(LAST_NEWS_GUID, null);
    }

    public void setLatsNewsID(String latsNewsID){
        mEditor = mSharedPreferences.edit();
        mEditor.putString(LAST_NEWS_GUID, latsNewsID);
        mEditor.apply();
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
