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
            num = in.nextInt();
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
        System.out.print("\n-------------------------------------\n              " +
                "Меню\n-------------------------------------\n" +
                "1) Играть против компьютера (easy)\n2) Играть против компьютера (pro)" +
                "\n3) Игрок против Игрока\n4) Cчет\n5) Выйти\nВведите число: ");
    }

    public static void PlayAgainstAi(Player player, boolean isPro) {
        AI ai;
        if (isPro) {
            System.out.print("\n-------------------------------------\nНачинаем игру в PRO режиме с компьютером!" +
                    "\n-------------------------------------\n");
            ai = new AI("AI", true);
        } else {
            System.out.print("\n-------------------------------------\nНачинаем игру в легком режиме с компьютером!" +
                    "\n-------------------------------------\n");
            ai = new AI("AI", false);
        }
        System.out.println("\n-----------\nТекущая доска:\n-----------\n");
        Board board = new Board(player, ai);
        int turn = 1;
        while (board.IsPlayable() && (player.CanPlay() || ai.CanPlay())) {
            board.PrintBoard();
            if (turn == 1) {
                System.out.println("Ход Игрока 1!\nТип шашек [◯]\n□ - возможное место\n-----------\n");
                board.PrintBoardWithPossibleTurns(player);
                int[][] tiesPlace = board.GetPossiblePlaces(player);
                ArrayList<Tie> turnT = new ArrayList<Tie>();
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (tiesPlace[i][j] == 1) {
                            turnT.add(new Tie(player, j, i));
                        }
                    }
                }
                if (!player.CanPlay() || turnT.size() == 0) {
                    System.out.println("Вариантов игры нет, пропуск хода!");
                    turn = 2;
                    continue;
                }
                System.out.println("Возможные ходы:");
                int n = 1;
                for (Tie t: turnT) {
                    System.out.printf("%s) Шашка ", n);
                    System.out.printf("( x = %s | ", t.getX() + 1);
                    System.out.printf("y = %s )\n", t.getY() + 1);
                    n++;
                }
                Scanner inputTie = new Scanner(System.in);
                int in = 0;
                while (in <= 0 || in > n) {
                    System.out.print("Введите число: ");
                    in = inputTie.nextInt();
                }
                Tie finalTie = turnT.get(in-1);
                System.out.println("Устанавливаем шашку!");
                board.SetTurnWithTie(finalTie);
                System.out.println("\nИгрок 1 завершил ход!\n-----------\n");
                turn = 2;
            } else {
                System.out.println("Ход компьютера!\nТип шашек [●]\n-----------\n");
                Tie tie = ai.MakeTurn(board);
                if (tie == null) {
                    ai.setCanPlay(false);
                    System.out.println("Вариантов игры нет, пропуск хода!");
                } else {
                    board.SetTurnWithTie(tie);
                }
                System.out.println("\nКомпьютер завершил ход!\n-----------\n");
                turn = 1;
            }

        }
        board.PrintWinner();
        if (player.getMaxScore() < board.getScore_user()) {
            player.setMaxScore(board.getScore_user());
        }
    }

    public static void PlayerVSPlayer(Player player) {

    }

    public static void Score(Player player) {
        System.out.printf("\n-------------------------\nЛучший счет = %s \n-------------------------\n", player.getMaxScore());
    }
}