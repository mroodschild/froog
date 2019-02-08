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

import org.apache.commons.lang3.ArrayUtils;
import org.ejml.data.DMatrixSparseCSC;
import org.gitia.froog.statistics.Clock;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class SparseMatrix {

    /**
     * Randomly generates matrix with the specified number of non-zero elements
     * filled with 1.
     *
     * @param numRows Number of rows
     * @param numCols Number of columns
     * @param nz_total Total number of non-zero elements in the matrix
     * @return Randomly generated matrix
     */
    public static DMatrixSparseCSC randomOnesCSC(int numRows, int numCols, int nz_total) {

        double[] selected = randomOnesDouble(numRows * numCols, nz_total);
        
        DMatrixSparseCSC ret = new DMatrixSparseCSC(numRows, numCols, nz_total);
        ret.indicesSorted = true;
        int idx = 0;
        Clock c = new Clock();
        c.start();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                ret.set(i, j, selected[idx++]);
            }
        }
        c.stop();
        c.printTime("datos agregados");
        return ret;
    }

    /**
     *
     * @param N cantidad de valores
     * @param shuffleUpTo total de valores no nulos
     * @return
     */
    private static double[] randomOnesDouble(int N, int shuffleUpTo) {
        double l[] = new double[N];
        for (int i = 0; i < shuffleUpTo; i++) {
            l[i] = 1;
        }
        Clock c = new Clock();
        c.start();
        ArrayUtils.shuffle(l);
        c.stop();
        c.printTime("shuffle");
        return l;
    }
}
