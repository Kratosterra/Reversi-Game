
public class Main {
    public static void main(String[] args) {
        Player player = new Player("Player 1");
        Environment.printIntro();
        Game.menu(player);
    }
}