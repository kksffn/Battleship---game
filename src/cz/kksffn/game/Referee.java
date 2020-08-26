package cz.kksffn.game;

import java.util.ArrayList;
import java.util.HashMap;

import cz.kksffn.display.IMap;
import cz.kksffn.display.MapInConsole;
import cz.kksffn.player.AI;
import cz.kksffn.player.Human;
import cz.kksffn.player.IPlayer;
import cz.kksffn.prepare.ShipName;
/*
The class is responsible for the control of the game. It

prepares the players and their ships - prepareGame()
switches the player's turns - gameLoop()
looks after not guessing the same cells twice - alreadyGuessed()
checks if the guess is hit, mis or sunk, informs the players and displays it - checkHit(), display(), isSunk(), sink()
ends the game - shows the winner and asks whether the player would like to play another game - endGame()


The cells are stored as int[] from 0 to 100
The guessed cells are stored in (ArrayList) player1Guesses and player2Guesses
The ships with their coordinates: (HashMap) player1Ships, player2Ships - ShipName and the array int[]
The hit/miss is recognized by comparison of the guessed cell with player1Ships.values() - if "hit", then the cell
        is removed from  player1Ships.values()
        If the values for a ship are empty, the ship is sunk --> removed
If the HashMap is empty ==> the player won, he sunk all of the opponent ships

 */
public class Referee {
    private HashMap<ShipName, ArrayList<Integer>> player1Ships;
    private HashMap<ShipName, ArrayList<Integer>> player2Ships;

    private boolean player1Move = true;
    private int player1Guess;
    private int player2Guess;
    private ArrayList<Integer> player1Guesses = new ArrayList<>();
    private ArrayList<Integer> player2Guesses = new ArrayList<>();

    private IPlayer player1;
    private IPlayer player2;

    private int numberOfMoves = 0;
    private IMap mapOfPlayer1Ships = new MapInConsole();
    private IMap mapOfPlayersGuesses = new MapInConsole();

    public Referee(int whichGame) {
        prepareGame(whichGame);
        gameLoop(player1Ships, player2Ships);
        displayBoards();
        endGame(player2Ships);
    }
    private void prepareGame(int whichGame) {
        if (whichGame == 1){
            this.player1 = new Human();
            this.player2 = new AI();
        }else if (whichGame == 2){
            this.player1 = new AI();
            this.player2 = new AI();
        }
        this.player1Ships = this.player1.getCoordinatesOfShips();
        this.player2Ships = this.player2.getCoordinatesOfShips();
        for (ShipName ship : player1Ships.keySet()){
            this.mapOfPlayer1Ships.setShips(player1Ships.get(ship));
        }
    }

    private void gameLoop(HashMap<ShipName, ArrayList<Integer>> player1Ships, HashMap<ShipName, ArrayList<Integer>> player2Ships) {
        while (!(player1Ships.isEmpty() || player2Ships.isEmpty())){
            if (player1Move){
                displayBoards();
                player1Guess = player1.getGuess();
                if (alreadyGuessed(player1Guess, player1Guesses)){
                    if (player1.isHuman()){
                        System.out.println("You have already guessed this cell. Try again.\n");
                    }
                }else{
                    numberOfMoves++;
                    player1Guesses.add(player1Guess);
                    checkHit(player1Guess, player2Ships);
                    player1Move = false;
                }
            }else{
                player2Guess = player2.getGuess();
                if (!alreadyGuessed(player2Guess, player2Guesses)) {
                    System.out.println("\t\t\t\t\t\t" + player2.getName() + " is shooting .........." +
                            "ABCDEFGHIJ".charAt(player2Guess / 10) + (player2Guess % 10 + 1) + ".");
                    player2Guesses.add(player2Guess);
                    checkHit(player2Guess, player1Ships);
                    player1Move = true;
                }
            }
        }
    }
    private void displayBoards() {
        System.out.println("Maps for " + player1.getName() +":");
        System.out.println(mapOfPlayer1Ships.toString());
        System.out.println(mapOfPlayersGuesses.toString());
    }
    private boolean alreadyGuessed(int Guess, ArrayList<Integer> Guesses) {
        int count = 0;
        for (Integer i : Guesses){
            if (Guess == i){
                count ++;
                break;
            }
        }
        return count > 0;
    }

    private void checkHit(int guess, HashMap<ShipName, ArrayList<Integer>> ships) {
        int count =0;
        boolean sunk = false;
        ShipName sunkShip = null;
        for (ShipName ship : ships.keySet()){
            if (count > 0){                     //Loď byla trefená, dál neprocházej.
                break;
            }
            for (int i = 0; i < ships.get(ship).size();i++){
                if (guess == ships.get(ship).get(i)) {
                    count++;
                    ships.get(ship).remove(ships.get(ship).get(i));   // Trefené části odstraníme.
                    sunkShip = ship;
                    if (isSunk(ship, ships)) {
                        sunk = true;            // Pokud loď vymažu tady, dostanu ConcurrentModificationException!
                        sunkShip = ship;
                        break;
                    }
                    break;
                }
            }
        }
        if (count == 0){display("MISS", guess);
        }else if (count > 0){display("HIT", guess);}
        if (sunk){
            sink(sunkShip, ships);
        }
    }
    private void display(String result, int guess){
        if(player1Move){
            if (!player1.isHuman()) {
                System.out.println(player1.getName() + " is shooting ...... "
                        + "ABCDEFGHIJ".charAt(guess / 10) + (guess % 10 + 1) + "..." + result);
            }else{
                System.out.println(result);
            }
            if (result.equals("MISS")){
                mapOfPlayersGuesses.setMiss(guess);
            }else if (result.equals("HIT")){
                mapOfPlayersGuesses.setHit(guess);
            }
            player1.updateGuesses(result, guess);
        }else{
            System.out.println("\t\t\t\t\t\t" + result);
            if (result.equals("MISS")){
                mapOfPlayer1Ships.setMiss(guess);
            }else if (result.equals("HIT")){
                mapOfPlayer1Ships.setHit(guess);
            }
            player2.updateGuesses(result, guess);
        }
    }
    private boolean isSunk(ShipName ship, HashMap<ShipName, ArrayList<Integer>> ships) {
        return (ships.get(ship).size() == 0);
    }
    private void sink(ShipName sunkShip, HashMap<ShipName, ArrayList<Integer>> ships) {
        if (player1Move && !player1.isHuman()){
            player1.updateGuesses(sunkShip);
        }else if(!player1Move){
            player2.updateGuesses(sunkShip);
        }
        ships.remove(sunkShip);
        System.out.println("\t\t\t\tAND THE SHIP IS SUNK!");
        System.out.println("There" +
                ((ships.size()==1)? " is 1 ship" : " are " +
                        ( (ships.size()==0)? "no ships" :
                         + ships.size() + " ships")) + " remaining.");
    }

    private void endGame(HashMap<ShipName, ArrayList<Integer>> player2Ships) {
        System.out.println("GAME OVER. There are no ships remaining. " +
                "It took " + (numberOfMoves) +" moves.");
        System.out.println("\n\n\n\n\n \t\t\tAnd the winner is: \n\n" +
                (player2Ships.isEmpty() ? "\t\t\t\t YOU, " + player1.getName()
                        : "\t\t\t\t  " + player2.getName()) + "." );
    }
}
