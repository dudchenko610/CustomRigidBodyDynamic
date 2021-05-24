package com.crazydev.base.implementation;

import java.io.IOException;

import com.crazydev.base.framework.Audio;
import com.crazydev.base.framework.Music;
import com.crazydev.base.framework.Sound;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

public class AndroidAudio implements Audio {
	
	private AssetManager assets;
	private SoundPool soundPool;
	private AudioManager audioManager;
	
	public AndroidAudio(Activity activity) {
	//	activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		this.assets = activity.getAssets();
		this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
		this.audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
		
	}
	
	@Override
	public Music newMusic(String filename) {
		try {
			AssetFileDescriptor assetDescriptor = assets.openFd(filename);
			return new AndroidMusic(assetDescriptor);
			
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load music '" + filename+ "'");
		}
	}

	@Override
	public Sound newSound(String filename) {
		try {
			AssetFileDescriptor assetDescriptor = assets.openFd(filename);
			int soundId = soundPool.load(assetDescriptor, 0);
			return new AndroidSound(soundPool, soundId);
			
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load music '" + filename+ "'");
		}
	}
	
	@Override
	public void volumeUp() {
		this.audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
	}

	@Override
	public void volumeDown() {
		this.audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
	}

	@Override
	public void volumeMute() {
		this.audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI);
	}

}
