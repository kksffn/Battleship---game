package cz.kksffn.player;

import cz.kksffn.prepare.ISetShips;
import cz.kksffn.prepare.ShipName;

import java.util.ArrayList;
import java.util.HashMap;
/*
This abstract class is implementation of IPlayer. The player has to:
place the ships on the board - sea
store the coordinates of ships (used for referee) - coordinatesOfShips
have the information if he is human - isHuman
 */
public abstract class APlayer implements IPlayer{
    HashMap<ShipName, ArrayList<Integer>> coordinatesOfShips;
    ISetShips sea;
    String name;
    boolean isHuman;

    public abstract HashMap<ShipName, ArrayList<Integer>> getCoordinatesOfShips();
    public abstract int getGuess();

    @Override
    public String getName() {
        return name;
    }

    public boolean isHuman() {
        return isHuman;
    }

    @Override
    public void updateGuesses(String result, int guess) {}
    @Override
    public void updateGuesses(ShipName sunkShip) {}
}
