package Test;

import model.Logik;
import model.MatrixException;

public class TestLogik {
    public static void main(String[] args) throws MatrixException {
        Logik l = new Logik("/home/christianjovanovic/Documents/Projekte/Graphenprogramm/csv/Matrix2.txt");
        System.out.println(l);
    }
}
