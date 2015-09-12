package com.almagems.mineraider;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;


public final class Audio {

    SoundPool soundPool;
    int soundIds[] = new int [10];
    public float musicVolume = 0.2f;
    public float soundVolume = 0.75f;

    private int musicStreamId;
    private int soundStreamId;

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
        soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);

        soundIds[0] = soundPool.load(context, R.raw.pol_a_cpu_life_short, 1);

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (sampleId == soundIds[0]) {
                    playMusic();
                }
            }
        });


        soundIds[1] = soundPool.load(context, R.raw.menu_glass_click, 1);
        soundIds[2] = soundPool.load(context, R.raw.seeds_cristal_jar_5, 1);
    }

    public void playMusic() {
        if (soundIds[0] == 0) {
            return;
        }

        int loopMode = -1; // (0 = no loop, -1 = loop forever)
        musicStreamId = soundPool.play(soundIds[0], musicVolume, musicVolume, 1, loopMode, 1.0f);
        System.out.println("Music stream id is: " + musicStreamId);
    }

    public void stopMusic() {
        soundPool.stop(musicStreamId);
    }

    public void playSound() {
        int loopMode = 0; // (0 = no loop, -1 = loop forever)
        soundStreamId = soundPool.play(soundIds[2], soundVolume, soundVolume, 1, loopMode, 1.0f);
    }

    public void stopSound(){
        soundPool.stop(soundStreamId);
    }

    public void pause() {
        soundPool.pause(musicStreamId);
    }

    public void resume() {
        soundPool.resume(musicStreamId);
    }

}
