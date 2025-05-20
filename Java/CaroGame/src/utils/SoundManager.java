package utils;

import javafx.scene.media.AudioClip;

public class SoundManager {

    private static final AudioClip moveSound;
    private static final AudioClip winSound;
    private static final AudioClip loseSound;
    private static final AudioClip drawSound;

    private static boolean soundEnabled = true;

    static {
        moveSound = new AudioClip(SoundManager.class.getResource("/sounds/move.wav").toExternalForm());
        winSound = new AudioClip(SoundManager.class.getResource("/sounds/win.wav").toExternalForm());
        loseSound = new AudioClip(SoundManager.class.getResource("/sounds/lose.wav").toExternalForm());
        drawSound = new AudioClip(SoundManager.class.getResource("/sounds/win.wav").toExternalForm());

    }

    public static void playMoveSound() {
        if (soundEnabled && moveSound != null) {
            moveSound.play();
        }
    }

    public static void playWinSound() {
        if (soundEnabled && winSound != null) {
            winSound.play();
        }
    }

    public static void playLoseSound() {
        if (soundEnabled && loseSound != null) {
            loseSound.play();
        }
    }

    public static void playDrawSound() {
        if (soundEnabled && drawSound != null) {
            drawSound.play();
        }
    }

    public static void setSoundEnabled(boolean enabled) {
        soundEnabled = enabled;
    }

    public static boolean isSoundEnabled() {
        return soundEnabled;
    }
}
