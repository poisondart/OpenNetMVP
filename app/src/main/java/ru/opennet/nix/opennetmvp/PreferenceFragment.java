package ru.opennet.nix.opennetmvp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PreferenceFragment extends PreferenceFragmentCompat implements
        Preference.OnPreferenceClickListener{
    private Preference mAboutPreference, mLicencePreference, mDevPreference, mNotificationsPreference;
    private String mMessage;

    public PreferenceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLicenseMessage();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.app_settings, rootKey);
        mAboutPreference = getPreferenceManager().findPreference(getString(R.string.app_key));
        mLicencePreference = getPreferenceManager().findPreference(getString(R.string.licence_key));
        mDevPreference = getPreferenceManager().findPreference(getString(R.string.go_dev_key));
        mNotificationsPreference = getPreferenceManager().findPreference(getString(R.string.notifications_key));
        mAboutPreference.setOnPreferenceClickListener(this);
        mLicencePreference.setOnPreferenceClickListener(this);
        mDevPreference.setOnPreferenceClickListener(this);
        mNotificationsPreference.setOnPreferenceClickListener(this);
    }
    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()){
            case "app_key":
                Toast.makeText(getActivity(), R.string.app_version, Toast.LENGTH_LONG).show();
                break;
            case "dev_key":
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:novembernix@gmail.com"));
                try {
                    startActivity(Intent.createChooser(intent, getString(R.string.go_dev)));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), getString(R.string.no_email_clients), Toast.LENGTH_SHORT).show();
                }
                break;
            case "licence_key":
                showLicenses();
                break;
            case "notifications_key":
                boolean shouldStartAlarm = !OpenNetService.isServiceAlarmOn(getActivity());
                OpenNetService.setServiceAlarm(getActivity(), shouldStartAlarm);
                break;
        }
        return true;
    }
    private void initLicenseMessage(){
        try{
            AssetManager assetManager = getActivity().getAssets();
            InputStream inputStream = assetManager.open("license.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String receiveString;
            StringBuilder stringBuilder = new StringBuilder();

            while ( (receiveString = bufferedReader.readLine()) != null ) {
                if(receiveString.equals("")){
                    stringBuilder.append(System.getProperty("line.separator"));
                }else{
                    stringBuilder.append(receiveString);
                }
            }
            inputStream.close();
            mMessage = stringBuilder.toString();

        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private void showLicenses(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.licence)
                .setMessage(mMessage)
                .setCancelable(false)
                .setPositiveButton("ОК",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
