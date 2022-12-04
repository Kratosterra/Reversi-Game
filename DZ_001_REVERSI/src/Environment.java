import java.util.ArrayList;
import java.util.Scanner;

public class Environment {
    static void getFromUserPossibleMove(Player player, Board board, ArrayList<Tie> turnT) {
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
        board.setTurnWithTie(finalTie, player);
    }

    static void printScore(Player player) {
        System.out.printf("\n-------------------------\nЛучший счет Игрока 1 = %s \n-------------------------\n",
                player.getMaxScore());
    }

    static void printPlayAgainstAiEasyIntro() {
        System.out.print("""

                -----------------------------------------------
                Начинаем игру в легком режиме с компьютером!
                ------------------------------------------------
                """);
    }

    static void printPlayAgainstAiProIntro() {
        System.out.print("""

                -----------------------------------------------
                Начинаем игру в PRO режиме с компьютером!
                -----------------------------------------------
                """);
    }

    static void printPlayerVsPlayerInfo() {
        System.out.print("""

                -----------------------------------------------
                Начинаем игру в режиме Игрок против Игрока!
                -----------------------------------------------
                """);
    }

    static void playerTwoInfo() {
        System.out.println("\n------------------------------------------\n");
        System.out.println("""
                Ход Игрока 2!
                Тип шашек [●]
                □ - возможное место

                ------------------------------------------
                """);
    }

    static void printPlayerOneInfo() {
        System.out.println("\n------------------------------------------\n");
        System.out.println("""
                Ход Игрока 1!
                Тип шашек [◯]
                □ - возможное место

                ------------------------------------------
                """);
    }

    static void printEndOfTurnPlayerOne() {
        System.out.println("\nИгрок 1 завершил ход!\n-----------\n");
    }

    static void printEndOfTurnPlayerTwo() {
        System.out.println("\nИгрок 2 завершил ход!\n-----------\n");
    }

    static void printNoVariants() {
        System.out.println("Вариантов игры нет, пропуск хода!");
    }

    static void printEndBoard(Board board) {
        System.out.println("\n-----------\nКонечная доска:\n-----------\n");
        board.printBoard();
    }

    public static void printIntro() {
        System.out.print("""
                               
                   ░█▀▀█ ░█▀▀▀ ░█──░█ ░█▀▀▀ ░█▀▀█ ░█▀▀▀█ ▀█▀ 　 ░█▀▀█ ─█▀▀█ ░█▀▄▀█ ░█▀▀▀
                   ░█▄▄▀ ░█▀▀▀ ─░█░█─ ░█▀▀▀ ░█▄▄▀ ─▀▀▀▄▄ ░█─ 　 ░█─▄▄ ░█▄▄█ ░█░█░█ ░█▀▀▀
                   ░█─░█ ░█▄▄▄ ──▀▄▀─ ░█▄▄▄ ░█─░█ ░█▄▄▄█ ▄█▄ 　 ░█▄▄█ ░█─░█ ░█──░█ ░█▄▄▄
                \s
                                                  ＲＵＬＥＳ
                   -------------------------------------------------------------------------
                   1) Первый ход делают чёрные. Далее игроки ходят по очереди.
                   
                   2) Делая ход, игрок должен поставить свою фишку на одну из
                      клеток доски таким образом, чтобы между этой поставленной фишкой
                      и одной из имеющихся уже на доске фишек его цвета находился
                      непрерывный ряд фишек соперника,
                      горизонтальный, вертикальный или диагональный.
                      
                   2) Все фишки соперника, входящие в «закрытый» на этом ходу ряд,
                      меняют цвет и переходят к ходившему игроку.
                      
                   3) Если в результате одного хода «закрывается» одновременно более
                      ряда фишек противника, то переворачиваются все фишки в этих рядах.
                      
                   4) Если игрок имеет возможные ходы, он не может отказаться от хода.
                   
                   5) Если игрок не имеет допустимых ходов, то ход передается сопернику.
                   
                   6) Игра прекращается, когда на доску выставлены все фишки или
                      когда ни один из игроков не может сделать хода.
                      
                   7) Игрок, чьих фишек на доске выставлено больше, объявляется победителем.
                      В случае равенства количества фишек засчитывается ничья.
                   -------------------------------------------------------------------------
                """);
    }

    public static void printMenu() {
        System.out.print("""

                -------------------------------------------------------
                                      𝙶𝙰𝙼𝙴 𝙼𝙴𝙽𝚄
                -------------------------------------------------------
                
                [1] Играть против компьютера [easy] | ｡ﾟ( ﾟ^∀^ﾟ)ﾟ｡
                [2] Играть против компьютера [pro]  | (ノಠ益ಠ)ノ彡┻━┻
                [3] Игрок против Игрока             | ＼(＾∀＾)メ(＾∀＾)ノ
                [4] Cчёт                            | (￣^￣)ゞ
                [5] Выйти                           | (°▽°)/
                
                Введите число:\s""");
    }

    static void printEndOfTurnAi() {
        System.out.println("\nКомпьютер завершил ход!\n-----------\n");
    }

    static void printAiInfo() {
        System.out.println("\n------------------------------------------\n");
        System.out.println("Ход компьютера!\nТип шашек [●]\n-----------\n");
    }

    static void exit() {
        System.out.println("Я не знаю такой команды, введите число от 1 до 5!\n");
    }

    static void outro() {
        System.out.println("""
                        Завершаем игру.
                        ----------------------------------------------------------------------------------------------------
                        ██████████████████████████████████████████████████████████████████████████████████████████████████▀█
                        █─▄─▄─█─█─██▀▄─██▄─▀█▄─▄█▄─█─▄█─▄▄▄▄███▄─▄▄─█─▄▄─█▄─▄▄▀███▄─▄▄─█▄─▄████▀▄─██▄─█─▄█▄─▄█▄─▀█▄─▄█─▄▄▄▄█
                        ███─███─▄─██─▀─███─█▄▀─███─▄▀██▄▄▄▄─████─▄███─██─██─▄─▄████─▄▄▄██─██▀██─▀─███▄─▄███─███─█▄▀─██─██▄─█
                        ▀▀▄▄▄▀▀▄▀▄▀▄▄▀▄▄▀▄▄▄▀▀▄▄▀▄▄▀▄▄▀▄▄▄▄▄▀▀▀▄▄▄▀▀▀▄▄▄▄▀▄▄▀▄▄▀▀▀▄▄▄▀▀▀▄▄▄▄▄▀▄▄▀▄▄▀▀▄▄▄▀▀▄▄▄▀▄▄▄▀▀▄▄▀▄▄▄▄▄▀
                        ----------------------------------------------------------------------------------------------------
                        \s""");
    }
}