package com.almagems.mineraider;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;


public final class Audio {

    MediaPlayer mediaPlayer;
    SoundPool soundPool;


    int soundIds[] = new int [10];
    public float musicVolume = 0.5f;
    public float soundVolume = 0.5f;

    private int soundStreamId;

    private int musicTrackPos = 0;


    // ctor
    public Audio() {
        //System.out.println("Audio ctor...");
    }

    public void setMusicVolume(float value) {
        if (value != musicVolume) {
            musicVolume = value;
            playMusic();
        }
    }

    public void setSoundVolume(float value) {
        if (value != soundVolume) {
            soundVolume = value;
            soundPool.setVolume(soundStreamId, soundVolume, soundVolume);
            playSound();
        }
    }

    private void createMediaPlayer(Context context) {
        mediaPlayer = MediaPlayer.create(context, R.raw.pol_better_world_short1);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

//        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                if (mp == mediaPlayer) {
//                    playMusic();
//                }
//            }
//        });
    }

    private void createSoundPool(Context context) {
        soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);

        //soundIds[0] = soundPool.load(context, R.raw.pol_a_cpu_life_short, 1);

//        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
//            @Override
//            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
//                if (sampleId == soundIds[0]) {
//                    playMusic();
//                }
//            }
//        });

        //soundIds[0] = soundPool.load(context, R.raw.fins_menu_click1, 1);
        soundIds[0] = soundPool.load(context, R.raw.fins_button_5 /*pop2*/, 1);
        soundIds[1] = soundPool.load(context, R.raw.mouth_07, 1);
        //soundIds[2] = soundPool.load(context, R.raw.infobleep, 1);
        soundIds[2] = soundPool.load(context, R.raw.sergeeo_xylophone_for_cartoon_2, 1);
        soundIds[3] = soundPool.load(context, R.raw.dland_hint, 1);

    }

    public void init(Context context) {
        createMediaPlayer(context);
        createSoundPool(context);
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
            mediaPlayer.seekTo(musicTrackPos);
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

    public void playSoundHint() {
        int loopMode = 0; // (0 = no loop, -1 = loop forever)
        soundStreamId = soundPool.play(soundIds[3], soundVolume, soundVolume, 1, loopMode, 1.0f);
    }

    public void stopSound(){
        soundPool.stop(soundStreamId);
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void resume() {
        mediaPlayer.start();
    }

    public void release() {
        if (soundPool != null ) {
            soundPool.release();
            soundPool = null;
        }

        if (mediaPlayer != null) {
            musicTrackPos = mediaPlayer.getCurrentPosition();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void create() {
        createSoundPool(Engine.activity);
        createMediaPlayer(Engine.activity);

        playMusic();
    }

}
