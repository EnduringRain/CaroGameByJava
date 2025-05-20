package utils;

public class Constants {

    // Đường dẫn FXML
    public static final String LOGIN_FXML = "/view/login/Login.fxml";

    public static final String REGISTER_FXML = "/view/register/Register.fxml";
    public static final String CARO_FXML = "/view/caro/Caro.fxml";
    public static final String RECORD_FXML = "/view/record/Record.fxml";

    // Đường dẫn hình ảnh
    public static final String X_IMAGE_PATH = "/images/x.png";
    public static final String O_IMAGE_PATH = "/images/o.png";

    // Cài đặt trò chơi
    public static final int DEFAULT_BOARD_SIZE = 20;
    public static final int MIN_BOARD_SIZE = 10;
    public static final int MAX_BOARD_SIZE = 50;
    public static final int DEFAULT_TIME_LIMIT = 30;

    // Độ khó AI
    public static final String DIFFICULTY_EASY = "Easy";
    public static final String DIFFICULTY_MEDIUM = "Medium";
    public static final String DIFFICULTY_HARD = "Hard";

    // Biểu tượng người chơi
    public static final String SYMBOL_X = "X";
    public static final String SYMBOL_O = "O";

    // CSS Styles
    public static final String STYLE_CELL_DEFAULT = "-fx-background-color: white; " +
            "-fx-border-color: #d0d0d0; " +
            "-fx-border-width: 1px; " +
            "-fx-padding: 0; " +
            "-fx-background-insets: 0;";

    public static final String STYLE_CELL_X = "-fx-background-color: #e8f4ff; " +
            "-fx-border-color: #d0d0d0; " +
            "-fx-border-width: 1px; " +
            "-fx-padding: 0; " +
            "-fx-background-insets: 0;";

    public static final String STYLE_CELL_O = "-fx-background-color: #fff1e6; " +
            "-fx-border-color: #d0d0d0; " +
            "-fx-border-width: 1px; " +
            "-fx-padding: 0; " +
            "-fx-background-insets: 0;";

    public static final String STYLE_CELL_X_WIN = "-fx-background-color: #b3e0ff; " +
            "-fx-border-color: #2196f3; " +
            "-fx-border-width: 2px; " +
            "-fx-padding: 0; " +
            "-fx-background-insets: 0;";

    public static final String STYLE_CELL_O_WIN = "-fx-background-color: #ffcccc; " +
            "-fx-border-color: #ff5252; " +
            "-fx-border-width: 2px; " +
            "-fx-padding: 0; " +
            "-fx-background-insets: 0;";

    public static final String STYLE_CELL_HOVER = "-fx-background-color: #f0f0f0; " +
            "-fx-border-color: #2196f3; " +
            "-fx-border-width: 1.2px; " +
            "-fx-padding: 0; " +
            "-fx-background-insets: 0;";

    public static final String STYLE_AI_THINKING = "-fx-text-fill: #f57c00;";
}
