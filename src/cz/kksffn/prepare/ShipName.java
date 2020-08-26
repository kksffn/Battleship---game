package cz.kksffn.prepare;

public enum ShipName {

    YACHT("\n\t|O|O|\n"),
    PATROL("\n\t|O|O|O|\n"),
    FRIGATE("\n\t|O|O|O|\n\t|O|\n"),
    BATTLESHIP("\n\t|O|O|\n\t|O|O|\n"),
    CARRIER("\n\t  |O|\n\t|O|O|O|\n\t  |O|\n"),
    CRUISER("\n\t|O|O|\n\t  |O|O|\n");
    private final String showIt;

    ShipName(String s) {
        this.showIt=s;
    }

    @Override
    public String toString() {
        return showIt;
    }
}
