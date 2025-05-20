package controller.caro;

import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class GameTimer {

    private Timer timer;
    private int timeLeft; // Thời gian cho mỗi lượt (giây)
    private int minutes; // Tổng thời gian phút
    private int seconds; // Tổng thời gian giây
    private int playTime; // Tổng thời gian chơi (giây)

    private final Label lbTime; // Hiển thị tổng thời gian
    private final Label lbTimeLeft; // Hiển thị thời gian còn lại của lượt
    private final ProgressBar pbTime; // Hiển thị thanh tiến trình thời gian

    private final CaroController controller;

    public GameTimer(Label lbTime, Label lbTimeLeft, ProgressBar pbTime, CaroController controller) {
        this.lbTime = lbTime;
        this.lbTimeLeft = lbTimeLeft;
        this.pbTime = pbTime;
        this.controller = controller;
        this.timeLeft = 30;
        this.minutes = 0;
        this.seconds = 0;
        this.playTime = 0;
    }

    public void startTimer() {
        // Reset thời gian
        minutes = 0;
        seconds = 0;
        playTime = 0;
        timeLeft = 30;

        lbTime.setText(String.format("%02d:%02d", minutes, seconds)); // Hiển thị thời gian ban đầu
        pbTime.setProgress(1.0);
        updateTimeLeftLabel(); // Cập nhật label thời gian còn lại của lượt

        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer(true); // Sử dụng daemon thread để timer tự kết thúc khi ứng dụng đóng
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if (!controller.isGameRunning()) { // Dừng timer nếu game không còn chạy
                        if (timer != null) {
                            timer.cancel();
                        }
                        return;
                    }
                    // Cập nhật tổng thời gian trận đấu (lbTime)
                    seconds++;
                    if (seconds == 60) {
                        minutes++;
                        seconds = 0;
                    }
                    lbTime.setText(String.format("%02d:%02d", minutes, seconds));
                    playTime = minutes * 60 + seconds;

                    // Cập nhật thời gian cho lượt chơi hiện tại (pbTime)
                    timeLeft--;
                    updateTimeLeftLabel();

                    if (timeLeft < 0) {
                        controller.handleTimeOut();
                    }
                    pbTime.setProgress(timeLeft / 30.0);
                });
            }
        }, 0, 1000);
    }

    public void resetTimer() {
        timeLeft = 30;
        pbTime.setProgress(1.0);
        updateTimeLeftLabel();
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        playTime = minutes * 60 + seconds; // Lưu tổng thời gian chơi
    }

    private void updateTimeLeftLabel() {
        if (lbTimeLeft != null) {
            lbTimeLeft.setText(String.format("%02d", Math.max(0, timeLeft))); // Đảm bảo không hiển thị số âm
        }
    }

    public int getPlayTime() {
        return playTime;
    }

    public void reset() {
        minutes = 0;
        seconds = 0;
        playTime = 0;
        timeLeft = 30;
        lbTime.setText("00:00");
    }
}
