package Test;

import model.Matrix;
import model.MatrixException;

public class TestMatrix {

    public static void main(String[] args) throws MatrixException {
        Matrix m = new Matrix("/home/christianjovanovic/Documents/Projekte/Graphenprogramm/csv/matrix");
        System.out.println(m);
    }
}
