package com.rf5860.sudoku;

import static java.lang.Math.sqrt;
import static java.util.Arrays.stream;

import java.util.logging.Level;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * <h1>Sudoku</h1>
 * <a href="https://en.wikipedia.org/wiki/Sudoku">Sudoku</a> is a number-placement puzzle. The aim is to fill a 9x9 grid with digits, according ot the following rules:
 * <ul>
 * <li>Each <strong>row</strong>> should contain the numbers 1 to 9</li>
 * <li>Each <strong>column</strong> should contain the numbers 1 to 9</li>
 * <li>Each <strong>sub-grid</strong> should contain the numbers 1 to 9</li>
 * <li>The same number cannot appear in the same row, column, or sub-grid</li>
 * </ul>
 * <br>
 * This implementation uses 0 to represent blank values.
 * </p>
 */
public class Sudoku {
    public static final int DEFAULT_SIZE = 9;

    private final int size;
    private final int[][] grid;

    /**
     * Constructs a new Sudoku grid of with the default size of {@value DEFAULT_SIZE}.
     */
    public Sudoku() {
        this(DEFAULT_SIZE);
    }

    /**
     * Constructs a new Sudoku grid from the supplied digits
     * @param digits an NxN matrix of digits<br>
     *        There must be an equal number of rows and columns.<br>
     *        Each row must be the same length
     * @throws IllegalArgumentException if there are unequal number of rows and columns, or if all rows are not of equal length
     */
    public Sudoku(@NotNull final int[][] digits) {
        grid = copy(digits);
        size = grid.length * grid.length;
    }

    /**
     * Constructs a new Sudoku grid of the given size.
     * @param size the size (total squares) in the grid.<br>
     *        This must be a perfect square
     * @throws IllegalArgumentException if the given is <strong>not</strong> a perfect square
     */
    public Sudoku(final int size) throws IllegalArgumentException {
        this.size = size;
        grid = new int[size][size];
    }

    /**
     * Get the size of the Sudoku grid.
     * @return the size of the Sudoku grid
     */
    public int getSize() {
        return size;
    }

    /**
     * Clear all squares in the grid (I.e. sets all values to <code>0</code>)
     */
    public void clearAll() {
        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[row].length; column++) {
                clear(row, column);
            }
        }
    }

    /**
     * Clear the given row and column.
     * @param row    the row to set
     * @param column the column to set
     */
    public void clear(final int row, final int column) {
        set(row, column, 0);
    }

    /**
     * Set the given row and column to the supplied value.
     * @param row    the row to set
     * @param column the column to set
     * @param digit  the digit to find in the sub-grid
     * @throws ArrayIndexOutOfBoundsException if either row or column is greater than the size of the Grid
     */
    public void set(final int row, final int column, final int digit) throws ArrayIndexOutOfBoundsException {
        grid[row][column] = digit;
    }

    /**
     * Get the value at the given row and column.
     * @param row    the row to check
     * @param column the column to check
     * @return the value at the given row and column
     * @throws ArrayIndexOutOfBoundsException if either row or column is greater than the size of the Grid
     */
    public int get(final int row, final int column) throws ArrayIndexOutOfBoundsException {
        return grid[row][column];
    }

    /**
     * Check if the given digit is in the given row.
     * @param row   the row to check
     * @param digit the digit to find in the given row
     * @return whether or not the digit is in the given row
     * @throws ArrayIndexOutOfBoundsException if the row is greater than the number of rows
     */
    public boolean isInRow(final int row, final int digit) throws ArrayIndexOutOfBoundsException {
        return stream(grid[row]).anyMatch(i -> i == digit);
    }

    /**
     * Check if the given digit is in the given column.
     * @param column the id of the column to check
     * @param digit  the digit to find in the given column
     * @return whether or not the digit is in the given column
     * @throws ArrayIndexOutOfBoundsException if the column is greater than the number of sub-grids
     */
    public boolean isInColumn(final int column, final int digit) throws ArrayIndexOutOfBoundsException {
        return stream(grid).map(row -> row[column]).anyMatch(i -> i == digit);
    }

    /**
     * <p>Check if the given digit is in the given sub-grid.</p>
     * <br>
     * <p>A Sudoku grid has <code>N</code> sub-grids - where <code>N</code> is the size of the Sudoku board.
     * <br>This implementation numbers these grids in <a href="https://en.wikipedia.org/wiki/Row-_and_column-major_order">row-major</a> order.
     * <br>The numbering for a 9x9 grid is depicted below.</p>
     * <pre>
     * ╔═══╦═══╦═══╗
     * ║   ║   ║   ║
     * ║ 1 ║ 2 ║ 3 ║
     * ║   ║   ║   ║
     * ╠═══╬═══╬═══╣
     * ║   ║   ║   ║
     * ║ 4 ║ 5 ║ 6 ║
     * ║   ║   ║   ║
     * ╠═══╬═══╬═══╣
     * ║   ║   ║   ║
     * ║ 7 ║ 8 ║ 9 ║
     * ║   ║   ║   ║
     * ╚═══╩═══╩═══╝
     * </pre>
     * @param boxId the id of the sub-grid to check
     * @param digit the digit to find in the given sub-grid
     * @return whether or not the digit is in the given sub-grid
     * @throws ArrayIndexOutOfBoundsException if the boxId is greater than the number of sub-grids
     */
    public boolean isInBox(final int boxId, final int digit) throws ArrayIndexOutOfBoundsException {
        final int index = (boxId - 1) * getSubGridLength(boxId);
        final int startRow = index * getLength();
//        return stream(grid).map(row -> row[column]).anyMatch(i -> i == digit);
        return false;
    }

    /**
     * Get the virtual row of a given sub-grid.
     * @param boxId the index of the box to get the virtual row for
     * @return the corresponding virtual row for the sub-grid
     */
    public int getSubGridRow(final int boxId) {
        return boxId / (getSubGridLength(boxId) * getLength());
    }

    /**
     * Get the virtual column of a given sub-grid.
     * @param boxId the index of the box to get the virtual column for
     * @return the corresponding virtual column for the sub-grid
     */
    public int getSubGridColumn(final int boxId) {
        // TODO: Implement
        return 0;
    }

    /**
     * <p>Get the length of each sub-grid for the current Sudoku Puzzle.<br>
     * For example, a 9x9 Sudoku puzzle, is comprised of 9 3x3 sub-grids</p>
     * <p>This is equal to sqrt(length)</p>
     *
     * @param boxId the index of the box to get length of
     * @return the length row for the sub-grid
     */
    public int getSubGridLength(final int boxId) {
        return (int) sqrt(getLength());
    }

    /**
     * Get the height (number of rows) of the Sudoku grid.
     * @return the height of the Sudoku grid
     * */
    @Contract(pure = true)
    public int getHeight() {
        return grid.length;
    }

    /**
     * Get the length (number of columns) of the Sudoku grid.
     * @return the length of the Sudoku grid
     */
    @Contract(pure = true)
    public int getLength() {
        return grid[0].length;
    }

    /**
     * Get a copy of the given row of the Sudoku grid.
     * @param row the row to get
     * @return a copy of the given row in the Sudoku grid
     * @throws ArrayIndexOutOfBoundsException if the given row is invalid
     */
    @Contract(pure = true)
    public int[] getRow(final int row) throws ArrayIndexOutOfBoundsException{
        final int[] rowCopy = new int[grid[row].length];
        System.arraycopy(grid[row], 0, rowCopy, 0, grid[row].length - 1);
        return rowCopy;
    }

    /**
     * Get a copy of the given column of the Sudoku grid.
     * @param column the column to get
     * @return a copy of the given column in the Sudoku grid
     * @throws ArrayIndexOutOfBoundsException if the given column is invalid
     */
    @Contract(pure = true)
    public int[] getColumn(final int column) {
        final int[] columnCopy = new int[getHeight()];
        for (int row = 0; row < getHeight(); row++) {
            columnCopy[row] = grid[row][column];
        }

        return columnCopy;
    }

    /**
     * Get a copy of the Sudoku grid.
     * @return a representation (clone) of the Sudoku grid
     */
    public int[][] getRepresentation() {
        return copy(grid);
    }

    private int[][] copy(@NotNull final int[][] src) {
        final int[][] copy = new int[src.length][];
        System.arraycopy(src, 0, copy, 0, src.length);
        return copy;
    }
}
