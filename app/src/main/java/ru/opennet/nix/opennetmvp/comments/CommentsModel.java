package ru.opennet.nix.opennetmvp.comments;

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

public class CommentsModel {
    private String mCommentsLink;

   public void loadComments(LoadCommentsCallback callback){
       LoadCommentsTask loadCommentsTask = new LoadCommentsTask(callback, mCommentsLink);
       loadCommentsTask.execute();
   }

    public void setLink(String link){
        mCommentsLink = link;
    }

    interface LoadCommentsCallback{
        void onLoad(List<Comment> comments);
    }

    static class LoadCommentsTask extends AsyncTask<Void, Void, List<Comment>>{
        private String mLink;
        private final LoadCommentsCallback mCallback;

        public LoadCommentsTask(LoadCommentsCallback callback, String link) {
            super();
            mLink = link;
            mCallback = callback;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Comment> comments) {
            super.onPostExecute(comments);
            if(mCallback != null){
                //Collections.sort(comments, Comment.COMPARE_BY_POSITION);
                mCallback.onLoad(comments);
            }
        }

        @Override
        protected List<Comment> doInBackground(Void... voids) {
            List<Comment> items = new ArrayList<>();
            try{
                URL url = new URL(mLink);
                InputStream inputStream = url.openConnection().getInputStream();
                items = parseRSSComments(inputStream);

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

    private static List<Comment> parseRSSComments(InputStream inputStream) throws XmlPullParserException, IOException {
        String author = null;
        String pubDate = null;
        String descr = null;
        int pos = -1;
        boolean isItem = false;
        boolean hook = false;

        List<Comment> items = new ArrayList<>();

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

                if (name.equalsIgnoreCase("author")) {
                    author = result;
                } else if (name.equalsIgnoreCase("pubDate")) {
                    pubDate = DateUtils.getCommentDate(result);
                } else if (name.equalsIgnoreCase("description")) {
                    if(!hook){
                        descr = result;
                    }else{
                        hook = false;
                        continue;
                    }
                }else if(name.equalsIgnoreCase("thread")){
                    char c = result.charAt(result.length() - 1);
                    pos = Character.getNumericValue(c);
                }

                if (author != null && pubDate != null && descr != null && pos != -1) {
                    if (isItem) {
                        Comment item = new Comment(author, pos, pubDate, descr);
                        items.add(item);
                    }

                    author = null;
                    pubDate = null;
                    descr = null;
                    pos = -1;
                    isItem = false;
                }
            }
            return items;
        }finally {
            inputStream.close();
        }
    }
}
