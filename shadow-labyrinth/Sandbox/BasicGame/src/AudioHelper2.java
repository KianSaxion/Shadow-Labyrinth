import nl.saxion.app.audio.MediaPlayer;

public class AudioHelper2 {
    private static MediaPlayer mediaPlayer;
    private static Thread playerThread;

    // Private constructor to prevent instantiation
    private AudioHelper2() {
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
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer = null;
            playerThread = null;
        }
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
     * Stops the currently playing audio file (if any) and starts playing a new audio file.
     *
     * @param filename the path to the new audio file
     * @param loop     whether the new audio should loop
     */
    public static synchronized void newSong(String filename, boolean loop) {
        stop(); // Stop any existing playback
        play(filename, loop); // Start playing the new song
    }
}
