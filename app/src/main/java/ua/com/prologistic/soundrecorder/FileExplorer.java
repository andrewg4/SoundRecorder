package ua.com.prologistic.soundrecorder;

/**
 * Created by Andrew on 09.06.2015.
 */

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ua.com.prologistic.soundrecorder.utils.Utils;

public class FileExplorer extends ListActivity {

    private static final int IDM_PREF = 101;
    private static final int IDM_EXIT = 102;
    private List<String> item = null;
    private List<String> path = null;
    private String rootDir;
    private TextView pathToDir;

    Utils utils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filexplorer);
        pathToDir = (TextView) findViewById(R.id.path);

        utils = new Utils();
        rootDir = utils.getAudioFolder();

        getDir(rootDir);
    }

    private void getDir(String dirPath) {
        pathToDir.setText("Розміщення: " + dirPath);
        item = new ArrayList<>();
        path = new ArrayList<>();
        File f = new File(dirPath);
        File[] files = f.listFiles();

        if (!dirPath.equals(rootDir)) {
            item.add(rootDir);
            path.add(rootDir);
            item.add("../");
            path.add(f.getParent());
        }

        for (int i = 0; i < files.length; i++) {
            File file = files[i];

            if (!file.isHidden() && file.canRead()) {
                path.add(file.getPath());
                if (file.isDirectory()) {
                    item.add(file.getName() + "/");
                } else {
                    item.add(file.getName());
                }
            }
        }

        ArrayAdapter<String> fileList =
                new ArrayAdapter<>(this, R.layout.row, item);
        setListAdapter(fileList);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        File file = new File(path.get(position));

        if (file.isDirectory()) {
            if (file.canRead()) {
                getDir(path.get(position));
            }
        } else {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), getMimeType(file.getAbsolutePath()));
            startActivity(Intent.createChooser(intent, "Оберіть програму : "));
        }
    }

    private String getMimeType(String absolutePath) {
        int exp = absolutePath.lastIndexOf('.');
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(absolutePath.substring(exp + 1));

    }


    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(this);

        int color = Color.BLACK;
        if (prefs.getBoolean(getString(R.string.pr_color_red), false)) {
            color += Color.RED;
        }
        if (prefs.getBoolean(getString(R.string.pr_color_green), false)) {
            color += Color.GREEN;
        }
        if (prefs.getBoolean(getString(R.string.pr_color_blue), false)) {
            color += Color.BLUE;
        }
        float fSize = Float.parseFloat(
                prefs.getString(getString(R.string.pr_size), "20"));

        String regular = prefs.getString(getString(R.string.pr_style), "");
        int typeface = Typeface.NORMAL;

        if (regular.contains("Bold")) {
            typeface += Typeface.BOLD;
        }
        if (regular.contains("Italic")) {
            typeface += Typeface.ITALIC;
        }

        pathToDir.setTextSize(fSize);
        pathToDir.setTextColor(color);
        pathToDir.setTypeface(null, typeface);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, IDM_PREF, Menu.NONE, "Налаштування")
                .setAlphabeticShortcut('t');
        menu.add(Menu.NONE, IDM_EXIT, Menu.NONE, "Вихід")
                .setAlphabeticShortcut('x');
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case IDM_PREF:
                Intent i = new Intent();
                i.setClass(this, PreferencesActivity.class);
                startActivity(i);
                break;
            case IDM_EXIT:
                finish();
                break;
            default:
                return false;
        }
        return true;
    }


}

