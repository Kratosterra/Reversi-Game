public class AI extends User{
    private boolean isPro;
    private boolean canPlay;

    public AI(String name, boolean isPro) {
        super(name);
        this.isPro = isPro;
    }

    public Tie MakeTurn (Board board) {
        if (isPro) {

        } else {

        }
        return new Tie(this, 1, 1);
    }

    public boolean CanPlay() {
        return canPlay;
    }

    public void setCanPlay(boolean canPlay) {
        this.canPlay = canPlay;
    }

}
