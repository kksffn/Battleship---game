package cz.kksffn.prepare;

import java.util.ArrayList;
import java.util.HashMap;
/*
The class responsible for placing the ships and saving their coordinates.
Control if the placing was successful - getSuccess()
 */
public interface ISetShips {
    HashMap<ShipName, ArrayList<Integer>> getCoordinatesOfShips();
    int setShipCoordinates();
    // Whether the placing of ships was successful:
    boolean getSuccess();
}
