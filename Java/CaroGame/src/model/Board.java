package model;

/**
 *
 * @author Vu
 */
public class Board {

    private final Cell[][] cells;
    private final int size;
    private static int WIN_LENGTH = 5;

    public Board(int size) {
        this.size = size;
        cells = new Cell[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = new Cell();
            }
        }
    }

    public Cell[][] getCells() {
        return cells;
    }

    public int getSize() {
        return size;
    }

    public boolean makeMove(int row, int col, String symbol) {
        if (row < 0 || row >= size || col < 0 || col >= size || !cells[row][col].isEmpty()) {
            return false;
        }
        cells[row][col].setSymbol(symbol);
        return true;
    }

    public boolean checkWin(int row, int col, String symbol) {
        return checkDirection(row, col, symbol, 1, 0) // Horizontal
                || checkDirection(row, col, symbol, 0, 1) // Vertical
                || checkDirection(row, col, symbol, 1, 1) // Diagonal \
                || checkDirection(row, col, symbol, 1, -1); // Diagonal /
    }

    private boolean checkDirection(int row, int col, String symbol, int dx, int dy) {
        int count = 1; // Đếm ô hiện tại
        // Kiểm tra về phía trước
        for (int i = 1; i < WIN_LENGTH; i++) {
            int r = row + i * dx;
            int c = col + i * dy;
            if (r >= 0 && r < size && c >= 0 && c < size && cells[r][c].getSymbol().equals(symbol)) {
                count++;
            } else {
                break;
            }
        }
        // Kiểm tra về phía sau
        for (int i = 1; i < WIN_LENGTH; i++) {
            int r = row - i * dx;
            int c = col - i * dy;
            if (r >= 0 && r < size && c >= 0 && c < size && cells[r][c].getSymbol().equals(symbol)) {
                count++;
            } else {
                break;
            }
        }
        return count >= WIN_LENGTH;
    }

    public boolean isDraw() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (cells[i][j].isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    public Board deepCopy() {
        Board copy = new Board(this.size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                copy.getCells()[i][j].setSymbol(this.cells[i][j].getSymbol());
            }
        }
        return copy;
    }

    // Thêm getter và setter cho WIN_LENGTH
    public static int getWinLength() {
        return WIN_LENGTH;
    }

    public static void setWinLength(int winLength) {
        WIN_LENGTH = winLength;
    }

    public int[][] findWinningLine(int row, int col, String symbol) {
        // Kiểm tra hàng ngang
        int[][] line = checkLineInDirection(row, col, 1, 0, symbol);
        if (line != null) {
            return line;
        }

        // Kiểm tra hàng dọc
        line = checkLineInDirection(row, col, 0, 1, symbol);
        if (line != null) {
            return line;
        }

        // Kiểm tra đường chéo xuống
        line = checkLineInDirection(row, col, 1, 1, symbol);
        if (line != null) {
            return line;
        }

        // Kiểm tra đường chéo lên
        line = checkLineInDirection(row, col, 1, -1, symbol);
        if (line != null) {
            return line;
        }

        return null;
    }

    private int[][] checkLineInDirection(int row, int col, int dx, int dy, String symbol) {
        int count = 1;
        int[][] line = new int[WIN_LENGTH][2];
        line[0] = new int[]{row, col};

        // Kiểm tra theo hướng dương
        for (int i = 1; i < WIN_LENGTH; i++) {
            int newRow = row + i * dx;
            int newCol = col + i * dy;

            if (newRow >= 0 && newRow < size && newCol >= 0 && newCol < size
                    && cells[newRow][newCol].getSymbol().equals(symbol)) {
                line[count] = new int[]{newRow, newCol};
                count++;
            } else {
                break;
            }
        }

        // Kiểm tra theo hướng âm
        for (int i = 1; i < WIN_LENGTH; i++) {
            int newRow = row - i * dx;
            int newCol = col - i * dy;

            if (newRow >= 0 && newRow < size && newCol >= 0 && newCol < size
                    && cells[newRow][newCol].getSymbol().equals(symbol)) {
                line[count] = new int[]{newRow, newCol};
                count++;
            } else {
                break;
            }
        }

        // Nếu đủ winLength quân liên tiếp
        if (count >= WIN_LENGTH) {
            // Cắt mảng để chỉ giữ lại các ô thắng
            int[][] result = new int[WIN_LENGTH][2];
            System.arraycopy(line, 0, result, 0, WIN_LENGTH);
            return result;
        }

        return null;
    }
}
