package model;

import java.util.ArrayList;
import java.util.Iterator;

import static model.Matrix.copy;

public class Logik {


    private Matrix matrix;
    private Matrix distanzMatrix;

    private Long[] exzentrizitaeten;

    public Logik(String filename) throws MatrixException {
        this.matrix = new Matrix(filename);
        this.distanzMatrix = this.distanzMatrixFertig();
        this.exzentrizitaeten = this.exzentrizitaeten();
    }

    public static Matrix multiply(Matrix m1, Matrix m2) {
        int laenge = m1.getMatrix().length;
        Long[][] ergebnis = new Long[laenge][laenge];
        int zw = 0;
        for (int i = 0; i < laenge; i++) {
            for (int j = 0; j < laenge; j++) {
                for (int k = 0; k < laenge; k++) {
                    ergebnis[i][j] += m1.getMatrix()[i][k] * m2.getMatrix()[k][j];
                }
            }
        }
        return new Matrix(ergebnis);
    }

    public Matrix potenzMatrix(int potenz) {
        Matrix potenzMatrix = new Matrix(this.matrix.getMatrix());
        for (int i = 1; i < potenz; i++) {
            potenzMatrix = multiply(potenzMatrix, new Matrix(this.matrix.getMatrix()));
        }
        return potenzMatrix;
    }

    public Matrix distanzMatrixInitialisierung() {
        Long[][] a = this.matrix.getMatrix();
        Long[][] distanz = new Long[a.length][a.length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; j++) {
                Long value = a[i][j];
                if (i == j) {
                    distanz[i][j] = 0l;
                }
                if (i != j && value == 0) {
                    distanz[i][j] = -1l;
                }
                if (i != j && value == 1) {
                    distanz[i][j] = 1l;
                }
            }
        }
        return new Matrix(distanz);
    }


    public Matrix distanzMatrixFertig() {
        Matrix distanz = this.distanzMatrixInitialisierung();
        Long[][] werte = distanz.getMatrix();
        Long[][] a = this.matrix.getMatrix();

        for (int i = 2; i <= a.length + 3; i++) {
            a = this.potenzMatrix(i).getMatrix();
            for (int j = 0; j < werte.length; j++) {
                for (int k = 0; k < werte.length; k++) {
                    if (werte[j][k] == -1 && a[j][k] != 0) {
                        werte[j][k] = (long)i;
                    }
                }
            }
        }
        return new Matrix(werte);
    }

    public Matrix wegMatrix(Matrix m) {
        Matrix weg = copy(m);
        for (int i = 0; i < weg.getMatrix().length; i++) {
            weg.getMatrix()[i][i] += 1;
        }

        Long[][] a = copy(this.matrix).getMatrix();

        for (int i = 2; i <= a.length + 3; i++) {
            a = this.potenzMatrix(i).getMatrix();
            for (int j = 0; j < weg.getMatrix().length; j++) {
                for (int k = 0; k < weg.getMatrix().length; k++) {
                    if (a[j][k] != 0) {
                        weg.getMatrix()[j][k] = (long)1;
                    }
                }
            }
        }
        return weg;
    }

    public Long[] exzentrizitaeten() {
        Long[][] distanz = this.distanzMatrix.getMatrix();
        Long[] exzentrizitaeten = new Long[this.distanzMatrix.getMatrix().length];
        for (int i = 0; i < distanz.length; i++) {
            for (int j = 0; j < distanz.length; j++) {
                exzentrizitaeten[j] = Math.max(exzentrizitaeten[j], distanz[i][j]);
            }
        }
        return exzentrizitaeten;
    }

    public Long durchmesser() {
        Long durchmesser = this.exzentrizitaeten[0];
        for (int i = 0; i < this.exzentrizitaeten.length; i++) {
            if (durchmesser < this.exzentrizitaeten[i]) {
                durchmesser = this.exzentrizitaeten[i];
            }
        }
        return durchmesser;
    }

    public Long radius() {
        Long radius = this.exzentrizitaeten[0];
        for (int i = 0; i < this.exzentrizitaeten.length; i++) {
            if (radius > this.exzentrizitaeten[i]) {
                radius = this.exzentrizitaeten[i];
            }
        }
        return radius;
    }

    public ArrayList<Integer> zentrum() {
        ArrayList<Integer> zentrum = new ArrayList<>();
        for (int i = 0; i < this.exzentrizitaeten.length; i++) {
            if (this.exzentrizitaeten[i] == this.radius()) {
                zentrum.add((i + 1));
            }
        }
        return zentrum;
    }

    public ArrayList<ArrayList<Integer>> komponenten() {
        ArrayList<ArrayList<Integer>> komponenten = new ArrayList<>();
        ArrayList<Integer> knoten = new ArrayList<>();
        Boolean a = false;
        Long[][] wegMatrix = copy(wegMatrix(this.matrix)).getMatrix();
        for (int i = 0; i < wegMatrix.length; i++) {
            if (!(new Matrix(wegMatrix).contains(0))) {
                for (int j = 0; j < wegMatrix.length; j++) {
                    if (!knoten.contains(j+1)) {
                        knoten.add(j + 1);
                    }
                }
                if (!komponenten.contains(knoten)) {
                    komponenten.add(knoten);
                }
            } else {
                Long[] row = wegMatrix[i];
                for (int j = 0; j < row.length; j++) {
                    if (row[j] == 1) {
                        knoten.add(j + 1);
                    }
                }
                if (!komponenten.contains(knoten)) {
                    komponenten.add(knoten);
                }
                knoten = new ArrayList<>();
            }
        }
        return komponenten;
    }

    public ArrayList<Integer> artikulationen(){
        Matrix kopie = copy(this.matrix);
        Matrix weg1 = wegMatrix(kopie);
        ArrayList<ArrayList<Integer>> komponenten = this.komponenten();
        ArrayList<Integer> artikulationen = new ArrayList<>();

        for (int i = 0; i < kopie.getMatrix().length; i++) {
            this.matrix = copy(this.matrix, i);
            Matrix weg2 = wegMatrix(this.matrix);
            if (!weg1.contains(0) && weg2.contains(0)){
                if (!artikulationen.contains(i+1)) artikulationen.add(i+1);
            }
            if (komponenten.size() < this.komponenten().size() -1){
                if (!artikulationen.contains(i+1)) artikulationen.add(i+1);
            }
            this.matrix = copy(kopie);
        }

        this.matrix = copy(kopie);

        return artikulationen;
    }

    public String toString() {
        StringBuilder s = new StringBuilder("Exzentrizitäten: \n");
        for (int i = 0; i < this.exzentrizitaeten.length; i++) {
            s.append("Knoten ").append(i + 1).append(": ").append(this.exzentrizitaeten[i]).append("\n");
        }
        s.append("\n");
        s.append("Durchmesser: ").append(this.durchmesser()).append("\n");
        s.append("Radius: ").append(this.radius()).append("\n");
        s.append("\n");
        s.append("Zentrum: {");
        Iterator a = this.zentrum().iterator();
        while (a.hasNext()) {
            s.append(a.next()).append(", ");
        }
        s.replace(s.lastIndexOf(","), s.lastIndexOf(",") + 2, "");
        s.append("} \n").append("\n");
        s.append("Komponenten: \n");
        Iterator iterator = this.komponenten().iterator();
        int counter = 1;
        while (iterator.hasNext()) {
            s.append("K").append(counter).append(": {").append(iterator.next().toString()).append("};\n");
            counter++;
        }
        s.append("\n");
        s.append("Atrikulationen: \n").append(this.artikulationen()).append("\n");
        s.append("\n");
        s.append("Distanzmatrix: \n").append(this.distanzMatrix.toString()).append("\n");
        s.append("Wegmatrix: \n").append(wegMatrix(this.matrix)).append("\n");
        s.append("A: \n").append(this.matrix).append("\n");
        for (int i = 2; i < this.matrix.getMatrix().length + 3; i++) {
            s.append("A").append(i).append(":\n").append(this.potenzMatrix(i)).append("\n");
        }
        return s.toString();
    }


}
