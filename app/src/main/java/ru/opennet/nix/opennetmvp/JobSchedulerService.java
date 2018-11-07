package ru.opennet.nix.opennetmvp;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.List;

import ru.opennet.nix.opennetmvp.news.NewsItem;
import ru.opennet.nix.opennetmvp.news.NewsModel;
import ru.opennet.nix.opennetmvp.utils.Links;
import ru.opennet.nix.opennetmvp.utils.OpenNetPreferences;

public class JobSchedulerService extends JobService {

    private static final String TAG = "JobSchedulerService";
    public static final int JOB_ID = 6996;
    public static final String CHANNEL_ID = "JobSchedulerServiceNotificationChannel";

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        if (!isNetworkAvailableAndConnected()) return false;

        NewsModel model = new NewsModel(NewsModel.RequestCount.FIRST_ONLY);
        model.setLink(Links.MAIN_NEWS_RSS_LINK);
        model.loadNews(new NewsModel.LoadNewsCallback() {
            @Override
            public void onLoad(List<NewsItem> items) {
                if (items != null) actionOnItems(items);
            }
        });
        return true;
    }

    private void actionOnItems(List<NewsItem> items) {
        if (items.size() == 0) {
            return;
        }
        OpenNetPreferences openNetPreferences = new OpenNetPreferences(getApplicationContext());
        String lastItemID = openNetPreferences.getLastNewsID();
        NewsItem item = items.get(0);

        String link = item.getLink();
        String title = item.getTitle();
        String desc = item.getDesc();
        String date = item.getDate();

        Log.i(TAG, " Pref Res: " + lastItemID);
        String receivedItemID = items.get(0).getLink().substring(45);
        if (receivedItemID.equals(lastItemID)) {
            Log.i(TAG, " Got an old result: " + receivedItemID);

        } else {
            Log.i(TAG, " Got a new result: " + receivedItemID);
            Resources resources = getResources();
            Intent articleIntent = ArticleActivity.newIntent(getApplicationContext(),
                    link, title, date, resources.getString(R.string.main_news));
            PendingIntent pi = TaskStackBuilder.create(this)
                    .addParentStack(MainActivity.class)
                    .addNextIntentWithParentStack(articleIntent)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(title)
                    .setContentText(desc)
                    .setContentIntent(pi)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(receivedItemID.hashCode(), builder.build());

            openNetPreferences.setLastNewsID(receivedItemID);
        }
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }

    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable &&
                cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }
}
