package ua.com.prologistic.soundrecorder;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import ua.com.prologistic.soundrecorder.utils.Utils;


public class MainActivity extends Activity {
    Button play,stop,record;
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;
    private MediaPlayer m;

    StopWatch stopWatch;
    Utils utils;
    private TextView timerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        utils = new Utils();

        timerView = (TextView) findViewById(R.id.timer);

        play=(Button)findViewById(R.id.button3);
        stop=(Button)findViewById(R.id.button2);
        record=(Button)findViewById(R.id.button);

        stop.setEnabled(false);
        play.setEnabled(false);

        outputFile = utils.setOutputAudioFile();
        Log.i("OUTPUTFILE", outputFile);

        // набір команд для кнопки Запис
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // стандартна ініціалізація звукозаписувача
                    myAudioRecorder = new MediaRecorder();
                    myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                    myAudioRecorder.setOutputFile(outputFile);

                    myAudioRecorder.prepare();
                    myAudioRecorder.start();

                    //ініціалізуємо секундомір з кроком в 1 секунду
                    stopWatch = new StopWatch(timerView, 1);
                    //стартуємо секундомір
                    stopWatch.start();
                }

                catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                catch (IOException e) {
                    e.printStackTrace();
                }

                record.setEnabled(false);
                stop.setEnabled(true);
                play.setEnabled(false);
                Toast.makeText(getApplicationContext(), "Почався аудіозапис", Toast.LENGTH_LONG).show();
            }
        });
        // набір команд для кнопки Стоп
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAudioRecorder.stop();
                myAudioRecorder.release();
                myAudioRecorder  = null;

                stop.setEnabled(false);
                play.setEnabled(true);
                record.setEnabled(true);

                stopWatch.stop();

                Toast.makeText(getApplicationContext(), "Уадіозапис успішо записано",Toast.LENGTH_LONG).show();
            }
        });

        // набір команд для кнопки Відтворити
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws IllegalArgumentException,SecurityException,IllegalStateException {
                  m = new MediaPlayer();

                try {
                    m.setDataSource(outputFile);
                }

                catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    m.prepare();
                }

                catch (IOException e) {
                    e.printStackTrace();
                }

                if(m.isPlaying()){
                    m.pause();
                } else {
                    m.start();
                }


                Toast.makeText(getApplicationContext(), "Відтворюємо аудіозапис", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //Стартує активність списку всіх аудіозаписів
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, FileExplorer.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}