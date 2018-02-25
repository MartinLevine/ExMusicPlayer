# ExMusicPlayer
[![](https://jitpack.io/v/Next5Studio/ExMusicPlayer.svg)](https://jitpack.io/#Next5Studio/ExMusicPlayer)
### 1、引用 ExMusicPlayer
> - 在repositories中添加下列代码

		allprojects {
			repositories {
				...
				maven { url 'https://jitpack.io' }         <--添加此代码
			}
		}
	 

> - 在dependency中添加项目依赖

		dependencies {
 			compile 'com.github.Next5Studio:ExMusicPlayer:v1.0.0'         <--添加此代码
		}
> - 重构你的项目



# 使用方法
- 播放音乐

		在代码中：
		ExMusicPlayer mPlayer = new ExMusicPlayer();
		mPlayer.createStreamFromUrl("https://xxx.xxx.xx.xx");//也有其他方法可以创建流媒体
		mPlayer.play();

- 绑定SeekBar

		ExMusicPlayer为了方便绑定SeekBar，特意设计了一个监听用来绑定SeekBar
		在代码中：
		mPlayer.setOnPositionChangeListener(new ExMusicPlayer.OnPositionChangeListener() {
				@Override
				public void onChange(int position){
						mSeekBar.setProgress(position);
				}
		});

- 使用频谱

		在代码中：
		private ExCircleWaveView mWaveView;
		mWaveView = findViewById(R.id.mWaveView);//绑定xml布局
		
		//然后弄一个时钟向WaveView里传递频谱数据
		 timer = new Timer();
		 timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mWaveView.setFFT(player.getFFTData());
            }
        },100,100);

# API文档
- ### 音乐播放相关
	- void createStreamFromUri(Context context, Uri uri)  通过句柄和请求Uri参数创建文件流
		- 参数一   context   句柄
		- 参数二   uri   请求Uri，可以是网络地址，也可以是Context中的Uri参数

	- void createStreamFormUrl(String url)  通过网络地址创建文件流
		- 参数一   url   网络地址

	- void createStreamFromFile(String path)  通过文件路径创建文件流
		- 参数一   path   文件路径

	- void createStreamFromDataSource(MediaDataSource dataSource)  通过媒体数据源创建文件流
		- 参数一   dataSource   媒体数据源

	- void createStreamFromFileDescriptor(FileDescriptor descriptor)  通过文件描述者创建文件流
		- 参数一   descriptor   文件描述者

	- int getDuration()  获取歌曲总时间

	- int getCurrentPosition()  获取现在播放的位置

	- int getAudioSessionId()  获取AudioSessionId

	- boolean isPlaying()  是否正在播放

	- boolean isLooping()  是否循环播放

	- void setLooping(boolean isLooping)  设置播放器是否循环播放
		- 参数一   isLooping   是否循环播放

	- MediaPlayer.TrackInfo[] getTrackInfo()  获取播放器Track信息

	- void reset()  重置播放器状态

	- void release()  释放播放器资源

	- void seekTo(int position)  设置播放位置
		- 参数一   position   播放位置

	- void seekTo(long position, int mode)  设置播放位置
		- 参数一   position   播放位置
		- 参数二   mode   模式

	- void setOnPositionChangeListener(final OnPositionChangeListener onPositionChangeListener)  监听位置改变
		- 参数一   onPositionChangeListener   位置改变监听接口

	- void setOnCompletionListener(final OnCompletionListener onCompletionListener)  监听播放完毕
		- 参数一   onCompletionListener   播放完毕监听接口

	- void play(boolean isRedefine)  开始播放
		- 参数一   isRedefine   是否重新播放

	- void pause()  暂停播放

	- void stop()  停止播放

- ### 频谱相关

	- void setVisualizerData(@VisualizerRange int visualizerRange, boolean enbleWave, boolean enbleFFT)  设置获取通道信息的相关属性
		- 参数一   visualizerRange   频谱数据长度
		- 参数二   enbleWave   是否捕获频谱数组
		- 参数三   enbleFFT   是否捕获傅里叶数组

	- void setFFtRange(int range)  设置自动傅里叶变换返回的FFT数组长度，请在getFFTData()之前调用
		- 参数一   range   傅里叶数组长度

	- byte[] getFFTData()  获取音频傅里叶通道信息，返回变换之后的傅里叶数组，默认长度45
		- 参数一   visualizerRange   频谱数据长度
		- 参数二   enbleWave   是否捕获频谱数组
		- 参数三   enbleFFT   是否捕获傅里叶数组

	- byte[] getWaveData()  获取音频频谱通道信息

### Made by 404 Not Found. on Next5Soft 
