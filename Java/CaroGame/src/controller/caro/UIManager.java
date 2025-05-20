package controller.caro;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class UIManager {

    private final Label lbTurn;
    private final Button btnSaveRecord;

    private String playerSymbol;
    private String aiSymbol;
    private String currentUser;

    public UIManager(Label lbTurn, Button btnSaveRecord) {
        this.lbTurn = lbTurn;
        this.btnSaveRecord = btnSaveRecord;
    }

    public void setSymbols(String playerSymbol, String aiSymbol) {
        this.playerSymbol = playerSymbol;
        this.aiSymbol = aiSymbol;
    }

    public void setCurrentUser(String username) {
        this.currentUser = username;
        if (btnSaveRecord != null) {
            if (username != null && !username.equalsIgnoreCase("Khách") && !username.isEmpty()) {
                btnSaveRecord.setVisible(true);
            } else {
                btnSaveRecord.setVisible(false);
            }
        }
    }

    public void updateTurnLabel(boolean isPlayerTurn) {
        if (isPlayerTurn) {
            lbTurn.setText("Lượt người chơi (" + playerSymbol + ")");
        } else {
            lbTurn.setText("Lượt AI (" + aiSymbol + ")");
        }
    }

    public void setAIThinking() {
        lbTurn.setText("Lượt AI (" + aiSymbol + ") đang suy nghĩ...");
    }

    public void showGameEndMessage(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Kết thúc trò chơi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

        if (btnSaveRecord != null && btnSaveRecord.isVisible()) {
            btnSaveRecord.setDisable(false);
        }
        lbTurn.setText("Game kết thúc");
    }

    public void showErrorAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public boolean showConfirmationDialog(String title, String content) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        return alert.showAndWait().filter(response -> response == javafx.scene.control.ButtonType.OK).isPresent();
    }

    public void resetForNewGame() {
        if (btnSaveRecord != null && btnSaveRecord.isVisible()) {
            btnSaveRecord.setDisable(true);
        }
    }

    public String getCurrentUser() {
        return currentUser;
    }
}
