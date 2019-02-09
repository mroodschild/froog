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

import java.util.stream.IntStream;
import org.apache.commons.lang3.ArrayUtils;
import org.ejml.MatrixDimensionException;
import static org.ejml.UtilEjml.stringShapes;
import org.ejml.data.DMatrixSparseCSC;
import org.ejml.data.DMatrixSparseTriplet;
import org.ejml.ops.ConvertDMatrixStruct;
import org.gitia.froog.statistics.Clock;

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
    
    public static DMatrixSparseCSC elementMult(DMatrixSparseCSC A, DMatrixSparseCSC B, DMatrixSparseCSC C){
        if( A.numCols != B.numCols || A.numRows != B.numRows )
            throw new MatrixDimensionException("All inputs must have the same number of rows and columns. "+stringShapes(A,B));
        C.reshape(A.numRows,A.numCols);

        return null;
    }

    /**
     * 
     * @param numRows
     * @param numCols
     * @param percent
     * @return 
     */
    private static DMatrixSparseTriplet randomOnesDouble(int numRows, int numCols, double percent) {
        //cantidad de elementos no nulos
        Clock c = new Clock();
        c.start();
        int total_number = (int) (numRows * numCols * percent);
        
        DMatrixSparseTriplet work = new DMatrixSparseTriplet(numRows, numCols, total_number);
        IntStream.range(0, numCols).parallel()
                .forEach(j -> {
                    //porcentaje de datos que deben tener "1"
                    int val = (int) (numRows * percent);
                    //minimo un valor (por las redes neuronales)
                    val = (val > 0) ? val : 1;
                    double l[] = new double[numRows];
                    //llenamos de unos
                    for (int i = 0; i < val; i++) {
                        l[i] = 1;
                    }
                    //mezclamos
                    ArrayUtils.shuffle(l);
                    //copiamos
                    for (int i = 0; i < numRows; i++) {
                        double v = l[i];
                        if (v != 0) {
                            work.addItem(i, j, v);
                        }
                    }
                });
        c.stop();
        c.printTime("onesDouble");
        return work;
    }
}
