import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Player player = new Player("Player 1");
        int num = 0;
        PrintIntro();
        while (num != 5) {
            PrintMenu();
            try {
                num = in.nextInt();
            } catch (Exception e) {
                in.next();
                num = 0;
            }
            player.setCanPlay(true);
            switch (num) {
                case (1) -> PlayAgainstAi(player, false);
                case (2) -> PlayAgainstAi(player, true);
                case (3) -> PlayerVSPlayer(player);
                case (4) -> Score(player);
                case (5) -> System.out.println("Завершаем игру");
                default -> System.out.println("Я не знаю такой команды, введите число от 1 до 5!\n");
            }
        }

    }

    public static void PrintIntro() {
        System.out.print("\n-------------------------------------\nИгра Reversi!");
    }

    public static void PrintMenu() {
        System.out.print("""

                -------------------------------------
                              Меню
                -------------------------------------
                1) Играть против компьютера (easy)
                2) Играть против компьютера (pro)
                3) Игрок против Игрока
                4) Cчёт
                5) Выйти
                Введите число:\s""");
    }

    public static void PlayAgainstAi(Player player, boolean isPro) {
        AI ai;
        if (isPro) {
            System.out.print("""

                    -----------------------------------------------
                    Начинаем игру в PRO режиме с компьютером!
                    -----------------------------------------------
                    """);
            ai = new AI("AI", true);
        } else {
            System.out.print("""

                    -----------------------------------------------
                    Начинаем игру в легком режиме с компьютером!
                    ------------------------------------------------
                    """);
            ai = new AI("AI", false);
        }

        Board board = new Board(player, ai);
        int turn = 1;
        while (board.isPlayable() && (player.canPlay() || ai.canPlay())) {
            if (turn == 1) {
                turn = MakeTurnPlayer(player, board);
            } else {
                turn = MakeTurnAi(player, ai, board);
            }

        }
        System.out.println("\n-----------\nКонечная доска:\n-----------\n");
        board.printBoard();
        board.printWinner(ai);
        if (player.getMaxScore() < board.getScoreUser()) {
            player.setMaxScore(board.getScoreUser());
        }

    }

    public static void PlayerVSPlayer(Player player) {
        Player player_2 = new Player("Player 2");
        Board board = new Board(player, player_2);
        System.out.print("""

                -----------------------------------------------
                Начинаем игру в режиме Игрок против Игрока!
                -----------------------------------------------
                """);
        int turn = 1;
        while (board.isPlayable() && (player.canPlay() || player_2.canPlay())) {
            if (turn == 1) {
               turn = MakeTurnPlayer(player, board);
            } else {
                turn = MakeTurnPlayer2(player_2, board);
            }
        }
        System.out.println("\n-----------\nКонечная доска:\n-----------\n");
        board.printBoard();
        board.printWinner(player_2);
        if (player.getMaxScore() < board.getScoreUser()) {
            player.setMaxScore(board.getScoreUser());
        }
    }

    public static void Score(Player player) {
        System.out.printf("\n-------------------------\nЛучший счет Игрока 1 = %s \n-------------------------\n", player.getMaxScore());
    }

    private static int MakeTurnAi(Player player, AI ai, Board board) {
        int turn;
        System.out.println("\n------------------------------------------\n");
        System.out.println("Ход компьютера!\nТип шашек [●]\n-----------\n");
        Tie tie = ai.makeTurn(board, player);
        if (tie == null) {
            System.out.println("Вариантов игры нет, пропуск хода!");
            ai.setCanPlay(false);
        } else {
            board.setTurnWithTie(tie, ai);
            ai.setCanPlay(true);
        }
        System.out.println("\nКомпьютер завершил ход!\n-----------\n");
        turn = 1;
        return turn;
    }

    private static int MakeTurnPlayer(Player player, Board board) {
        int turn;
        System.out.println("\n------------------------------------------\n");
        System.out.println("""
                Ход Игрока 1!
                Тип шашек [◯]
                □ - возможное место

                ------------------------------------------
                """);
        ArrayList<Tie> turnT = getPossibleMovesForGame(player, board);
        if (turnT.size() == 0) {
            System.out.println("Вариантов игры нет, пропуск хода!");
            player.setCanPlay(false);
        } else {
            getFromUserPossibleMove(player, board, turnT);
            System.out.println("\nИгрок 1 завершил ход!\n-----------\n");
            player.setCanPlay(true);
        }
        turn = 2;
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

    private static void getFromUserPossibleMove(Player player, Board board, ArrayList<Tie> turnT) {
        System.out.println("Возможные ходы:");
        int n = 1;
        for (Tie t: turnT) {
            System.out.printf("%s) Шашка ", n);
            System.out.printf("( x = %s | ", t.getY() + 1);
            System.out.printf("y = %s )\n", t.getX() + 1);
            n++;
        }
        Scanner inputTie = new Scanner(System.in);
        int in = 0;
        while (in <= 0 || in > n-1) {
            System.out.print("Введите число: ");
            try {
                in = inputTie.nextInt();
            } catch (Exception e) {
                inputTie.next();
                in = 0;
            }
        }
        Tie finalTie = turnT.get(in-1);
        System.out.println("Устанавливаем шашку!");
        board.setTurnWithTie(finalTie, player);
    }


    private static int MakeTurnPlayer2(Player player_2, Board board) {
        int turn;
        System.out.println("\n------------------------------------------\n");
        System.out.println("""
                Ход Игрока 2!
                Тип шашек [●]
                □ - возможное место

                ------------------------------------------
                """);
        ArrayList<Tie> turnT = getPossibleMovesForGame(player_2, board);
        if (turnT.size() == 0) {
            System.out.println("Вариантов игры нет, пропуск хода!");
            player_2.setCanPlay(false);
        } else {
            getFromUserPossibleMove(player_2, board, turnT);
            System.out.println("\nИгрок 2 завершил ход!\n-----------\n");
            player_2.setCanPlay(true);
        }
        turn = 1;
        return turn;
    }
}