package cz.kksffn.prepare;


import cz.kksffn.display.IMap;
import java.util.ArrayList;
import java.util.HashMap;
/*
This abstract class implementsISetShips.
It should remember the coordinates of ships - HashMap coordinatesOfShips
It has a map of ships - it's used for human to display where the ships are - IMap map
It has an information whether the placing of ships was successful - boolean success (sometimes it's not sucessful
for computer
 */
public abstract class ASetShips implements ISetShips {

    HashMap<ShipName, ArrayList<Integer>> coordinatesOfShips = new HashMap<>();
    IMap map;
    boolean success = true;


    // Placing the ships:
    abstract void instructions();
    abstract void sailOutYacht();
    abstract void sailOutPatrol();
    abstract void sailOutFrigate();
    abstract void sailOutBattleship();
    abstract void sailOutCarrier();
    abstract void sailOutCruiser();


    //    Updating the list of ships:
    abstract void actualizeCoordinatesOfShips(ArrayList<Integer> coordinates, ShipName ship);
    //    Getting the coordinates of ships for players and the referee of the cz.kksffn.game:
    public abstract HashMap<ShipName, ArrayList<Integer>> getCoordinatesOfShips();
    // Whether the placing of ships was successful:
    public abstract boolean getSuccess();


}
