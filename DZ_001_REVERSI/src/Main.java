import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Player player = new Player("Player 1");
        Game.PrintIntro();
        Game.Menu(player);
    }
}