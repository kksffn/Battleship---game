package cz.kksffn.player;

import cz.kksffn.prepare.ShipName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
/*
The class implements IStrategy.
The average number of guesses for computer to finish the game is 50; for human player it was about 47...
It could and should be improved

Quite messy class...

myGuess - the cell where the computer shoots
lastHit - the cell of the last hit
allGuesses - not to repeat the guess - it's updated based on:
    already guessed cells
    sunk ships (not to shoot next to it and not to shoot nonsenses),
allHits - for updating allGuesses
lastHits - to find out where to shoot next
numberOfNotRandomMoves - not to shoot randomly if lastHits is not empty and the ship is not sunk
sunkShips - the list of sunk ships - to make guesses more clever

It guesses the coordinates - findCoordinates()
If it sunk the patrol, the frigate and the carrier it shouldn't guess third cell in a row - doNotGuessTheThird()
If it guessed three in a row it shouldn't guess fourth in a row - doNotGuessFourth()
    and if it hit four times it should try to sunk carrier... - tryToSnkCarrier()
If it guessed three in a shape of corner - thinkOfWhatNotToGuess():
    the behavior of the computer depends on what ships are sunk - IT ALWAYS UPDATES THE CELLS WHERE IT WILL NOT
    TRY TO SHOOT DEPENDING ON WHAT SHIPS IT HAS ALREADY SUNK
If the last guess was hit it will shoot next to this cell - it has 4 tries - tryNearestCells()
If the ship is not sunk and it doesn't have cells next to the actual hit where to shoot
    it chooses the first of its last hits - shootNextToFirstHitAgain()

If none of above is true it will shoot randomly
 */
public class Strategy implements IStrategy{
    private Random rand = new Random();
    private int myGuess;   // Pole, kam bude střílet
    private int lastHit = -1;   //Poslední hit

    private ArrayList<Integer> allGuesses = new ArrayList<>();      // Co všechno už hádal
    private int numberOfNotRandomMoves = 0;


    private ArrayList<Integer> allHits = new ArrayList<>();
    private ArrayList<Integer> lastHits = new ArrayList<>(5);        //Poslední hity
    private ArrayList<ShipName> sunkShips = new ArrayList<>(6);  //Pole k ukládání potopených lodí
    private int myChances = 0;

    @Override
    public int findCoordinates() {
        do{
            if (lastHits.size()>=2 && sunkShips.contains(ShipName.PATROL)
                    && sunkShips.contains(ShipName.FRIGATE)
                    && sunkShips.contains(ShipName.CARRIER)){
                doNotGuessThird();      //Don't guess third cell in a row!
            }
            if (lastHits.size()>=3){
                if (lastHits.size()==4){
                    tryToSinkCarrier();
                }
                doNotGuessFourth();             //Don't guess forth cell in a row!
                thinkOfWhatNotToGuess();        //If last hits have the shape of corner.
            }
            //AI nepotopil loď, ale už neví, kam má mířit:
            if (!lastHits.isEmpty() && numberOfNotRandomMoves == 0){shootNextToFirstHitAgain();}
            //AI: Pokud se naposled trefil, bude teď střílet okolo trefeného místa:
            tryNearestCells();
            //AI: Pokud zadal novou souřadnici, vystřelí:
        }while (alreadyGuessed(myGuess, allGuesses));
        return myGuess;
    }

    private void tryToSinkCarrier() {
        ArrayList<Integer> help = (ArrayList<Integer>) lastHits.clone();
        Collections.sort(help);
        int first = help.get(0);
        int second = help.get(1);
        int third = help.get(2);
        int fourth = help.get(3);
        if ((third-second == 1) && (fourth-third == 1) && (first == third - 10)) {
            addToGuesses(second-10);
            addToGuesses(fourth-10);
            if (isNotOnTheRightEdge(second)){
                addToGuesses(second + 10);
                addToGuesses(fourth + 10);
            }
            if (isNotOnTheLeftEdge(first)){
                addToGuesses(first-10);
            }
            if (isNotOnTheTop(fourth)){
                addToGuesses(fourth+1);
            }
            if (isNotOnBottom(second)){
                addToGuesses(second-1);
            }
        }
        if ((second-first == 1) && (third-second == 1) && (fourth == second + 10)) {
            addToGuesses(first+10);
            addToGuesses(third+10);
            if (isNotOnTheLeftEdge(first)){
                addToGuesses(first - 10);
                addToGuesses(third - 10);
            }
            if (isNotOnTheRightEdge(fourth)){
                addToGuesses(fourth+10);
            }
            if (isNotOnBottom(first)){
                addToGuesses(first-1);
            }
            if (isNotOnTheTop(third)){
                addToGuesses(third+1);
            }
        }
        if ((third-first == 10) && (fourth - third == 10) && third == second + 1) {
            addToGuesses(first-1);
            addToGuesses(fourth-1);
            if (isNotOnTheTop(first)){
                addToGuesses(first + 1);
                addToGuesses(fourth+ 1);
            }
            if (isNotOnBottom(second)){
                addToGuesses(second-1);
            }
            if (isNotOnTheLeftEdge(first)){
                addToGuesses(first-10);
            }
            if (isNotOnTheRightEdge(fourth)){
                addToGuesses(fourth+10);
            }
        }
        if ((second-first == 10) && (fourth-second == 10) && third == second + 1) {
            addToGuesses(first+1);
            addToGuesses(fourth+1);
            if (isNotOnBottom(first)){
                addToGuesses(first - 1);
                addToGuesses(fourth-1);
            }
            if (isNotOnTheTop(third)){
                addToGuesses(third+1);
            }
            if (isNotOnTheLeftEdge(first)){
                addToGuesses(first-10);
            }
            if (isNotOnTheRightEdge(fourth)){
                addToGuesses(fourth+10);
            }
        }
    }
    private void thinkOfWhatNotToGuess() {      //This is mess
        ArrayList<Integer> help = (ArrayList<Integer>) lastHits.clone();
        Collections.sort(help);
        for (int i = 0; i < (help.size() - 2); i++) {
            int max = help.get(i + 2);
            int center = help.get(i + 1);
            int min = help.get(i);
            if (max-center==9 && center-min==1){
                if (sunkShips.contains(ShipName.CARRIER)){
                    if (isNotOnTheLeftEdge(min)){
                        addToGuesses(min - 10);
                    }
                    if (isNotOnBottom(min)){
                        addToGuesses(min - 1);
                    }
                }
                if (sunkShips.contains(ShipName.BATTLESHIP)){
                    addToGuesses(max+1);
                }
                if (sunkShips.contains(ShipName.CRUISER)){
                    if (isNotOnTheLeftEdge(min)){
                        addToGuesses(center - 10);
                    }
                    if (isNotOnBottom(min)){
                        addToGuesses(max - 1);
                    }
                }
                if (sunkShips.contains(ShipName.FRIGATE)){
                    if (isNotOnTheTop(center)){
                        addToGuesses(center+1);
                    }
                    if (isNotOnTheRightEdge(max)){
                        addToGuesses(max+10);
                    }
                }
            }
            if ((max-center==10) && center-min==1){
                if (sunkShips.contains(ShipName.CARRIER)){
                    if (isNotOnTheLeftEdge(min)){
                        addToGuesses(center - 10);
                    }
                    if (isNotOnTheTop(min)){
                        addToGuesses(center +1);
                    }
                }
                if (sunkShips.contains(ShipName.BATTLESHIP)){
                    addToGuesses(max-1);
                }
                if (sunkShips.contains(ShipName.CRUISER)){
                    if (isNotOnTheLeftEdge(center)){
                        addToGuesses(min - 10);
                    }
                    if (isNotOnTheTop(center)){
                        addToGuesses(max + 1);
                    }
                }
                if (sunkShips.contains(ShipName.FRIGATE)){
                    if (isNotOnTheRightEdge(max)){
                        addToGuesses(max + 10);
                    }
                    if (isNotOnBottom(min)){
                        addToGuesses(min - 1);
                    }
                }
            }
            if (max-center==1 && center-min==10){
                if (sunkShips.contains(ShipName.CARRIER)){
                    if (isNotOnTheRightEdge(center)){
                        addToGuesses(center + 10);
                    }
                    if (isNotOnBottom(center)){
                        addToGuesses(center - 1);
                    }
                }
                if (sunkShips.contains(ShipName.BATTLESHIP)){
                    addToGuesses(max-10);
                }
                if (sunkShips.contains(ShipName.CRUISER)){
                    if (isNotOnTheRightEdge(center)){
                        addToGuesses(max+10);
                    }
                    if (isNotOnBottom(center)){
                        addToGuesses(min-1);
                    }
                }
                if (sunkShips.contains(ShipName.FRIGATE)){
                    if (isNotOnTheTop(max)){
                        addToGuesses(max + 1);
                    }
                    if (isNotOnTheLeftEdge(min)){
                        addToGuesses(min - 10);
                    }
                }
            }
            if (max-center==1 && center-min==9){
                if (sunkShips.contains(ShipName.CARRIER)){
                    if (isNotOnTheRightEdge(max)){
                        addToGuesses(max+10);
                    }
                    if (isNotOnTheTop(max)){
                        addToGuesses(max+1);
                    }
                }
                if (sunkShips.contains(ShipName.BATTLESHIP)){
                    addToGuesses(min-1);
                }
                if (sunkShips.contains(ShipName.CRUISER)){
                    if (isNotOnTheRightEdge(max)){
                        addToGuesses(center + 10);
                    }
                    if (isNotOnTheTop(max)){
                        addToGuesses(min + 1);
                    }
                }
                if (sunkShips.contains(ShipName.FRIGATE)){
                    if (isNotOnBottom(center)){
                        addToGuesses(center - 1);
                    }
                    if (isNotOnTheLeftEdge(min)){
                        addToGuesses(min - 10);
                    }
                }
            }
        }
    }

    private boolean isNotOnTheTop(int cell){
        return cell % 10 != 9;
    }
    private boolean isNotOnBottom(int cell){
        return cell % 10 != 0;
    }
    private boolean isNotOnTheLeftEdge(int cell){
        return cell >= 10;
    }
    private boolean isNotOnTheRightEdge(int cell){
        return cell <= 89;
    }

    private void doNotGuessThird() { // Don'nt guess the third cell in a row, if PATROL, FRIGATE AND CARRIER are sunk.
        ArrayList<Integer> help = (ArrayList<Integer>) lastHits.clone();
        Collections.sort(help);
        for (int i = 0; i < (help.size() - 1); i++){
            int min = help.get(i);
            int max = help.get(i+1);
            if (max - min == 1){
                if (isNotOnTheTop(max)) {
                    addToGuesses(max + 1);
                }
                if (isNotOnBottom(min))
                {
                    addToGuesses(min - 1);
                }
            }
            if (max - min == 10){
                if (isNotOnTheLeftEdge(min)) {
                    addToGuesses(min - 10);
                }
                if (isNotOnTheRightEdge(max))
                {
                    addToGuesses(max + 10);
                }
            }
        }
    }
    private void doNotGuessFourth() {
        ArrayList<Integer> help = (ArrayList<Integer>) lastHits.clone();
        Collections.sort(help);
        for (int i = 0; i < (help.size() - 2); i++){
           int max = help.get(i+2);
           int center = help.get(i+1);
           int min = help.get(i);
           if ((center - min == 1) && (max - center == 1)){
               addTheRightCells(max, min, isNotOnBottom(min), 1, isNotOnTheTop(max),
                       isNotOnTheLeftEdge(min), 10, isNotOnTheRightEdge(min));

           }
           if ((center - min == 10) && (max - center == 10)){
               addTheRightCells(max, min, isNotOnTheLeftEdge(min), 10, isNotOnTheRightEdge(max),
                       isNotOnTheTop(min), 1, isNotOnBottom(min));
           }
        }
    }
    private void addTheRightCells(int max, int min, boolean notOnBottom, int i2, boolean notOnTheTop,
                                  boolean notOnTheLeftEdge, int i3, boolean notOnTheRightEdge) {
        if (notOnBottom) {
            addToGuesses(min - i2);
        }
        if (notOnTheTop) {
            addToGuesses(max + i2);
        }
        if (sunkShips.contains(ShipName.FRIGATE)) {
            if (notOnTheLeftEdge) {
                addToGuesses(min + i3);
                addToGuesses(max + i3);
            }
            if (notOnTheRightEdge) {
                addToGuesses(min - i3);
                addToGuesses(max - i3);
            }
        }
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

    private void shootNextToFirstHitAgain() {
        myChances--;
        int startingCell = 0;
        //            lastHits.remove(0);  // První hit odstraníme, může se stát, že bude potřebovat druhý hit...
        //NEBUDE POTŘEBA! Navíc: plusko by nenašel ani tak! Odstraní se až po třetím pokusu!
        if (myChances == 0) startingCell = 1;   //Využil všechny šance, tak ho posuneme na další buňku
        lastHit = lastHits.get(startingCell);    // Začne znovu hledat od začátku lodi
        numberOfNotRandomMoves = 4;  // Znovu 4 pokusy na první hit
    }
    private void tryNearestCells() {
        switch (numberOfNotRandomMoves) {
            case 4 -> {     //Bude zkoušet 4 buňky vedle posledního HITu.
                if (lastHit ==99){
                    myGuess = lastHit - 1;
                    numberOfNotRandomMoves = 2;
                }else if((lastHit)%10==9){
                    myGuess = lastHit + 10;
                    numberOfNotRandomMoves--;
                }else{
                    myGuess = lastHit + 1;    //Zkusí buňku nad a sníží počet nenáhodných pokusů o 1...
                }
                numberOfNotRandomMoves--;
            }
            case 3 -> {
                if ((lastHit == 90)){
                    myGuess = lastHit - 10;
                    numberOfNotRandomMoves = 1;
                }else if (lastHit + 10 > 99){
                    myGuess = lastHit - 1;
                    numberOfNotRandomMoves--;
                }else{
                    myGuess = lastHit + 10;
                }
                numberOfNotRandomMoves--;
            }
            case 2 -> {
                if (lastHit == 0){
//                    findTheMostProbableCell();
                    numberOfNotRandomMoves--;
                }else if((lastHit)%10 == 0){
                    myGuess = lastHit - 10;
                    numberOfNotRandomMoves--;
                }else{
                    myGuess = lastHit - 1;
                }
                numberOfNotRandomMoves--;
            }
            case 1 -> {
                if (lastHit >9){
                    myGuess = lastHit - 10;
                }else{
//                    findTheMostProbableCell();
                }
                numberOfNotRandomMoves--;
            }
            default -> findTheMostProbableCell();
        }
    }
    private void findTheMostProbableCell() {
        ProbabilityCounting probability = new ProbabilityCounting(allGuesses, sunkShips);
        myGuess = probability.getMyGuess();
    }

    @Override
    public void updateGuesses(String result, int guess){
        allGuesses.add(guess);
        if (result.equals("HIT")){
            myChances = 3;
            numberOfNotRandomMoves = 4;   //Byl HIT, takže další pokusy půjdou vedle této buňky
            allHits.add(guess);        //Přidáme hit do seznamu hitů, který se použije při potopení lodi
            lastHits.add(guess);//a také do posledních hitů - ten použije cz.kksffn.player.AI při hledání lodi
            // k aktualizaci polí, kam se cz.kksffn.player.AI nemá trefovat..
            lastHit = guess;

        }
    }
    @Override
    public void updateGuesses(ShipName sunkShip){
        // Pole okolo lodi se přidají do allGuesses, aby  se tam už netrefoval!
        switch(sunkShip){
            case YACHT:
                for (int i = 0; i<2; i++){
                    Integer cell = allHits.get(allHits.size()- 1 - i);
                    updateGuesses(cell);
                }
                break;
            case PATROL:
                for (int i = 0; i<3; i++){
                    Integer cell = allHits.get(allHits.size()- 1 - i);
                    updateGuesses(cell);
                }
                break;
            case FRIGATE:
            case CRUISER:
            case BATTLESHIP:
                for (int i = 0; i<4; i++){
                    Integer cell = allHits.get(allHits.size()- 1 - i);
                    updateGuesses(cell);
                }
                break;
            case CARRIER:
                for (int i = 0; i<5; i++){
                    Integer cell = allHits.get(allHits.size()- 1 - i);
                    updateGuesses(cell);
                }
                break;
        }
        numberOfNotRandomMoves = 0;
        lastHits.clear();       //Vyprázdníme seznam, už ho nepotřebujeme!
        sunkShips.add(sunkShip);
    }
    private void updateGuesses(Integer cell) {
        if (!allGuesses.contains(cell - 1) && cell % 10 != 0) {
            allGuesses.add(cell - 1);
        }
        if (!allGuesses.contains(cell + 1) && cell % 10 != 9) {
            allGuesses.add(cell + 1);
        }
        if (!allGuesses.contains(cell - 10) && cell > 9) {
            allGuesses.add(cell - 10);
        }
        if (!allGuesses.contains(cell + 10) && cell < 90) {
            allGuesses.add(cell + 10);
        }
        if (!allGuesses.contains(cell - 11) && cell - 11 >= 0 && cell % 10 != 0) {
            allGuesses.add(cell - 11);
        }
        if (!allGuesses.contains(cell - 9) && cell % 10 != 9 && cell > 9) {
            allGuesses.add(cell - 9);
        }
        if (!allGuesses.contains(cell + 9) && cell % 10 != 0 && cell + 9 <= 99) {
            allGuesses.add(cell + 9);
        }
        if (!allGuesses.contains(cell + 11) && cell % 10 != 9 && cell + 11 <= 99) {
            allGuesses.add(cell + 11);
        }
    }
    private void addToGuesses(int cell){
        if (!allGuesses.contains(cell)){
            allGuesses.add(cell);
        }
    }
}
