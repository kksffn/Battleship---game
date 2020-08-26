package cz.kksffn.player;

import cz.kksffn.display.MapInConsole;
import cz.kksffn.prepare.ShipName;
import java.util.ArrayList;

public class ProbabilityCounting{

    private ArrayList<Integer> probability = new ArrayList<>(100);
    private ArrayList<Integer> allGuesses;
    private ArrayList<ShipName> sunkShips;


    public ProbabilityCounting(ArrayList<Integer> guesses, ArrayList<ShipName> sunkShips){
        this.allGuesses = guesses;
        this.sunkShips = sunkShips;
        for (int i =0; i<100;i++){
            probability.add(i, 0);
        }
    }

    public int getMyGuess() {
        setProbability();
//        MapInConsole mapOfProbability = new MapInConsole();
//        mapOfProbability.showProbability(probability);

        return getGuess();
    }

    private void setProbability() {
        if (!sunkShips.contains(ShipName.YACHT)){
            tryYacht();
        }
        if (!sunkShips.contains(ShipName.PATROL)){
            tryPatrol();
        }
        if (!sunkShips.contains(ShipName.FRIGATE)){
            tryFrigate();
        }
        if (!sunkShips.contains(ShipName.BATTLESHIP)){
            tryBattleship();
        }
        if (!sunkShips.contains(ShipName.CARRIER)){
            tryCarrier();
        }
        if (!sunkShips.contains(ShipName.CRUISER)){
            tryCruiser();
        }
    }

    private void tryFrigate() {
        for (int i=2; i<90; i++){
            if (i%10 >= 2){
                if (!allGuesses.contains(i) && !allGuesses.contains(i-1) &&
                        !allGuesses.contains(i-2) &&!allGuesses.contains(i+8)){
                    updateProbability(i);
                    updateProbability(i-1);
                    updateProbability(i-2);
                    updateProbability(i+8);
                }
                if (!allGuesses.contains(i) && !allGuesses.contains(i+10) &&
                        !allGuesses.contains(i+9) &&!allGuesses.contains(i+8)){
                    updateProbability(i);
                    updateProbability(i+10);
                    updateProbability(i+9);
                    updateProbability(i+8);
                }
                if (!allGuesses.contains(i) && !allGuesses.contains(i+10) &&
                        !allGuesses.contains(i-2) &&!allGuesses.contains(i-1)){
                    updateProbability(i);
                    updateProbability(i-1);
                    updateProbability(i-2);
                    updateProbability(i+10);
                }
                if (!allGuesses.contains(i+10) && !allGuesses.contains(i+9) &&
                        !allGuesses.contains(i-2) &&!allGuesses.contains(i+8)){
                    updateProbability(i+10);
                    updateProbability(i+9);
                    updateProbability(i-2);
                    updateProbability(i+8);
                }
            }
        }
        for (int i=1; i<80; i++){
            if (i%10 != 0){
                if (!allGuesses.contains(i) && !allGuesses.contains(i+10) &&
                        !allGuesses.contains(i+20) &&!allGuesses.contains(i-1)){
                    updateProbability(i);
                    updateProbability(i+10);
                    updateProbability(i+20);
                    updateProbability(i-1);
                }
                if (!allGuesses.contains(i) && !allGuesses.contains(i+9) &&
                        !allGuesses.contains(i+19) &&!allGuesses.contains(i-1)){
                    updateProbability(i);
                    updateProbability(i+9);
                    updateProbability(i+19);
                    updateProbability(i-1);
                }
                if (!allGuesses.contains(i) && !allGuesses.contains(i+10) &&
                        !allGuesses.contains(i+20) &&!allGuesses.contains(i+19)){
                    updateProbability(i);
                    updateProbability(i+10);
                    updateProbability(i+20);
                    updateProbability(i+19);
                }
                if (!allGuesses.contains(i+20) && !allGuesses.contains(i+19) &&
                        !allGuesses.contains(i+9) &&!allGuesses.contains(i-1)){
                    updateProbability(i-1);
                    updateProbability(i+9);
                    updateProbability(i+19);
                    updateProbability(i+20);
                }
            }
        }
    }

    private void tryCruiser() {
        for (int i=0; i<90; i++){
            if (i%10 >= 2 &&
                    !allGuesses.contains(i) &&
                    !allGuesses.contains(i-1) &&
                    !allGuesses.contains(i+8) &&
                    !allGuesses.contains(i+9)){
                updateProbability(i);
                updateProbability(i-1);
                updateProbability(i+8);
                updateProbability(i+9);
            }
        }
        for (int i=10; i<100; i++){
            if (i%10 >= 2 &&
                    !allGuesses.contains(i) &&
                    !allGuesses.contains(i-1) &&
                    !allGuesses.contains(i-11) &&
                    !allGuesses.contains(i-12)){
                updateProbability(i);
                updateProbability(i-1);
                updateProbability(i-11);
                updateProbability(i-12);
            }
        }
        for (int i=0; i<80; i++){
            if (i%10 != 0 &&
                    !allGuesses.contains(i) &&
                    !allGuesses.contains(i+10) &&
                    !allGuesses.contains(i+9) &&
                    !allGuesses.contains(i+19)){
                updateProbability(i);
                updateProbability(i+10);
                updateProbability(i+19);
                updateProbability(i+9);
            }
        }
        for (int i=10; i<90; i++){
            if (i%10 != 0 &&
                    !allGuesses.contains(i) &&
                    !allGuesses.contains(i-1) &&
                    !allGuesses.contains(i-11) &&
                    !allGuesses.contains(i+10)){
                updateProbability(i);
                updateProbability(i-1);
                updateProbability(i-11);
                updateProbability(i+10);
            }
        }

    }
    private void tryBattleship() {
        for (int i =0; i<90; i++){
            if (i%10!=0 &&
                    !allGuesses.contains(i) &&
                    !allGuesses.contains(i+10) &&
                    !allGuesses.contains(i-1) &&
                    !allGuesses.contains(i+9)){
                updateProbability(i);
                updateProbability(i-1);
                updateProbability(i+10);
                updateProbability(i+9);
            }
        }
    }
    private void tryCarrier() {
        for (int i=0; i<100; i++){
            if (i>9 && i<90 && i%10!=0 && i%10!=1 &&
                    !allGuesses.contains(i) &&
                    !allGuesses.contains(i-1) &&
                    !allGuesses.contains(i-2) &&
                    !allGuesses.contains(i-11) &&
                    !allGuesses.contains(i+9)){
                updateProbability(i);
                updateProbability(i-1);
                updateProbability(i-2);
                updateProbability(i-11);
                updateProbability(i+9);
            }
        }
    }
    private void tryPatrol() {
        for (int i =0; i<80; i++){
            if (!allGuesses.contains(i) && !allGuesses.contains(i+10)&& !allGuesses.contains(i+20)){
                updateProbability(i);
                updateProbability(i+10);
                updateProbability(i+20);
            }
        }
        for (int i = 0; i<100; i++ ){
            if (i%10!=0 && i%10!=1 &&
                    !allGuesses.contains(i) && !allGuesses.contains(i-1) && !allGuesses.contains(i-2)){
                updateProbability(i);
                updateProbability(i-1);
                updateProbability(i-2);
            }
        }
    }
    private void tryYacht() {
        for (int i =0; i<90; i++){
            if (!allGuesses.contains(i) && !allGuesses.contains(i+10)){
                updateProbability(i);
                updateProbability(i+10);
            }
        }
        for (int i = 0; i<100; i++ ){
            if (i%10!=0 && !allGuesses.contains(i) && !allGuesses.contains(i-1)){
                updateProbability(i);
                updateProbability(i-1);
            }
        }
    }

    private void updateProbability(int cell) {
        int prob = probability.get(cell);
        prob++;
        probability.set(cell, prob);
    }

    private int getGuess() {
        int guess = -1;
        int k=0;
        for (int i = 0; i<100; i++){
            if (probability.get(i)>k){
                guess = i;
                k = probability.get(i);
            }
        }
        return guess;
    }
}
