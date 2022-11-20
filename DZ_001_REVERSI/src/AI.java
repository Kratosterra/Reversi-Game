import java.util.ArrayList;

public class AI extends User{
    private final boolean isPro;
    private boolean canPlay = true;

    public AI(String name, boolean isPro) {
        super(name);
        this.isPro = isPro;
    }

    public Tie makeTurn(Board board, User player) {
        Tie finaleTie = null;
        if (isPro) {
            finaleTie = getFinaleTiePro(board, player, finaleTie);
        } else {
            finaleTie = getFinaleTieEasy(board, finaleTie);
        }
        return finaleTie;
    }

    private Tie getFinaleTieEasy(Board board, Tie finaleTie) {
        double max_R = -1.0;

        ArrayList<Tie> listOfPossibleTurns = new ArrayList<>();
        int[][] ties = board.getPossiblePlaces(this);
        for (int i = 0; i < ties.length; i++) {
            for (int j = 0; j < ties[i].length; j++) {
                if (ties[i][j] == 1) {
                    listOfPossibleTurns.add(new Tie(this, i, j));
                }
            }
        }
        for (Tie point: listOfPossibleTurns) {
            double R = getInfoForClosedTies(board, point);
            if (R > max_R) {
                max_R = R;
                finaleTie = point;
            }
        }
        return finaleTie;
    }

    private double getInfoForClosedTies(Board board, Tie point) {
        ArrayList<Tie> closedTies = new ArrayList<>();
        getClosedTies(closedTies, point, board);
        double ss = evaluateTie(point);
        ArrayList<Double> sList = new ArrayList<>();
        evaluateClosedTies(sList, closedTies);
        return getSumOf(sList) + ss;
    }

    private Tie getFinaleTiePro(Board board, User player, Tie finaleTie) {
        double max_R = -1.0;

        ArrayList<Tie> listOfPossibleTurns = new ArrayList<>();
        int[][] ties = board.getPossiblePlaces(this);
        for (int i = 0; i < ties.length; i++) {
            for (int j = 0; j < ties[i].length; j++) {
                if (ties[i][j] == 1) {
                    listOfPossibleTurns.add(new Tie(this, i, j));
                }
            }
        }
        for (Tie point: listOfPossibleTurns) {
            getInfoForClosedTies(board, point);
            double R = getInfoForClosedTies(board, point);
            double R_player = getEnemyFunctionR(board, point, player);
            R -= R_player;
            if (R > max_R) {
                max_R = R;
                finaleTie = point;
            }
        }
        return finaleTie;
    }

    private double getEnemyFunctionR(Board b, Tie point, User player) {
        Board newBoard = new Board(player, this);
        Tie[][] newTie = new Tie[8][8];
        for (int i = 0; i < b.getBoard().length; i++) {
            for (int j = 0; j < b.getBoard()[i].length; j++) {
                if (b.getBoard()[i][j] != null) {
                   newTie[i][j] =
                           new Tie(b.getBoard()[i][j].getMaster(), b.getBoard()[i][j].getX(), b.getBoard()[i][j].getY());
                }
            }
        }
        newBoard.setBoard(newTie);
        newBoard.setTurnWithTie(point, this);
        double max_R = -1.0;
        ArrayList<Tie> listOfPossibleTurns = new ArrayList<>();
        int[][] ties = newBoard.getPossiblePlaces(player);
        for (int i = 0; i < ties.length; i++) {
            for (int j = 0; j < ties[i].length; j++) {
                if (ties[i][j] == 1) {
                    listOfPossibleTurns.add(new Tie(player, i, j));
                }
            }
        }
        for (Tie p: listOfPossibleTurns) {
            double R = getInfoForClosedTies(newBoard, p);
            if (R > max_R) {
                max_R = R;
            }
        }
        return max_R;
    }
    private double evaluateTie(Tie point) {
        int x = point.getX();
        int y = point.getY();
        if (Math.max(x, y) == 7 || Math.min(x, y) == 0) {
            return 2;
        }
        return 1;
    }

    private double getSumOf(ArrayList<Double> sList) {
        double sum = 0;
        for (double num: sList) {
            sum += num;
        }
        return sum;
    }

    private void getClosedTies(ArrayList<Tie> closedTies, Tie tie, Board board) {
        int x = tie.getX();
        int y = tie.getY();

        getClosedLine(x, y, tie.isWhite(), -1, -1, closedTies, board);
        getClosedLine(x, y, tie.isWhite(), 1, -1, closedTies, board);
        getClosedLine(x, y, tie.isWhite(), 1, 1, closedTies, board);
        getClosedLine(x, y, tie.isWhite(), -1, 1, closedTies, board);

        getClosedLine(x, y, tie.isWhite(), 0, -1, closedTies, board);
        getClosedLine(x, y, tie.isWhite(), 0, 1, closedTies, board);
        getClosedLine(x, y, tie.isWhite(), 1, 0, closedTies, board);
        getClosedLine(x, y, tie.isWhite(), -1, 0, closedTies, board);
    }

    private void getClosedLine(int x, int y, boolean isWhite, int forX, int forY, ArrayList<Tie> closedTies, Board b) {
        Tie[][] board = b.getBoard();
        int x_ = x;
        int y_ = y;
        x_ += forX;
        y_ += forY;
        boolean flag = false;
        ArrayList<Tie> tiesToAdd = new ArrayList<>();
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
                    tiesToAdd.add(board[x_][y_]);
                }
                x_ += forX;
                y_ += forY;
            }
        }
        if (flag) {
            closedTies.addAll(tiesToAdd);
        }
    }

    private void evaluateClosedTies(ArrayList<Double> sList, ArrayList<Tie> closedTies) {
        for (Tie tie: closedTies) {
            int x = tie.getX();
            int y = tie.getY();
            if ((Math.min(x, y) == 0 && (Math.max(x, y) == 0 || Math.max(x, y) == 7)) ||
                    (Math.max(x, y) == 7 && (Math.min(x, y) == 0 || Math.min(x, y) == 7))) {
                sList.add(0.8);
            } else if (Math.max(x, y) == 7 || Math.min(x, y) == 0) {
                sList.add(0.4);
            } else {
                sList.add(0.0);
            }
        }
    }

    public boolean canPlay() {
        return canPlay;
    }

    public void setCanPlay(boolean canPlay) {
        this.canPlay = canPlay;
    }

}
