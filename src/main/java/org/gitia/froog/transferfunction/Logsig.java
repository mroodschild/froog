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
package org.gitia.froog.transferfunction;

import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class Logsig implements TransferFunction {

    /**
     * Logsig: <br>
     * 1 / (1 + e^(-a))
     *
     * @param a
     * @return
     */
    @Override
    public SimpleMatrix output(SimpleMatrix a) {
        SimpleMatrix div = a.scale(-1).elementExp().plus(1);
        SimpleMatrix b = new SimpleMatrix(a.numRows(), a.numCols());
        b.set(1);
        return b.elementDiv(div);
    }

    /**
     * 
     * @param W W[neuronas x entrada]
     * @param a a[entrada x 1]
     * @param B B[neuronas x 1]
     * @return 
     */
    @Override
    public SimpleMatrix outputZ(SimpleMatrix W, SimpleMatrix a, SimpleMatrix B) {
        //return W.mult(a).plus(B);
        SimpleMatrix aux = W.mult(a);
        for (int i = 0; i < aux.numCols(); i++) {
            aux.setColumn(i, 0, aux.extractVector(false, i).plus(B).getMatrix().getData());
        }
        return aux;
    }

    /**
     * Función Derivada de la Logsig
     *
     * @param a
     * @return a * (1 - a)
     */
    @Override
    public SimpleMatrix derivative(SimpleMatrix a) {
        return a.scale(-1).plus(1).elementMult(a);
    }

    @Override
    public SimpleMatrix derivative(SimpleMatrix a, double b) {
        return derivative(a).plus(b);
    }

    @Override
    public SimpleMatrix derivative(SimpleMatrix yCalc, SimpleMatrix yObs) {
        //calculamos la derivada del "a" de salida
        SimpleMatrix f_a = derivative(yCalc);
        //ponemos la derivada de la salida en la posición de la salida
        return yCalc.minus(yObs).elementMult(f_a);
    }

    @Override
    public String toString() {
        return "logsig";
    }

}
