import java.util.ArrayList;
import java.util.Objects;

public class Board {
    static private final int[] DIRECTIOS = {-1, -1, -1,  0, -1, 1, 0, -1, 0, 1, 1, -1, 1, 0, 1, 1};

    public void setBoard(Tie[][] board) {
        this.board = board;
    }

    private Tie[][] board = new Tie[8][8];
    private int scoreUser;
    private int scoreEnemy;

    public Board(User first, User second) {
        this.scoreEnemy = 2;
        this.scoreUser = 2;

        board[3][4] = new Tie(first, 3, 4);
        board[4][3] = new Tie(first, 4, 3);
        board[3][3] = new Tie(second, 3, 3);
        board[4][4] = new Tie(second, 4, 4);
        countPoints();
    }


    public void printBoard() {
        int y = 0;
        StringBuilder str = new StringBuilder();
        str.append("Y\\X 1   2   3   4   5   6   7   8 \n ⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯\n");
        for (Tie[] row: board) {
            str.append(y+1);
            str.append("| ");
            for (Tie tie: row) {
                str.append(" ");
                if (tie == null) {
                    str.append(" ");
                } else {
                    str.append(tie);
                }
                str.append(" |");
            }
            str.append("\n⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯\n");
            y++;
        }

        System.out.println(str);
    }

    public void printBoardWithPossibleTurns(User player) {
        int[][] ties = getPossiblePlaces(player);
        printBoardWithPossibilities(ties);
    }


    private int[][] getAllNeighbors(boolean isForEnemy) {
        int[][] ties = new int[8][8];
        for (Tie[] row: board) {
            for (Tie tie: row) {
                if (tie != null) {
                    if (isForEnemy) {
                        ties = checkNeighbors(tie, !tie.isWhite(), ties);
                    } else {
                        ties = checkNeighbors(tie, tie.isWhite(), ties);
                    }

                }
            }
        }
        return ties;
    }

    private int[][] checkNeighbors(Tie tie, boolean type, int[][] ties) {
        if (type) {
            int x = tie.getX();
            int y = tie.getY();
            for (int i = 0; i < 16; i+=2) {
                x += DIRECTIOS[i];
                y += DIRECTIOS[i+1];
                if (x >= 0 && y >= 0 && x < 8 && y < 8) {
                    if (board[x][y] == null) {
                        ties[x][y] = 1;
                    }
                }
                x = tie.getX();
                y = tie.getY();
            }
        }
        return ties;
    }

    public int[][] getPossiblePlaces(User player) {
        int[][] ties;
        if ((Objects.equals(player.getName(), "Player 2")) || (player instanceof AI)) {
            ties =  getAllNeighbors(true);
            getOnlyValidNeighbors(ties, true);
        } else {
            ties = getAllNeighbors(false);
            getOnlyValidNeighbors(ties, false);
        }
        return ties;
    }

    private void getOnlyValidNeighbors(int[][] ties, boolean IsForEnemy) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (ties[i][j] == 1) {
                    if (checkOnDiagonales(i, j, IsForEnemy) || checkOnVertical(i, j, IsForEnemy)) {
                        ties[i][j] = 1;
                    } else {
                        ties[i][j] = 0;
                    }
                }
            }
        }
    }

    private boolean checkOnDiagonales(int x, int y, boolean IsForEnemy) {
        return checkLine(x, y, -1, -1, IsForEnemy) || checkLine(x, y, 1, 1, IsForEnemy) ||
                checkLine(x, y, 1, -1, IsForEnemy) || checkLine(x, y, -1, 1, IsForEnemy);
    }

    private boolean checkOnVertical(int x, int y, boolean IsForEnemy) {
        return checkLine(x, y, -1, 0, IsForEnemy) || checkLine(x, y, 0, -1, IsForEnemy) ||
                checkLine(x, y, 1, 0, IsForEnemy) || checkLine(x, y, 0, 1, IsForEnemy);
    }

    private boolean checkLine(int x, int y, int forX, int forY, boolean IsForEnemy) {
        int x_ = x;
        int y_ = y;
        x_ += forX;
        y_ += forY;
        boolean flag = false;
        ArrayList<Integer> coordinates = new ArrayList<>();
        if (x_ >= 0 && y_ >= 0 && x_ < 8 && y_ < 8) {
            while (x_ >= 0 && y_ >= 0 && x_ < 8 && y_ < 8) {
                if (this.board[x_][y_] == null) {
                    return false;
                }
                if (board[x_][y_].isWhite() == !IsForEnemy) {
                    coordinates.add(x_);
                    coordinates.add(y_);
                }
                if (board[x_][y_].isWhite() == IsForEnemy) {
                    flag = true;
                    break;
                }
                x_ += forX;
                y_ += forY;
            }
        }
        return (coordinates.size() > 0) && flag;
    }

    public void printBoardWithPossibilities(int[][] ties) {
        StringBuilder str = new StringBuilder();
        int x = 0;
        int y = 0;
        str.append("Y\\X 1   2   3   4   5   6   7   8 \n ⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯\n");
        for (Tie[] row: board) {
            str.append(x+1);
            str.append("| ");
            for (Tie tie: row) {
                str.append(" ");
                if (tie == null) {
                    str.append(ties[x][y] == 1 ? "□" : " ");
                } else {
                    str.append(tie);
                }
                str.append(" |");
                y += 1;
            }
            y = 0;
            str.append("\n ⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯\n");
            x += 1;
        }

        System.out.println(str);
    }

    public int getScoreUser() {
        return scoreUser;
    }

    public Tie[][] getBoard() {
        return board;
    }

    public void setTurnWithTie(Tie tie, User player) {
        board[tie.getX()][tie.getY()] = tie;
        turnTies(tie, player);
        countPoints();
    }

    private void countPoints() {
        scoreUser = 0;
        scoreEnemy = 0;
        for (Tie[] row: board) {
            for (Tie tie: row) {
                if (tie != null) {
                    if (tie.isWhite()) {
                        scoreEnemy += 1;
                    } else {
                        scoreUser += 1;
                    }
                }
            }
        }
    }

    private void turnTies(Tie tie, User player) {
        int x = tie.getX();
        int y = tie.getY();

        turnLine(x, y, player, tie.isWhite(), -1, -1);
        turnLine(x, y, player, tie.isWhite(), 1, -1);
        turnLine(x, y, player, tie.isWhite(), 1, 1);
        turnLine(x, y, player, tie.isWhite(), -1, 1);

        turnLine(x, y, player, tie.isWhite(), 0, -1);
        turnLine(x, y, player, tie.isWhite(), 0, 1);
        turnLine(x, y, player, tie.isWhite(), 1, 0);
        turnLine(x, y, player, tie.isWhite(), -1, 0);
    }

    private void turnLine(int x, int y, User player, boolean isWhite, int forX, int forY) {
        int x_ = x;
        int y_ = y;
        x_ += forX;
        y_ += forY;
        boolean flag = false;
        ArrayList<Integer> coordinatesToTurn = new ArrayList<>();
        if (x_ >= 0 && y_ >= 0 && x_ < 8 && y_ < 8) {
            while (x_ >= 0 && y_ >= 0 && x_ < 8 && y_ < 8) {
                if (board[x_][y_] == null) {
                    return;
                }
                if (board[x_][y_].isWhite() == isWhite) {
                    flag = true;
                    break;
                }
                if (board[x_][y_].isWhite() == !isWhite) {
                    coordinatesToTurn.add(x_);
                    coordinatesToTurn.add(y_);
                }
                x_ += forX;
                y_ += forY;
            }
        }
        if (flag) {
            for (int i = 0; i < coordinatesToTurn.size(); i += 2) {
                board[coordinatesToTurn.get(i)][coordinatesToTurn.get(i+1)].setNewMaster(player);
            }
        }
    }

    public boolean isPlayable() {
        countPoints();
        return (scoreUser + scoreEnemy < 64) && (scoreEnemy != 0) && (scoreUser != 0);
    }

    public void printWinner(User player2) {
        if (scoreEnemy == scoreUser) {
            if ((Objects.equals(player2.getName(), "Player 2"))) {
                System.out.println("              Ничья!\n★★★★★★★★★★★★★★★★★★★★★★★★★★★★\n");
                System.out.printf("Игрок 1: %s                ", scoreUser);
                System.out.printf("Игрок 2: %s          \n", scoreEnemy);
            } else if ((player2 instanceof AI)) {
                System.out.println("              Ничья!\n★★★★★★★★★★★★★★★★★★★★★★★★★★★★\n");
                System.out.printf("Игрок 1: %s                ", scoreUser);
                System.out.printf("Компьютер: %s          \n", scoreEnemy);
            }
        } else if (scoreEnemy > scoreUser) {
            if ((Objects.equals(player2.getName(), "Player 2"))) {
                System.out.println("           Выйграл Игрок 2!\n★★★★★★★★★★★★★★★★★★★★★★★★★★★★\n");
                System.out.printf("Игрок 1: %s                ", scoreUser);
                System.out.printf("Игрок 2: %s          \n", scoreEnemy);
            } else if ((player2 instanceof AI)) {
                System.out.println("           Выйграл Компьютер!\n★★★★★★★★★★★★★★★★★★★★★★★★★★★★\n");
                System.out.printf("Игрок 1: %s                ", scoreUser);
                System.out.printf("Компьютер: %s         \n", scoreEnemy);
            }
        } else {
            if ((Objects.equals(player2.getName(), "Player 2"))) {
                System.out.println("           Выйграл Игрок 1!\n★★★★★★★★★★★★★★★★★★★★★★★★★★★★\n");
                System.out.printf("Игрок 1: %s                ", scoreUser);
                System.out.printf("Игрок 2: %s          \n", scoreEnemy);
            } else if ((player2 instanceof AI)) {
                System.out.println("           Выйграл Игрок 1!\n★★★★★★★★★★★★★★★★★★★★★★★★★★★★\n");
                System.out.printf("Игрок 1: %s                ", scoreUser);
                System.out.printf("Компьютер: %s         \n", scoreEnemy);
            }
        }
        System.out.println("\n★★★★★★★★★★★★★★★★★★★★★★★★★★★★\n");
    }
}
