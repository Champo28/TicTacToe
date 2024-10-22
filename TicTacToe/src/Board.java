import java.util.*;
// import java.util.ArrayList;

/**
 * Represents a board.
 */
public class Board implements Ilayout,Cloneable{

    private ID[][] board;
    private ID playersTurn;
    private ID winner;
    private HashSet<Integer> movesAvailable;
    private int moveCount;
    private boolean gameOver;
    private int lastMove;
    Board() {
        board = new ID[M][N];
        movesAvailable = new HashSet<>();
        lastMove = 0;
        reset();
    }

    // Constructor just for test cases
    Board(ID[][] board){
        this.board = board;
        movesAvailable = new HashSet<>();
        playersTurn = ID.X;
        for (int row = 0; row < N; row++)
            for (int col = 0; col < M; col++) {
                if(board[row][col] == ID.Blank){
                    movesAvailable.add(row * N + col);
                }
            }
    }

    /**
     * Set the cells to be blank and load the available moves (all the moves are
     * available at the start of the game).
     */
    private void initialize () {
        System.out.println("initiating");
        for (int row = 0; row < N; row++)
            for (int col = 0; col < M; col++) {
                board[row][col] = ID.Blank;
            }
        movesAvailable.clear();

        for (int i = 0; i < N*M; i++) {
            movesAvailable.add(i);
        }
    }

    /**
     * Restart the game with a new blank board.
     */
    public void reset() {
        moveCount = 0;
        gameOver = false;
        playersTurn = ID.X;
        winner = ID.Blank;
        initialize();
    }

    /**
     * Places an X or an O on the specified index depending on whose turn it is.
     * @param index     position starts in 0 and increases from left to right and from top to bottom
     * @return          true if the move has not already been played
     */
    public boolean move (int index) {

        return move(index / N, index % M);

    }

    /**
     * Places an X or an O on the specified location depending on who turn it is.
     * @param x         the x coordinate of the location
     * @param y         the y coordinate of the location
     * @return          true if the move has not already been played
     */
    public boolean move (int x, int y) {

        if (gameOver) {
            throw new IllegalStateException("Game over. No more moves can be played.");
        }

        if (board[x][y] == ID.Blank) {
            board[x][y] = playersTurn;
        } else {
            return false;
        }

        moveCount++;
        movesAvailable.remove(x * N + y);
        lastMove = x * N + y;

        // The game is a draw.
        if (moveCount == N * M) {
            winner = ID.Blank;
            gameOver = true;
        }

        // Check for a winner.
        /** YOUR CODE HERE */
        if(checkRowsWin(playersTurn) || checkColumnsWin(playersTurn) || checkDiagonalsWin(playersTurn)){
            winner = playersTurn;
            gameOver = true;
            return false;
        }


        playersTurn = (playersTurn == ID.X) ? ID.O : ID.X;
        return true;
    }

    /**
     * Checks if the specified player has won in any row.
     *
     * @param player The player ID to check for a win.
     * @return True if the player has won in any row, false otherwise.
     */
    private boolean checkRowsWin(ID player){
        int count = 1;
        for(int i = 0; i < N; i++){
            for(int j = 0; j < M-1; j++){
                if(board[i][j] == player && board[i][j+1] == player) count++;
                else count = 1;
                if(count == K) return true;
            }
            count = 1;
        }
        return false;
    }

    /**
     * Checks if the specified player has won in any column.
     *
     * @param player The player ID to check for a win.
     * @return True if the player has won in any column, false otherwise.
     */
    private boolean checkColumnsWin(ID player){
        int count = 1;
        for(int i = 0; i < N; i++){
            for(int j = 0; j < M-1; j++){
                if(board[j][i] == player && board[j+1][i] == player) count++;
                else count = 1;
                if(count == K) return true;
            }
            count = 1;
        }
        return false;
    }

    /**
     * Checks if the specified player has won in any diagonal.
     *
     * @param player The player ID to check for a win.
     * @return True if the player has won in any diagonal, false otherwise.
     */
    private boolean checkDiagonalsWin(ID player){
        for(int i = 0; i <= M - K; i++){
            for(int j = 0; j <= N - K; j++){
                if(cellEqualsInDiagonal(player,i,j, 1)) return true;
            }
        }

        for(int i = 0; i <= M - K; i++){
            for(int j = K - 1; j < N; j++){
                if(cellEqualsInDiagonal(player,i,j, -1)) return true;
            }
        }

        return false;
    }

    /**
     * Checks if cells in a diagonal starting at the specified row and column
     * with the given direction are all equal to the specified player.
     *
     * @param player The player ID to check for equality.
     * @param row The starting row of the diagonal.
     * @param col The starting column of the diagonal.
     * @param direction The direction to traverse the diagonal (1 for upward, -1 for downward).
     * @return True if cells in the diagonal are all equal to the player, false otherwise.
     */
    private boolean cellEqualsInDiagonal(ID player, int row, int col, int direction){
        for(int x = 1; x < K; x++){
            row+=1;
            col+=direction;
            if(board[row][col] != player){
                return false;
            }
        }
        return true;
    }

    /**
     * Check to see if the game is over (if there is a winner or a draw).
     * @return          true if the game is over
     */
    public boolean isGameOver () {
        return gameOver;
    }

    /**
     * Check to see who's turn it is.
     * @return          the player whose turn it is
     */
    public ID getTurn () {
        return playersTurn;
    }

    /**
     * @return          the player who won (or Blank if the game is a draw)
     */
    public ID getWinner () {
        if (!gameOver) {
            throw new IllegalStateException("Not over yet!");
        }
        return winner;
    }

    /**
     * Get the indexes of all the positions on the board that are empty.
     * @return          the empty cells
     */
    public HashSet<Integer> getAvailableMoves () {
        return movesAvailable;
    }

    /**
     * Gets the last move made on the game board.
     *
     * @return The integer representing the last move made on the game board.
     */
    public int getLastMove() {
        return this.lastMove;
    }

    /**
     * @return  a deep copy of the board
     */
    public Object clone () {
        try {
            Board b = (Board) super.clone();

            b.board = new ID[M][N];
            for (int i = 0; i < M; i++)
                for (int j=0; j < N; j++)
                    b.board[i][j] = this.board[i][j];

            b.playersTurn       = this.playersTurn;
            b.winner            = this.winner;
            b.movesAvailable    = new HashSet<Integer>();
            b.movesAvailable.addAll(this.movesAvailable);
            b.moveCount         = this.moveCount;
            b.gameOver          = this.gameOver;
            b.lastMove          = this.lastMove;
            return b;
        }
        catch (Exception e) {
            throw new InternalError();
        }
    }

    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder();

        for (int y = 0; y < M; y++) {
            for (int x = 0; x < N; x++) {

                if (board[y][x] == ID.Blank) {
                    sb.append("-");
                } else {
                    sb.append(board[y][x].name());
                }
                sb.append(" ");

            }
            if (y != M - 1) {
                sb.append("\n");
            }
        }
        return new String(sb);
    }

    /**
     *
     * @return the children of the receiver.
     */
    public List<Ilayout> children() {
        List<Ilayout> children = new ArrayList<>();
        for(Integer i: movesAvailable){
            if(isBlank(i)){
                int x = i / M;
                int y = i % N;
                Board b = (Board) clone();
                b.move(x,y);
                children.add(b);
            }
        }
        return children;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (getClass() != other.getClass()) return false;
        Board that = (Board) other;

        for (int y = 0; y < M; y++)
            for (int x = 0; x < N; x++)
                if (board[x][y] != that.board[x][y]) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return board.hashCode();
    }

    public boolean isBlank (int index) {
        int x=index / M;
        int y=index % N;
        return (board[x][y] == ID.Blank);
    }
}