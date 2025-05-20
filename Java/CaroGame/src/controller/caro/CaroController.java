package controller.caro;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Board;
import utils.Constants;
import utils.SoundManager;

public class CaroController implements Initializable {

    @FXML
    private ProgressBar pbTime;
    @FXML
    private Label lbTurn;
    @FXML
    private GridPane gridCaro;
    @FXML
    private RadioButton rdEasy;
    @FXML
    private RadioButton rdMed;
    @FXML
    private RadioButton rdHard;
    @FXML
    private Button btnNewG;
    @FXML
    private Button btnSaveRecord;
    @FXML
    private Button btnQuit;
    @FXML
    private Spinner<Integer> spinN;
    @FXML
    private ComboBox<String> cbTurn;
    @FXML
    private ComboBox<String> cbSymbol;
    @FXML
    private Label lbTime;
    @FXML
    private Label lbTimeLeft;
    @FXML
    private Spinner<Integer> spinNToWin;

    // Các class quản lý
    private BoardManager boardManager;
    private GameTimer gameTimer;
    private UIManager uiManager;

    // Các thuộc tính cơ bản
    private boolean isPlayerTurn = true;
    private boolean gameRunning = false;
    private String difficulty = Constants.DIFFICULTY_MEDIUM;
    private String playerSymbol = Constants.SYMBOL_X;
    private String aiSymbol = Constants.SYMBOL_O;
    private int boardSize = Constants.DEFAULT_BOARD_SIZE;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Khởi tạo spinner(Ma trận) Default 20(DEFAULT_BOARD_SIZE), MIn 20(MIN_BOARD_SIZE) và Max 50 (MAX_BOARD_SIZE)
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                Constants.MIN_BOARD_SIZE, Constants.MAX_BOARD_SIZE, Constants.DEFAULT_BOARD_SIZE);
        spinN.setValueFactory(valueFactory);

        // Khởi tạo combobox lựa chọn ng đi trước
        cbTurn.getItems().addAll("Người chơi đi trước", "AI đi trước");
        cbTurn.setValue("Người chơi đi trước");

        // Khởi tạo combobox lựa chọn ký hiệu chơi (x hoặc O)
        cbSymbol.getItems().addAll(Constants.SYMBOL_X, Constants.SYMBOL_O);
        cbSymbol.setValue(Constants.SYMBOL_X);

        // Khởi tạo 1 togglegroup nhóm các radio button chế độ chơi
        ToggleGroup difficultyGroup = new ToggleGroup();
        rdEasy.setToggleGroup(difficultyGroup);
        rdMed.setToggleGroup(difficultyGroup);
        rdHard.setToggleGroup(difficultyGroup);
        rdMed.setSelected(true);

        // Khởi tạo progressbar ban đầu - thời gian lượt chơi
        pbTime.setProgress(1.0);

        // Khởi tạo giá trị cho các label
        lbTurn.setText("Chưa bắt đầu");
        lbTime.setText("00:00");
        lbTimeLeft.setText(String.valueOf(Constants.DEFAULT_TIME_LIMIT));

        // Khởi tạo các manager
        uiManager = new UIManager(lbTurn, btnSaveRecord);
        gameTimer = new GameTimer(lbTime, lbTimeLeft, pbTime, this);
        boardManager = new BoardManager(gridCaro, boardSize, playerSymbol, aiSymbol, difficulty);

        // Khởi tạo spinNToWin - số ký hiệu để thắng
        SpinnerValueFactory<Integer> winLengthFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                3, 10, 5); // Min: 3, Max: 10, Default: 5
        spinNToWin.setValueFactory(winLengthFactory);
    }

    // Methods lấy tên người đăng nhập nếu có
    public void setCurrentUser(String username) {
        uiManager.setCurrentUser(username);
    }

    @FXML
    private void radioAction(ActionEvent event) {
        // Kiểm tra xem radio nào được chọn
        if (rdEasy.isSelected()) {
            difficulty = Constants.DIFFICULTY_EASY;
        } else if (rdMed.isSelected()) {
            difficulty = Constants.DIFFICULTY_MEDIUM;
        } else if (rdHard.isSelected()) {
            difficulty = Constants.DIFFICULTY_HARD;
        }

        // Game khởi động AI chạy với độ khó tương ứng
        if (gameRunning) {
            boardManager.setDifficulty(difficulty);
        }
    }

    @FXML
    private void btnNewGameAction(ActionEvent event) {
        gameTimer.stopTimer();
        gameTimer.reset();

        boardSize = spinN.getValue();

        // Cập nhật WIN_LENGTH từ giá trị của spinner
        if (spinNToWin != null) {
            int winLength = spinNToWin.getValue();
            Board.setWinLength(winLength);
        }

        isPlayerTurn = cbTurn.getValue().equals("Người chơi đi trước");

        // Xác định biểu tượng cho người chơi và AI
        if (cbSymbol != null && cbSymbol.getValue() != null) {
            // Nếu combo chọn ký hiệu là X thì người chơi chơi với ký hiệu X và AI là 0 và ngược lại.
            if (cbSymbol.getValue().startsWith(Constants.SYMBOL_X)) {
                playerSymbol = Constants.SYMBOL_X;
                aiSymbol = Constants.SYMBOL_O;
            } else {
                playerSymbol = Constants.SYMBOL_O;
                aiSymbol = Constants.SYMBOL_X;
            }
        }

        uiManager.setSymbols(playerSymbol, aiSymbol);
        boardManager.reset(boardSize, playerSymbol, aiSymbol);
        boardManager.createBoard();
        boardManager.setupCellEvents(this);

        gameRunning = true;
        uiManager.resetForNewGame();
        uiManager.updateTurnLabel(isPlayerTurn);
        gameTimer.startTimer();

        if (!isPlayerTurn) {
            performAIMove();
        }
    }

    public void handleCellClick(int row, int col) {
        if (!gameRunning || !isPlayerTurn) {
            return;
        }

        if (!boardManager.getBoard().getCells()[row][col].isEmpty()) {
            return;
        }

        boardManager.makeMove(row, col, playerSymbol);
        SoundManager.playMoveSound();

        if (boardManager.checkWin(row, col, playerSymbol)) {
            boardManager.highlightWinningLine(row, col, playerSymbol);
            SoundManager.playWinSound();
            endGame(playerSymbol + " thắng!");
            return;
        }

        if (boardManager.isDraw()) {
            endGame("Hòa!");
            return;
        }

        isPlayerTurn = false;
        uiManager.updateTurnLabel(isPlayerTurn);
        gameTimer.resetTimer();

        if (gameRunning) {
            performAIMove();
        }
    }

    private void performAIMove() {
        if (!gameRunning) {
            return;
        }

        uiManager.setAIThinking();

        Thread aiThread = new Thread(() -> {
            int[] bestMove = boardManager.findBestMove();
            final int row = bestMove[0];
            final int col = bestMove[1];

            Platform.runLater(() -> {
                if (!gameRunning) {
                    return;
                }

                boardManager.makeMove(row, col, aiSymbol);
                SoundManager.playMoveSound();

                if (boardManager.checkWin(row, col, aiSymbol)) {
                    boardManager.highlightWinningLine(row, col, aiSymbol);
                    SoundManager.playLoseSound();
                    endGame(aiSymbol + " thắng!");
                    return;
                }

                if (boardManager.isDraw()) {
                    endGame("Hòa!");
                    return;
                }

                isPlayerTurn = true;
                uiManager.updateTurnLabel(isPlayerTurn);
                gameTimer.resetTimer();
            });
        });

        aiThread.setDaemon(true);
        aiThread.start();
    }

    // Chọn nước đi ngẫu nhiên nếu hết thời gian mà chưa đánh
    public void handleTimeOut() {
        if (!gameRunning) {
            return;
        }
        performAIMove();
    }

    private void endGame(String message) {
        gameRunning = false;
        gameTimer.stopTimer();

        if (message.equals("Hòa!")) {
            SoundManager.playDrawSound();
        }

        uiManager.showGameEndMessage(message);
    }

    @FXML
    private void showRecord(ActionEvent event) {

    }

    @FXML
    private void btnSaveRecord(ActionEvent event) {

    }

    @FXML
    private void btnQuitAction(ActionEvent event) {
        if (uiManager.showConfirmationDialog("Xác nhận thoát", "Bạn có chắc muốn thoát không?")) {
            gameTimer.stopTimer();
            Stage stage = (Stage) btnQuit.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    private void spinNMouseClicked(MouseEvent event) {
        // Nội dung xử lý cho sự kiện click chuột trên spinN (nếu có)
    }

    @FXML
    private void cbTurnMouseClick(MouseEvent event) {
        // Nội dung xử lý cho sự kiện click chuột trên cbTurn (nếu có)
    }

    // Getter cho các thuộc tính cần thiết
    public boolean isGameRunning() {
        return gameRunning;
    }

    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }
}
