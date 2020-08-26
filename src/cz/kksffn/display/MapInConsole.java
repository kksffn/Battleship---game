package cz.kksffn.display;

import java.util.ArrayList;
/*
The class is responsible for displaying the map of players ships (where opponent guesses are visible)
and the map of his guesses in console.
The size of map is 10x10 cells
Hit is represented by 'O' or '@'
Miss is represented by '.'
 */
public class MapInConsole implements IMap{

    private char[][] map = new char[10][10];

    public MapInConsole() {
        for (int i=0; i<10; i++){
            for (int j=0;j<10;j++){
                map[i][j] = '_';
            }
        }
    }
    public void showProbability(ArrayList<Integer> probability){
        String board = ("\n\t   __ __ __ __ __ __ __ __ __ __");
        for (int i=0;i<10;i++){
            if (i==0){
                board += ("\n\t" + (10-i) +"|");
            }else{
                board += ("\n\t " + (10-i) +"|");
            }
            for (int j=0;j<10;j++){
                Integer cell = probability.get(j*10 + (9-i));
                board += ((cell < 10 ? "0" : "")
                        + cell + "|");
            }
        }
        board += ("\n\t   a  b  c  d  e  f  g  h  i  j ".toUpperCase());
        board += ("\n");
        System.out.println(board);
    }
    @Override
    public void setShips(ArrayList<Integer> array) {
        for (Integer cell : array) {
            int x = cell / 10;
            int y = cell % 10;
            map[9 - y][x] = 'O';
        }
    }


    @Override
    public void setMiss(int cell) {
        int x = cell/10;
        int y = cell%10;
        map[9-y][x]= '.';
    }

    @Override
    public void setHit(int cell) {
        int x = cell/10;
        int y = cell%10;
        if (map[9-y][x] == '_'){map[9-y][x] = 'O';}
        else if (map[9-y][x] == 'O'){map[9-y][x] = '@';}
    }
    @Override
    public String toString() {
        String board = ("\n\t   _ _ _ _ _ _ _ _ _ _");
        for (int i=0;i<10;i++){
            if (i==0){
            board += ("\n\t" + (10-i) +"|");
            }else{
                board += ("\n\t " + (10-i) +"|");
            }
            for (int j=0;j<10;j++){
                board += (map[i][j] + "|");
            }
        }
        board += ("\n\t   a b c d e f g h i j ".toUpperCase());
        board += ("\n");
        return board;
    }

}
