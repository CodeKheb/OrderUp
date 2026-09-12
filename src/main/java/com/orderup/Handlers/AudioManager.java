package com.orderup.Handlers;

import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.dsl.FXGL;

public class AudioManager {

    public static void playBackgroundMusic() {
        Music music = FXGL.getAssetLoader().loadMusic("background_music.wav");
        FXGL.getAudioPlayer().loopMusic(music);
        music.getAudio().setVolume(1);
    }

    public static void pop() {
        FXGL.play("pop.wav");
    }

    public static void stopMusic() {
        FXGL.getAudioPlayer().stopAllMusic();
        FXGL.getAudioPlayer().stopAllSounds();
    }
}
