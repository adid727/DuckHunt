package com.project.AdidNissan.finalproject;

/**
 * Created by Adid on 3/17/2017.
 */

        import android.content.Context;
        import android.media.AudioManager;
        import android.media.MediaPlayer;
        import android.media.SoundPool;

public class SoundPlayer {

    private static SoundPool soundPool;
    private static int Win;
    private static int Lose;

    public SoundPlayer(Context context) {
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

        Win = soundPool.load(context, R.raw.gunshot, 1);
        Lose = soundPool.load(context, R.raw.tracker, 1);

    }

    public void playGunShot() {
        soundPool.play(Win, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playLose() {
        soundPool.play(Lose, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playTitleMusic(Context context){
        MediaPlayer mp = MediaPlayer.create(context, R.raw.titlescreen);
        mp.setLooping(true);
    }

}
