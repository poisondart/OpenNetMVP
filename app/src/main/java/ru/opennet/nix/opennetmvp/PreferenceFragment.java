package ru.opennet.nix.opennetmvp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

public class PreferenceFragment extends PreferenceFragmentCompat implements
        Preference.OnPreferenceClickListener{
    private Preference mAboutPreference, mLicencePreference, mDevPreference;
    private String mMessage;

    public static final String KEY_BROWSER_TYPE = "b_key";
    public static final String KEY_THEME_TYPE = "t_key";

    public PreferenceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.app_settings, rootKey);
        mAboutPreference = getPreferenceManager().findPreference(getString(R.string.app_key));
        mLicencePreference = getPreferenceManager().findPreference(getString(R.string.licence_key));
        mDevPreference = getPreferenceManager().findPreference(getString(R.string.go_dev_key));
        mAboutPreference.setOnPreferenceClickListener(this);
        mLicencePreference.setOnPreferenceClickListener(this);
        mDevPreference.setOnPreferenceClickListener(this);
    }
    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()){
            case "a_key":
                Toast.makeText(getActivity(), R.string.app_vesion, Toast.LENGTH_LONG).show();
                break;
            case "dev_key":
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:carpuninnandroid@yandex.ru"));
                try {
                    startActivity(Intent.createChooser(intent, getString(R.string.go_dev)));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), getString(R.string.no_email_clients), Toast.LENGTH_SHORT).show();
                }
                break;
            case "l_key":
                //showLicenses();
                break;
        }
        return true;
    }
}
