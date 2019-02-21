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

import java.util.Random;
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
        IntStream.range(0, numCols).parallel()
                .forEach(j -> {
                    //  for (int j = 0; j < numCols; j++) {
                    double l[] = new double[numRows];
                    shuffle(l, val, j, work);
                    //}
                });
        return work;
    }

    public static void shuffle(double[] l, int val, int col, DMatrixSparseTriplet work) {
        for (int i = 0; i < val; i++) {
            l[i] = 1;
        }
        ArrayUtils.shuffle(l);
        int flag = 0;
        for (int i = 0; i < l.length; i++) {
            double v = l[i];
            if (v != 0) {
                work.addItem(i, col, v);
                if (++flag == val) {
                    break;
                }
            }
        }
    }
}
