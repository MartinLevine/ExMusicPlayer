package com.nirtonsoft.mediaplayer.Utils;

import android.content.Context;
import android.media.MediaDataSource;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.RequiresApi;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 404 Not Found. on 18-2-21.
 */

public class ExMusicPlayer {

    public static final int VISUALIZER_SIZERANGE_SHORT = 0;
    public static final int VISUALIZER_SIZERANGE_LONG = 1;

    private OnPositionChangeListener onPositionChangeListener;
    private OnCompletionListener onCompletionListener;
    private Visualizer mVisualizer;
    private static MediaPlayer player;

    private byte[] mWaveData;
    private byte[] mFFTData;
    private int mFFTCount = 45;

    public ExMusicPlayer(){
        player = new MediaPlayer();
        setVisualizerData(VISUALIZER_SIZERANGE_SHORT,true,true);
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mVisualizer.setEnabled(false);
            }
        });
    }

    @IntDef({VISUALIZER_SIZERANGE_SHORT,VISUALIZER_SIZERANGE_LONG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface VisualizerRange{}

    /**
     * 设置获取通道信息的数据
     * @param visualizerRange 频谱范围
     * @param enbleWave 是否捕获频谱数据数组
     * @param enbleFFT 是否捕获傅里叶变换数组
     */
    public void setVisualizerData(@VisualizerRange int visualizerRange, boolean enbleWave, boolean enbleFFT){
        if (mVisualizer != null){
            mVisualizer.release();
        }
        mVisualizer = new Visualizer(player.getAudioSessionId());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[visualizerRange]);
        mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int i) {
                mWaveData = waveform;
            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] fft, int unknown) {
                byte[] model = new byte[fft.length / 2 + 1];
                model[0] = (byte)Math.abs(fft[1]);
                int j = 1;
                for (int i = 2; i < mFFTCount;){
                    model[j] = (byte) Math.hypot(fft[i],fft[i+1]);
                    i += 2;
                    j++;
                }
                mFFTData = model;
            }
        },Visualizer.getMaxCaptureRate()/2,enbleWave,enbleFFT);
        mVisualizer.setEnabled(true);
    }

    /**
     * 获取音频频谱通道信息
     * @return
     */
    public byte[] getWaveData(){
        return this.mWaveData;
    }

    /**
     * 获取音频通道信息
     * @return
     */
    public byte[] getFFTData(){
        return this.mFFTData;
    }

    /**
     * 设置自动傅里叶变换返回的FFT数组长度
     * @param range 长度
     */
    public void setFFtRange(int range){
        this.mFFTCount = range;
    }

    /**
     * 通过资源文件和句柄直接创建文件流
     * @param context 句柄
     * @param resId 资源文件ID
     */
    public void createStream(Context context, int resId){
        player = MediaPlayer.create(context,resId);
    }

    /**
     * 通过文件路径创建文件流
     * @param path 文件路径
     */
    public void createStreamFromFile(String path){
        try {
            player.reset();
            player.setDataSource(path);
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过网络地址创建文件流
     * @param url 网络地址
     */
    public void createStreamFromUrl(String url){
        try {
            player.reset();
            player.setDataSource(url);
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过句柄和请求Uri创建文件流，Uri可以是网络地址，也可以是Context中的Uri地址
     * @param context 句柄
     * @param uri 请求Uri
     */
    public void createStreamFromUri(Context context, Uri uri){
        try {
            player.setDataSource(context,uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过媒体数据源创建文件流
     * @param dataSource 媒体数据源
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void createStreamFromDataSource(MediaDataSource dataSource){
        player.setDataSource(dataSource);
    }

    /**
     * 通过文件描述者创建文件流
     * @param descriptor 文件描述者
     */
    public void createStreamFromFileDescriptor(FileDescriptor descriptor){
        try {
            player.setDataSource(descriptor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始播放
     */
    public void play(boolean isRedefine){
        //player.prepareAsync();
        if (isRedefine){
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mVisualizer.setEnabled(true);
                    player.start();
                }
            });
        } else {
            player.start();
        }
    }

    /**
     * 暂停播放
     */
    public void pause(){
        player.pause();
        mVisualizer.setEnabled(false);
    }

    /**
     * 停止播放
     */
    public void stop(){
        player.reset();
        player.stop();
    }

    /**
     * 获取歌曲总时间
     * @return
     */
    public int getDuration(){
        return player.getDuration();
    }

    /**
     * 获取现在播放的位置
     * @return
     */
    public int getCurrentPosition(){
        return player.getCurrentPosition();
    }

    /**
     * 获取AudioSessionId
     * @return
     */
    public int getAudioSessionId(){
        return player.getAudioSessionId();
    }

    /**
     * 获取当前是否正在播放
     * @return
     */
    public boolean isPlaying(){
        return player.isPlaying();
    }

    /**
     * 获取播放器是否循环播放
     * @return
     */
    public boolean isLooping() {
        return player.isLooping();
    }

    /**
     * 设置播放器是否循环播放
     * @param isLooping 是否循环
     */
    public void setLooping(boolean isLooping){
        player.setLooping(isLooping);
    }

    /**
     * 获取播放器Track信息
     * @return
     */
    public MediaPlayer.TrackInfo[] getTrackInfo(){
        return player.getTrackInfo();
    }

    /**
     * 重置MediaPlayer的状态
     */
    public void reset(){
        player.reset();
    }

    /**
     * 释放流媒体资源
     */
    public void release(){
        player.release();
    }

    /**
     * 设置播放位置
     * @param position 播放位置
     */
    public void seekTo(int position){
        player.seekTo(position);
    }

    /**
     * 设置播放位置 API >= 安卓o
     * @param position 播放位置
     * @param mode 模式
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void seekTo(long position, int mode){
        player.seekTo(position,mode);
    }

    /**
     * 监听位置改变
     * @param onPositionChangeListener
     */
    public void setOnPositionChangeListener(final OnPositionChangeListener onPositionChangeListener){
        this.onPositionChangeListener = onPositionChangeListener;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (player.isPlaying())
                {
                    onPositionChangeListener.onChange(player.getCurrentPosition());
                }
            }
        },1000,1000);
    }

    public interface OnPositionChangeListener{
        void onChange(int position);
    }

    /**
     * 监听播放完成
     * @param onCompletionListener
     */
    public void setOnCompletionListener(final OnCompletionListener onCompletionListener){
        this.onCompletionListener = onCompletionListener;
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                onCompletionListener.onCompletion(ExMusicPlayer.this);
            }
        });
    }

    public interface OnCompletionListener{
        void onCompletion(ExMusicPlayer exMediaPlayer);
    }

}
