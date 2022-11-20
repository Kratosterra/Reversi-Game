import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Board {
    static private final int[] DIRECTIOS = {-1, -1, -1,  0, -1, 1, 0, -1, 0, 1, 1, -1, 1, 0, 1, 1};

    private Tie[][] board = new Tie[8][8];
    private int score_user;
    private int score_enemy;

    public Board(User first, User second) {
        this.score_enemy = 2;
        this.score_user = 2;

        board[3][4] = new Tie(first, 3, 4);
        board[4][3] = new Tie(first, 4, 3);
        board[3][3] = new Tie(second, 3, 3);
        board[4][4] = new Tie(second, 4, 4);
        CountPoints();
    }


    public void PrintBoard() {
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
                    str.append(tie.toString());
                }
                str.append(" |");
            }
            str.append("\n⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯\n");
            y++;
        }

        System.out.println(str.toString());
    }

    public void PrintBoardWithPossibleTurns(User player) {
        int[][] ties = GetPossiblePlaces(player);
        PrintBoardWithPossibilities(ties);
    }


    private int[][] GetAllNeighbors(boolean isForEnemy) {
        int[][] ties = new int[8][8];
        for (Tie[] row: board) {
            for (Tie tie: row) {
                if (tie != null) {
                    if (isForEnemy) {
                        ties = CheckNeighbors(tie, !tie.isWhite(), ties);
                    } else {
                        ties = CheckNeighbors(tie, tie.isWhite(), ties);
                    }

                }
            }
        }
        return ties;
    }

    private int[][] CheckNeighbors(Tie tie, boolean type, int[][] ties) {
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

    public int[][] GetPossiblePlaces(User player) {
        int[][] ties;
        if ((Objects.equals(player.getName(), "Player 2")) || (player instanceof AI)) {
            ties =  GetAllNeighbors(true);
            GetOnlyValidNeighbors(ties, true);
        } else {
            ties = GetAllNeighbors(false);
            GetOnlyValidNeighbors(ties, false);
        }
        return ties;
    }

    private void GetOnlyValidNeighbors(int[][] ties, boolean IsForEnemy) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (ties[i][j] == 1) {
                    if (CheckOnDiagonales(i, j, ties, IsForEnemy) || CheckOnVertical(i, j, ties, IsForEnemy)) {
                        ties[i][j] = 1;
                    } else {
                        ties[i][j] = 0;
                    }
                }
            }
        }
    }

    private boolean CheckOnDiagonales(int x, int y, int[][] ties, boolean IsForEnemy) {
        return CheckLine(x, y, ties, -1, -1, IsForEnemy) || CheckLine(x, y, ties, 1, 1, IsForEnemy) ||
                CheckLine(x, y, ties, 1, -1, IsForEnemy) || CheckLine(x, y, ties, -1, 1, IsForEnemy);
    }

    private boolean CheckOnVertical(int x, int y, int[][] ties, boolean IsForEnemy) {
        return CheckLine(x, y, ties, -1, 0, IsForEnemy) || CheckLine(x, y, ties, 0, -1, IsForEnemy) ||
                CheckLine(x, y, ties, 1, 0, IsForEnemy) || CheckLine(x, y, ties, 0, 1, IsForEnemy);
    }

    private boolean CheckLine(int x, int y, int[][] ties, int forX, int forY, boolean IsForEnemy) {
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

    public void PrintBoardWithPossibilities(int[][] ties) {
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
                    str.append(tie.toString());
                }
                str.append(" |");
                y += 1;
            }
            y = 0;
            str.append("\n ⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯\n");
            x += 1;
        }

        System.out.println(str.toString());
    }

    public int getScore_user() {
        return score_user;
    }

    public int getScore_enemy() {
        return score_enemy;
    }

    public Tie[][] getBoard() {
        return board;
    }

    public void SetTurnWithTie(Tie tie, User player) {
        board[tie.getX()][tie.getY()] = tie;
        TurnTies(tie, player);
        CountPoints();
    }

    private void CountPoints() {
        score_user = 0;
        score_enemy = 0;
        for (Tie[] row: board) {
            for (Tie tie: row) {
                if (tie != null) {
                    if (tie.isWhite()) {
                        score_enemy += 1;
                    } else {
                        score_user += 1;
                    }
                }
            }
        }
    }

    private void TurnTies(Tie tie, User player) {
        int x = tie.getX();
        int y = tie.getY();

        TurnLine(x, y, player, tie.isWhite(), -1, -1);
        TurnLine(x, y, player, tie.isWhite(), 1, -1);
        TurnLine(x, y, player, tie.isWhite(), 1, 1);
        TurnLine(x, y, player, tie.isWhite(), -1, 1);

        TurnLine(x, y, player, tie.isWhite(), 0, -1);
        TurnLine(x, y, player, tie.isWhite(), 0, 1);
        TurnLine(x, y, player, tie.isWhite(), 1, 0);
        TurnLine(x, y, player, tie.isWhite(), -1, 0);
    }

    private void TurnLine(int x, int y, User player, boolean isWhite, int forX, int forY) {
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

    public boolean IsPlayable() {
        CountPoints();
        return (score_user + score_enemy < 64) && (score_enemy != 0) && (score_user != 0);
    }

    public void PrintWinner(User player, User player2) {
        if (score_enemy == score_user) {
            if ((Objects.equals(player2.getName(), "Player 2"))) {
                System.out.println("              Ничья!\n★★★★★★★★★★★★★★★★★★★★★★★★★★★★\n");
                System.out.printf("Игрок 1: %s                ", score_user);
                System.out.printf("Игрок 2: %s          \n", score_enemy);
            } else if ((player2 instanceof AI)) {
                System.out.println("              Ничья!\n★★★★★★★★★★★★★★★★★★★★★★★★★★★★\n");
                System.out.printf("Игрок 1: %s                ", score_user);
                System.out.printf("Компьютер: %s          \n", score_enemy);
            }
        } else if (score_enemy > score_user) {
            if ((Objects.equals(player2.getName(), "Player 2"))) {
                System.out.println("           Выйграл Игрок 2!\n★★★★★★★★★★★★★★★★★★★★★★★★★★★★\n");
                System.out.printf("Игрок 1: %s                ", score_user);
                System.out.printf("Игрок 2: %s          \n", score_enemy);
            } else if ((player2 instanceof AI)) {
                System.out.println("           Выйграл Компьютер!\n★★★★★★★★★★★★★★★★★★★★★★★★★★★★\n");
                System.out.printf("Игрок 1: %s                ", score_user);
                System.out.printf("Компьютер: %s         \n", score_enemy);
            }
        } else {
            if ((Objects.equals(player2.getName(), "Player 2"))) {
                System.out.println("           Выйграл Игрок 1!\n★★★★★★★★★★★★★★★★★★★★★★★★★★★★\n");
                System.out.printf("Игрок 1: %s                ", score_user);
                System.out.printf("Игрок 2: %s          \n", score_enemy);
            } else if ((player2 instanceof AI)) {
                System.out.println("           Выйграл Игрок 1!\n★★★★★★★★★★★★★★★★★★★★★★★★★★★★\n");
                System.out.printf("Игрок 1: %s                ", score_user);
                System.out.printf("Компьютер: %s         \n", score_enemy);
            }
        }
        System.out.println("\n★★★★★★★★★★★★★★★★★★★★★★★★★★★★\n");
    }
}
