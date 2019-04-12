package com.rf5860.sudoku;

import static java.lang.Math.sqrt;
import static java.util.Arrays.copyOf;
import static java.util.Arrays.deepEquals;
import static java.util.Arrays.deepHashCode;
import static java.util.Arrays.deepToString;
import static java.util.Arrays.fill;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * <h1>Sudoku</h1>
 * <a href="https://en.wikipedia.org/wiki/Sudoku">Sudoku</a> is a number-placement puzzle. The aim is to fill a 9x9 grid with digits, with the following 4 constraints:
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
public class Sudoku implements Cloneable {
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
     * Constructs a new Sudoku grid from the supplied digits.
     *
     * @param digits an NxN matrix of digits<br>
     *               There must be an equal number of rows and columns.<br>
     *               Each row must be the same length
     * @throws IllegalArgumentException if there are unequal number of rows and columns, or if all rows are not of equal length
     */
    public Sudoku(@NotNull final int[][] digits) {
        grid = new int[digits.length][];
        range(0, digits.length).forEachOrdered(i -> grid[i] = copyOf(digits[i], digits[i].length));
        size = grid.length * grid.length;
    }

    /**
     * Constructs a new Sudoku grid of the given size.
     *
     * @param size the size (total squares) in the grid.<br>
     *             This must be a perfect square
     * @throws IllegalArgumentException if the given is <strong>not</strong> a perfect square
     */
    public Sudoku(final int size) throws IllegalArgumentException {
        if (isNotAPerfectSquare(size)) {
            throw new IllegalArgumentException("Size must be a perfect square");
        }
        this.size = size;
        grid = new int[size][size];
    }

    /**
     * Checks if a number is not a perfect square.
     *
     * @param num the number to check
     * @return <code>false</code> if the number is not a perfect square, <code>true</code> otherwise
     */
    private boolean isNotAPerfectSquare(final int num) {
        final double sqrt = sqrt(num);
        return Math.pow((int) sqrt, 2) != Math.pow(sqrt, 2);
    }

    /**
     * Get the size of the Sudoku grid.
     *
     * @return the size of the Sudoku grid
     */
    public int getSize() {
        return size;
    }

    /**
     * Clear all squares in the grid (I.e. sets all values to <code>0</code>)
     *
     * @return the current Sudoku
     */
    public Sudoku clearAll() {
        stream(grid).forEach(row -> fill(row, 0));
        return this;
    }

    /**
     * Clear the given row and column.
     *
     * @param row    the row to set
     * @param column the column to set
     */
    public void clear(final int row, final int column) {
        set(row, column, 0);
    }

    /**
     * Set the given row and column to the supplied value.
     *
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
     *
     * @param row    the row to check
     * @param column the column to check
     * @return the value at the given row and column
     * @throws ArrayIndexOutOfBoundsException if either row or column is greater than the size of the Grid
     */
    public int get(final int row, final int column) throws ArrayIndexOutOfBoundsException {
        return grid[row][column];
    }

    /**
     * Check if the current Sudoku puzzle is solved.
     *
     * @return <code>true</code> if the puzzle is solved (Has no <code>0</code>'s), <code>false</code> otherwise
     */
    public boolean isSolved() {
        return stream(grid).anyMatch(row -> stream(row).anyMatch(i -> i == 0));
    }

    /**
     * Check if the given digit is in the given column.
     *
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
     *
     * @param subGridIndex the id of the sub-grid to check
     * @param digit        the digit to find in the given sub-grid
     * @return whether or not the digit is in the given sub-grid
     * @throws ArrayIndexOutOfBoundsException if the subGridIndex is greater than the number of sub-grids
     */
    public boolean isInSubGrid(final int subGridIndex, final int digit) throws ArrayIndexOutOfBoundsException {
        final List<Pair<Integer, Integer>> subGridIndexes = getSubGridIndexes(subGridIndex);
        return subGridIndexes.stream().anyMatch(index -> grid[index.getLeft()][index.getRight()] == digit);
    }

    /**
     * Check if the given digit is in the given row.
     *
     * @param row   the row to check
     * @param digit the digit to find in the given row
     * @return whether or not the digit is in the given row
     * @throws ArrayIndexOutOfBoundsException if the row is greater than the number of rows
     */
    public boolean isInRow(final int row, final int digit) throws ArrayIndexOutOfBoundsException {
        return stream(grid[row]).anyMatch(i -> i == digit);
    }

    /**
     * Check if the given cell is populated.
     *
     * @param row    the row to check
     * @param column the column to check
     * @return whether or not the given cell is already populated
     */
    @Contract(pure = true)
    private boolean isCellPopulated(final int row, final int column) {
        return grid[row][column] != 0;
    }

    /**
     * Check if the given move is valid.
     *
     * @param row    the row to check
     * @param column the column to check
     * @param digit  the value to check at the given row and column
     * @return <code>true</code> if the move is valid, <code>false</code> otherwise
     */
    public boolean isMoveValid(final int row, final int column, final int digit) {
        return !isCellPopulated(row, column) && !isInRow(row, digit) && !isInColumn(column, digit) && !isInSubGrid(getSubGridIndex(row, column), digit);
    }

    /**
     * Get the indexes for all cells in the given sub-grid.
     *
     * @param subGridIndex the index of the sub-grid to get indexes for
     * @return an array pairs of indexes for all cells in the given sub-grid
     */
    public List<Pair<Integer, Integer>> getSubGridIndexes(final int subGridIndex) {
        final Pair<Integer, Integer> startingIndex = getSubGridStartingIndex(subGridIndex);
        return range(startingIndex.getLeft(),
                     startingIndex.getLeft() + getSubGridLength())
            .boxed().flatMap(row -> range(startingIndex.getRight(), startingIndex.getRight() + getSubGridLength())
                .boxed().map(column -> Pair.of(row, column)))
            .collect(toList());
    }

    /**
     * Determine the sub-grid of a given row and column.
     *
     * @param row the row to determine the sub-grid of
     * @param column the column to determine the sub-grid of
     * @return the index for the sub-grid with the given row and column (0-based, left-to-right, top-to-bottom)
     */
    public int getSubGridIndex(final int row, final int column) {
        return row / getSubGridHeight() + column / getSubGridLength();
    }

    /**
     * Get the values in the given sub-grid.
     *
     * @param subGridIndex the index of the sub-grid to get the values for
     * @return an array of the values in the given sub-grid
     */
    public int[] getSubGridValues(final int subGridIndex) {
        return getSubGridIndexes(subGridIndex).stream().mapToInt(indexes -> grid[indexes.getLeft()][indexes.getRight()]).toArray();
    }

    /**
     * Get the index a given sub-grid starts in.
     *
     * @param subGridIndex the index of the sub-grid to get the starting index for
     * @return the starting (top-leftmost) index for the sub-grid
     */
    public Pair<Integer, Integer> getSubGridStartingIndex(final int subGridIndex) {
        return Pair.of(getSubGridRow(subGridIndex) * getSubGridHeight(), getSubGridColumn(subGridIndex) * getSubGridLength());
    }

    /**
     * Get the virtual row of a given sub-grid.
     *
     * @param subGridIndex the index of the sub-grid to get the virtual row for
     * @return the corresponding virtual row for the sub-grid
     */
    public int getSubGridRow(final int subGridIndex) {
        return (subGridIndex - 1) / getSubGridHeight();
    }

    /**
     * Get the virtual column of a given sub-grid.
     *
     * @param subGridIndex the index of the sub-grid to get the virtual column for
     * @return the corresponding virtual column for the sub-grid
     */
    public int getSubGridColumn(final int subGridIndex) {
        return (subGridIndex - 1) % getSubGridLength();
    }

    /**
     * <p>Get the length of each sub-grid for the current Sudoku Puzzle.<br>
     * For example, a 9x9 Sudoku puzzle, is comprised of 9 3x3 sub-grids</p>
     * <p>This is equal to sqrt(length)</p>
     *
     * @return the length row for the sub-grid
     */
    public int getSubGridLength() {
        return (int) sqrt(getLength());
    }

    /**
     * <p>Get the height of each sub-grid for the current Sudoku Puzzle.<br>
     * For example, a 9x9 Sudoku puzzle, is comprised of 9 3x3 sub-grids</p>
     * <p>This is equal to sqrt(height)</p>
     *
     * @return the height row for the sub-grid
     */
    public int getSubGridHeight() {
        return (int) sqrt(getHeight());
    }

    /**
     * Get the height (number of rows) of the Sudoku grid.
     *
     * @return the height of the Sudoku grid
     */
    @Contract(pure = true)
    public int getHeight() {
        return grid.length;
    }

    /**
     * Get the length (number of columns) of the Sudoku grid.
     *
     * @return the length of the Sudoku grid
     */
    @Contract(pure = true)
    public int getLength() {
        return grid[0].length;
    }

    /**
     * Get a copy of the given row of the Sudoku grid.
     *
     * @param row the row to get
     * @return a copy of the given row in the Sudoku grid
     * @throws ArrayIndexOutOfBoundsException if the given row is invalid
     */
    @Contract(pure = true)
    public int[] getRow(final int row) throws ArrayIndexOutOfBoundsException {
        final int[] rowCopy = new int[grid[row].length];
        System.arraycopy(grid[row], 0, rowCopy, 0, grid[row].length);
        return rowCopy;
    }

    /**
     * Gets a copy of all rows in the Sudoku grid.
     *
     * @return a copy of all rows in the Sudoku grid
     */
    @Contract(pure = true)
    public int[][] getRows() {
        return copyOf(grid, grid.length);
    }

    /**
     * Get a copy of the given column of the Sudoku grid.
     *
     * @param column the column to get
     * @return a copy of the given column in the Sudoku grid
     * @throws ArrayIndexOutOfBoundsException if the given column is invalid
     */
    @Contract(pure = true)
    public int[] getColumn(final int column) throws ArrayIndexOutOfBoundsException {
        final int[] columnCopy = new int[getHeight()];
        for (int row = 0; row < getHeight(); row++) {
            columnCopy[row] = grid[row][column];
        }

        return columnCopy;
    }

    /**
     * Get a copy of all columns in the Sudoku grid.
     *
     * @return a copy of all columns in the Sudoku grid
     */
    @Contract(pure = true)
    public int[][] getColumns() {
        return range(0, grid.length).mapToObj(column -> stream(grid).mapToInt(row -> row[column]).toArray()).toArray(int[][]::new);
    }

    @Override
    public int hashCode() {
        return deepHashCode(grid);
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sudoku)) {
            return false;
        }

        final Sudoku sudoku = (Sudoku) o;

        return deepEquals(grid, sudoku.grid);
    }

    @Override
    protected Sudoku clone() {
        return new Sudoku(grid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("Sudoku [%dx%d] = %s", getHeight(), getLength(), deepToString(grid));
    }


    /**
     * Get a a textual copy of the current Sudoku grid.
     *
     * @return a textual copy of the current the Sudoku grid
     */
    public String print() {
        final StringBuilder sb = new StringBuilder();
        final int totalHeight = getHeight() + getSubGridHeight() + 1;
        final int totalLength = getLength() + getSubGridLength() + 1;
        for (int row = 0; row < totalHeight; row++) {
            final boolean isDisplayRow = row % (getSubGridHeight() + 1) == 0;
            for (int column = 0; column < totalLength; column++) {
                final boolean isDisplayColumn = column % (getSubGridLength() + 1) == 0;
                if (isDisplayRow) {
                    if (isDisplayColumn) {
                        if (column == 0) {
                            sb.append(row == 0 ? "╔" : (row == totalHeight - 1 ? "╚" : "╠"));
                        } else if (column == totalLength - 1) {
                            sb.append(row == 0 ? "╗" : (row == totalHeight - 1 ? "╝" : "╣"));
                        } else {
                            sb.append(row == 0 ? "╦" : (row == totalHeight - 1 ? "╩" : "╬"));
                        }
                    } else {
                        sb.append("═");
                    }
                } else if (isDisplayColumn) {
                    sb.append("║");
                } else {
                    sb.append(grid[row - 1 - (row / (getSubGridHeight() + 1))][column - 1 - (column / (getSubGridLength() + 1))]);
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
