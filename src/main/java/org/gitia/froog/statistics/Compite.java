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

import java.util.stream.IntStream;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class Compite {

    public static SimpleMatrix eval(SimpleMatrix matrix) {
        int rows = matrix.numRows();
        int cols = matrix.numCols();
        SimpleMatrix aux = new SimpleMatrix(rows, cols);
        for (int i = 0; i < aux.numRows(); i++) {
        //IntStream.range(0, rows).parallel().forEach(i -> {
            int idx = i * cols;
            int end = idx + cols;
            int pos = idx;
            double max = matrix.get(idx++);
            for (int j = idx; j < end; j++) {
                if (max < matrix.get(j)) {
                    max = matrix.get(j);
                    //guardamos la posición del mas grande
                    pos = j;
                }
            }
            aux.set(pos, 1);
        };//);
        return aux;

    }
    
}
