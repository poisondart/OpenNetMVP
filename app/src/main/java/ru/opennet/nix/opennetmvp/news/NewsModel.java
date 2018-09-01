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
import java.util.ArrayList;
import java.util.List;

import ru.opennet.nix.opennetmvp.utils.DateUtils;

public class NewsModel {

    private String mRequestLink;

    public enum RequestCount {ALL, FIRST_ONLY}

    private RequestCount mCount;

    public NewsModel(RequestCount count){
        mCount = count;
    }

    public void setLink(String link){
        mRequestLink = link;
    }

    public void loadNews(LoadNewsCallback callback){
        LoadNewsTask loadNewsTask = new LoadNewsTask(mRequestLink, callback, mCount);
        loadNewsTask.execute();
    }

    public interface LoadNewsCallback{
        void onLoad(List<NewsItem> items);
    }

    static class LoadNewsTask extends AsyncTask<Void, Void, List<NewsItem>>{

        private String mLink;
        private final LoadNewsCallback mCallback;
        private RequestCount mCount;

        LoadNewsTask(String link, LoadNewsCallback callback, RequestCount count) {
            super();
            mLink = link;
            mCallback = callback;
            mCount = count;
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
                items = parseXMLNews(inputStream, mCount);

            }catch (MalformedURLException m){
                m.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
                return null;
            }catch (XmlPullParserException x){
                x.printStackTrace();
            }
            return items;
        }
    }

    private static List<NewsItem> parseXMLNews(InputStream inputStream, RequestCount count)
            throws XmlPullParserException, IOException{
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
                    pubDate = DateUtils.getNewsDate(result);
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
                        if(count == RequestCount.FIRST_ONLY){
                            return items;
                        }
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
