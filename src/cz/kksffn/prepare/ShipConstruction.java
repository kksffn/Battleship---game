package cz.kksffn.prepare;

public class ShipConstruction {

    private ShipConstruction() {
    }

    public static void main(String[] args) {
        for (int n=1;n<=4;n++){
            int A=n*79;
            if (711000000 % A == 0) {
                for (int B = 3; B <= 702 - A; B++) {
                    if (711000000 % B == 0) {
                        for (int C = B; C <= 702 - (A + B); C++) {
                            if (711000000 % C == 0) {
                                int D = 711 - (A + B + C);
                                if (A * B * C * D == 711000000) {
                                    System.out.printf("%d %d %d %d.\n",A,B,C,D);
                                }
                            }
                        }
                    }
                }
            }
        }
    }


}
