package ru.opennet.nix.opennetmvp;

import android.app.Application;

import com.gw.swipeback.tools.WxSwipeBackActivityManager;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class OpenNetApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        WxSwipeBackActivityManager.getInstance().init(this);
        Realm.init(getApplicationContext());
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("opennet.realm")
                .schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(realmConfig);
    }
}
