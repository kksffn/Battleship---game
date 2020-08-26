package cz.kksffn.player;

import cz.kksffn.prepare.ShipName;


/*
This interface represents "the brain" of computer.
It guesses the cell (not randomly if the ships is not sunk and some of the last guesses were hits)  - findCoordinates()
To do so it has to store and update "forbidden" cells - the places not to guess -
        besides other things based on information from referee - updateGuesses()
 */
public interface IStrategy {

    int findCoordinates();
    void updateGuesses(String result, int guess);
    void updateGuesses(ShipName sunkShip);

}
