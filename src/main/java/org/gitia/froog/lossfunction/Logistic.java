/*
 * The MIT License
 *
 * Copyright 2018 Matías Roodschild <mroodschild@gmail.com>.
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
package org.gitia.froog.lossfunction;

import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class Logistic implements LossFunction {

    /**
     * @param A [outputs x 1] one output
     * @param Y [outputs x 1] one output
     * @return L(A, Y) = -y<sup>(i)</sup>log(a<sup>(i)</sup>) - (1 -
     * y<sup>(i)</sup>)log(1 - a<sup>(i)</sup>)
     */
    @Override
    public double cost(SimpleMatrix A, SimpleMatrix Y) {
        return Y.elementMult(A.elementLog())
                .plus(Y.negative().plus(1).elementMult(A.negative().plus(1).elementLog()))
                .elementSum();
    }

    /**
     * Calculamos el costo
     *
     * La entrada y la salida deben estar en formato vertical<br>
     *
     * @param A
     * @param Y
     * @return J = sum(L(A, Y) / m <br>
     * L(A, Y) = -y<sup>(i)</sup>log(a<sup>(i)</sup>) - (1 -
     * y<sup>(i)</sup>)log(1 - a<sup>(i)</sup>)
     */
    @Override
    public double costAll(SimpleMatrix A, SimpleMatrix Y) {
        double m = A.numCols();
        return -1 * Y.elementMult(A.elementLog())
                .plus(Y.negative().plus(1).elementMult(A.negative().plus(1).elementLog()))
                .elementSum() / m;
        }

    @Override
    public String toString() {
        return "logistic";
    }
    
}
