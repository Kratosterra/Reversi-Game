import java.util.Objects;

public class Tie {
    private User master;
    private int x;
    private int y;

    private boolean isWhite;

    public Tie(User master, int x, int y) {
        this.master = master;
        this.x = x;
        this.y = y;
        if ((master instanceof AI) || (Objects.equals(master.getName(), "Player 2"))) {
            this.isWhite = true;
        }
    }

    public void setNewMaster(User master) {
        this.isWhite = (master instanceof AI) || (Objects.equals(master.getName(), "Player 2"));
        this.master = master;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public User getMaster() {
        return master;
    }

    public boolean isWhite() {
        return isWhite;
    }

    @Override
    public String toString() {
        if (isWhite) {
            return "●";
        } else {
            return "◯";
        }
    }
}
