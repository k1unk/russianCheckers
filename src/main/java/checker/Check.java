package checker;

public class Check {
    public static Matrix checkersMap;
    static void start() {
        checkersMap = new Matrix(Box.WHITESQUARE);
        placeSquares();
    }

    Box get(Coord coord) {
        return checkersMap.get(coord);
    }

    private static void placeSquares() {
        for (int y = 0; y < 8; y++) {
            String fileName = "NUM" + (8 - y);
            checkersMap.set(new Coord(0, y), Box.valueOf(fileName));
        }
        checkersMap.set(new Coord(1, 8), Box.NUMA);
        checkersMap.set(new Coord(2, 8), Box.NUMB);
        checkersMap.set(new Coord(3, 8), Box.NUMC);
        checkersMap.set(new Coord(4, 8), Box.NUMD);
        checkersMap.set(new Coord(5, 8), Box.NUME);
        checkersMap.set(new Coord(6, 8), Box.NUMF);
        checkersMap.set(new Coord(7, 8), Box.NUMG);
        checkersMap.set(new Coord(8, 8), Box.NUMH);
        for (int y = 3; y < 5; y++) {
            for (int x = 1; x < 9; x++) {
                if ((x + y) % 2 == 0) {
                    checkersMap.set(new Coord(x, y), Box.BLACKSQUARE);
                }
            }
        }
        for (int y = 0; y < 3; y++) {
            for (int x = 1; x < 9; x++) {
                if ((x + y) % 2 == 0) {
                    checkersMap.set(new Coord(x, y), Box.BLACK);
                }
            }
        }
        for (int y = 5; y < 8; y++) {
            for (int x = 1; x < 9; x++) {
                if ((x + y) % 2 == 0) {
                    checkersMap.set(new Coord(x, y), Box.WHITE);
                }
            }
        }
    }
}
