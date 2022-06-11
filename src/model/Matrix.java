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

    public void setMatrix(String filename) throws MatrixException {
        this.matrix = readCsv(filename);
    }

    public int[][] getMatrix() {
        return matrix;
    }

    /*
    Gibt die benötigte größe für die Matrix zurück
     */
    public static int getLaenge(String filename) throws MatrixException {
        int laenge = 0;
        try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String zeile;
            while ((zeile = br.readLine()) != null){
                laenge = zeile.trim().split(";").length;
            }
        }catch (IOException e){
            throw new MatrixException("Fehler in Klasse Matrix() bei Methode getLaenge(): CSV-Datei konnte nicht eingelesen werden!");
        }
        return laenge;
    }

    public static int[] convertToIntArray(String[] strings){
        int[] result = new int[strings.length];
        for (int i = 0; i < strings.length; i++){
            result[i] = Integer.parseInt(strings[i]);
        }
        return result;
    }

    /*
    Liest die Matrix aus einer CSV Datei ein.
     */
    public static int[][] readCsv(String filename) throws MatrixException {
        int[][] ma = new int[getLaenge(filename)][getLaenge(filename)];

        try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String zeile;
            for (int i = 0; i < ma.length; i++){
                if ((zeile = br.readLine()) != null){
                   String[] teil = zeile.trim().split(";");
                   ma[i] = convertToIntArray(teil);
                }
            }
        }catch (IOException e){
            throw new MatrixException("Fehler in Klasse Matrix() bei Methode readCsv(): die Matrix konnte aus der CSV-Datei nicht eingelesen werden!");
        }
        return ma;
    }

    public String toString(){
        String m = "";
        for (int i = 0; i < this.matrix.length; i++){
            for (int j = 0; j < this.matrix[i].length; j++){
                m += this.matrix[i][j] + " ";
            }
            m += "\n";
        }
        return m;
    }

}
