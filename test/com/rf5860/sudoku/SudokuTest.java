package com.rf5860.sudoku;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SudokuTest {
    private static final int[][] completedPuzzleDigits = {
        {4, 8, 3,   9, 2, 1,   6, 5, 7},
        {9, 6, 7,   3, 4, 5,   8, 2, 1},
        {2, 5, 1,   8, 7, 6,   4, 9, 3},

        {5, 4, 8,   1, 3, 2,   9, 7, 6},
        {7, 2, 9,   5, 6, 4,   1, 3, 8},
        {1, 3, 6,   7, 9, 8,   2, 4, 5},

        {3, 7, 2,   6, 8, 9,   5, 1, 4},
        {8, 1, 4,   2, 5, 3,   7, 6, 9},
        {6, 9, 5,   4, 1, 7,   3, 8, 2}
    };
    private static final int[][] partiaPuzzleDigits = {
        {4, 8, 3,   9, 2, 1,   6, 5, 7},
        {9, 6, 7,   3, 4, 5,   8, 2, 1},
        {2, 5, 1,   8, 7, 6,   4, 9, 3},

        {5, 4, 8,   1, 3, 2,   9, 7, 6},
        {7, 2, 9,   5, 6, 4,   1, 3, 8},
        {1, 3, 6,   7, 9, 8,   2, 4, 5},

        {3, 7, 2,   6, 8, 9,   5, 1, 4},
        {8, 1, 4,   2, 5, 3,   7, 6, 9},
        {6, 9, 5,   4, 1, 7,   3, 8, 2}
    };
    private Sudoku completedPuzzle;
    private Sudoku partialPuzzle;

    @BeforeEach
    private void setup() {
        completedPuzzle = new Sudoku(completedPuzzleDigits);
        partialPuzzle = new Sudoku(partiaPuzzleDigits);
    }

    @Test
    public void clearAll() {

    }

    @Test
    public void clear() {
    }

    @Test
    public void isInRow() {
    }

    @Test
    public void isInColumn() {
    }

    @Test
    public void isInBox() {
    }

    @Test
    void getSubGridRow() {
    }

    @Test
    void getSubGridLength() {
    }

    @Test
    void getRowLength() {
    }

    @Test
    void getSubGridColumn() {
    }

    @Test
    void getSubGridLength1() {
    }

    @Test
    void getRepresentation() {
        final int[][] representation = completedPuzzle.getRepresentation();
        assertThat(completedPuzzle).isEqualTo(representation);
    }

    @Test
    void getRow() {

    }

    @Test
    void getColumn() {
    }
}