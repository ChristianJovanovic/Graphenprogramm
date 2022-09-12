package model;

import java.util.ArrayList;
import java.util.Iterator;

import static model.Matrix.copy;

public class Logik {


    private Matrix matrix;
    private Matrix distanzMatrix;

    private int[] exzentrizitaeten;

    public Logik(String filename) throws MatrixException {
        this.matrix = new Matrix(filename);
        this.distanzMatrix = this.distanzMatrixFertig();
        this.exzentrizitaeten = this.exzentrizitaeten();
    }

    public static Matrix multiply(Matrix m1, Matrix m2) {
        int laenge = m1.getMatrix().length;
        int[][] ergebnis = new int[laenge][laenge];
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
        if (potenz <= 20) {
            for (int i = 1; i < potenz; i++) {
                potenzMatrix = multiply(potenzMatrix, new Matrix(this.matrix.getMatrix()));
            }
        }else{
            return potenzMatrix(potenz - (potenz - 20));
        }
        return potenzMatrix;
    }

    public Matrix distanzMatrixInitialisierung() {
        int[][] a = this.matrix.getMatrix();
        int[][] distanz = new int[a.length][a.length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; j++) {
                int value = a[i][j];
                if (i == j) {
                    distanz[i][j] = 0;
                }
                if (i != j && value == 0) {
                    distanz[i][j] = -1;
                }
                if (i != j && value == 1) {
                    distanz[i][j] = 1;
                }
            }
        }
        return new Matrix(distanz);
    }


    public Matrix distanzMatrixFertig() {
        Matrix distanz = this.distanzMatrixInitialisierung();
        int[][] werte = distanz.getMatrix();
        int[][] a = this.matrix.getMatrix();

        for (int i = 2; i <= a.length + 3; i++) {
            a = this.potenzMatrix(i).getMatrix();
            for (int j = 0; j < werte.length; j++) {
                for (int k = 0; k < werte.length; k++) {
                    if (werte[j][k] == -1 && a[j][k] != 0) {
                        werte[j][k] = i;
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

        int[][] a = copy(this.matrix).getMatrix();

        for (int i = 2; i <= a.length + 3; i++) {
            a = this.potenzMatrix(i).getMatrix();
            for (int j = 0; j < weg.getMatrix().length; j++) {
                for (int k = 0; k < weg.getMatrix().length; k++) {
                    if (a[j][k] != 0) {
                        weg.getMatrix()[j][k] = 1;
                    }
                }
            }
        }
        return weg;
    }

    public int[] exzentrizitaeten() {
        int[][] distanz = this.distanzMatrix.getMatrix();
        int[] exzentrizitaeten = new int[this.distanzMatrix.getMatrix().length];
        for (int i = 0; i < distanz.length; i++) {
            for (int j = 0; j < distanz.length; j++) {
                exzentrizitaeten[j] = Math.max(exzentrizitaeten[j], distanz[i][j]);
            }
        }
        return exzentrizitaeten;
    }

    public int durchmesser() {
        int durchmesser = this.exzentrizitaeten[0];
        for (int i = 0; i < this.exzentrizitaeten.length; i++) {
            if (durchmesser < this.exzentrizitaeten[i]) {
                durchmesser = this.exzentrizitaeten[i];
            }
        }
        return durchmesser;
    }

    public int radius() {
        int radius = this.exzentrizitaeten[0];
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
        int[][] wegMatrix = copy(wegMatrix(this.matrix)).getMatrix();
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
                int[] row = wegMatrix[i];
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

    public ArrayList<ArrayList<Integer>> bruecken(){
        ArrayList<ArrayList<Integer>> bruecken = new ArrayList<>();
        ArrayList<ArrayList<Integer>> kompo = this.komponenten();
        Matrix kopie = copy(this.matrix);
        Matrix a1 = copy(this.matrix);
        for (int i = 0; i <a1.getMatrix().length ; i++) {
            for (int j = 0; j <a1.getMatrix().length ; j++) {
                if (a1.getMatrix()[i][j] == 1) {
                    a1.getMatrix()[i][j] = 0;
                    a1.getMatrix()[j][i] = 0;
                    this.matrix = a1;
                    if (this.komponenten().size() > kompo.size()){
                        ArrayList<Integer> kante = new ArrayList<>();
                        kante.add(Math.min(j+1, i+1));
                        kante.add(Math.max(j+1, i+1));
                        if (!bruecken.contains(kante)) bruecken.add(kante);
                    }
                    this.matrix = copy(kopie);
                    a1 = copy(this.matrix);
                }
            }
        }
        this.matrix = copy(kopie);
        return bruecken;
    }



    public String toString() {
        StringBuilder s = new StringBuilder("Exzentrizitäten: \n");
        for (int i = 0; i < this.exzentrizitaeten.length; i++) {
            s.append( String.format("Knoten%-2d: %-2d\n", i+1, this.exzentrizitaeten[i]));
        }
        s.append("\n");
        if (this.komponenten().size() < 2) {
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
        }
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
        s.append("Brücken: ").append(this.bruecken()).append("\n");
        s.append("\n");
        s.append("Distanzmatrix: \n").append(this.distanzMatrix.toString()).append("\n");
        s.append("Wegmatrix: \n").append(wegMatrix(this.matrix)).append("\n");
        s.append("A: \n").append(this.matrix).append("\n");
        for (int i = 2; i < this.matrix.getMatrix().length + 3; i++) {
            s.append("A").append(i).append(":\n").append(this.potenzMatrix(i)).append("\n");
            if (i >= 6){
                break;
            }
        }
        return s.toString();
    }


}
