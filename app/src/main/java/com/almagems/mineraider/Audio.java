package com.almagems.mineraider;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;


public final class Audio {

    SoundPool soundPool;
    MediaPlayer mediaPlayer;
    int soundIds[] = new int [10];
    public float musicVolume = 0.2f;
    public float soundVolume = 0.75f;

    private int musicStreamId;
    private int soundStreamId;

    private int musicTrackPos = -1;


    // ctor
    public Audio() {
        System.out.println("Audio ctor...");
    }

    public void setMusicVolume(float value) {
        if (value != musicVolume) {
            musicVolume = value;
            if (musicStreamId != 0) {
                soundPool.setVolume(musicStreamId, musicVolume, musicVolume);
            }

            if (musicStreamId == 0) {
                playMusic();
            }
        }
    }

    public void setSoundVolume(float value) {
        if (value != soundVolume) {
            soundVolume = value;
            soundPool.setVolume(soundStreamId, soundVolume, soundVolume);
            playSound();
        }
    }

    public void init(Context context) {
        mediaPlayer = MediaPlayer.create(context, R.raw.darkwaryurmc_minor_key_music_box);
        //mediaPlayer = MediaPlayer.create(context, R.raw.setuniman_percussive_loop1);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
/*
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (mp == mediaPlayer) {
                    playMusic();
                }
            }
        });
*/
        soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
        //soundIds[0] = soundPool.load(context, R.raw.pol_a_cpu_life_short, 1);
/*
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (sampleId == soundIds[0]) {
                    playMusic();
                }
            }
        });
*/
        //soundIds[0] = soundPool.load(context, R.raw.fins_menu_click1, 1);
        soundIds[0] = soundPool.load(context, R.raw.pop2, 1);
        soundIds[1] = soundPool.load(context, R.raw.mouth_07, 1);
        //soundIds[2] = soundPool.load(context, R.raw.infobleep, 1);
        soundIds[2] = soundPool.load(context, R.raw.sergeeo_xylophone_for_cartoon_2, 1);
    }

    public void playMusic() {
        if (mediaPlayer == null) {
            return;
        }

        if ( mediaPlayer.isPlaying() ) {
            mediaPlayer.setVolume(musicVolume, musicVolume);
        } else {
            mediaPlayer.setLooping(true);
            mediaPlayer.setVolume(musicVolume, musicVolume);
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
        }
    }

    public void stopMusic() {
        mediaPlayer.stop();
    }

    public void playSound() {
        int loopMode = 0; // (0 = no loop, -1 = loop forever)
        soundStreamId = soundPool.play(soundIds[0], soundVolume, soundVolume, 1, loopMode, 1.0f);
    }

    public void playSoundGemHit() {
        int loopMode = 0; // (0 = no loop, -1 = loop forever)
        soundStreamId = soundPool.play(soundIds[1], soundVolume, soundVolume, 1, loopMode, 1.0f);
    }

    public void playSoundInfoBeep() {
        int loopMode = 0; // (0 = no loop, -1 = loop forever)
        soundStreamId = soundPool.play(soundIds[2], soundVolume, soundVolume, 1, loopMode, 1.0f);
    }

    public void stopSound(){
        soundPool.stop(soundStreamId);
    }

    public void pause() {
        if (soundPool == null ) {
            return;
        }

        if (mediaPlayer == null) {
            return;
        }

        musicTrackPos =  mediaPlayer.getCurrentPosition();
        mediaPlayer.stop();
    }

    public void resume() {
        if (soundPool == null) {
            return;
        }

        if (mediaPlayer == null) {
            return;
        }

        mediaPlayer.seekTo(musicTrackPos);
    }

}
