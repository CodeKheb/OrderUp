package com.orderup.Handlers;

import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.dsl.FXGL;

import com.orderup.Models.CustomerProcess.CharacterType;

public class AudioManager {

    /** The looping background music track, or null if not started yet. */
    private static Music backgroundMusic;

    private static double musicVolume = 0.65;

    private static double soundVolume = 1.0;

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
        backgroundMusic.getAudio().setVolume(musicVolume);
    }

    public static void pop() {
        playSfx("pop.wav");
    }

    public static void click() {
        playSfx("click.wav");
    }

    public static void missed() {
        playSfx("missed.wav");
    }
    
    public static void playIntro(CharacterType type) {
        playSfx(type.getPrefix() + type.getSpriteIndex() + "_introduction.wav");
    }

    public static void playAnnoyed(CharacterType type) {
        playSfx(type.getPrefix() + type.getSpriteIndex() + "_annoyed.wav");
    }

    public static void playPerfect(CharacterType type) {
        playSfx(type.getPrefix() + type.getSpriteIndex() + "_perfect.wav");
    }

    public static double getMusicVolume() {
        return musicVolume;
    }

    public static void setMusicVolume(double volume) {
        musicVolume = Math.min(Math.max(volume, 0.0), 1.0);
        if (backgroundMusic != null) {
            backgroundMusic.getAudio().setVolume(musicVolume);
        }
    }

    public static double getAudioVolume() {
        return soundVolume;
    }

    public static void setGlobalSoundVolume(double volume) {
        soundVolume = Math.min(Math.max(volume, 0.0), 1.0);
    }

    private static void playSfx(String name) {
        Sound sound = FXGL.getAssetLoader().loadSound(name);
        sound.getAudio().setVolume(soundVolume);
        FXGL.getAudioPlayer().playSound(sound);
    }
}
