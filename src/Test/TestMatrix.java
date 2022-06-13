package Test;

import model.Matrix;
import model.MatrixException;
import model.Logik;

import static model.Logik.multiply;
import static model.Matrix.copy;

public class TestMatrix {

    public static void main(String[] args) throws MatrixException {
        Matrix m = new Matrix("/home/christianjovanovic/Documents/Projekte/Graphenprogramm/csv/TestMatrix");
        System.out.println(m);
        Matrix m2 = multiply(m, m);
        System.out.println(m2);
        System.out.println(copy(m2, 2));
    }
}
