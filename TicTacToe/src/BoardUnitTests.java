import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class BoardUnitTests{
    private final Ilayout.ID X = Ilayout.ID.X;
    private final Ilayout.ID O = Ilayout.ID.O;
    private final Ilayout.ID B = Ilayout.ID.Blank;
    private final Board BlankBoard = new Board(new Ilayout.ID[][]{{B,B,B,B},
                                                                  {B,B,B,B},
                                                                  {B,B,B,B},
                                                                  {B,B,B,B}});

    @Test
    public void testReset(){
        Ilayout.ID[][] b = new Ilayout.ID[][]{{X,O,B,X},
                                              {O,B,O,X},
                                              {B,B,X,O},
                                              {O,X,B,B}};
        Board board = new Board(b);
        board.reset();

        assertEquals(16, board.getAvailableMoves().size());
        assertEquals(board, BlankBoard);
        assertEquals(X, board.getTurn());
        assertFalse(board.isGameOver());
    }

    @Test
    public void testClone(){
        Ilayout.ID[][] b = new Ilayout.ID[][]{{X,O,B,X},
                                              {O,B,O,X},
                                              {B,B,X,O},
                                              {O,X,B,B}};
        Board board = new Board(b);
        Board board2 = new Board(b.clone());
        assertEquals(board,board2);

        Board board3 = new Board();
        assertEquals(board3,BlankBoard);
    }

    @Test
    public void testIsBlank(){
        Ilayout.ID[][] b = new Ilayout.ID[][]{{X,O,B,X},
                                              {O,B,O,X},
                                              {B,B,X,O},
                                              {O,X,B,B}};
        Board board = new Board(b);
        assertTrue(board.isBlank(2));
        assertFalse(board.isBlank(0));
    }

    @Test
    public void testCheckRowsWin(){

        Ilayout.ID[][] b = new Ilayout.ID[][]{{X,X,B,X},
                                              {O,B,O,X},
                                              {X,B,O,O},
                                              {B,B,O,B}};
        Board board = new Board(b);
        board.move(0,2);
        assertTrue(board.isGameOver());
        assertEquals(board.getWinner(), X);
    }

    @Test
    public void testCheckColumnsWin(){
        Ilayout.ID[][] b = new Ilayout.ID[][]{{O,X,B,X},
                                              {B,X,O,X},
                                              {B,B,B,O},
                                              {B,X,O,B}};
        Board board = new Board(b);
        board.move(2,1);
        assertTrue(board.isGameOver());
        assertEquals(board.getWinner(), X);
    }
    @Test
    public void testCheckDiagonalWins(){
        Ilayout.ID[][] b = new Ilayout.ID[][]{{O,X,B,X},
                                              {B,O,B,X},
                                              {B,X,B,O},
                                              {X,O,O,B}};
        Board board = new Board(b);
        board.move(1,2);
        assertTrue(board.isGameOver());
        assertEquals(board.getWinner(), X);
        //case 2
        Ilayout.ID[][] b2 = new Ilayout.ID[][]{{X,X,B,O},
                                               {B,X,B,O},
                                               {B,B,X,O},
                                               {X,O,O,B}};
        Board board2 = new Board(b2);
        board2.move(3,3);
        assertTrue(board2.isGameOver());
        assertEquals(board2.getWinner(), X);
    }

    @Test
    public void testChildren1(){
        List<Ilayout> c =  BlankBoard.children();
        assertEquals(16,c.size());
        Ilayout.ID[][] first = new Ilayout.ID[][]{{X,B,B,B},
                                                  {B,B,B,B},
                                                  {B,B,B,B},
                                                  {B,B,B,B}};
        assertEquals(c.get(0),new Board(first));
        assertEquals(c.get(0).getLastMove(),0);

        Ilayout.ID[][] last = new Ilayout.ID[][]{{B,B,B,B},
                                                 {B,B,B,B},
                                                 {B,B,B,B},
                                                 {B,B,B,X}};

        assertEquals(c.get(15),new Board(last));
        assertEquals(c.get(15).getLastMove(),15);

        Ilayout.ID[][] random = new Ilayout.ID[][]{{B,B,B,B},
                                                   {B,B,B,B},
                                                   {B,X,B,B},
                                                   {B,B,B,B}};

        assertEquals(c.get(9),new Board(random));
        assertEquals(c.get(9).getLastMove(),9);
    }

    @Test
    public void testChildren2(){
        Board board = simulation();
        List<Ilayout> c = board.children();
        assertEquals(c.size(), 5);

        Ilayout.ID[][] first = new Ilayout.ID[][]{{X,O,X,O},
                                                  {X,O,X,O},
                                                  {O,O,X,B},
                                                  {X,B,B,B}};
        assertEquals(c.get(0),new Board(first));
        assertEquals(c.get(0).getLastMove(),9);

        Ilayout.ID[][] last = new Ilayout.ID[][]{{X,O,X,O},
                                                 {X,O,X,O},
                                                 {O,B,X,B},
                                                 {X,B,B,O}};

        assertEquals(c.get(4),new Board(last));
        assertEquals(c.get(4).getLastMove(),15);

        Ilayout.ID[][] random = new Ilayout.ID[][]{{X,O,X,O},
                                                   {X,O,X,O},
                                                   {O,B,X,B},
                                                   {X,B,O,B}};

        assertEquals(c.get(3),new Board(random));
        assertEquals(c.get(3).getLastMove(),14);
    }
    private Board simulation(){
        Board board = new Board();
        for(int i = 0; i < 8; i++){
            board.move(i);
        }
        board.move(12);
        board.move(8);
        board.move(10);

        /*
            X O X O
            X O X O
            O - X -
            X - - -
         */

        return board;
    }
}
