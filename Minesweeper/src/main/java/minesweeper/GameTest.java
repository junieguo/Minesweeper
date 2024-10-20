package minesweeper;

import java.awt.event.ActionEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameTest {
    private Minesweeper minesweeper;

    public GameTest() {
    }

    @BeforeEach
    public void setUp() {
        this.minesweeper = new Minesweeper(true);
    }

    @Test
    public void testMinesweeperInitialization() {
        Assertions.assertNotNull(this.minesweeper);
        Assertions.assertNotNull(this.minesweeper.frame);
        Assertions.assertNotNull(this.minesweeper.cellPanel);
        Assertions.assertNotNull(this.minesweeper.resetButton);
        Assertions.assertNotNull(this.minesweeper.saveButton);
        Assertions.assertNotNull(this.minesweeper.flag);
        Assertions.assertNotNull(this.minesweeper.cells);
        Assertions.assertNotNull(this.minesweeper.flagged);
        Assertions.assertNotNull(this.minesweeper.solution);
        Assertions.assertNotNull(this.minesweeper.random);
    }

    @Test
    public void testSetBombs() {
        this.minesweeper.setBombs();
        int uniquePositions = this.countUniquePositions(this.minesweeper.xBombs, this.minesweeper.yBombs);
        Assertions.assertEquals(Minesweeper.bombs, uniquePositions);
        Assertions.assertTrue(uniquePositions <= 256);
    }

    @Test
    public void testGetSolution() {
        this.minesweeper.getSolution();
        Assertions.assertNotNull(this.minesweeper.solution);
    }

    @Test
    public void testIsMine() {
        this.minesweeper.setBombs();

        for(int i = 0; i < Minesweeper.bombs; ++i) {
            Assertions.assertTrue(this.minesweeper.isMine(this.minesweeper.xBombs[i], this.minesweeper.yBombs[i]));
        }

    }

    private int countUniquePositions(int[] xPositions, int[] yPositions) {
        int count = 0;

        for(int i = 0; i < xPositions.length; ++i) {
            for(int j = i + 1; j < xPositions.length; ++j) {
                if (xPositions[i] == xPositions[j] && yPositions[i] == yPositions[j]) {
                    ++count;
                }
            }
        }

        return xPositions.length - count;
    }

    @Test
    void testInitialization() {
        Assertions.assertNotNull(this.minesweeper);
    }

    @Test
    void testFlagCell() {
        this.minesweeper.flag.doClick();
        this.minesweeper.cells[0][0].doClick();
        Assertions.assertEquals("", this.minesweeper.cells[0][0].getText());
        this.minesweeper.cells[0][0].doClick();
        Assertions.assertEquals("", this.minesweeper.cells[0][0].getText());
        this.minesweeper.cells[0][0].doClick();
        Assertions.assertEquals("", this.minesweeper.cells[0][0].getText());
        this.minesweeper.flag.doClick();
        this.minesweeper.cells[0][0].doClick();
        Assertions.assertEquals("", this.minesweeper.cells[0][0].getText());
    }

    @Test
    void testCheckOutOfBounds() {
        Assertions.assertDoesNotThrow(() -> {
            this.minesweeper.check(-1, 0);
        });
        Assertions.assertDoesNotThrow(() -> {
            this.minesweeper.check(0, -1);
        });
        Assertions.assertDoesNotThrow(() -> {
            this.minesweeper.check(16, 0);
        });
        Assertions.assertDoesNotThrow(() -> {
            this.minesweeper.check(0, 16);
        });
    }

    @Test
    public void testFlagging() {
        Assertions.assertFalse(this.minesweeper.flagging);
        this.minesweeper.actionPerformed(new ActionEvent(this.minesweeper.flag, 1001, (String)null));
        Assertions.assertTrue(this.minesweeper.flagging);
        this.minesweeper.actionPerformed(new ActionEvent(this.minesweeper.flag, 1001, (String)null));
        Assertions.assertFalse(this.minesweeper.flagging);
    }

    @Test
    void testFlaggedCellCannotBeUncovered() {
        this.minesweeper.flag.doClick();
        this.minesweeper.cells[0][0].doClick();
        Assertions.assertEquals("", this.minesweeper.cells[0][0].getText());
        this.minesweeper.flag.doClick();
        this.minesweeper.cells[0][0].doClick();
        Assertions.assertEquals("", this.minesweeper.cells[0][0].getText());
    }

    @Test
    void testFlaggingMultipleCells() {
        this.minesweeper.flag.doClick();
        this.minesweeper.cells[0][0].doClick();
        this.minesweeper.cells[1][1].doClick();
        this.minesweeper.cells[2][2].doClick();
        Assertions.assertEquals("", this.minesweeper.cells[0][0].getText());
        Assertions.assertEquals("", this.minesweeper.cells[1][1].getText());
        Assertions.assertEquals("", this.minesweeper.cells[2][2].getText());
    }

    @Test
    public void testMinePlacement() {
        Minesweeper minesweeper = new Minesweeper(true);
        int mineCount = 0;

        for(int i = 0; i < 16; ++i) {
            for(int j = 0; j < 16; ++j) {
                if (minesweeper.isMine(i, j)) {
                    ++mineCount;
                }
            }
        }

        Assertions.assertEquals(Minesweeper.bombs, mineCount);
    }

    @Test
    public void testNumberMines() {
        int Mines = true;
        Minesweeper minesweeper = new Minesweeper(true);
        int mineCount = 0;

        for(int i = 0; i < 16; ++i) {
            for(int j = 0; j < 16; ++j) {
                if (minesweeper.isMine(i, j)) {
                    ++mineCount;
                }
            }
        }

        Assertions.assertEquals(40, mineCount);
    }

    @Test
    private boolean containsMine(int x, int y, int[] mineXPositions, int[] mineYPositions) {
        for(int i = 0; i < mineXPositions.length; ++i) {
            if (x == mineXPositions[i] && y == mineYPositions[i]) {
                return true;
            }
        }

        return false;
    }

    @Test
    public void testRevealCell() {
        int bombs = 0;
        if (!this.minesweeper.isMine(0, 0)) {
            if (this.minesweeper.isMine(0, 1)) {
                ++bombs;
            }

            if (this.minesweeper.isMine(1, 1)) {
                ++bombs;
            }

            if (this.minesweeper.isMine(1, 0)) {
                ++bombs;
            }

            this.minesweeper.revealCell(0, 0);
            String bomb = null;
            if (bombs == 0) {
                bomb = "0";
            }

            if (bombs == 1) {
                bomb = "1";
            }

            if (bombs == 2) {
                bomb = "2";
            }

            if (bombs == 3) {
                bomb = "3";
            }

            Assertions.assertEquals(bomb, this.minesweeper.cells[0][0].getText());
        }

    }

    @Test
    public void testAdjacentCellBomb() {
        if (!this.minesweeper.isMine(0, 0) && !this.minesweeper.isMine(0, 1) && this.minesweeper.isMine(1, 1) && !this.minesweeper.isMine(1, 0)) {
            this.minesweeper.revealCell(0, 0);
            Assertions.assertEquals("1", this.minesweeper.cells[0][0].getText());
        }

    }

    @Test
    void testFlaggedCellCannotBeRevealed() {
        this.minesweeper.flag.doClick();
        this.minesweeper.cells[0][0].doClick();
        Assertions.assertEquals("", this.minesweeper.cells[0][0].getText());
        this.minesweeper.flag.doClick();
        this.minesweeper.cells[0][0].doClick();
        Assertions.assertEquals("", this.minesweeper.cells[0][0].getText());
    }
}
