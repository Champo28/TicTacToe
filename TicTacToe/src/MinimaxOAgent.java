import java.util.List;
/**
 * The MinimaxOAgent class uses the Minimax algorithm
 * to make optimal moves for the "O" player in a game. It is designed to play against opponents
 * on a given layout (board).
 */
public class MinimaxOAgent {

    private final static int EASY = 1;
    private final static int MEDIUM = 3;
    private final static int HARD = 6;
    private final static int INSANE = 9;
    /**
     * Plays a move using the Minimax algorithm for the "O" player.
     *
     * @param board The current layout (board) of the game.
     * @return The best move determined by the Minimax algorithm.
     */
    static int play(Ilayout board){
        int bestVal = -1000;
        int bestMove = 0;
        List<Ilayout> children = board.children();
        for(Ilayout child: children){
            int moveVal = minimax(child,0, false, Integer.MIN_VALUE, Integer.MAX_VALUE, HARD);
            if(moveVal > bestVal){
                bestVal = moveVal;
                bestMove = child.getLastMove();
            }
        }
        return bestMove;
    }

    /**
     * The Minimax algorithm for determining the best move.
     *
     * @param board                The current layout (board) of the game.
     * @param depth                The current depth of the recursive search.
     * @param isMaximizingPlayer   Indicates whether the current player is maximizing.
     * @param alpha                The alpha value for alpha-beta pruning.
     * @param beta                 The beta value for alpha-beta pruning.
     * @param maxDepth             The maximum depth to search in the game tree.
     * @return The evaluation value of the current move.
     */
    private static int minimax(Ilayout board, int depth, boolean isMaximizingPlayer, int alpha, int beta, int maxDepth){
        if(board.isGameOver()){
            if(board.getWinner() == Ilayout.ID.X) return -10;
            if(board.getWinner() == Ilayout.ID.O) return 10;
            return 0;
        }
        if(depth == maxDepth) return 0;

        if(isMaximizingPlayer){
            int maxEval = Integer.MIN_VALUE;
            List<Ilayout> children = board.children();
            for(Ilayout child: children){
                int eval = minimax(child,depth + 1, false, alpha, beta, maxDepth);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, maxEval);
                if(beta <= alpha) break;
            }
            return maxEval;
        }
        else{
            int minEval = Integer.MAX_VALUE;
            List<Ilayout> children = board.children();
            for(Ilayout child: children){
                int eval = minimax(child,depth + 1, true, alpha, beta, maxDepth);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, minEval);
                if(beta <= alpha) break;
            }
            return minEval;
        }
    }
}
