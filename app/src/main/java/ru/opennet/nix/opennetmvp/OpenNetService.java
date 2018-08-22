package ru.opennet.nix.opennetmvp;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import java.util.List;
import ru.opennet.nix.opennetmvp.news.NewsItem;
import ru.opennet.nix.opennetmvp.news.NewsModel;
import ru.opennet.nix.opennetmvp.utils.Links;
import ru.opennet.nix.opennetmvp.utils.OpenNetPreferences;

public class OpenNetService extends IntentService {

    private static final String TAG = "OpenNetPollService";
    private static final long POLL_INTERVAL = AlarmManager.INTERVAL_FIFTEEN_MINUTES;

    public static Intent newIntent(Context context){
        return new Intent(context, OpenNetService.class);
    }

    public OpenNetService() {
        super(TAG);

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if(!isNetworkAvailableAndConnected()){
            return;
        }

        NewsModel model = new NewsModel(NewsModel.RequestCount.FIRST_ONLY);
        model.setLink(Links.MAIN_NEWS_RSS_LINK);
        model.loadNews(new NewsModel.LoadNewsCallback() {
            @Override
            public void onLoad(List<NewsItem> items) {
                action(items);
            }
        });

        Log.i(TAG, "Received an intent: " + intent);
    }



    private boolean isNetworkAvailableAndConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable &&
                cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }

    private void action(List<NewsItem> items){
        if(items.size() == 0){
            return;
        }
        OpenNetPreferences openNetPreferences = new OpenNetPreferences(getApplicationContext());
        String lastItemID = openNetPreferences.getLastNewsID();
        NewsItem item = items.get(0);

        String link = item.getLink();
        String title = item.getTitle();
        String desc = item.getDesc();
        String date = item.getDate();

        Log.i(TAG, "Pref Res: " + lastItemID);
        String receivedItemID = items.get(0).getLink().substring(45);
        if(receivedItemID.equals(lastItemID)){
            Log.i(TAG, "Got an old result: " + receivedItemID);

        }else{
            Log.i(TAG, "Got a new result: " + receivedItemID);
            Resources resources = getResources();
            Intent articleIntent = ArticleActivity.newIntent(getApplicationContext(),
                    link, title, date, resources.getString(R.string.main_news));
            PendingIntent pi = TaskStackBuilder.create(this)
                    .addParentStack(MainActivity.class)
                    .addNextIntentWithParentStack(articleIntent)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notification = new Notification.Builder(this)
                    .setTicker(item.getTitle())
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(title)
                    .setContentText(desc)
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .build();

            NotificationManagerCompat notificationManager =
                    NotificationManagerCompat.from(this);
            notificationManager.notify(0, notification);

            openNetPreferences.setLastNewsID(receivedItemID);
        }

    }

    public static void setServiceAlarm(Context context, boolean isOn){
        Intent i = OpenNetService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (isOn){
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), POLL_INTERVAL, pi);
        }else{
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

    public static boolean isServiceAlarmOn(Context context){
        Intent i = OpenNetService.newIntent(context);
        PendingIntent pi = PendingIntent
                .getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }
}
