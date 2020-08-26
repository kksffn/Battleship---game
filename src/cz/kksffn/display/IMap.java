package cz.kksffn.display;

import java.util.ArrayList;
/*
The class is responsible for displaying the maps for player. It has to show where the ships are
and hits and misses of the player and his opponent.
 */
public interface IMap {

    void setShips(ArrayList<Integer> pole);
    void setMiss(int cell);
    void setHit(int cell);

}
