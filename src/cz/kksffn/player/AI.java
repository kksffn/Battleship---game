package cz.kksffn.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import cz.kksffn.prepare.SetShipsInConsole;
import cz.kksffn.prepare.ShipName;
/*
The class represents a computer player.
The computer has its strategy for guessing - IStrategy think

It constructs a player with
a name - setName()
a map of ships - SetShipsInConsole(). - This was not successful sometimes it may
    be repeated in constructor. - sea = SetShipsInConsole()

The coordinates of ships are to use of referee - public getCoordinatesOfShips()
The referee asks computer for guessing - public getGuess()
The referee informs the computer if its guess was successful or not or the ship is sunk - updateGuesses()
 */
public class AI extends APlayer {
    private IStrategy think = new Strategy();

    public AI() {
        this.name = setName();
        this.isHuman = false;
        this.sea = new SetShipsInConsole(false);
        while (!sea.getSuccess()){
            this.sea = new SetShipsInConsole(false);
        }
        this.coordinatesOfShips = sea.getCoordinatesOfShips();
    }
    private String setName() {
        String[] names = {"Pepa", "Jean", "Adéla", "John", "Karel", "IBM 100", "Franta", "Alois",
        "Ďurica", "Olča", "Lenka", "Rostislav", "Lukáš", "Jana", "Jane", "Shaun", "Agáta", "Marie", "Eva",
        "Dana", "Oskar", "Ivan", "Kamila", "Jan", "Natálie", "Oleg", "Kvído", "Jaroslav", "Stanislav",
        "Hana", "Rudolf", "Herbert", "Rudolf"};
        Random rand = new Random();
        return names[rand.nextInt(names.length)];
    }
    @Override
    public HashMap<ShipName, ArrayList<Integer>> getCoordinatesOfShips() {
        return coordinatesOfShips;
    }
    @Override
    public int getGuess() {return think.findCoordinates();}
    @Override
    public void updateGuesses(String result, int guess) {
        think.updateGuesses(result, guess);
    }
    public void updateGuesses(ShipName sunkShip){
        think.updateGuesses(sunkShip);
    }
}
