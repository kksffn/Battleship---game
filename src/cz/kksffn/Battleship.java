package cz.kksffn;

import cz.kksffn.game.CommunicationWithUser;
import cz.kksffn.game.Referee;

/*
The application class - main(String[] args)
provides some information about the game,
asks user to input whether he wants to play or he wants to see the gae between two computers and
starts the game.
After the game it asks whether the player wants to play another game - playAnotherGame()
 */

public class Battleship {

    public static void main(String[] args){
        System.out.println("\n\nLet's play the game BATTLESHIP.\nIt's the game for two players. Each player places " +
                "his ships on the grid (10x10). The ships can't touch each other. You don't know where the opponent's " +
                "ships are.\n" +
                "Your GOAL is to sink all of your opponent's ships.\n" +
                "You call out grid coordinates - this is your shot. Your opponent says \"HIT\" or \"MISS\" and he has to " +
                "tell you, if the ship is sunk.\n\n");
        System.out.println("Press enter to continue.");
        CommunicationWithUser.inputString();
        int whichGame;
        System.out.println("Do you want to:\n" +
                "\tplay against computer, or\t\t             (Input 1)\n" +
                "\twatch the game between two computers?\t\t (Input 2)");
        whichGame = CommunicationWithUser.inputInt()%2;
        if (whichGame == 1){new Referee(1);}
        else {new Referee(2);}
        playAnotherGame();
    }

    private static void playAnotherGame() {
        System.out.println("\n\n\n\n\n\n\nDo you want to play another game?");
        String anotherGame = CommunicationWithUser.inputYesNo();
        if (anotherGame.equals("yes")){
            main(new String[]{""});
        }
    }
}

