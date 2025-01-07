package entity;
import nl.saxion.app.audio.MediaPlayer;
public class AudioHelper {
    private static MediaPlayer mediaPlayer;
    private static Thread playerThread;

    // Private constructor to prevent instantiation
    private AudioHelper() {
    }

    /**
     * Plays the specified audio file in a loop if specified.
     * If an audio file is already playing, this method does nothing.
     *
     * @param filename the path to the audio file
     * @param loop     whether the audio should loop
     */
    public static synchronized void play(String filename, boolean loop) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            return; // Ignore if already playing
        }
        stop(); // Stop any existing playback
        mediaPlayer = new MediaPlayer(filename, loop);
        playerThread = new Thread(mediaPlayer);
        playerThread.start();
    }

    /**
     * Stops the currently playing audio file.
     * If no audio is playing, this method does nothing.
     */
    public static synchronized void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            try {
                playerThread.join(); // Ensure the thread has finished
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            mediaPlayer = null;
            playerThread = null;
        }
    }

    /**
     * Pauses the currently playing audio file.
     * If no audio is playing, this method does nothing.
     */
    public static synchronized void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    /**
     * Resumes the currently paused audio file.
     * If no audio is paused, this method does nothing.
     */
    public static synchronized void resume() {
        if (mediaPlayer != null && mediaPlayer.isPaused()) {
            mediaPlayer.resume();
        }
    }

    /**
     * Sets the volume of the currently playing audio file.
     *
     * @param volume the volume level (0.0 to 1.0)
     */
    public static synchronized void setVolume(float volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    /**
     * Sets whether the currently playing audio file should loop.
     *
     * @param loop whether the audio should loop
     */
    public static synchronized void setLoop(boolean loop) {
        if (mediaPlayer != null) {
            mediaPlayer.setLoop(loop);
        }
    }

    /**
     * Returns the filename of the currently playing audio file.
     *
     * @return the filename of the currently playing audio file, or null if no audio is playing
     */
    public static synchronized String getFilename() {
        return mediaPlayer != null ? mediaPlayer.getFilename() : null;
    }

    /**
     * Checks if an audio file is currently playing.
     *
     * @return true if an audio file is playing, false otherwise
     */
    public static synchronized boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    /**
     * Checks if an audio file is currently paused.
     *
     * @return true if an audio file is paused, false otherwise
     */
    public static synchronized boolean isPaused() {
        return mediaPlayer != null && mediaPlayer.isPaused();
    }

    /**
     * Checks if the currently playing audio file is set to loop.
     *
     * @return true if the audio is set to loop, false otherwise
     */
    public static synchronized boolean isLoop() {
        return mediaPlayer != null && mediaPlayer.isLoop();
    }

    /**
     * Stops the currently playing audio file (if any) and starts playing a new audio file.
     *
     * @param filename the path to the new audio file
     * @param loop     whether the new audio should loop
     */
    public static synchronized void newSong(String filename, boolean loop) {
        stop(); // Stop any existing playback
        play(filename, loop); // Start playing the new song
    }

    public static boolean isSongInArray(String song, String[] songs) {
        if (song == null) return false;
        for (String s : songs) {
            if (song.equals(s)) {
                return true;
            }
        }
        return false;
    }
}