import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Board {
    static private int DIRECTIOS[] = {-1, -1, -1,  0, -1, 1, 0, -1, 0, 1, 1, -1, 1, 0, 1, 1};

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
        int c_y = 0;
        int c_x = 0;
        for (Tie[] row: board) {
            c_x += 1;
            for (Tie tie: row) {
                if (tie != null) {
                    int x = c_x;
                    int y = c_y;
                    if (isForEnemy) {
                        ties = CheckNeighbors(!tie.isWhite(), ties, y, x, c_x, c_y);
                    } else {
                        ties = CheckNeighbors(tie.isWhite(), ties, y, x, c_x, c_y);
                    }

                }
                c_y += 1;
            }
            c_y = 0;
        }
        return ties;
    }

    // -1 - наши
    // -2 - противник
    // 1 - возможное место
    private int[][] CheckNeighbors(boolean tie, int[][] ties, int y, int x, int c_x, int c_y) {
        if (tie) {
            if (ties[x][y] != 1) {
                ties[x][y] = -1;
            }

            for (int i = 0; i < 16; i+=2) {
                x += DIRECTIOS[i];
                y += DIRECTIOS[i+1];
                if (board[x][y] == null) {
                    ties[x][y] = 1;
                }
            }

        } else {
            if (ties[x][y] != 1) {
                ties[x][y] =  -2;
            }
        }
        System.out.println(ties.toString());
        return ties;
    }

    public int[][] GetPossiblePlaces(User player) {
        int[][] ties;
        if ((Objects.equals(player.getName(), "Player 2")) || (player instanceof AI)) {
            System.out.println("For player 2 or Ai");
            ties =  GetAllNeighbors(true);
            GetOnlyValidNeighbors(ties);
        } else {
            ties = GetAllNeighbors(false);
            GetOnlyValidNeighbors(ties);
        }
        return ties;
    }

    private void GetOnlyValidNeighbors(int[][] ties) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (ties[i][j] == 1) {
                    if (CheckOnDiagonales(i, j, ties) || CheckOnVertical(i, j, ties)) {
                        ties[i][j] = 1;
                    } else {
                        ties[i][j] = 0;
                    }
                }
            }
        }
    }

    private boolean CheckOnDiagonales(int x, int y, int[][] ties) {
        return CheckLine(x, y, ties, -1, -1) || CheckLine(x, y, ties, 1, 1) ||
                CheckLine(x, y, ties, 1, -1) || CheckLine(x, y, ties, -1, 1);
    }

    private boolean CheckOnVertical(int x, int y, int[][] ties) {
        return CheckLine(x, y, ties, -1, 0) || CheckLine(x, y, ties, 0, -1) ||
                CheckLine(x, y, ties, 1, 0) || CheckLine(x, y, ties, 0, 1);
    }

    private static boolean CheckLine(int x, int y, int[][] ties, int forX, int forY) {
        int x_ = x;
        int y_ = y;
        x_ += forX;
        y_ += forY;
        if (x_ >= 0 && y_ >= 0 && x_ < 8 && y_ < 8 && ties[x_][y_] == -2) {
            while (x_ >= 0 && y_ >= 0 && x_ < 8 && y_ < 8) {
                if (ties[x_][y_] == 0) {
                    break;
                }
                if (ties[x_][y_] == -1) {
                    return true;
                }
                x_ += forX;
                y_ += forY;
            }
        }
        return false;
    }

    private void PrintBoardWithPossibilities(int[][] ties) {
        StringBuilder str = new StringBuilder();
        int y = 0;
        int x = 0;
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
        ArrayList<Integer> coordinatesToTurn = new ArrayList<>();
        if (x_ >= 0 && y_ >= 0 && x_ < 8 && y_ < 8) {
            while (x_ >= 0 && y_ >= 0 && x_ < 8 && y_ < 8) {
                if (board[x_][y_] == null) {
                    return;
                }
                if (board[x_][y_].isWhite() == isWhite) {
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

        for (int i = 0; i < coordinatesToTurn.size(); i += 2) {
            board[coordinatesToTurn.get(i)][coordinatesToTurn.get(i+1)].setNewMaster(player);
        }
    }

    public boolean IsPlayable() {
        CountPoints();
        return (score_user + score_enemy < 64) && (score_enemy != 0) && (score_user != 0);
    }

    public void PrintWinner() {
        if (score_enemy == score_user) {
            System.out.println("Ничья!");
        } else if (score_enemy > score_user) {
            System.out.println("Игрок 1 - проиграл");
        } else {
            System.out.println("Победа игрока 1");
        }
    }
}
