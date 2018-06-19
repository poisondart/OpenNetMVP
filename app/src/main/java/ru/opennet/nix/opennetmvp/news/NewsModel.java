package ru.opennet.nix.opennetmvp.news;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsModel {

    private String mRequestLink;

    public void setLink(String link){
        mRequestLink = link;
    }

    public void loadNews(LoadNewsCallback callback){
        LoadNewsTask loadNewsTask = new LoadNewsTask(mRequestLink, callback);
        loadNewsTask.execute();
    }

    interface LoadNewsCallback{
        void onLoad(List<NewsItem> items);
    }

    static class LoadNewsTask extends AsyncTask<Void, Void, List<NewsItem>>{

        private String mLink;
        private final LoadNewsCallback mCallback;

        LoadNewsTask(String link, LoadNewsCallback callback) {
            super();
            mLink = link;
            mCallback = callback;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<NewsItem> newsItems) {
            super.onPostExecute(newsItems);
            if(mCallback != null){
                mCallback.onLoad(newsItems);
            }
        }

        @Override
        protected List<NewsItem> doInBackground(Void... voids) {
            List<NewsItem> items = new ArrayList<>();
            try{
                URL url = new URL(mLink);
                InputStream inputStream = url.openConnection().getInputStream();
                items = parseXMLNews(inputStream);

            }catch (MalformedURLException m){
                m.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }catch (XmlPullParserException x){
                x.printStackTrace();
            }
            return items;
        }
    }

    private static List<NewsItem> parseXMLNews(InputStream inputStream) throws XmlPullParserException, IOException{
        String title = null;
        String pubDate = null;
        String descr = null;
        String link = null;
        boolean isItem = false;
        boolean hook = false;
        List<NewsItem> items = new ArrayList<>();
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
                        descr = result;
                    }else{
                        hook = false;
                        continue;
                    }
                }else if(name.equalsIgnoreCase("link")){
                    link = result;
                }

                if (title != null && pubDate != null && descr != null && link != null) {
                    if (isItem) {
                        NewsItem item = new NewsItem(pubDate, title, descr, link);
                        items.add(item);
                    }

                    title = null;
                    pubDate = null;
                    descr = null;
                    link = null;
                    isItem = false;
                }
            }
            return items;
        }finally {
            inputStream.close();
        }
    }

}
