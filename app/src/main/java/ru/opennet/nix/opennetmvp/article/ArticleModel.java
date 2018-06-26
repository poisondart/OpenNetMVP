package ru.opennet.nix.opennetmvp.article;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ArticleModel {

    private String mNumLink;

    public void setNumLink(String link){
        mNumLink = link;
    }

    interface LoadArticleCallback{
        void onLoad(Article article);
    }

    public void loadNews(LoadArticleCallback callback){
        LoadArticleTask loadArticleTask = new LoadArticleTask(mNumLink, callback);
        loadArticleTask.execute();
    }

    static class LoadArticleTask extends AsyncTask<Void, Void, Article>{
        private String mNumLink;
        private final LoadArticleCallback mCallback;

        public LoadArticleTask(String numLink, LoadArticleCallback loadArticleCallback) {
            super();
            mNumLink = numLink;
            mCallback = loadArticleCallback;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Article article) {
            super.onPostExecute(article);
            if(mCallback != null){
                mCallback.onLoad(article);
            }
        }

        @Override
        protected Article doInBackground(Void... voids) {
            Article article = new Article();
            try{
                URL url = new URL(mNumLink);
                InputStream inputStream = url.openConnection().getInputStream();
                article = parseXMLArticle(inputStream);

            }catch (MalformedURLException m){
                m.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }catch (XmlPullParserException x){
                x.printStackTrace();
            }
            return article;
        }
    }

    private static Article parseXMLArticle(InputStream inputStream) throws XmlPullParserException, IOException {
        String title = null;
        String pubDate = null;
        String text = null;
        boolean isItem = false;
        boolean hook = false;

        Article article = new Article();

        try{
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.nextTag();

            while(xmlPullParser.next() != XmlPullParser.END_DOCUMENT){
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();

                if(name == null){
                    continue;
                }

                if(name.equals("channel")){
                    hook = true;
                    continue;
                }

                if(eventType == XmlPullParser.END_TAG){
                    if(name.equalsIgnoreCase("item")){
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

                Log.d("MyXmlParser", "Parsing name ==> " + name);
                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (name.equalsIgnoreCase("title")) {
                    title = result;
                } else if (name.equalsIgnoreCase("pubDate")) {
                    DateFormat oldDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
                    DateFormat newDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:MM", new Locale("ru"));
                    try{
                        Date date = oldDateFormat.parse(result);
                        pubDate = newDateFormat.format(date);
                    }catch (ParseException p){
                        p.printStackTrace();
                    }
                } else if (name.equalsIgnoreCase("description")) {
                    if(!hook){
                        text = result;
                    }else{
                        hook = false;
                        continue;
                    }
                }

                if (title != null && pubDate != null && text != null) {
                    if (isItem) {
                        article.setDate(pubDate);
                        article.setText(text);
                        article.setTitle(title);
                    }
                }
            }
            return article;
        }finally {
            inputStream.close();
        }
    }
}
