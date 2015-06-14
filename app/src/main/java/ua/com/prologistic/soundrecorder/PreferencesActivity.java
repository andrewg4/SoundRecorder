package ua.com.prologistic.soundrecorder;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Andrew on 09.06.2015.
 */


public class PreferencesActivity extends PreferenceActivity {
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
