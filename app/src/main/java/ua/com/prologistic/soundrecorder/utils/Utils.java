package ua.com.prologistic.soundrecorder.utils;

import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Andrew on 09.06.2015.
 */
public class Utils {
    // назва теки для зберігання аудіо-файлів
    private final String AUDIO_FOLDER = "dyktofon";

    // отримуємо абсолютний шлях до теки відтворення
    public String getAudioFolder(){
        return new File(Environment.getExternalStorageDirectory(), AUDIO_FOLDER).getAbsolutePath();
    }

    // перевіряємо наявність теки для аудіо-записів
    private void checkAndMakeDir(String folderName){
        File f = new File(Environment.getExternalStorageDirectory(),folderName);

        if (!f.exists()) {
            f.mkdirs();
        }
    }

    // генеруємо назву для 1 запису звуку по даті та часу
    public String setOutputAudioFile(){
        checkAndMakeDir(AUDIO_FOLDER);

        return  getAudioFolder()
                + "/"
                + getCurrentDate()
                + ".3gp" ;
    }

    // повертає дату та час саме в цю секунду для унікальності назви аудіо-файлу
    private String getCurrentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }
}
