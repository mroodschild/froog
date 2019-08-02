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

import java.util.stream.IntStream;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class Matrix {
    /**
     *
     * @param m matrix
     * @param axis (0 = Vertical Mean, 1 = Horizontal Mean)
     * @return
     */
    public static SimpleMatrix mean(SimpleMatrix m, int axis) {
        int row = m.numRows();
        int cols = m.numCols();
        final SimpleMatrix mean;
        switch (axis) {
            case 0:
                mean = new SimpleMatrix(1, cols);
                IntStream.range(0, cols).parallel()
                        .forEach((int i) -> {
                            int idx = i;
                            double sum = 0;
                            for (int j = 0; j < row; j++) {
                                System.out.println(i+" idx "+idx);
                                sum += m.get(idx);
                                idx+=cols;
                            }
                            mean.set(i, sum / (double) row);
                        });
                break;
            case 1:
                mean = new SimpleMatrix(row, 1);
                IntStream.range(0, row).parallel()
                        .forEach((int i) -> {
                            int idx = i * cols;
                            double sum = 0;
                            for (int j = 0; j < cols; j++) {
                                sum += m.get(idx++);
                            }
                            mean.set(i, sum / (double) cols);
                        });
                break;
            default:
                mean = null;
                break;
        }
        return mean;
    }
}
