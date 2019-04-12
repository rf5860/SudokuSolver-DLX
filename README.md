# Sudoku Solver

## Details
 
### General

This uses Knuth's implementation of [Algorithm X](https://en.wikipedia.org/wiki/Algorithm_X) - known as [Dancing Links (DLX)](https://en.wikipedia.org/wiki/Dancing_Links)

#### Algorithm X

Algorithm X is an algorithm utilising backtracking to find all solutions to an [Exact Cover](https://en.wikipedia.org/wiki/Exact_cover) problem.

The general steps of Algorithm X are:

- If there are no empty columns, the current solution is valid
- If not:
  - Choose a column
  - Choose a row satisfying the constraint
  - Include the row as a partial solution
  - Remove all columns satisfied by the constraint
  - Repeat

![Algorithm X](doc/algorithm-x.png)

##### Exact Cover Problems

###### Overview

To understand Algorithm X, it's necessary to understand what an Exact Cover problem is.

An Exact Cover is a set of **choices** and **constraints**. The aim is to satisfy all constraints, using exactly one choice. There may be multiple solutions.

As an example, take a collections sets: S = {A, B, C, D, E, F}, where each set contains numbers in the range from 1 to 7:

A = {1, 3, 2}     C = {4, 5, 7}
B = {3, 4}        D = {2, 7, 6}
E = {5, 6, 7}     F = {1, 5}

The goal is to select subsets where each number is present once, and only once. 
This problem can be represented asa matrix, where columns are numbers and rows are sets:

```text
  | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 
A | 1 | 0 | 0 | 1 | 0 | 0 | 1 |
B | 1 | 0 | 0 | 1 | 0 | 0 | 0 |
C | 0 | 0 | 0 | 1 | 1 | 0 | 1 |
D | 0 | 0 | 1 | 0 | 1 | 1 | 0 |
E | 0 | 1 | 1 | 0 | 0 | 1 | 1 |
F | 0 | 1 | 0 | 0 | 0 | 0 | 1 |
```

Sub-set S* = {B, D, F} is an exact cover:

```text
  | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 
B | 0 | 0 | 1 | 1 | 0 | 0 | 0 |
D | 0 | 1 | 0 | 0 | 0 | 1 | 1 |
F | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
```

We can represent the problem using a matrix, where columns are numbers and rows are sets:

Some popular examples of Exact Cover problems include:

 - [Sudoku](https://en.wikipedia.org/wiki/Sudoku)
 - [N Queens](https://en.wikipedia.org/wiki/Eight_queens_puzzle)
 - [Tiling / Tessellation](https://en.wikipedia.org/wiki/Tessellation)

###### Modelling

An Exact Cover problem can be modelled as a table, with:

- Constraints represented by columns 
- Choices represented by rows
- Satisfied constraints represented with an `X` (For a given row/column pair)

It has the following qualities:

- [Recursive](https://en.wikipedia.org/wiki/Recursion_(computer_science))
- [Non-deterministic](https://en.wikipedia.org/wiki/Nondeterministic_algorithm)
- [Depth-first](https://en.wikipedia.org/wiki/Depth-first_search)
- [Backtracking](https://en.wikipedia.org/wiki/Backtracking)

This is similar to a Sudoku puzzle - and a Sudoku can be treated as an Exact Cover problem, by re-framing it in the following way:

There are 324 (81 + 81 + 81 + 81) constraints, based on:

- Every row must have 9 values in it, and there are 9 rows (81 Constraints)
- Every column must have 9 values in it, and there are 9 columns (81 Constraints) 
- Every sub-grid must have 9 values in it, and there are 9 sub-grids (81 Constraints) 
- Every cell must have a number (81 Constraints) 

Which can be represented as 729 rows in a matrix, with each row being a (row, column, value) triple, each of which contribute to the four constraints:

```text
                          │Each row holds 1 to 9│Each column holds 1 to 9│Each sub-grid holds 1 to 9│Each cell holds one value│
                          │1 2 3 4 5 6 7 8 9    │1 2 3 4 5 6 7 8 9       │1 2 3 4 5 6 7 8 9         │1 2 3 4 5 6 7 8 9        │
(row 1, column 1, value 1)│1                    │1                       │1                         │1                        │
(row 1, column 1, value 2)│  2                  │  2                     │  2                       │  2                      │
(row 1, column 1, value 3)│    3                │    3                   │    3                     │    3                    │
...
(row 9, column 9, value 7)│            7        │            7           │            7             │            7            │
(row 9, column 9, value 8)│              8      │              8         │              8           │              8          │
(row 9, column 9, value 9)│                9    │                9       │                9         │                9        │
```

Because most of this matrix is empty, it is a [sparse matrix]()

##### [Dancing Links (DLX)](http://www.ocf.berkeley.edu/~jchu/publicportal/sudoku/0011047.pdf)

Dancing Links is an **efficient** implementation of Algorithm X. The efficiency stems from the fact that a node in a [doubly-linked list](https://en.wikipedia.org/wiki/Doubly_linked_list) can be reinserted in `O(1)` (instead of `O(n)`, in the naive approach).

This can be demonstrated via the following pseudo-code.

```text
// Removing a Node
x.left.right ← x.right;
x.right.left ← x.left;
//  Adding it back (Assuming 'x' is unchanged):
x.left.right ← x;
x.right.left ← x;
```

In this implementation, every constraint is represented by four special header nodes which corresponding to the different constraints. Each node in the sparse matrix (which corresponds to a partial solution) has a pointer to one of these special header nodes.

```text

Constraint A     Constraint B     Constraint C            
   ╔═══╗            ╔═══╗            ╔═══╗
   ║ A ║            ║ A ║            ║ A ║ 
   ╚═══╝            ╚═══╝            ╚═══╝

```