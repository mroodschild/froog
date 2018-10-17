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
package org.gitia.froog.layer.initialization;

import java.util.Random;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class DefaultInit implements WeightInit {

    Random random = new Random();

     /**
     * Inicializamos una matriz de pesos con las filas y columnas dadas<br>
     * r = sqrt(6)/(sqrt(s + n + 1)) val = 2 * rand * r - r
     *
     * @param matrix inicializada según la forma: sqrt(6)/(sqrt(s + n + 1)) 
     */
    @Override
    public void init(SimpleMatrix matrix) {
        double r = Math.sqrt(6) / Math.sqrt(matrix.numRows() + matrix.numCols() + 1);
        for (int i = 0; i < matrix.getNumElements(); i++) {
            matrix.set(i, random.nextDouble() * 2 * r - r);
        }
    }

    @Override
    public void setRandom(Random random) {
        this.random = random;
    }

    @Override
    public String toString() {
        return "default";
    }
}
