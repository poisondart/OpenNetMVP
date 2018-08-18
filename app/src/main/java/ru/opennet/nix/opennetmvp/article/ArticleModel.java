package ru.opennet.nix.opennetmvp.article;

import android.os.AsyncTask;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import io.realm.Realm;
import ru.opennet.nix.opennetmvp.utils.Links;

public class ArticleModel {

    private Article mArticle;
    private List<ArticlePart> mArticleParts = new ArrayList<>();


    public void setArticleParts(List<ArticlePart> articleParts){
        mArticleParts = articleParts;
    }

    public List<ArticlePart> getArticleParts(){
        return mArticleParts;
    }

    public void setArticle(Article article){
        mArticle = article;
    }

    interface LoadArticleCallback{
        void onLoad(List<ArticlePart> articleParts);
    }

    interface LoadCommentsLinkCallback{
        void onLoad(String commentsLink);
    }

    public void loadNews(LoadArticleCallback callback){
        LoadArticleTask loadArticleTask = new LoadArticleTask(mArticle.getLink(), callback);
        loadArticleTask.execute();
    }

    public void loadCommentsLink(LoadCommentsLinkCallback callback){
        LoadCommentsTask loadCommentsTask = new LoadCommentsTask(mArticle.getLink(), callback);
        loadCommentsTask.execute();
    }

    private static class LoadCommentsTask extends AsyncTask<Void, Void, String>{
        private String mLink;
        private final LoadCommentsLinkCallback mCallback;

        public LoadCommentsTask(String link, LoadCommentsLinkCallback callback) {
            super();
            mLink = link;
            mCallback = callback;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(mCallback != null){
                mCallback.onLoad(s);
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            String comlink = "";
            try{
                comlink = parseHTMLArticleCommentsLink(mLink);
            }catch (IOException e){
                e.printStackTrace();
            }
            return comlink;
        }
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

    private static String parseHTMLArticleCommentsLink(String url) throws IOException{
        Document mDocument;
        Element mElement;
        String comlink = "";
        mDocument = Jsoup.connect(url).get();
        mElement = mDocument.select("input[name = om]").first();
        if(mElement != null && mElement.hasAttr("value")){
            comlink = mElement.attr("value");
        }else{
            return null;
        }

        return Links.COMMENTS_LINK.concat(comlink);
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

    @ParametersAreNonnullByDefault
    public void saveArticle(Realm realm){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(mArticle);
                realm.copyToRealm(mArticleParts);
            }
        });
    }

    @ParametersAreNonnullByDefault
    public void deleteArticle(Realm realm){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Article.class).equalTo(Article.LINK, mArticle.getLink()).findAll().deleteAllFromRealm();
                realm.where(ArticlePart.class).equalTo(ArticlePart.ARTICLE_LINK, mArticle.getLink())
                        .findAll().deleteAllFromRealm();
            }
        });
    }

    @ParametersAreNonnullByDefault
    public boolean checkArticleInRealm(Realm realm){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if(!realm.where(Article.class).equalTo(Article.LINK, mArticle.getLink()).findAll().isEmpty()){
                    mArticleParts.addAll(realm.copyFromRealm(realm.where(ArticlePart.class)
                            .equalTo(ArticlePart.ARTICLE_LINK, mArticle.getLink()).findAll()));
                }
            }
        });
        return (mArticleParts.size() > 0);
    }
}
