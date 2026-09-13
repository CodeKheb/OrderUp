package com.orderup.Handlers;

import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.dsl.FXGL;

import com.orderup.Models.CustomerProcess.CharacterType;

public class AudioManager {

    /** The looping background music track, or null if not started yet. */
    private static Music backgroundMusic;

    /**
     * Starts the background music from the beginning, looping forever.
     * <br><br>
     * Meant to be called once at app launch; if the music is already
     * playing this does nothing, so starting a new game (or returning
     * to the menu) never restarts the track — it just keeps going in
     * the background across every scene.
     */
    public static void playBackgroundMusic() {
        if (backgroundMusic != null) {
            return;
        }

        backgroundMusic = FXGL.getAssetLoader().loadMusic("background_music.wav");
        FXGL.getAudioPlayer().loopMusic(backgroundMusic);
        backgroundMusic.getAudio().setVolume(0.65);
    }

    public static void pop() {
        FXGL.play("pop.wav");
    }

    public static void click() {
        FXGL.play("click.wav");
    }

    public static void missed() {
        FXGL.play("missed.wav");
    }
    
    public static void playIntro(CharacterType type) {
        FXGL.play(type.getPrefix() + type.getSpriteIndex() + "_introduction.wav");
    }

    public static void playAnnoyed(CharacterType type) {
        FXGL.play(type.getPrefix() + type.getSpriteIndex() + "_annoyed.wav");
    }

    public static void playPerfect(CharacterType type) {
        FXGL.play(type.getPrefix() + type.getSpriteIndex() + "_perfect.wav");
    }
}
