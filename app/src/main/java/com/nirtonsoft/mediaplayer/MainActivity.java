package com.nirtonsoft.mediaplayer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.nirtonsoft.mediaplayer.CustomViews.ExCircleWaveView;
import com.nirtonsoft.mediaplayer.Utils.ExMusicPlayer;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    private SeekBar mSeekBar;
    private ExMusicPlayer player;
    private ExCircleWaveView mWaveView;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int i = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
            if (i != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},0);
            }
        }


        mSeekBar = findViewById(R.id.mSeekbar);
        mWaveView = findViewById(R.id.mWaveView);

        player = new ExMusicPlayer();
        //player.createStream(this,R.raw.test);
        mSeekBar.setMax(player.getDuration());

        player.setOnPositionChangeListener(new ExMusicPlayer.OnPositionChangeListener() {
            @Override
            public void onChange(int position) {
                Log.d("position", "onChange: " + position);
                mSeekBar.setProgress(position);
            }
        });

        player.setOnCompletionListener(new ExMusicPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(ExMusicPlayer exMediaPlayer) {
                exMediaPlayer.stop();
                //exMediaPlayer.createStreamFormUrl("http://dl.stream.qqmusic.qq.com/M800000ZxHxP2oszLf.mp3?vkey=30BB0C48C14B3E4C328597BAE1CF8ED003F9E826DD3434A6A1C9E38EF73C56B0DC7D11E5145285764A29FD27F53EEA11C1791B11CCFC9D68&guid=1519400887222&fromtag=30");
                //exMediaPlayer.play();
            }
        });

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mWaveView.setFFT(player.getFFTData());
            }
        },100,100);

    }

    public void stop(View v){
        player.stop();
    }

    public void play(View v){
        player.createStreamFormUrl("http://i.oppsu.cn/link/30780431.mp3");
        mSeekBar.setMax(player.getDuration());
        player.play(true);
    }

    public void pause(View v){
        player.pause();
    }

}
