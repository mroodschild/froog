/*
 * Copyright 2019 Matías Roodschild <mroodschild@gmail.com>.
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
import java.util.stream.IntStream;
import org.apache.commons.lang3.ArrayUtils;
import org.ejml.data.MatrixType;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class UtilSimpleMatrix {

    /**
     * This function creates a matrix with a random percent of ones on columns,
     * it's design to be used on droupout with dense matrix
     *
     * @param numRows
     * @param numCols
     * @param percent
     * @return
     */
    public static SimpleMatrix randomOnesSM(int numRows, int numCols, double percent) {
        return randomOnesSM(numRows, numCols, percent, new Random());
    }

    /**
     * This function creates a matrix with a random percent of ones on columns,
     * it's design to be used on droupout with dense matrix
     *
     * @param numRows
     * @param numCols
     * @param percent
     * @param r
     * @return
     */
    public static SimpleMatrix randomOnesSM(int numRows, int numCols, double percent, Random r) {

        //cantidad de elementos no nulos
        int aux = (int) (numRows * percent);

        //minimo un valor (por las redes neuronales)
        final int val = (aux > 0) ? aux : 1;
        SimpleMatrix m = new SimpleMatrix(numRows, numCols, MatrixType.DDRM);
        IntStream.range(0, numCols).parallel()
                .forEach(j -> {
                    double l[] = new double[numRows];
                    for (int i = 0; i < val; i++) {
                        l[i] = 1;
                    }
                    ArrayUtils.shuffle(l, r);

                    m.setColumn(j, 0, l);
                });
        return m;
    }

}
