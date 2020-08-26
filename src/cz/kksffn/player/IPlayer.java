package cz.kksffn.player;

import java.util.ArrayList;
import java.util.HashMap;
import cz.kksffn.prepare.ShipName;
/*
This interface represents the player, who:
has ships placed somewhere - stored for the referee - getCoordinatesOfShips()
has a name - getName()
has an information if he is human or computer - isHuman()
keeps information of his guesses and numbers of cells where he doesn't want to shoot - updateGuesses()
            - THIS IS USED FOR COMPUTER ONLY -  for its strategy
 */
public interface IPlayer {


    HashMap<ShipName, ArrayList<Integer>> getCoordinatesOfShips();
    int getGuess();
    String getName();
    boolean isHuman();
    void updateGuesses(String result, int guess);
    void updateGuesses(ShipName sunkShip);


}
