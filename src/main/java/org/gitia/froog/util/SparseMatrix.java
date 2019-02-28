/*
 * Copyright 2019 
 *   Matías Roodschild <mroodschild@gmail.com>.
 *   Jorge Gotay Sardiñas <jgotay57@gmail.com>.
 *   Adrian Will <adrian.will.01@gmail.com>.
 *   Sebastián Rodriguez <sebastian.rodriguez@gitia.org>
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
package org.gitia.froog.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import org.apache.commons.lang3.ArrayUtils;
import org.ejml.MatrixDimensionException;
import static org.ejml.UtilEjml.stringShapes;
import org.ejml.data.DMatrixSparseCSC;
import org.ejml.data.DMatrixSparseTriplet;
import org.ejml.data.MatrixType;
import org.ejml.ops.ConvertDMatrixStruct;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.statistics.Clock;
import org.gitia.jdataanalysis.Util;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class SparseMatrix {

    static double tiempo1 = 0;
    static double tiempo2 = 0;

    /**
     * Randomly generates matrix with the specified number of non-zero elements
     * filled with 1. There is at least one number with "one" per column.
     *
     * @param numRows Number of rows
     * @param numCols Number of columns
     * @param percent percent of ones in the columns
     * @return Randomly generated matrix
     */
    public static DMatrixSparseCSC randomOnesColumnsCSC(int numRows, int numCols, double percent) {
        DMatrixSparseTriplet work = randomOnesDouble(numRows, numCols, percent);
        return ConvertDMatrixStruct.convert(work, (DMatrixSparseCSC) null);
    }

//    public static DMatrixSparseCSC elementMult(DMatrixSparseCSC A, DMatrixSparseCSC B, DMatrixSparseCSC C) {
//        if (A.numCols != B.numCols || A.numRows != B.numRows) {
//            throw new MatrixDimensionException("All inputs must have the same number of rows and columns. " + stringShapes(A, B));
//        }
//        C.reshape(A.numRows, A.numCols);
//
//        return null;
//    }
    /**
     *
     * @param numRows
     * @param numCols
     * @param percent
     * @return
     */
    private static DMatrixSparseTriplet randomOnesDouble(int numRows, int numCols, double percent) {

        //cantidad de elementos no nulos
        int aux = (int) (numRows * percent);
        final int val = (aux > 0) ? aux : 1;
        //minimo un valor (por las redes neuronales)
        final int total_number = val * numCols;
        DMatrixSparseTriplet work = new DMatrixSparseTriplet(numRows, numCols, total_number);
        IntStream.range(0, numCols)//.parallel()
                .forEach(j -> {
                    //  for (int j = 0; j < numCols; j++) {

                    shuffleList(numRows, val, j, work);
                    //double l[] = new double[numRows];
                    //shuffle(l, val, j, work);
                    //}
                });

        System.out.println("Tiempo creando:\t" + tiempo1 / 1000 + "\ttiempo agregando\t" + tiempo2 / 1000);
        return work;
    }

    public static void shuffle(double[] vectorVacio, int cantValores, int colIndex, DMatrixSparseTriplet work) {
        Clock c = new Clock();
        c.start();
        for (int i = 0; i < cantValores; i++) {
            vectorVacio[i] = 1;
        }
        ArrayUtils.shuffle(vectorVacio);
        c.stop();
        tiempo1 += c.time();
        int flag = 0;
        c.start();
        for (int i = 0; i < vectorVacio.length; i++) {
            double v = vectorVacio[i];
            if (v != 0) {
                work.addItem(i, colIndex, v);
                if (++flag == cantValores) {
                    break;
                }
            }
        }
        c.stop();
        tiempo2 += c.time();
    }

    public static void shuffleList(int numRows, int cantValores, int colIndex, DMatrixSparseTriplet work) {
        Random r = new Random();
        int size = numRows;
        //LinkedList<Integer> posiciones = new LinkedList<>();
        List<Integer> posiciones = new ArrayList<>(size);
        Clock c = new Clock();
        //generamos los indices
        c.start();
        for (int i = 0; i < size; i++) {
            posiciones.add(i);
        }
        c.stop();
        tiempo1 += c.time();
        //c.printTime("agregando posiciones");

        int idx;
        //seleccionamos una cantidad de valores
        c.start();
        for (int i = 0; i < cantValores; i++) {
            idx = r.nextInt(posiciones.size());
            //guardamos el indice de la posicion seleccionada
            work.addItem(posiciones.get(idx), colIndex, 1);
            posiciones.remove(idx);
        }
        c.stop();
        tiempo2 += c.time();
        //c.printTime("agregando valores");
    }

}
