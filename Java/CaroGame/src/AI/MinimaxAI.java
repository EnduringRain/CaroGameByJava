package AI;

import model.Board;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinimaxAI {

    private final int maxDepth;
    private final Map<String, Integer> transpositionTable;
    private static final int SEARCH_RADIUS = 2; // Chỉ xem xét ô trong bán kính 2 từ nước đi hiện tại
    private final Evaluator evaluator;

    public MinimaxAI(String difficulty) {
        maxDepth = switch (difficulty) {
            case "Easy" ->
                1;
            case "Medium" ->
                2;
            case "Hard" ->
                3;
            default ->
                2;
        };
        transpositionTable = new HashMap<>();
        evaluator = new Evaluator();
    }

    public int[] findBestMove(Board board) {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = new int[]{-1, -1};
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        Evaluator evaluator = new Evaluator();

        // Tạo danh sách các nước đi hợp lệ (gần các nước đã đi)
        List<int[]> validMoves = getValidMoves(board);

        // Nếu bàn cờ trống, đi vào giữa
        if (validMoves.isEmpty()) {
            return new int[]{board.getSize() / 2, board.getSize() / 2};
        }

        // Thêm kiểm tra nước đi thắng ngay lập tức
        for (int[] move : validMoves) {
            int i = move[0];
            int j = move[1];
        
            // Tạo bản sao của bàn cờ để mô phỏng
            Board boardCopy = board.deepCopy();
            
            // Chỉ đánh một nước O tại vị trí này
            boardCopy.makeMove(i, j, "O");
            if (boardCopy.checkWin(i, j, "O")) {
                return new int[]{i, j};   // Trả về nước thắng ngay
            }
        }

        // Kiểm tra nước đi phòng thủ - chặn người chơi sắp thắng
        for (int[] move : validMoves) {
            int i = move[0];
            int j = move[1];

            // Tạo bản sao của bàn cờ để mô phỏng
            Board boardCopy = board.deepCopy();
            
            // Kiểm tra xem người chơi có thể thắng ở vị trí này không
            boardCopy.makeMove(i, j, "X");
            if (boardCopy.checkWin(i, j, "X")) {
                return new int[]{i, j};   // Trả về nước chặn
            }
        }
        
        // Kiểm tra nước tạo double threats (tấn công hai hướng)
        for (int[] move : validMoves) {
            int i = move[0];
            int j = move[1];
            
            // Tạo bản sao của bàn cờ để mô phỏng
            Board boardCopy = board.deepCopy();
            
            boardCopy.makeMove(i, j, "O");
            if (evaluator.hasDoubleThreats(boardCopy, i, j)) {
                return new int[]{i, j};   // Trả về nước tạo double threats
            }
        }

        // Áp dụng minimax cho các nước đi còn lại
        for (int[] move : validMoves) {
            int i = move[0];
            int j = move[1];

            // Tạo bản sao của bàn cờ để mô phỏng
            Board boardCopy = board.deepCopy();
            
            boardCopy.makeMove(i, j, "O");
            int score = minimax(boardCopy, 0, false, alpha, beta);

            if (score > bestScore) {
                bestScore = score;
                bestMove[0] = i;
                bestMove[1] = j;
            }
            alpha = Math.max(alpha, bestScore);
            if (beta <= alpha) {
                break;
            }
        }

        return bestMove;
    }

    private List<int[]> getValidMoves(Board board) {
        List<int[]> validMoves = new ArrayList<>();
        int size = board.getSize();

        // Kiểm tra nếu bàn cờ trống
        boolean isEmpty = true;
        for (int i = 0; i < size && isEmpty; i++) {
            for (int j = 0; j < size && isEmpty; j++) {
                if (!board.getCells()[i][j].isEmpty()) {
                    isEmpty = false;
                    break;
                }
            }
        }

        // Nếu bàn cờ trống, trả về một danh sách chỉ chứa trung tâm
        if (isEmpty) {
            validMoves.add(new int[]{size / 2, size / 2});
            return validMoves;
        }

        boolean[][] hasChecked = new boolean[size][size];

        // Tìm tất cả các nước đã đi và các ô trống xung quanh
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (!board.getCells()[i][j].isEmpty()) {
                    // Tìm các ô trống xung quanh
                    for (int di = -SEARCH_RADIUS; di <= SEARCH_RADIUS; di++) {
                        for (int dj = -SEARCH_RADIUS; dj <= SEARCH_RADIUS; dj++) {
                            int newI = i + di;
                            int newJ = j + dj;

                            // Kiểm tra trong giới hạn bàn cờ, là ô trống và chưa được thêm
                            if (newI >= 0 && newI < size && newJ >= 0 && newJ < size
                                    && board.getCells()[newI][newJ].isEmpty()
                                    && !hasChecked[newI][newJ]) {
                                validMoves.add(new int[]{newI, newJ});
                                hasChecked[newI][newJ] = true;
                            }
                        }
                    }
                }
            }
        }

        return validMoves;
    }

    private static final int WIN_SCORE = Integer.MAX_VALUE / 2;
    private static final int LOSE_SCORE = Integer.MIN_VALUE / 2;
    
    private int minimax(Board board, int depth, boolean isMaximizing, int alpha, int beta) {
        // Kiểm tra thắng/thua với điểm số được cải thiện
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                if (!board.getCells()[i][j].isEmpty()) {
                    String symbol = board.getCells()[i][j].getSymbol();
                    if (board.checkWin(i, j, symbol)) {
                        if (symbol.equals("O")) {
                            return WIN_SCORE - depth; // AI thắng, ưu tiên thắng sớm
                        } else {
                            return LOSE_SCORE + depth; // Người chơi thắng, ưu tiên phòng thủ
                        }
                    }
                }
            }
        }
        
        // Tạo khóa cho trạng thái bàn cờ hiện tại
        String boardKey = getBoardKey(board, depth, isMaximizing);

        // Kiểm tra xem đã tính toán trạng thái này chưa
        if (transpositionTable.containsKey(boardKey)) {
            return transpositionTable.get(boardKey);
        }

        if (depth == maxDepth) {
            int score = evaluator.evaluate(board);
            transpositionTable.put(boardKey, score);
            return score;
        }

        List<int[]> validMoves = getValidMoves(board);

        // Kiểm tra nếu không còn nước đi
        if (validMoves.isEmpty()) {
            return 0; // Hòa
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int[] move : validMoves) {
                int i = move[0];
                int j = move[1];

                if (board.getCells()[i][j].isEmpty()) {
                    // Tạo bản sao của bàn cờ để mô phỏng
                    Board boardCopy = board.deepCopy();
                    
                    boardCopy.makeMove(i, j, "O");
                    int score = minimax(boardCopy, depth + 1, false, alpha, beta);
                    bestScore = Math.max(score, bestScore);
                    alpha = Math.max(alpha, bestScore);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            transpositionTable.put(boardKey, bestScore);
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int[] move : validMoves) {
                int i = move[0];
                int j = move[1];

                if (board.getCells()[i][j].isEmpty()) {
                    // Tạo bản sao của bàn cờ để mô phỏng
                    Board boardCopy = board.deepCopy();
                    
                    boardCopy.makeMove(i, j, "X");
                    int score = minimax(boardCopy, depth + 1, true, alpha, beta);
                    bestScore = Math.min(score, bestScore);
                    beta = Math.min(beta, bestScore);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            transpositionTable.put(boardKey, bestScore);
            return bestScore;
        }
    }

    // Tạo chuỗi đại diện cho trạng thái bàn cờ (dùng làm khóa cho cache)
    private String getBoardKey(Board board, int depth, boolean isMaximizing) {
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                key.append(board.getCells()[i][j].getSymbol().isEmpty() ? "-" : board.getCells()[i][j].getSymbol());
            }
        }
        key.append("|").append(depth).append("|").append(isMaximizing ? "1" : "0");
        return key.toString();
    }
}
