# Minesweeper

## Core Concepts

### 1. 2D Arrays

In this Minesweeper code, arrays are used for various purposes, including representing the game grid, storing the positions of bombs, and keeping track of flagged cells:

- **`JButton[][] cells`**: This 2D array represents the grid of buttons in the Minesweeper game. Each button corresponds to a cell in the grid. The array is used to store and manage these buttons.
  
- **`int[][] solution`**: A 2D array that represents the solution grid of the game. It stores information about whether a cell contains a mine (`17` indicates a mine) or the number of neighboring mines. This array is used to generate the solution and determine the content of each cell during the game.
  
- **`boolean[][] flagged`**: A 2D boolean array that keeps track of whether a cell is flagged. If `flagged[i][j]` is `true`, it means the cell at position `(i, j)` is flagged. This array is used to implement the flagging mechanism in the game.
  
- **`int[] xBombs` and `int[] yBombs`**: These arrays store the x and y coordinates of the bomb positions. They are used to randomly place bombs on the grid.

### 2. File I/O

File I/O is used to allow players to save and resume games. The `ObjectOutputStream` and `ObjectInputStream` classes are employed to write and read serialized objects, respectively. File I/O is used to store and retrieve the entire state of the game, including:

- The 2D array representing the game grid.
- The positions of bombs.
- Other game-related information.

The `ObjectOutputStream` handles serialization of the game state, and the `ObjectInputStream` handles deserialization.

### 3. JUnit Testable Component

Several unit tests are used to verify the functionality of different aspects of the game. These include:

- Initialization of the game.
- Flagging cells.
- Checking for mines.
- Handling edge cases like out-of-bounds errors.

### 4. Recursion

Recursion is used to uncover adjacent cells when an empty cell is clicked. The recursive function `display()` uncovers adjacent empty cells until it reaches a boundary or cells with neighboring mines.

## Implementation

- **`Minesweeper`**: The main class that represents the game. It includes game logic, graphical user interface (GUI), and methods for handling user interactions.
  
- **`Game`**: Initializes an instance of the `Minesweeper` class and starts the game.
  
- **`GameTest`**: JUnit tests for the Minesweeper game.

## External Resources

- **`Bomb.png`**
- **`Flag.png`**
- **`output-onlinepngtools.png`** (normal cells)
