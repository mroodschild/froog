/*
 * Copyright 2018 
 *   Matías Roodschild <mroodschild@gmail.com>.
 *   Jorge Gotay Sardiñas <jgotay57@gmail.com>.
 *   Adrian Will <adrian.will.01@gmail.com>.
 *   Sebastián Rodriguez <sebastian.rodriguez@gitia.org>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gitia.froog.statistics;

import org.apache.commons.lang3.ArrayUtils;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class ConfusionMatrix {

    SimpleMatrix confusionMatrix;
    int elements;
    double aciertos;
    double aciertosPorc;

    public ConfusionMatrix() {
    }

    /**
     * los datos ingresados ya deben estar formados de la siguiente manera:<br>
     * <br>
     * [1,0,0]<br>
     * [0,1,0]<br>
     * [0,0,1]<br>
     * [1,0,0]<br>
     * <br>
     * IMPORTANTE: tener presente que solo un valor debe ser 1 y el resto 0.
     *
     * @param calc
     * @param obs
     */
    public void eval(SimpleMatrix calc, SimpleMatrix obs) {
        elements = calc.numRows();
        int clases = obs.numCols();
        //Creamos la matriz donde serán guardados los datos
        confusionMatrix = new SimpleMatrix(clases, clases);
        for (int i = 0; i < elements; i++) {
            addValue(getIndex(obs.extractVector(true, i)), getIndex(calc.extractVector(true, i)));
        }
        aciertos = confusionMatrix.diag().elementSum();
        aciertosPorc = aciertos / ((double) elements);
    }

    /**
     *
     * Esta función indica el índice de la clase, es decir:<br>
     * si a = [0,0,0,1], devuelve index = 3<br>
     * si no es encontrado devuelve -1<br>
     *
     * @param a
     * @return
     */
    private int getIndex(SimpleMatrix a) {
        return ArrayUtils.indexOf(a.getDDRM().getData(), 1);
    }

    /**
     *
     * En esta función buscamos la fila "obs" y sumamos uno en la col "calc"
     *
     * @param obs este parámetro es la clase observada
     * @param calc este parámetro es la clase calculada
     */
    private void addValue(int obs, int calc) {
        //sumamos uno en la posición indicada
        double a = confusionMatrix.get(obs, calc) + 1;
        //actualizamos
        confusionMatrix.set(obs, calc, a);
    }

    /**
     *
     * @return
     */
    public SimpleMatrix getConfusionMatrix() {
        return confusionMatrix;
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        double[] yo = {
            1, 0, 0,
            0, 1, 0,
            0, 0, 1,
            1, 0, 0,
            0, 1, 0};
        SimpleMatrix obs = new SimpleMatrix(5, 3, true, yo);

        double[] yc = {
            1, 0, 0,
            0, 1, 0,
            0, 1, 0,
            1, 0, 0,
            0, 0, 1};
        SimpleMatrix calc = new SimpleMatrix(5, 3, true, yc);

        System.out.println("Obs");
        obs.print();
        System.out.println("Calc");
        calc.print();

        ConfusionMatrix matrix = new ConfusionMatrix();
        matrix.eval(calc, obs);
        System.out.println("");
        matrix.printStats();
    }

    /**
     *
     */
    public void printStats() {
        System.out.println(this.toString());
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        String info = "";
        info += "\nConfusion Matrix\n";
        //info += confusionMatrix.toString();
        info += getStringMatrix()+"\n";
        info += "\nAccuracy:\t" + aciertos + "/" + (double) elements;
        info += "\nAccuracy %:\t" + aciertosPorc;
        int size = confusionMatrix.numCols();
        double VP = 0;
        double FN = 0;
        info += "\nAccuracy per class:\t";
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i != j) {
                    FN += confusionMatrix.get(i, j);
                } else {
                    VP = confusionMatrix.get(i, j);
                }
            }
            info += "\nClass " + i + ": \t" + VP / (VP + FN) + "\t"+VP+"/"+(VP + FN);
            VP = 0;
            FN = 0;
        }
        return info;
    }
    
    /**
     * 
     * @return matrix with format
     */
    private String getStringMatrix(){
        String m = "";
        for (int i = 0; i < confusionMatrix.numRows(); i++) {
            for (int j = 0; j < confusionMatrix.numCols(); j++) {
                m += String.valueOf(confusionMatrix.get(i, j))+"\t";
            }
            m+="\n";
        }
        return m;
    }

    /**
     * cantidad de aciertos
     *
     * @return
     */
    public double getAciertos() {
        return aciertos;
    }

    /**
     * porcentaje de aciertos
     *
     * @return
     */
    public double getAciertosPorc() {
        return aciertosPorc;
    }

    /**
     * cantidad de datos totales
     *
     * @return
     */
    public int getElements() {
        return elements;
    }

}
