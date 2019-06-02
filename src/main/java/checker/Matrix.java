package checker;

public class Matrix {
    private Box[][] matrix;
    Matrix(Box defaultBox) {
        matrix = new Box[Ranges.getSize().x][Ranges.getSize().y];
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                matrix[x][y] = defaultBox;
            }
        }
    }

    public Box get(Coord coord) {
        if (Ranges.inRange(coord)) {
            return matrix[coord.x][coord.y];
        }
        return null;
    }

    void set(Coord coord, Box box) {
        if (Ranges.inRange(coord)) {
            matrix[coord.x][coord.y] = box;
        }
    }
}
