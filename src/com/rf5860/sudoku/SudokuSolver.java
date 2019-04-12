package com.rf5860.sudoku;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

public class SudokuSolver {
    private final Sudoku grid;
    private Sudoku possibleSolution;

    /**
     * Clone and return the Sudoku instance.
     * @return a clone of the Sudoku instance
     */
    public Sudoku getGrid() {
        return grid.clone();
    }

    /**
     * Clone and return the current possible solution.
     * @return a clone of the current possible solution
     */
    public Sudoku getPossibleSolution() {
        return possibleSolution.clone();
    }

    /**
     * Generate a list of all possible solutions for the Sudoku puzzle.
     * @return a list of all possible solutions
     */
    public List<Sudoku> generateAlSolutions() {
        // TODO: Implement
        return new ArrayList<>();
    }

    /**
     * Sudoku Solver using Dancing Links (DLX) implementation of Algorithm X.
     * @param sudoku The Sudoku puzzle to solve
     */
    public SudokuSolver(@NotNull final Sudoku sudoku) {
        this.grid = sudoku.clone();
        possibleSolution = sudoku.clone();
    }
}
