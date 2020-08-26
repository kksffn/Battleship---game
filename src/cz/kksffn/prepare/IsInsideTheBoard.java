package cz.kksffn.prepare;

import java.util.ArrayList;

public class IsInsideTheBoard {


    private IsInsideTheBoard() {
    }


    //      Checking if the ships are inside the map:
    public static boolean yacht(ArrayList<Integer> yachtXY, boolean isHuman) {
        if ((yachtXY.get(0) - yachtXY.get(1) == 1 && (yachtXY.get(1) %10 == 9 || yachtXY.get(0)%10 == 0)) ||
                (yachtXY.get(1) - yachtXY.get(0) ==10 && (yachtXY.get(0) > 89 || yachtXY.get(1)<10))){  // On the edge
            if (isHuman){
                System.out.println("I can't set the ship here. I have to ask you for new coordinates.");
            }
            return false;
        }else {
            return true;
        }
    }
    public static boolean patrol(ArrayList<Integer> patrolXY, boolean isHuman) {
        if ( ((patrolXY.get(0) - patrolXY.get(2)) == 2 && (patrolXY.get(0)%10<2 || patrolXY.get(2)%10>7)) ||
                ((patrolXY.get(2) - patrolXY.get(0))==20 && (patrolXY.get(0) >79 ||patrolXY.get(2)<20))){ //not inside
            if (isHuman){
                System.out.println("I can't set the ship here. I have to ask you for new coordinates.");
            }
            return false;
        }else {
            return true;
        }
    }
    public static boolean frigate(ArrayList<Integer> frigateXY, int choice, boolean isHuman) {
        boolean checkIt = true;
        switch (choice){
            case 1:
            case 3:
            case 2:
                if (frigateXY.get(0) % 10 < 2 || frigateXY.get(0) > 89){
                    checkIt = false;
                }
                break;
            case 4:
                if (frigateXY.get(0) % 10 < 2 || frigateXY.get(0) < 12){
                    checkIt = false;
                }
                break;
            case 5:
            case 7:
            case 8:
                if (frigateXY.get(0) % 10 == 0 || frigateXY.get(0) > 79){
                    checkIt = false;
                }
                break;
            case 6:
                if (frigateXY.get(0) % 10 == 0 || frigateXY.get(0) <21){
                    checkIt = false;
                }
                break;
        }
        if (isHuman && !checkIt){
            System.out.println("The ship doesn't fit into the board. Try again.");
        }
        return checkIt;
    }
    public static boolean carrier(ArrayList<Integer> carrierXY, boolean isHuman) {
        if (carrierXY.get(0) % 10 < 2 || carrierXY.get(0) < 10 || carrierXY.get(0) > 89){
            if (isHuman){
                System.out.println("The ship doesn't fit into the board. Try again.");
            }
            return false;
        }else{
            return true;
        }
    }
    public static boolean battleship(ArrayList<Integer> battleshipXY, boolean isHuman) {
        if ((battleshipXY.get(0) % 10 == 0) || (battleshipXY.get(0) > 89)){
            if (isHuman){
                System.out.println("The ship doesn't fit into the board. Try again.");
            }
            return false;
        }else{
            return true;
        }
    }
    public static boolean cruiser(ArrayList<Integer> cruiserXY, int choice, boolean isHuman) {
        boolean checkIt = true;
        switch (choice) {
            case 1:
                if (cruiserXY.get(0) %10<2 || cruiserXY.get(0) >89){
                    checkIt =false;
                }
                break;
            case 2:
                if(cruiserXY.get(0) %10<2 || cruiserXY.get(0) <10){
                    checkIt =false;
                }
                break;
            case 3:
                if(cruiserXY.get(0) %10==0 || cruiserXY.get(0) >79){
                    checkIt =false;
                }
                break;
            case 4:
                if(cruiserXY.get(0) %10==0 || cruiserXY.get(0) <10 || cruiserXY.get(0) >89){
                    checkIt =false;
                }
                break;
        }
        if (isHuman && !checkIt){
            System.out.println("I can't set the ship here. I have to ask you for new coordinates.");
        }
        return checkIt;
    }
}
