package minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Random;

public class Minesweeper implements ActionListener, Serializable {
    static int bombs;
    int startX;
    int startY;
    JButton[][] cells;
    int[][] solution;
    boolean[][] flagged;
    int[] xBombs;
    int[] yBombs;
    boolean test;
    JFrame frame;
    JPanel cellPanel;
    JLabel textfield;
    JButton resetButton;
    JButton saveButton;
    JButton flag;


    boolean flagging;
    int count = 0;
    int lastXchecked;
    int lastYchecked;

    transient Random random;

    public Minesweeper(boolean testMode) {
        Graohics();
        setBombs();
        getSolution();

        if (!testMode) {
            File savedGameFile = new File("minesweeper_save.csv");
            if (savedGameFile.exists()) {
                int response = JOptionPane.showConfirmDialog(null,
                        "Do you want to load the saved game?", "Load Game",
                        JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    Minesweeper savedGame = loadGame("minesweeper_save.csv");
                    if (savedGame != null) {
                        this.frame.dispose();
                        this.frame = savedGame.frame;
                        this.cellPanel = savedGame.cellPanel;
                        this.textfield = savedGame.textfield;
                        this.resetButton = savedGame.resetButton;
                        this.flag = savedGame.flag;
                        this.cells = savedGame.cells;
                        this.solution = savedGame.solution;
                        this.flagged = savedGame.flagged;
                        this.random = savedGame.random;
                        this.xBombs = savedGame.xBombs;
                        this.yBombs = savedGame.yBombs;
                        this.flagging = savedGame.flagging;
                        this.count = savedGame.count;
                        this.lastXchecked = savedGame.lastXchecked;
                        this.lastYchecked = savedGame.lastYchecked;
                        this.startX = savedGame.startX;
                        this.startY = savedGame.startY;

                        this.frame.setVisible(true);

                        getSolution();
                    }
                }
            }
        }
    }

    public Minesweeper() {
        this(false);
    }

    public void setBombs() {
        for (int i = 0; i < bombs; i++) {
            xBombs[i] = random.nextInt(16);
            yBombs[i] = random.nextInt(16);
            for (int j = 0; j < i; j++) {
                while (xBombs[i] == xBombs[j] && yBombs[i] == yBombs[j]) {
                    xBombs[i] = random.nextInt(16);
                    yBombs[i] = random.nextInt(16);
                    j = 0;
                }
            }
        }
    }

    public void saveGame(String fileName) {
        try (ObjectOutputStream outputStream =
                     new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(this);
            JOptionPane.showMessageDialog(frame, "Game saved succesfully!",
                    "Game Saved", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Minesweeper loadGame(String fileName) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            Minesweeper minesweeper = (Minesweeper) inputStream.readObject();
            JOptionPane.showMessageDialog(frame, "Game loaded succesfully!",
                    "Game Loaded", JOptionPane.INFORMATION_MESSAGE);
            return minesweeper;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    ImageIcon flagIcon = new ImageIcon("src/main/java/resources/Flag.png");

    ImageIcon icon = new ImageIcon("src/main/java/resources/output-onlinepngtools.png");

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == flag) {
            toggleFlagMode();
        } else if (e.getSource() == saveButton) {
            saveGame("minesweeper_save.csv");
        } else if (e.getSource() == resetButton) {
            restartGame();
        } else {
            handleCellClick(e);
        }
    }

    private void toggleFlagMode() {
        flagging = !flagging;
        flag.setForeground(flagging ? Color.RED : Color.GRAY);
        flag.setText(flagging ? "Flag" : "Flag");
    }

    private void restartGame() {
        frame.dispose();
        new Minesweeper();
    }

    private void handleCellClick(ActionEvent e) {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                if (e.getSource() == cells[i][j]) {
                    if (flagging) {
                        handleFlagClick(i, j);
                    } else {
                        handleRegularClick(i, j);
                    }
                }
            }
        }
    }

    private void handleFlagClick(int i, int j) {
        if (flagged[i][j]) {
            cells[i][j].setText("");
            cells[i][j].setIcon(icon);
            flagged[i][j] = false;
        } else if (cells[i][j].getText().equals("")) {
            cells[i][j].setForeground(Color.RED);
            cells[i][j].setIcon(flagIcon);
            flagged[i][j] = true;
        }
    }

    private void handleRegularClick(int i, int j) {
        if (!flagged[i][j]) {
            check(i, j);
        }
    }

    public void getSolution()
    {
        for (int y = 0; y < solution.length; y++)
        {
            for (int x = 0; x < solution[0].length; x++)
            {
                if (isMine(x, y)) {
                    solution[y][x] = 17; // Indicate mine
                } else {
                    solution[y][x] = countBombsAround(x, y);
                }
            }
        }
    }

    public boolean isMine(int x, int y)
    {
        for (int i = 0; i < bombs; i++) {
            if (x == xBombs[i] && y == yBombs[i]) {
                return true;
            }
        }
        return false;
    }

    public int countBombsAround(int x, int y)
    {
        int bombsAround = 0;

        for (int i = 0; i < bombs; i++) {
            int dx = Math.abs(x - xBombs[i]);
            int dy = Math.abs(y - yBombs[i]);

            // Count bombs only when both x and y differences are less than or equal to 1
            if (dx <= 1 && dy <= 1 && !(dx == 0 && dy == 0)) {
                bombsAround++;
            }
        }

        return bombsAround;
    }

    public void check(int y, int x)
    {
        boolean over=false;
        if (x < 0 || x >= 16 || y < 0 || y >= 16) {
            System.out.println("Out of bounds");
            return;
        }
        if(solution[y][x]==(17))
        {
            gameOver(false);
            over=true;
        }
        if(!over)
        {
            revealCell(y,x);
            checkWinner();
        }
    }
    public void revealCell(int y, int x) {
        getColor(y, x);

        if (solution[y][x] == 0) {
            startX = x;
            startY = y;
            count = 0;
            display();
        }
    }

    ImageIcon bombIcon = new ImageIcon("src/main/java/org/cis1200/resources/Bomb.png");

    public void gameOver(boolean won)
    {
        if(!won)
        {
            if (!test) {
                JOptionPane.showMessageDialog(frame, "Game Over! You hit a bomb.",
                        "Game Over", JOptionPane.INFORMATION_MESSAGE);
            } else {
                System.out.print("game over");
            }
        }
        else
        {
            if (test) {
                System.out.print("won");
            } else {
                JOptionPane.showMessageDialog(frame, "Congradulations!, you won",
                        "Game Over", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        for(int i=0; i<cells.length; i++)
        {
            for(int j=0; j<cells[0].length; j++)
            {
                cells[i][j].setBackground(null);
                cells[i][j].setEnabled(false);
                for(int count=0; count<bombs; count++)
                {
                    if(j==xBombs[count] && i==yBombs[count])
                    {
                        cells[i][j].setIcon(bombIcon);
                    }
                }
            }
        }
        flag.setEnabled(false);
    }


    public void checkWinner()
    {
        int cellsleft=0;

        for(int i=0; i<cells.length; i++)
        {
            for(int j=0; j<cells[0].length; j++)
            {
                if(cells[i][j].getText()=="" || cells[i][j].getIcon()==flagIcon)
                    cellsleft++;
            }
        }
        if(cellsleft==bombs)
            gameOver(true);
    }

    public void display() {
        if (count < 1) {
            if ((startX - 1) >= 0) {
                getColor(startY, startX - 1);
            }
            if ((startX + 1) < 16) {
                getColor(startY, startX + 1);
            }
            if ((startY - 1) >= 0) {
            getColor(startY - 1, startX);
        }
        if ((startY + 1) < 16) {
            getColor(startY + 1, startX);
        }
        if ((startY + 1) < 16 && (startX + 1) < 16) {
            getColor(startY + 1, startX + 1);

        }
        if ((startY - 1) >= 0 && (startX - 1) >= 0) {
            getColor(startY - 1, startX - 1);
        }
        if ((startY + 1) < 16 && (startX - 1) >= 0) {
            getColor(startY + 1, startX - 1);
        }
        if ((startY - 1) >= 0 && (startX + 1) < 16) {} {
            getColor(startY - 1, startX + 1);
        }
        count++;
        display();
    } else {
        for (int y = 0; y < cells.length; y++) {
            for (int x = 0; x < cells.length; x++) {
                if (cells[y][x].getText().equals("0")) {
                    if (y - 1 >= 0) {
                        if ((cells[y - 1][x].getText().equals("")) && !(cells[y - 1][x].getIcon().equals(flagIcon))) {
                            lastXchecked = x;
                            lastYchecked = y;
                        }
                    }
                    if (x - 1 >= 0) {
                        if ((cells[y][x - 1].getText().equals("")) && !(cells[y][x - 1].getIcon().equals(flagIcon))) {
                            lastXchecked = x;
                            lastYchecked = y;
                        }
                    }
                    if (y + 1 >= 0) {
                        if ((cells[y + 1][x].getText().equals("")) && !(cells[y + 1][x].getIcon().equals(flagIcon))) {
                            lastXchecked = x;
                            lastYchecked = y;
                        }
                    }
                    if (x + 1 >= 0) {
                        if ((cells[y][x + 1].getText().equals("")) && !(cells[y][x + 1].getIcon().equals(flagIcon))) {
                            lastXchecked = x;
                            lastYchecked = y;
                        }
                    }
                }
            }
        }

        if (lastXchecked < 17 && lastYchecked < 17) {
            startX = lastXchecked;
            startY = lastYchecked;
            count = 0;

            display();
        }
    }
}




    public void getColor(int row, int col) {
        int value = solution[row][col];
        cells[row][col].setText(String.valueOf(value));
        switch (value) {
            case 0:
                cells[row][col].setEnabled(false);
                break;
            case 1:
                cells[row][col].setForeground(Color.BLUE);
                break;
            case 2:
                cells[row][col].setForeground(Color.GREEN);
                break;
            case 3:
                cells[row][col].setForeground(Color.RED);
                break;
            case 4:
                cells[row][col].setForeground(Color.MAGENTA);
                break;
            case 5:
                cells[row][col].setForeground(Color.BLUE);
                break;
            case 6:
                cells[row][col].setForeground(Color.GREEN);
                break;
            case 7:
                cells[row][col].setForeground(Color.RED);
                break;
            case 8:
                cells[row][col].setForeground(Color.MAGENTA);
                break;
            default:
                break;
        }
    }


    public void Graohics() {
        frame = new JFrame();
        frame.setVisible(true);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        cellPanel = new JPanel();
        cellPanel.setVisible(true);
        cellPanel.setLayout(null);

        resetButton = new JButton();
        resetButton.setForeground(Color.BLUE);
        resetButton.setText("Reset");
        resetButton.setFont(new Font("Monospaced Bold", Font.BOLD, 20));
        resetButton.setFocusable(false);
        resetButton.addActionListener(this);

        saveButton = new JButton();
        saveButton.setForeground(Color.GREEN);
        saveButton.setText("Save");
        saveButton.setFont(new Font("Monospaced Bold", Font.BOLD, 20));
        saveButton.setBackground(Color.WHITE);
        saveButton.setFocusable(false);
        saveButton.addActionListener(this);

        flag = new JButton();
        flag.setForeground(Color.GRAY);
        flag.setText("Flag");
        flag.setFont(new Font("Monospaced Bold", Font.BOLD, 20));
        flag.setBackground(Color.WHITE);
        flag.setFocusable(false);
        flag.addActionListener(this);

        cells = new JButton[16][16];
        solution = new int[16][16];
        ImageIcon icon = new ImageIcon("src/main/java/resources/output-onlinepngtools.png");

        bombs = 30;

        lastXchecked = 17;
        lastYchecked = 17;

        flagged = new boolean[16][16];
        random = new Random();
        xBombs = new int[bombs];
        yBombs = new int[bombs];

        int cellSize = 30;

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                cells[i][j] = new JButton("", icon);
                cells[i][j].setHorizontalTextPosition(JButton.CENTER);
                cells[i][j].setVerticalTextPosition(JButton.CENTER);
                cells[i][j].setPreferredSize(new Dimension(cellSize, cellSize));
                cells[i][j].setFocusable(false);
                cells[i][j].setFont(new Font("Monospaced Bold", Font.BOLD, 12));
                cells[i][j].setBounds(j * cellSize, i * cellSize, cellSize, cellSize);
                cells[i][j].addActionListener(this);
                cellPanel.add(cells[i][j]);
            }
        }

        frame.add(cellPanel);


        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel leftBottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftBottomPanel.add(flag);

        JPanel rightBottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightBottomPanel.add(resetButton);
        rightBottomPanel.add(saveButton);

        bottomPanel.add(leftBottomPanel, BorderLayout.WEST);
        bottomPanel.add(rightBottomPanel, BorderLayout.EAST);

        frame.setSize(450, 550);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.setLocationRelativeTo(null);
        frame.setTitle("Minesweeper");

    }
}


