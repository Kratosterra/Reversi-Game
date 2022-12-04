import java.util.ArrayList;
import java.util.Scanner;

public class Game {

    static void menu(Player player) {
        Scanner in = new Scanner(System.in);
        int num = 0;
        while (num != 5) {
            Environment.printMenu();
            try {
                num = in.nextInt();
            } catch (Exception e) {
                in.next();
                num = 0;
            }
            player.setCanPlay(true);
            switch (num) {
                case (1) -> playAgainstAi(player, false);
                case (2) -> playAgainstAi(player, true);
                case (3) -> playerVSPlayer(player);
                case (4) -> score(player);
                case (5) -> Environment.outro();
                default -> Environment.exit();
            }
        }
    }

    public static void playAgainstAi(Player player, boolean isPro) {
        AI ai;
        if (isPro) {
            Environment.printPlayAgainstAiProIntro();
            ai = new AI("AI", true);
        } else {
            Environment.printPlayAgainstAiEasyIntro();
            ai = new AI("AI", false);
        }

        Board board = new Board(player, ai);
        int turn = 1;
        while (board.isPlayable() && (player.canPlay() || ai.canPlay())) {
            if (turn == 1) {
                turn = makeTurnPlayerOne(player, board);
            } else {
                turn = makeTurnAi(player, ai, board);
            }

        }
        Environment.printEndBoard(board);
        board.printWinner(ai);
        if (player.getMaxScore() < board.getScoreUser()) {
            player.setMaxScore(board.getScoreUser());
        }

    }

    public static void playerVSPlayer(Player player) {
        Player player_2 = new Player("Player 2");
        Board board = new Board(player, player_2);
        Environment.printPlayerVsPlayerInfo();
        int turn = 1;
        while (board.isPlayable() && (player.canPlay() || player_2.canPlay())) {
            if (turn == 1) {
               turn = makeTurnPlayerOne(player, board);
            } else {
                turn = makeTurnPlayerTwo(player_2, board);
            }
        }
        Environment.printEndBoard(board);
        board.printWinner(player_2);
        if (player.getMaxScore() < board.getScoreUser()) {
            player.setMaxScore(board.getScoreUser());
        }
    }

    public static void score(Player player) {
        Environment.printScore(player);
    }

    private static int makeTurnAi(Player player, AI ai, Board board) {
        int turn;
        Environment.printAiInfo();
        Tie tie = ai.makeTurn(board, player);
        if (tie == null) {
            Environment.printNoVariants();
            ai.setCanPlay(false);
        } else {
            board.setTurnWithTie(tie, ai);
            ai.setCanPlay(true);
        }
        Environment.printEndOfTurnAi();
        turn = 1;
        return turn;
    }

    private static int makeTurnPlayerOne(Player player, Board board) {
        int turn;
        Environment.printPlayerOneInfo();
        ArrayList<Tie> turnT = getPossibleMovesForGame(player, board);
        if (turnT.size() == 0) {
            Environment.printNoVariants();
            player.setCanPlay(false);
        } else {
            Environment.getFromUserPossibleMove(player, board, turnT);
            Environment.printEndOfTurnPlayerOne();
            player.setCanPlay(true);
        }
        turn = 2;
        return turn;
    }

    private static int makeTurnPlayerTwo(Player playerTwo, Board board) {
        int turn;
        Environment.playerTwoInfo();
        ArrayList<Tie> turnT = getPossibleMovesForGame(playerTwo, board);
        if (turnT.size() == 0) {
            Environment.printNoVariants();
            playerTwo.setCanPlay(false);
        } else {
            Environment.getFromUserPossibleMove(playerTwo, board, turnT);
            Environment.printEndOfTurnPlayerTwo();
            playerTwo.setCanPlay(true);
        }
        turn = 1;
        return turn;
    }

    private static ArrayList<Tie> getPossibleMovesForGame(Player player, Board board) {
        board.printBoardWithPossibleTurns(player);
        int[][] tiesPlace = board.getPossiblePlaces(player);
        ArrayList<Tie> turnT = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (tiesPlace[i][j] == 1) {
                    turnT.add(new Tie(player, i, j));
                }
            }
        }
        return turnT;
    }
}
