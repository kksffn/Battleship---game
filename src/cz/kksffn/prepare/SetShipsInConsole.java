package cz.kksffn.prepare;

import cz.kksffn.display.IMap;
import cz.kksffn.display.MapInConsole;
import cz.kksffn.game.CommunicationWithUser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/*
The class is responsible for placing the ships in console. It's quite complicated - the shapes of ships are complicated,
it controls the rules - whether the placement of each ship is possible, whether the sip doesn't collide with another one-
    ...IsInsideTheBoard(), notCollision(), inputInCollision(),
    updates the forbidden cells - actualizeForbiddenXY()
It saves the coordinates of each cell for each ship - actualizeCoordinatesOfShips()
    and enables the player to get the coordinates - getCoordinatesOfShips()
The placement takes place n several steps: sailOut"TheNameOfShip"() gets the first cell of the ship -
        if the player is human it provides some information and communicates with him - getChoiceForYacht()
        after getting the first cell it set the other ones depending on the shape and the
        direction (rotation) of the ship - trySailOut"TheNameOfShip"()
For computer player (and for the human if he wishes) it places the ships randomly - this was not successful
    every 28 times average so I add the method getSuccess to find this out and let the computer set ships once more
 */

public class SetShipsInConsole extends ASetShips{

    private HashMap<ShipName, ArrayList<Integer>> coordinatesOfShips = new HashMap<>();
    private ArrayList<Integer> forbiddenXY = new ArrayList<>();
    private IMap map = new MapInConsole();
    private final boolean isHuman;
    private Random rand = new Random();
    private boolean success = true;

    public SetShipsInConsole(boolean isHuman) {
        this.isHuman = isHuman;

        instructions();

        sailOutYacht();
        sailOutPatrol();
        sailOutFrigate();
        sailOutBattleship();
        sailOutCarrier();
        sailOutCruiser();
    }
    void instructions() {
        if (isHuman){
            System.out.println("First you are to set six ships on the board. They have to be set not to touch each other" +
                    "(nor in the corner).\nYou can rotate or upend them. \n" +
                    "The ships are:");
            System.out.println(ShipName.YACHT.toString() +
                    ShipName.PATROL.toString() +
                    ShipName.FRIGATE.toString() +
                    ShipName.BATTLESHIP.toString() +
                    ShipName.CARRIER.toString() +
                    ShipName.CRUISER.toString());
            System.out.println(map.toString());
            }
    }

    //      Places the ships on the board:
    void sailOutYacht() {
        int choice;
        if (isHuman){
            choice = getChoiceForYacht();
        }else{choice = rand.nextInt(2) + 1;}

        while (true){
            ArrayList<Integer> yachtXY = new ArrayList<Integer>();
            yachtXY.add(0, setShipCoordinates());
            if (choice == 2) {
                yachtXY.add(1, yachtXY.get(0) - 1);
            } else {
                yachtXY.add(1, yachtXY.get(0) + 10);
            }
            if (IsInsideTheBoard.yacht(yachtXY, isHuman)) {
                actualizeForbiddenXY(yachtXY);
                actualizeCoordinatesOfShips(yachtXY, ShipName.YACHT);
                break;
            }
        }
    }
    private int getChoiceForYacht() {
        int choice;
        System.out.println("Let's set the most easy ship. Choose the rotation of the ship:");
        System.out.println("\t\t|O|O|\t\t|O|");
        System.out.println("\t\t     \t\t|O|");
        System.out.println("\t\t (1)        (2)");
        choice = CommunicationWithUser.inputInt();
        if (choice < 1 || choice > 2) {
            choice = choice % 2;
            if (choice < 1) {
                choice = 2;
            }
            System.out.printf("I don't understand. I chose the shape %d.\n", choice);
        }
        System.out.println("I need coordinates of the cell which is on the top or on the left side the most.");
        return choice;
    }
    void sailOutPatrol() {
        int choice;
        if (isHuman){
            choice = getChoiceForPatrol();
        }else{choice = rand.nextInt(2) + 1;}

        while (true){
            ArrayList<Integer> patrolXY = new ArrayList<Integer>();
            patrolXY.add(0, setShipCoordinates());
            if (choice == 2) {
                patrolXY.add(1, patrolXY.get(0) - 1);
                patrolXY.add(2, patrolXY.get(0) - 2);
            } else {
                patrolXY.add(1, patrolXY.get(0) + 10);
                patrolXY.add(2, patrolXY.get(0) + 20);
            }
            if (IsInsideTheBoard.patrol(patrolXY, isHuman) && notCollision(patrolXY)) {
                actualizeForbiddenXY(patrolXY);
                actualizeCoordinatesOfShips(patrolXY, ShipName.PATROL);
                break;
            }
        }
    }
    private int getChoiceForPatrol() {
        int choice;
        System.out.println(map.toString());
        System.out.println("Let's set another ship. Choose the rotation of the ship:");
        System.out.println("\t\t|O|O|O|\t\t|O|");
        System.out.println("\t\t       \t\t|O|");
        System.out.println("\t\t       \t\t|O|");
        System.out.println("\t\t  (1)      (2)");
        choice = CommunicationWithUser.inputInt();
        if (choice < 1 || choice > 2) {
            choice = choice % 2;
            if (choice < 1) {
                choice = 2;
            }
            System.out.printf("I don't understand. I chose the shape %d.", choice);
        }
        System.out.println("I need coordinates of the cell which is on the top or on the left side the most.");
        return choice;
    }
    void sailOutFrigate() {
        int choice;
        int firstCellXY;

        if (isHuman){
            choice = getChoiceForFrigate();
        }else{
            choice = rand.nextInt(8)+1;
        }
        while (true){
            firstCellXY = setShipCoordinates();
            ArrayList<Integer> frigateXY = new ArrayList<>();
            frigateXY = trySailOutFrigate(firstCellXY, choice);
            if (IsInsideTheBoard.frigate(frigateXY, choice, isHuman) && notCollision(frigateXY)){
                actualizeForbiddenXY(frigateXY);
                actualizeCoordinatesOfShips(frigateXY, ShipName.FRIGATE);
                break;
            }
        }
    }
    private int getChoiceForFrigate() {
        System.out.println(map.toString());
        int choice;
        System.out.println("Let's set another ship. Choose the rotation of the ship:\n"
                + "\t|O|   |O|O|  |O|O|    |O|                                        \n"
                + "\t|O|     |O|  |O|      |O|  |O|O|O|      |O|  |O|      |O|O|O|   \n"
                + "\t|O|O|   |O|  |O|    |O|O|  |O|      |O|O|O|  |O|O|O|      |O|   \n"
                + "\t (1)   (2)    (3)    (4)     (5)      (6)      (7)      (8)      \n");

        choice = CommunicationWithUser.inputInt();
        if (choice < 1 || choice > 8){
            choice = choice%8;
            if (choice<1){choice = 8;}
            System.out.printf("I don't understand. I chose the shape %d.", choice);
        }
        System.out.println("I need coordinates of the cell which is on the top (and on the left side the most " +
                "if necessary).");
        return choice;
    }
    private ArrayList<Integer> trySailOutFrigate(int coord, int choice) {
        ArrayList<Integer> frigateXY = new ArrayList<Integer>();
        frigateXY.add(0, coord);
        switch (choice) {
            case 1 -> {
                frigateXY.add(1, frigateXY.get(0) - 1);
                frigateXY.add(2, frigateXY.get(0) - 2);
                frigateXY.add(3, frigateXY.get(0) + 8);
            }
            case 2 -> {
                frigateXY.add(1, frigateXY.get(0) + 10);
                frigateXY.add(2, frigateXY.get(0) + 9);
                frigateXY.add(3, frigateXY.get(0) + 8);
            }
            case 3 -> {
                frigateXY.add(1, frigateXY.get(0) + 10);
                frigateXY.add(2, frigateXY.get(0) - 1);
                frigateXY.add(3, frigateXY.get(0) - 2);
            }
            case 4 -> {
                frigateXY.add(1, frigateXY.get(0) - 1);
                frigateXY.add(2, frigateXY.get(0) - 2);
                frigateXY.add(3, frigateXY.get(0) - 12);
            }
            case 5 -> {
                frigateXY.add(1, frigateXY.get(0) - 1);
                frigateXY.add(2, frigateXY.get(0) + 10);
                frigateXY.add(3, frigateXY.get(0) + 20);
            }
            case 6 -> {
                frigateXY.add(1, frigateXY.get(0) - 1);
                frigateXY.add(2, frigateXY.get(0) - 11);
                frigateXY.add(3, frigateXY.get(0) - 21);
            }
            case 7 -> {
                frigateXY.add(1, frigateXY.get(0) - 1);
                frigateXY.add(2, frigateXY.get(0) + 9);
                frigateXY.add(3, frigateXY.get(0) + 19);
            }
            case 8 -> {
                frigateXY.add(1, frigateXY.get(0) + 10);
                frigateXY.add(2, frigateXY.get(0) + 20);
                frigateXY.add(3, frigateXY.get(0) + 19);
            }
        }
        return frigateXY;
    }
    void sailOutBattleship() {
        int firstCellXY;
        if (isHuman){
            informationForBattleship();
        }
        while (true){
            firstCellXY = setShipCoordinates();
            ArrayList<Integer> battleshipXY;
            battleshipXY = trySailOutBattleship(firstCellXY);
            if (IsInsideTheBoard.battleship(battleshipXY, isHuman) && notCollision(battleshipXY)){
                actualizeForbiddenXY(battleshipXY);
                actualizeCoordinatesOfShips(battleshipXY, ShipName.BATTLESHIP);
                break;
            }
        }
    }
    private void informationForBattleship() {
        System.out.println(map.toString());
        System.out.println("Let's set another ship. Input the coordinates of left upper cell."
                + ShipName.BATTLESHIP.toString());        
    }
    private ArrayList<Integer> trySailOutBattleship(int coord) {
        ArrayList<Integer> battleshipXY = new ArrayList<Integer>();
        battleshipXY.add(0, coord);
        battleshipXY.add(1, battleshipXY.get(0) + 10);
        battleshipXY.add(2, battleshipXY.get(0) - 1);
        battleshipXY.add(3, battleshipXY.get(0) + 9);
        return battleshipXY;
    }
    void sailOutCarrier() {
        int firstCellXY;
        if (isHuman){informationForCarrier();}
        int count = 0;
        while (true) {
            count++;
            firstCellXY = setShipCoordinates();
            ArrayList<Integer> carrierXY;
            carrierXY = trySailOutCarrier(firstCellXY);
            if (IsInsideTheBoard.carrier(carrierXY, isHuman) && notCollision(carrierXY)){
                actualizeForbiddenXY(carrierXY);
                actualizeCoordinatesOfShips(carrierXY, ShipName.CARRIER);
                break;
            }
            if (count>=1_000_000){//From time to time the computer can't place the ships, so it will get another chance
                success = false;
                break;
            }
        }

    }
    private void informationForCarrier() {
        System.out.println(map.toString());
        System.out.println("Let's set another ship. Input the coordinates of the upper cell."
                + ShipName.CARRIER.toString());
    }
    private ArrayList<Integer> trySailOutCarrier(int coord) {
        ArrayList<Integer> carrierXY = new ArrayList<Integer>();
        carrierXY.add(0, coord);
        carrierXY.add(1, carrierXY.get(0) - 1);
        carrierXY.add(2, carrierXY.get(0) - 2);
        carrierXY.add(3, carrierXY.get(0) + 9);
        carrierXY.add(4, carrierXY.get(0) - 11);
        return carrierXY;
    }
    void sailOutCruiser() {
        int choice;
        int firstCellXY;
        if (isHuman){
            choice = getChoiceForCruiser();
        }else{choice = rand.nextInt(4)+1;}
        int count = 0;
        while (true){
            count++;
            firstCellXY = setShipCoordinates();
            ArrayList<Integer> cruiserXY;
            cruiserXY = trySailOutCruiser(firstCellXY, choice);
            if (IsInsideTheBoard.cruiser(cruiserXY, choice, isHuman) && notCollision(cruiserXY)){
                actualizeForbiddenXY(cruiserXY);
                actualizeCoordinatesOfShips(cruiserXY, ShipName.CRUISER);
                break;
            }
            if (count>=1_000_000){ //From time to time the computer can't place the ships, so it will get another chance
                success = false;
                break;
            }
        }
    }

    @Override
    public boolean getSuccess() {
        return success;
    }
    private int getChoiceForCruiser() {
        System.out.println(map.toString());
        int choice;
        System.out.println("Let's set the last ship. Choose the rotation of the ship:\n" +
                "\t|O|      |O|                            \n" +
                "\t|O|O|  |O|O|  |O|O|      |O|O|            \n" +
                "\t  |O|  |O|      |O|O|  |O|O|             \n" +
                "\t (1)     (2)     (3)     (4)             \n");
        choice = CommunicationWithUser.inputInt();
        if (choice < 1 || choice > 4) {
            choice = choice % 4;
            if (choice < 1) {
                choice = 4;
            }
            System.out.printf("I don't understand. I chose the shape %d.", choice);
        }
        System.out.println("I need coordinates of the cell which is on the top (and on the left side the most " +
                "if necessary).");
        return choice;
    }
    private ArrayList<Integer> trySailOutCruiser(int coord, int choice) {
        ArrayList<Integer> cruiserXY = new ArrayList<Integer>();
        cruiserXY.add(0, coord);
        switch (choice) {
            case 1 -> {
                cruiserXY.add(1, cruiserXY.get(0) - 1);
                cruiserXY.add(2, cruiserXY.get(0) + 9);
                cruiserXY.add(3, cruiserXY.get(0) + 8);
            }
            case 2 -> {
                cruiserXY.add(1, cruiserXY.get(0) - 1);
                cruiserXY.add(2, cruiserXY.get(0) - 11);
                cruiserXY.add(3, cruiserXY.get(0) - 12);
            }
            case 3 -> {
                cruiserXY.add(1, cruiserXY.get(0) + 10);
                cruiserXY.add(2, cruiserXY.get(0) + 9);
                cruiserXY.add(3, cruiserXY.get(0) + 19);
            }
            case 4 -> {
                cruiserXY.add(1, cruiserXY.get(0) - 1);
                cruiserXY.add(2, cruiserXY.get(0) - 11);
                cruiserXY.add(3, cruiserXY.get(0) + 10);
            }
        }
        return cruiserXY;
    }

    //      Checking the collisions of the ships:
    private boolean notCollision(ArrayList<Integer> auxiliaryXY) {
        if (!inputInCollisions(auxiliaryXY)) {
            return false;
        } else {
            return true;
        }
    }
    private boolean inputInCollisions(ArrayList<Integer> whereTheShipIs) {
        boolean canIt = true;
        for (int i : whereTheShipIs){
            for (int j : forbiddenXY){
                if (i==j){
                    if (isHuman){
                        int row = i%10;
                        int col = i/10;
                        char xCoords = "ABCDEFGHIJ".charAt(col);
                        System.out.println("Collision on " + xCoords + (row+1) +"!");
                    }
                    canIt =false;
                    break;
                }
            }
        }
        return canIt;
    }

//      Checking if the ships are inside the map:
    //    Updating the list of ships:
    void actualizeCoordinatesOfShips(ArrayList<Integer> coordinates, ShipName ship) {
        coordinatesOfShips.put(ship, coordinates);
        map.setShips(coordinatesOfShips.get(ship));
    }
    //      Updating the cells where other ships can't be.
    private void actualizeForbiddenXY(ArrayList<Integer> whereTheShipIs) {
        for (int i : whereTheShipIs) {
            forbiddenXY.add(i);
            if (i % 10 != 9) {
                forbiddenXY.add(i + 1);
            }
            if (i % 10 != 0) {
                forbiddenXY.add(i - 1);
            }
            if (i > 9) {
                forbiddenXY.add(i - 10);
                if (i % 10 != 9) {
                    forbiddenXY.add(i - 9);
                }
                if (i % 10 != 0) {
                    forbiddenXY.add(i - 11);
                }
            }
            if (i < 90) {
                forbiddenXY.add(i + 10);
                if (i % 10 != 9) {
                    forbiddenXY.add(i + 11);
                }
                if (i % 10 != 0) {
                    forbiddenXY.add(i + 9);
                }
            }
        }
        for (int i = 1; i<forbiddenXY.size(); i++) {
            for (int j = 0; j < i; j++) {
                if (forbiddenXY.get(j).equals(forbiddenXY.get(i))) {
                    forbiddenXY.remove(j);      //Vymaž duplicitní položky.
                    i--;        //Snižuji indexy, položky se totiž posunuly
                    j--;
                }
            }
        }
    }

    public int setShipCoordinates() {
        int coordinate;
        if (isHuman){
            coordinate = CommunicationWithUser.inputCoordinates();
        }else {
            coordinate = rand.nextInt(100);
        }
        return coordinate;
    }
    @Override
    public HashMap<ShipName, ArrayList<Integer>> getCoordinatesOfShips() {
        return this.coordinatesOfShips;
    }
}
