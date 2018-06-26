package ru.opennet.nix.opennetmvp.article;

import android.os.AsyncTask;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArticleModel {

    private String mLink;

    public void setData(String link){
        mLink = link;
    }

    interface LoadArticleCallback{
        void onLoad(List<ArticlePart> articleParts);
    }

    public void loadNews(LoadArticleCallback callback){
        LoadArticleTask loadArticleTask = new LoadArticleTask(mLink, callback);
        loadArticleTask.execute();
    }

    private static class LoadArticleTask extends AsyncTask<Void, Void, List<ArticlePart>>{
        private String mLink;
        private final LoadArticleCallback mCallback;

        public LoadArticleTask(String link, LoadArticleCallback loadArticleCallback) {
            super();
            mLink = link;
            mCallback = loadArticleCallback;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<ArticlePart> articleParts) {
            super.onPostExecute(articleParts);
            if(mCallback != null){
                mCallback.onLoad(articleParts);
            }
        }

        @Override
        protected List<ArticlePart> doInBackground(Void... voids) {
            List<ArticlePart> articleParts = new ArrayList<>();
            try{
                articleParts = parseHTMLArticle(mLink);
            }catch (IOException e){
                e.printStackTrace();
            }
            return articleParts;
        }
    }

    private static List<ArticlePart> parseHTMLArticle(String url) throws IOException{
        Document mDocument;
        Element mElement;
        Elements mChilds;
        List<ArticlePart> articleParts = new ArrayList<>();
            mDocument = Jsoup.connect(url).get();
            mElement = mDocument.select("td[class = chtext]").first();
            mChilds = mElement.getAllElements();
            for(Element e : mChilds){
                if(e.tagName().equals("p") || e.tagName().equals("li")){
                    ArticlePart articlePart = new ArticlePart(ArticlePart.SIMPLE_TEXT, e.html(), url);
                    articleParts.add(articlePart);
                }else if(e.tagName().equals("iframe")){
                    ArticlePart articlePart = new ArticlePart(ArticlePart.VIDEO_ITEM, e.attr("src"), url);
                    articlePart.initVideoID();
                    articleParts.add(articlePart);
                }else if(e.tagName().equals("img")){
                    ArticlePart articlePart = new ArticlePart(ArticlePart.IMAGE, e.attr("src"), url);
                    articleParts.add(articlePart);
                }
            }
        return articleParts;
    }
}
