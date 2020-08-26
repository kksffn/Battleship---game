package cz.kksffn.game;

import java.util.Scanner;
import static java.lang.Integer.parseInt;
/*
The class is responsible for communication with user in console. It has to accept
integers (rotation of the ships, ...),
strings (player's name)
yes/no
coordinates of ships and guesses
It has private constructor for not to allow instances of the class
 */

public class CommunicationWithUser {
    private static Scanner sc = new Scanner(System.in);;
    private CommunicationWithUser() {}

    public static String inputString(){return sc.nextLine();}
    public static String inputYesNo(){
        String yesNo;
        do {
            System.out.print("\t\t\t Input yes/no: ");
            yesNo = sc.nextLine().trim();
        } while (!yesNo.toLowerCase().equals("yes") && !yesNo.toLowerCase().equals("no"));
        return yesNo;
    }
    public static int inputInt(){
        int number;
        String s;
        while (true){
            try{
                System.out.print("\t\t\tInput number, please:");
                s = sc.nextLine().trim();
                number = parseInt(s);
                break;
            }catch (Exception e){
            }
        }
        return number;
    }
    public static int inputCoordinates() {
        String regex="[a-jA-J]0?(([0]?[1-9])|(10))";
        System.out.print("Input coordinates. Allowed forms are: a1, a01, A1, A01,...:  ");

        String sour =sc.nextLine().trim();
        while (!sour.matches(regex)) {
            System.out.println("I don't understand your input. Input the coordinates once more, please.");
            sour = sc.nextLine().trim();
        }
        return parseInt(String.valueOf(Character.toLowerCase(sour.charAt(0)) - 'a'))*10 +
                (parseInt(sour.substring(1))-1);
    }
}
