package com.rf5860.sudoku.assertions;

import com.rf5860.sudoku.Sudoku;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class SudokuAssert extends AbstractAssert<SudokuAssert, Sudoku> {

    public SudokuAssert(final Sudoku sudoku, final Class<?> selfType) {
        super(sudoku, selfType);
    }

    @NotNull
    @Contract("_ -> new")
    public static SudokuAssert assertThat(final Sudoku sudoku) {
        return new SudokuAssert(sudoku, SudokuAssert.class);
    }

    public SudokuAssert hasAllCellsSetTo(final int value) {
        for (int row = 0; row < actual.getHeight(); row++) {
            for (int column = 0; column < actual.getLength(); column++) {
                Assertions.assertThat(actual.get(row, column)).isEqualTo(value);
            }
        }
        return this;
    }
}
