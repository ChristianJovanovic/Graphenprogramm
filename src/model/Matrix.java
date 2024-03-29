package model;
/*
Christian Jovanovic
 */

import java.io.*;

public class Matrix implements Serializable {

    private int[][] matrix;

    public Matrix(String filename) throws MatrixException {
        setMatrix(filename);
    }

    public Matrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public void setMatrix(String filename) throws MatrixException {
        this.matrix = readCsv(filename);
    }

    public void setMatrix(int[][] matrix){
        this.matrix = matrix;
    }

    public int[][] getMatrix() {
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

    /*
    Konvertiert ein String[] in ein int[]
     */
    public static int[] convertToIntArray(String[] strings) {
        int[] result = new int[strings.length];
        for (int i = 0; i < strings.length; i++) {
            result[i] = Integer.parseInt(strings[i]);
        }
        return result;
    }

    /*
    Liest die Matrix aus einer CSV Datei ein.
    @ma die Matrix, die zurückgegeben wird.
     */
    public static int[][] readCsv(String filename) throws MatrixException {
        int[][] ma = new int[getLaenge(filename)][getLaenge(filename)];

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


    /*
    Kopiert eine Matrix
     */
    public static Matrix copy(Matrix m){
        int[][] a = m.getMatrix();
        int[][] result = new int[a.length][a.length];
        for (int i = 0; i < a.length ; i++) {
            for (int j = 0; j < a.length ; j++) {
                result[i][j] = a[i][j];
            }
        }
        return new Matrix(result);
    }

    /*
    Kopiert eine Matrix ohne n
     */
    public static Matrix copy(Matrix m, int n){
        if (n == 0){
            return m;
        }
        int[][] a = copy(m).getMatrix();
        int[][] result = new int[a.length - 1][a.length - 1];

        if(n != 1) {
            for (int i = 0; i < result.length; i++) {
                for (int j = 0; j < result.length; j++) {
                    if (i != n) result[i][j] = a[i][j];
                }
            }
        }
        if (n == 1){
            for (int i = 1; i <result.length ; i++) {
                for (int j = 1; j <result.length ; j++) {
                    result[i][j] = a[i][j];
                }

            }
        }

        return new Matrix(result);
    }

    /*

     */
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
                m += String.format("%4d",this.matrix[i][j]);
            }
            m += "\n";
        }
        return m;
    }

}
