package AI;

import model.Board;
import model.Cell;

public class Evaluator {

    private static final int WIN_LENGTH = 5;

    public int evaluate(Board board) {
        int score = 0;
        score += evaluateDirection(board, 1, 0);  // Hàng ngang
        score += evaluateDirection(board, 0, 1);  // Hàng dọc
        score += evaluateDirection(board, 1, 1);  // Đường chéo xuống
        score += evaluateDirection(board, 1, -1); // Đường chéo lên
        return score;
    }

    private int evaluateDirection(Board board, int dx, int dy) {
        int score = 0;
        Cell[][] cells = board.getCells();
        int size = board.getSize();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                // Kiểm tra xem có thể tạo thành chuỗi dài WIN_LENGTH từ vị trí (i,j) không
                if (i + (WIN_LENGTH - 1) * dx >= size
                        || i + (WIN_LENGTH - 1) * dx < 0
                        || j + (WIN_LENGTH - 1) * dy >= size
                        || j + (WIN_LENGTH - 1) * dy < 0) {
                    continue; // Bỏ qua nếu không đủ ô
                }

                int oCount = 0, xCount = 0, emptyCount = 0;
                boolean nearOpponent = false;

                // Kiểm tra đoạn dài WIN_LENGTH ô
                for (int k = 0; k < WIN_LENGTH; k++) {
                    int r = i + k * dx;
                    int c = j + k * dy;

                    String symbol = cells[r][c].getSymbol();
                    switch (symbol) {
                        case "O" ->
                            oCount++;
                        case "X" ->
                            xCount++;
                        default ->
                            emptyCount++;
                    }
                }

                // Kiểm tra xem đoạn này có gần nước đi của đối thủ không
                for (int k = -1; k <= WIN_LENGTH; k++) {
                    int r = i + k * dx;
                    int c = j + k * dy;
                    if (r >= 0 && r < size && c >= 0 && c < size && cells[r][c].getSymbol().equals("X")) {
                        nearOpponent = true;
                        break;
                    }
                }
                
                // Tính điểm dựa trên mẫu quân cờ
                if (oCount > 0 && xCount == 0) {
                    // Chỉ có quân O (AI)
                    switch (oCount) {
                        case 5 -> score += 100000;  // Thắng
                        case 4 -> score += emptyCount > 0 ? 10000 : 0;  // 4 quân liên tiếp
                        case 3 -> score += emptyCount > 1 ? 1000 : 0;   // 3 quân liên tiếp
                        case 2 -> score += emptyCount > 2 ? 100 : 0;    // 2 quân liên tiếp
                        case 1 -> score += emptyCount > 3 ? 10 : 0;     // 1 quân
                    }
                } else if (xCount > 0 && oCount == 0) {
                    // Chỉ có quân X (người chơi)
                    switch (xCount) {
                        case 5 -> score -= 100000;  // Thua
                        case 4 -> score -= emptyCount > 0 ? 10000 : 0;  // Ngăn chặn 4 quân
                        case 3 -> score -= emptyCount > 1 ? 1000 : 0;   // Ngăn chặn 3 quân
                        case 2 -> score -= emptyCount > 2 ? 100 : 0;    // Ngăn chặn 2 quân
                        case 1 -> score -= emptyCount > 3 ? 10 : 0;     // Ngăn chặn 1 quân
                    }
                }
            }
        }
        return score;
    }

    public boolean hasDoubleThreats(Board board, int x, int y) {
        int threats = 0;
        // Kiểm tra các hướng
        if (checkThreat(board, x, y, 1, 0)) {
            threats++; // ngang
        }
        if (checkThreat(board, x, y, 0, 1)) {
            threats++; // dọc
        }
        if (checkThreat(board, x, y, 1, 1)) {
            threats++; // chéo xuống
        }
        if (checkThreat(board, x, y, 1, -1)) {
            threats++; // chéo lên
        }
        return threats >= 2;
    }

    private boolean checkThreat(Board board, int x, int y, int dx, int dy) {
        Cell[][] cells = board.getCells();
        int size = board.getSize();
        int count = 0;
        int empty = 0;

        // Kiểm tra 5 ô liên tiếp
        for (int i = -4; i <= 4; i++) {
            int newX = x + i * dx;
            int newY = y + i * dy;
            if (newX >= 0 && newX < size && newY >= 0 && newY < size) {
                String symbol = cells[newX][newY].getSymbol();
                if (symbol.equals("O")) {
                    count++;
                } else if (symbol.equals("")) {
                    empty++;
                }
            }
        }
        return count == 3 && empty == 2;
    }
}
