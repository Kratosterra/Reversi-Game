import java.util.ArrayList;

public class AI extends User{

    static private final double MIN_R = -10.0;

    private final boolean isPro;
    private boolean canPlay = true;

    public AI(String name, boolean isPro) {
        super(name);
        this.isPro = isPro;
    }

    public Tie makeTurn(Board board, User player) {
        Tie finaleTie;
        if (isPro) {
            finaleTie = getFinaleTiePro(board, player);
        } else {
            finaleTie = getFinaleTieEasy(board);
        }
        return finaleTie;
    }

    private Tie getFinaleTieEasy(Board board) {
        Tie finaleTie = null;
        double maxR = MIN_R;
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
            double r = getInfoForClosedTies(board, point);
            if (r > maxR) {
                maxR = r;
                finaleTie = point;
            }
        }
        return finaleTie;
    }

    private double getInfoForClosedTies(Board board, Tie point) {
        ArrayList<Tie> closedTies = new ArrayList<>();
        getClosedTies(closedTies, point, board);
        ArrayList<Double> sList = new ArrayList<>();
        evaluateClosedTies(sList, closedTies);
        return getSumOf(sList) + evaluateTie(point);
    }

    private Tie getFinaleTiePro(Board board, User player) {
        Tie finaleTie = null;
        double maxR = MIN_R;
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
            double r = getInfoForClosedTies(board, point);
            double rPlayer = getEnemyFunctionR(board, point, player);
            r -= rPlayer;
            if (r > maxR) {
                maxR = r;
                finaleTie = point;
            }
        }
        return finaleTie;
    }

    private double getEnemyFunctionR(Board oldBoard, Tie point, User player) {
        Board newBoard = new Board(player, this);
        Tie[][] newTie = new Tie[8][8];
        Tie[][] bBoard = oldBoard.getBoard();
        for (int i = 0; i < bBoard.length; i++) {
            for (int j = 0; j < bBoard[i].length; j++) {
                if (bBoard[i][j] != null) {
                    newTie[i][j] =
                            new Tie(bBoard[i][j].getMaster(), bBoard[i][j].getX(), bBoard[i][j].getY());
                }
            }
        }
        newBoard.setBoard(newTie);
        newBoard.setTurnWithTie(point, this);
        double maxR = -1.0;
        ArrayList<Tie> listOfPossibleTurns = new ArrayList<>();
        int[][] ties = newBoard.getPossiblePlaces(player);
        for (int i = 0; i < ties.length; i++) {
            for (int j = 0; j < ties[i].length; j++) {
                if (ties[i][j] == 1) {
                    listOfPossibleTurns.add(new Tie(player, i, j));
                }
            }
        }
        for (Tie tie: listOfPossibleTurns) {
            double r = getInfoForClosedTies(newBoard, tie);
            if (r > maxR) {
                maxR = r;
            }
        }
        return maxR;
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

    private void getClosedLine(int x, int y, boolean isWhite, int offsetX, int offsetY, ArrayList<Tie> closedTies,
                               Board boardNow) {
        Tie[][] board = boardNow.getBoard();
        int x_ = x;
        int y_ = y;
        x_ += offsetX;
        y_ += offsetY;
        boolean isAllyFound = false;
        ArrayList<Tie> tiesToAdd = new ArrayList<>();
        if (x_ >= 0 && y_ >= 0 && x_ < 8 && y_ < 8) {
            while (x_ >= 0 && y_ >= 0 && x_ < 8 && y_ < 8) {
                if (board[x_][y_] == null) {
                    return;
                }
                if (board[x_][y_].isWhite() == isWhite) {
                    isAllyFound = true;
                    break;
                }
                if (board[x_][y_].isWhite() == !isWhite) {
                    tiesToAdd.add(board[x_][y_]);
                }
                x_ += offsetX;
                y_ += offsetY;
            }
        }
        if (isAllyFound) {
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