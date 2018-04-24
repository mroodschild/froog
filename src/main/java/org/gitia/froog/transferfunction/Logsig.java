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

import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class Logsig implements TransferFunction {

    /**
     * Logsig: <br>
     * 1 / (1 + e^(-z))
     *
     * @param z
     * @return a
     */
    @Override
    public SimpleMatrix output(SimpleMatrix z) {
        SimpleMatrix a = z.scale(-1).elementExp().plus(1);
        CommonOps_DDRM.divide(1, a.getDDRM());
        return a;
    }

    /**
     *
     * @param W W[neurons x inputs]
     * @param a a[inputs x m] where m is the amount of data
     * @param B B[neurons x 1]
     * @return
     */
    @Override
    public SimpleMatrix outputZ(SimpleMatrix W, SimpleMatrix a, SimpleMatrix B) {
        SimpleMatrix b = new SimpleMatrix(1, a.numCols());
        b.fill(1);
        SimpleMatrix aux = B.mult(b);
        CommonOps_DDRM.multAdd(W.getDDRM(), a.getDDRM(), aux.getDDRM());
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
        return a.negative().plus(1).elementMult(a);
    }

    @Override
    public SimpleMatrix derivative(SimpleMatrix a, double b) {
        return derivative(a).plus(b);
    }

    @Override
    public SimpleMatrix derivative(SimpleMatrix yCalc, SimpleMatrix yObs) {
        return yCalc.minus(yObs);
    }

    @Override
    public String toString() {
        return "logsig";
    }

}
