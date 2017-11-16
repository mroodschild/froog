/*
 * The MIT License
 *
 * Copyright 2017 
 *   Matías Roodschild <mroodschild@gmail.com>.
 *   Jorge Gotay Sardiñas <jgotay57@gmail.com>.
 *   Adrian Will <adrian.will.01@gmail.com>.
 *   Sebastián Rodriguez <sebastian.rodriguez@gitia.org>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
