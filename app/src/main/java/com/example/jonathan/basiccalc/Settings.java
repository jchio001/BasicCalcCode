package com.example.jonathan.basiccalc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.CheckBox;


public class Settings extends ActionBarActivity {

    private static final String SETTING_CHECK_BOX = "CHECK";
    private static final String SETTING_CHECK1_BOX = "CHECK1";
    private CheckBox cb, cb1;
    @Override
    protected void onCreate(Bundle savedSettings) {
        super.onCreate(savedSettings);
        setContentView(R.layout.activity_settings);
        cb = (CheckBox) findViewById(R.id.checkBox);
        cb1 = (CheckBox) findViewById(R.id.checkBox1);
        cb.setChecked(isCheckedSettingEnabled());
        cb1.setChecked(isChecked1SettingEnabled());
    }

    private boolean isCheckedSettingEnabled() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        return sp.getBoolean(SETTING_CHECK_BOX, false);
    }

    private boolean isChecked1SettingEnabled() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        return sp.getBoolean(SETTING_CHECK1_BOX, false);
    }


    private void saveSettings(boolean enabled, boolean enabled1) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.edit().putBoolean(SETTING_CHECK_BOX, enabled).apply();
        sp.edit().putBoolean(SETTING_CHECK1_BOX, enabled1).apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    public void onPause() {
        super.onPause();
        // Persist the setting. Could also do this with an OnCheckedChangeListener.
        saveSettings(cb.isChecked(), cb1.isChecked());
    }

    public void clickedDone (View v) {
        saveSettings(cb.isChecked(),cb1.isChecked());
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
