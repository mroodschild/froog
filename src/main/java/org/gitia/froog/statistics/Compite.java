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
package org.gitia.froog.statistics;

import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class Compite {
    
    public static SimpleMatrix eval(SimpleMatrix matrix){
        SimpleMatrix aux = new SimpleMatrix(matrix.numRows(), matrix.numCols());
        double max;
        int pos;
        for (int i = 0; i < aux.numRows(); i++) {
            SimpleMatrix row = matrix.extractVector(true, i);
            max = row.get(0);
            pos = 0;
            //inicializamos en 1 ya que el 0 ya fue tomado
            for (int j = 1; j < row.numCols(); j++) {
                if (max < row.get(j)) {
                    max = row.get(j);
                    //guardamos la posición del mas grande
                    pos = j;
                }
            }
            //guardamos en la fila y columna un 1
            aux.set(i, pos, 1);
        }
        return aux;
        
    }
    
}
