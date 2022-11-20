public class AI extends User{
    private boolean isPro;
    private boolean canPlay = true;

    public AI(String name, boolean isPro) {
        super(name);
        this.isPro = isPro;
    }

    public Tie MakeTurn (Board board) {
        if (isPro) {

        } else {

        }
        return null;
    }

    public boolean CanPlay() {
        return canPlay;
    }

    public void setCanPlay(boolean canPlay) {
        this.canPlay = canPlay;
    }

}
