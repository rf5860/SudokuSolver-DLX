package com.rf5860.sudoku;

import static com.rf5860.sudoku.assertions.SudokuAssert.assertThat;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static java.util.stream.IntStream.rangeClosed;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.function.IntFunction;

import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

public class SudokuTest {
    private static final int[][] completedPuzzle = {
        {4, 8, 3, 9, 2, 1, 6, 5, 7},
        {9, 6, 7, 3, 4, 5, 8, 2, 1},
        {2, 5, 1, 8, 7, 6, 4, 9, 3},

        {5, 4, 8, 1, 3, 2, 9, 7, 6},
        {7, 2, 9, 5, 6, 4, 1, 3, 8},
        {1, 3, 6, 7, 9, 8, 2, 4, 5},

        {3, 7, 2, 6, 8, 9, 5, 1, 4},
        {8, 1, 4, 2, 5, 3, 7, 6, 9},
        {6, 9, 5, 4, 1, 7, 3, 8, 2}
    };

    @NotNull
    @Contract(pure = true)
    private static List<Object[]> rowProvider() {
        return range(0, completedPuzzle.length)
            .mapToObj(row -> new Object[] {
                new Sudoku(completedPuzzle),
                row,
                completedPuzzle[row]
            }).collect(toList());
    }

    @NotNull
    @Contract(pure = true)
    private static List<Object[]> columnProvider() {
        return range(0, completedPuzzle.length)
            .mapToObj(column -> new Object[] {
                new Sudoku(completedPuzzle),
                column,
                stream(completedPuzzle).mapToInt(row -> row[column]).toArray()
            }).collect(toList());
    }

    @NotNull
    @Contract(pure = true)
    private static List<Object[]> subGridProvider() {
        return asList(new Object[][] {
            {1, 0, 0}, {2, 0, 1}, {3, 0, 2},
            {4, 1, 0}, {5, 1, 1}, {6, 1, 2},
            {7, 2, 0}, {8, 2, 1}, {9, 2, 2}
        });
    }

    @NotNull
    private static Pair<Integer, Integer>[] generateBox(@NotNull final List<Integer> rows, @NotNull final List<Integer> columns) {
        return rows.stream()
                   .flatMap(i -> columns.stream().map(j -> Pair.of(i, j)))
                   .toArray((IntFunction<@NotNull Pair<Integer, Integer>[]>) Pair[]::new);
    }

    @NotNull
    @Contract(pure = true)
    private static List<Object[]> subGridIndexesProvider() {
        return asList(new Object[][] {
            {1, generateBox(asList(0, 1, 2), asList(0, 1, 2))},
            {2, generateBox(asList(0, 1, 2), asList(3, 4, 5))},
            {3, generateBox(asList(0, 1, 2), asList(6, 7, 8))},
            {4, generateBox(asList(3, 4, 5), asList(0, 1, 2))},
            {5, generateBox(asList(3, 4, 5), asList(3, 4, 5))},
            {6, generateBox(asList(3, 4, 5), asList(6, 7, 8))},
            {7, generateBox(asList(6, 7, 8), asList(0, 1, 2))},
            {8, generateBox(asList(6, 7, 8), asList(3, 4, 5))},
            {9, generateBox(asList(6, 7, 8), asList(6, 7, 8))}
        });
    }

    @Test
    public void clearAll() {
        assertThat(new Sudoku(completedPuzzle).clearAll()).hasAllCellsSetTo(0);
    }

    @Test
    public void clear() {
        final Sudoku sudoku = new Sudoku(completedPuzzle);
        range(0, sudoku.getHeight())
            .forEach(row -> range(0, sudoku.getHeight())
                .forEach(column -> sudoku.clear(row, column)));

        assertThat(sudoku).hasAllCellsSetTo(0);
    }

    @Test
    void isInSubGrid() {
        final Sudoku sudokuMissingDigits = new Sudoku(new int[][] {
            {4, 9, 2, 5, 7, 1, 0, 8, 6},
            {8, 6, 5, 4, 0, 3, 7, 1, 9},
            {3, 7, 0, 8, 9, 6, 2, 4, 5},
            {9, 3, 8, 1, 0, 7, 0, 2, 4},
            {2, 0, 7, 3, 6, 9, 8, 5, 1},
            {1, 5, 6, 2, 4, 8, 9, 3, 7},
            {6, 8, 4, 9, 1, 2, 5, 7, 3},
            {5, 2, 9, 7, 3, 4, 1, 6, 8},
            {0, 1, 3, 6, 0, 5, 4, 0, 2}
        });
        rangeClosed(1, 9).forEachOrdered(i -> {
            assertThat(sudokuMissingDigits.isInSubGrid(i, i)).as("Expected sub-grid %d to not have %d", i, i).isFalse();
            rangeClosed(1, 9).filter(j -> j != i).forEachOrdered(num -> assertThat(sudokuMissingDigits.isInSubGrid(i, num))
                .as("Expected sub-grid %d to have %d", i, num)
                .isTrue());
        });
    }

    @Test
    public void isMoveValid() {
        final Sudoku sudoku = new Sudoku(completedPuzzle);
        // TODO: Test
    }

    @Test
    public void isInRow() {
        final Sudoku sudokuMissingDigits = new Sudoku(new int[][] {
            {4, 8, 3, 9, 2, 0, 6, 5, 7},
            {9, 6, 7, 3, 4, 5, 8, 0, 1},
            {2, 5, 1, 8, 7, 6, 4, 9, 0},
            {5, 0, 8, 1, 3, 2, 9, 7, 6},
            {7, 2, 9, 0, 6, 4, 1, 3, 8},
            {1, 3, 0, 7, 9, 8, 2, 4, 5},
            {3, 0, 2, 6, 8, 9, 5, 1, 4},
            {0, 1, 4, 2, 5, 3, 7, 6, 9},
            {6, 0, 5, 4, 1, 7, 3, 8, 2}
        });
        range(0, 9).forEachOrdered(i -> {
            assertThat(sudokuMissingDigits.isInRow(i, i + 1)).as("Expected row %d to not have %d", i, i + 1).isFalse();
            rangeClosed(1, 9).filter(j -> j != i + 1).forEachOrdered(num -> assertThat(sudokuMissingDigits.isInRow(i, num))
                .as("Expected row %d to have %d", i + 1, num)
                .isTrue());
        });
    }

    @Test
    public void isInColumn() {
        final Sudoku sudokuMissingDigits = new Sudoku(new int[][] {
            {4, 8, 0, 9, 2, 1, 6, 5, 7},
            {9, 6, 7, 3, 4, 5, 8, 2, 1},
            {2, 5, 1, 8, 7, 0, 4, 9, 3},
            {5, 4, 8, 1, 3, 2, 9, 7, 6},
            {7, 0, 9, 5, 6, 4, 1, 3, 8},
            {0, 3, 6, 7, 9, 8, 2, 4, 5},
            {3, 7, 2, 6, 8, 9, 5, 1, 4},
            {8, 1, 4, 2, 0, 3, 0, 6, 0},
            {6, 9, 5, 0, 1, 7, 3, 0, 2}

        });
        range(0, 9).forEachOrdered(i -> {
            assertThat(sudokuMissingDigits.isInColumn(i, i + 1)).as("Expected column %d to not have %d", i, i + 1).isFalse();
            rangeClosed(1, 9).filter(j -> j != i + 1).forEachOrdered(num -> assertThat(sudokuMissingDigits.isInColumn(i, num))
                .as("Expected column %d to have %d", i + 1, num)
                .isTrue());
        });
    }

    @ParameterizedTest(name = "{index}: getSubGridIndexes({0}) == {1})")
    @MethodSource("subGridIndexesProvider")
    void getSubGridIndexes(final int subGridIndex, final Pair<Integer, Integer>[] indexes) {
        assertThat(new Sudoku(completedPuzzle).getSubGridIndexes(subGridIndex)).containsExactly(indexes);
    }

    @Test
    void getSubGridValues() {
        final Sudoku sudoku = new Sudoku(completedPuzzle);
        assertThat(sudoku.getSubGridValues(1)).containsExactly(4, 8, 3, 9, 6, 7, 2, 5, 1);
        assertThat(sudoku.getSubGridValues(2)).containsExactly(9, 2, 1, 3, 4, 5, 8, 7, 6);
        assertThat(sudoku.getSubGridValues(3)).containsExactly(6, 5, 7, 8, 2, 1, 4, 9, 3);
        assertThat(sudoku.getSubGridValues(4)).containsExactly(5, 4, 8, 7, 2, 9, 1, 3, 6);
        assertThat(sudoku.getSubGridValues(5)).containsExactly(1, 3, 2, 5, 6, 4, 7, 9, 8);
        assertThat(sudoku.getSubGridValues(6)).containsExactly(9, 7, 6, 1, 3, 8, 2, 4, 5);
        assertThat(sudoku.getSubGridValues(7)).containsExactly(3, 7, 2, 8, 1, 4, 6, 9, 5);
        assertThat(sudoku.getSubGridValues(8)).containsExactly(6, 8, 9, 2, 5, 3, 4, 1, 7);
        assertThat(sudoku.getSubGridValues(9)).containsExactly(5, 1, 4, 7, 6, 9, 3, 8, 2);
    }

    @ParameterizedTest(name = "{index}: getSubGridStartingIndex({0}) == [{1}][{2}])")
    @CsvSource({
        "1, 0, 0", "2, 0, 3", "3, 0, 6",
        "4, 3, 0", "5, 3, 3", "6, 3, 6",
        "7, 6, 0", "8, 6, 3", "9, 6, 6",
    })
    void getSubGridStartingIndex(final int subGridIndex, final int expectedRow, final int expectedColumn) {
        assertThat(new Sudoku(completedPuzzle).getSubGridStartingIndex(subGridIndex)).isEqualTo(Pair.of(expectedRow, expectedColumn));
    }

    @ParameterizedTest(name = "{index}: getSubGridRow({0}) == [{1}])")
    @MethodSource("subGridProvider")
    void getSubGridRow(final int subGridIndex, final int expectedRow) {
        assertThat(new Sudoku(completedPuzzle).getSubGridRow(subGridIndex)).isEqualTo(expectedRow);
    }

    @ParameterizedTest(name = "{index}: getSubGridColumn({0}) == [{2}])")
    @MethodSource("subGridProvider")
    void getSubGridColumn(final int subGridIndex, @SuppressWarnings("unused") final int expectedRow, final int expectedColumn) {
        assertThat(new Sudoku(completedPuzzle).getSubGridColumn(subGridIndex)).isEqualTo(expectedColumn);
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 5, 6, 7, 8})
    void testConstructorRequiresPerfectSquare(final int size) {
        assertThrows(IllegalArgumentException.class, () -> new Sudoku(size));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 4, 9, 16, 25, 36, 49, 64, 81})
    void testConstructorAcceptsPerfectSquare(final int size) {
        assertDoesNotThrow(() -> new Sudoku(size));
    }

    @Test
    void getSubGridLength() {
        assertThat(new Sudoku(new int[9][9]).getSubGridLength()).isEqualTo(3);
    }

    @Test
    void getSubGridHeight() {
        assertThat(new Sudoku(new int[9][9]).getSubGridHeight()).isEqualTo(3);
    }

    @Test
    void getLength() {
        final Sudoku completedPuzzle = new Sudoku(new int[9][9]);
        assertThat(completedPuzzle.getLength()).isEqualTo(9);
    }

    @Test
    void getHeight() {
        final Sudoku completedPuzzle = new Sudoku(new int[9][9]);
        assertThat(completedPuzzle.getHeight()).isEqualTo(9);
    }

    @ParameterizedTest(name = "{index}: getColumn({1}) == {2} (Grid: {0})")
    @MethodSource("columnProvider")
    void getColumn(final Sudoku puzzle, final int columnNumber, final int[] expectedColumn) {
        Assertions.assertThat(puzzle.getColumn(columnNumber)).containsExactly(expectedColumn);
    }

    @ParameterizedTest(name = "{index}: getRow({1}) == {2} (Grid: {0})")
    @MethodSource("rowProvider")
    void getRow(final Sudoku puzzle, final int rowNumber, final int[] expectedRow) {
        assertThat(puzzle.getRow(rowNumber)).containsExactly(expectedRow);
    }

    @Test
    void getRows() {
        assertThat(new Sudoku(completedPuzzle).getRows()).isEqualTo(completedPuzzle);
    }

    @Test
    void getColumns() {
        assertThat(new Sudoku(completedPuzzle).getColumns()).isEqualTo(new int[][] {
            {4, 9, 2, 5, 7, 1, 3, 8, 6},
            {8, 6, 5, 4, 2, 3, 7, 1, 9},
            {3, 7, 1, 8, 9, 6, 2, 4, 5},

            {9, 3, 8, 1, 5, 7, 6, 2, 4},
            {2, 4, 7, 3, 6, 9, 8, 5, 1},
            {1, 5, 6, 2, 4, 8, 9, 3, 7},

            {6, 8, 4, 9, 1, 2, 5, 7, 3},
            {5, 2, 9, 7, 3, 4, 1, 6, 8},
            {7, 1, 3, 6, 8, 5, 4, 9, 2}
        });
    }

    @Test
    void printPuzzle() {
        new Sudoku(completedPuzzle).print();
    }
}