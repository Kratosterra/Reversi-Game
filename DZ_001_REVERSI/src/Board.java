import java.util.Objects;

public class Board {
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

    public int[][] GetPossiblePlaces(User player) {
        int[][] ties = new int[8][8];
        if ((Objects.equals(player.getName(), "Player 2"))) {
            ties[0][0] = 1;
            ties[1][1] = 1;
        } else {
            ties[7][7] = 1;
            ties[6][6] = 1;
        }
        return ties;
    }

    private void PrintBoardWithPossibilities(int[][] ties) {
        StringBuilder str = new StringBuilder();
        int y = 0;
        int x = 0;
        str.append("Y\\X 1   2   3   4   5   6   7   8 \n ⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯\n");
        for (Tie[] row: board) {
            str.append(y+1);
            str.append("| ");
            for (Tie tie: row) {
                str.append(" ");
                if (tie == null) {
                    str.append(ties[y][x] == 1 ? "□" : " ");
                } else {
                    str.append(tie.toString());
                }
                str.append(" |");
                x += 1;
            }
            x = 0;
            str.append("\n ⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯\n");
            y += 1;
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

    public void SetTurnWithTie(Tie tie) {
        board[tie.getX()][tie.getY()] = tie;
        TurnTies(tie);
        CountPoints();
    }

    private void CountPoints() {
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

    private void TurnTies(Tie tie) {

    }

    public boolean IsPlayable() {
        return score_user + score_enemy < 64;
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
