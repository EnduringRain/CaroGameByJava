package controller.caro;

import model.Board;
import AI.MinimaxAI;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.Priority;
import utils.Constants;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class BoardManager {

    private Board board;
    private int boardSize;
    private Button[][] cellButtons;
    private final GridPane gridCaro;
    private String playerSymbol;
    private String aiSymbol;
    private final Image xImage;
    private final Image oImage;
    private MinimaxAI ai;
    private String difficulty;

    public BoardManager(GridPane gridCaro, int boardSize, String playerSymbol, String aiSymbol, String difficulty) {
        this.gridCaro = gridCaro;
        this.boardSize = boardSize;
        this.playerSymbol = playerSymbol;
        this.aiSymbol = aiSymbol;
        this.difficulty = difficulty;

        // Tải hình ảnh
        xImage = new Image(getClass().getResourceAsStream(Constants.X_IMAGE_PATH));
        oImage = new Image(getClass().getResourceAsStream(Constants.O_IMAGE_PATH));

        // Khởi tạo board và AI
        board = new Board(boardSize);
        ai = new MinimaxAI(difficulty);
    }

    public void createBoard() {
        // Xóa các con và ràng buộc cũ trước khi tạo bảng mới
        gridCaro.getChildren().clear();
        gridCaro.getColumnConstraints().clear();
        gridCaro.getRowConstraints().clear();

        // Thiết lập khoảng cách giữa các ô bằng 0
        gridCaro.setHgap(0);
        gridCaro.setVgap(0);

        // Thiết lập ColumnConstraints
        for (int i = 0; i < boardSize; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / boardSize);
            colConst.setHgrow(Priority.ALWAYS);
            gridCaro.getColumnConstraints().add(colConst);
        }

        // Thiết lập RowConstraints
        for (int i = 0; i < boardSize; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / boardSize);
            rowConst.setVgrow(Priority.ALWAYS);
            gridCaro.getRowConstraints().add(rowConst);
        }

        cellButtons = new Button[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Button cell = createCell(i, j);
                cellButtons[i][j] = cell;
                gridCaro.add(cell, j, i);
            }
        }
    }

    private Button createCell(int row, int col) {
        Button cell = new Button();
        cell.setPrefSize(50, 50);
        cell.setMinSize(15, 15);
        cell.setMaxSize(50, 50);
        cell.setStyle(Constants.STYLE_CELL_DEFAULT);
        cell.setFocusTraversable(false);
        return cell;
    }

    public void setupCellEvents(CaroController controller) {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                final int row = i;
                final int col = j;

                cellButtons[i][j].setOnAction(event -> {
                    controller.handleCellClick(row, col);
                });

                cellButtons[i][j].setOnMouseEntered(e -> {
                    if (controller.isGameRunning() && controller.isPlayerTurn() && board.getCells()[row][col].isEmpty()) {
                        cellButtons[row][col].setStyle(Constants.STYLE_CELL_HOVER);
                    }
                });

                cellButtons[i][j].setOnMouseExited(e -> {
                    if (controller.isGameRunning() && board.getCells()[row][col].isEmpty()) {
                        cellButtons[row][col].setStyle(Constants.STYLE_CELL_DEFAULT);
                    } else if (controller.isGameRunning() && !board.getCells()[row][col].isEmpty()) {
                        String symbol = board.getCells()[row][col].getSymbol();
                        if (symbol.equals(Constants.SYMBOL_X)) {
                            cellButtons[row][col].setStyle(Constants.STYLE_CELL_X);
                        } else if (symbol.equals(Constants.SYMBOL_O)) {
                            cellButtons[row][col].setStyle(Constants.STYLE_CELL_O);
                        }
                    }
                });
            }
        }
    }

    public void drawSymbol(int row, int col, String symbol) {
        Button cell = cellButtons[row][col];
        if (cell != null) {
            cell.setGraphic(null); // Xóa graphic cũ nếu có

            ImageView imageView = new ImageView();
            // Giảm padding để biểu tượng lớn hơn
            double padding = cell.getHeight() * 0.15;
            imageView.fitWidthProperty().bind(cell.widthProperty().subtract(padding));
            imageView.fitHeightProperty().bind(cell.heightProperty().subtract(padding));
            imageView.setPreserveRatio(true);

            if (symbol.equals(Constants.SYMBOL_X) && xImage != null && !xImage.isError()) {
                imageView.setImage(xImage);
                cell.setStyle(Constants.STYLE_CELL_X);

                // Thêm hiệu ứng animation
                imageView.setScaleX(0);
                imageView.setScaleY(0);
                ScaleTransition st = new ScaleTransition(Duration.millis(200), imageView);
                st.setFromX(0);
                st.setFromY(0);
                st.setToX(1);
                st.setToY(1);
                st.play();

            } else if (symbol.equals(Constants.SYMBOL_O) && oImage != null && !oImage.isError()) {
                imageView.setImage(oImage);
                cell.setStyle(Constants.STYLE_CELL_O);

                // Thêm hiệu ứng animation
                imageView.setScaleX(0);
                imageView.setScaleY(0);
                ScaleTransition st = new ScaleTransition(Duration.millis(200), imageView);
                st.setFromX(0);
                st.setFromY(0);
                st.setToX(1);
                st.setToY(1);
                st.play();
            }

            // Chỉ đặt graphic nếu có ảnh hợp lệ, tránh lỗi nếu ảnh không tải được
            if (imageView.getImage() != null) {
                cell.setGraphic(imageView);
            }
        }
    }

    public void makeMove(int row, int col, String symbol) {
        board.makeMove(row, col, symbol);
        drawSymbol(row, col, symbol);
    }

    public boolean checkWin(int row, int col, String symbol) {
        return board.checkWin(row, col, symbol);
    }

    public boolean isDraw() {
        return board.isDraw();
    }

    public int[] findBestMove() {
        return ai.findBestMove(board);
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
        this.ai = new MinimaxAI(difficulty);
    }

    public Board getBoard() {
        return board;
    }

    public Button[][] getCellButtons() {
        return cellButtons;
    }

    public void reset(int boardSize, String playerSymbol, String aiSymbol) {
        this.boardSize = boardSize;
        this.playerSymbol = playerSymbol;
        this.aiSymbol = aiSymbol;
        this.board = new Board(boardSize);
    }

    public java.util.List<int[]> getEmptyPositions() {
        java.util.List<int[]> emptyPositions = new java.util.ArrayList<>();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board.getCells()[i][j].isEmpty()) {
                    emptyPositions.add(new int[]{i, j});
                }
            }
        }
        return emptyPositions;
    }

    public void highlightWinningLine(int row, int col, String symbol) {
        // Tìm đường thắng
        int[][] winningLine = board.findWinningLine(row, col, symbol);

        if (winningLine != null) {
            // Tạo hiệu ứng highlight cho từng ô trong đường thắng
            for (int[] pos : winningLine) {
                Button cell = cellButtons[pos[0]][pos[1]];

                // Hiệu ứng nhấp nháy
                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(cell.styleProperty(),
                                symbol.equals(Constants.SYMBOL_X) ? Constants.STYLE_CELL_X_WIN : Constants.STYLE_CELL_O_WIN)),
                        new KeyFrame(Duration.millis(500), new KeyValue(cell.styleProperty(),
                                symbol.equals(Constants.SYMBOL_X) ? Constants.STYLE_CELL_X : Constants.STYLE_CELL_O)),
                        new KeyFrame(Duration.millis(1000), new KeyValue(cell.styleProperty(),
                                symbol.equals(Constants.SYMBOL_X) ? Constants.STYLE_CELL_X_WIN : Constants.STYLE_CELL_O_WIN))
                );
                timeline.setCycleCount(5);
                timeline.play();
            }
        }
    }
}
