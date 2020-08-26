package cz.kksffn.player;

import java.util.ArrayList;
import java.util.HashMap;
import cz.kksffn.game.CommunicationWithUser;
import cz.kksffn.prepare.SetShipsInConsole;
import cz.kksffn.prepare.ShipName;

/*
The class represents a human player.
It constructs a player with
a name - setName()
a map of ships - sea = SetShipsInConsole().

The coordinates of ships are to use of referee - public getCoordinatesOfShips()
The referee asks player for guessing - public getGuess()
 */
public class Human extends APlayer {


    public Human() {
        this.isHuman = true;
        this.name = setName();
        this.sea = new SetShipsInConsole(setShips());
        this.coordinatesOfShips = sea.getCoordinatesOfShips();
    }
    private boolean setShips() {
        System.out.println("Do you want to set ships manually?\n" +
                "\t\t\"yes\" if you would like to set ships manually.\n" +
                "\t\t\"no\" if you want me to do it for you.");
        String yesNo = CommunicationWithUser.inputYesNo();
        return yesNo.equals("yes");
    }
    private String setName() {
        String input ="";
        while (input.equals("")){
            System.out.print("What's your name? ");
            input = CommunicationWithUser.inputString();
        }
        return input;
    }
    @Override
    public HashMap<ShipName, ArrayList<Integer>> getCoordinatesOfShips() {
        return coordinatesOfShips;
    }
    @Override
    public int getGuess() {
        return CommunicationWithUser.inputCoordinates();
    }
}

