package com.rf5860.sudoku;

import lombok.Data;

@Data
public class Node {
    private Node left;
    private Node right;
    private Node up;
    private Node down;
}
