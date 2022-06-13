package model;
/*
Christian Jovanovic
 */

import java.io.*;

public class Matrix implements Serializable {

    private Long[][] matrix;

    public Matrix(String filename) throws MatrixException {
        setMatrix(filename);
    }

    public Matrix(Long[][] matrix) {
        this.matrix = matrix;
    }

    public void setMatrix(String filename) throws MatrixException {
        this.matrix = readCsv(filename);
    }

    public Long[][] getMatrix() {
        return matrix;
    }

    /*
    Gibt die benötigte größe für die Matrix zurück
     */
    public static int getLaenge(String filename) throws MatrixException {
        int laenge = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String zeile;
            while ((zeile = br.readLine()) != null) {
                laenge = zeile.trim().split(";").length;
            }
        } catch (IOException e) {
            throw new MatrixException("Fehler in Klasse Matrix() bei Methode getLaenge(): CSV-Datei konnte nicht eingelesen werden!");
        }
        return laenge;
    }

    public static Long[] convertToIntArray(String[] strings) {
        Long[] result = new Long[strings.length];
        for (int i = 0; i < strings.length; i++) {
            result[i] = Long.parseLong(strings[i]);
        }
        return result;
    }

    /*
    Liest die Matrix aus einer CSV Datei ein.
     */
    public static Long[][] readCsv(String filename) throws MatrixException {
        Long[][] ma = new Long[getLaenge(filename)][getLaenge(filename)];

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String zeile;
            for (int i = 0; i < ma.length; i++) {
                if ((zeile = br.readLine()) != null) {
                    String[] teil = zeile.trim().split(";");
                    ma[i] = convertToIntArray(teil);
                }
            }
        } catch (IOException e) {
            throw new MatrixException("Fehler in Klasse Matrix() bei Methode readCsv(): die Matrix konnte aus der CSV-Datei nicht eingelesen werden!");
        }
        return ma;
    }


    public static Matrix copy(Matrix m){
        Long[][] a = m.getMatrix();
        Long[][] result = new Long[a.length][a.length];
        for (int i = 0; i < a.length ; i++) {
            for (int j = 0; j < a.length ; j++) {
                result[i][j] = a[i][j];
            }
        }
        return new Matrix(result);
    }

    public static Matrix copy(Matrix m, int n){
        if (n == 0){
            return m;
        }
        Long[][] a = copy(m).getMatrix();
        Long[][] result = new Long[a.length - 1][a.length - 1];

        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result.length; j++) {
                if (i != n) result[i][j] = a[i][j];
            }
        }
        return new Matrix(result);
    }

    public boolean contains(int a) {
        boolean b = false;
        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < this.matrix.length; j++) {
                if (this.matrix[i][j] == a) {
                    b = true;
                }
            }
        }
        return b;
    }

    public String toString() {
        String m = "";
        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < this.matrix[i].length; j++) {
                if (this.matrix[i][j] >= 10){
                    m += this.matrix[i][j] + " ";
                }
                else {
                    m += this.matrix[i][j] +"  ";
                }
            }
            m += "\n";
        }
        return m;
    }

}
