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
import org.ejml.ops.CommonOps;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class Softmax implements TransferFunction {

    /**
     *
     * la salida será:<br>
     * <table>
     * <tr><td></td><td>[ 1
     * ]</td><td></td><td>[e<sup>(z<sub>1</sub>)</sup>]</td></tr>
     * <tr><td>a =
     * </td><td>------</td><td>*</td><td>[e<sup>(z<sub>2</sub>)</sup>]</td></tr>
     * <tr><td></td><td>&sum(e<sup>zi</sup>)</td><td></td><td>[e<sup>(z<sub>3</sub>)</sup>]</td></tr>
     * </table>
     *
     * @param z
     * @return
     */
    @Override
    public SimpleMatrix output(SimpleMatrix z) {
//        SimpleMatrix p = z.elementExp();
//        double sum = p.elementSum();
//        return p.divide(sum);
        SimpleMatrix p = z.elementExp();
        SimpleMatrix sum = new SimpleMatrix(1, p.numCols());
        CommonOps_DDRM.sumCols(p.getMatrix(), sum.getMatrix());
        for (int i = 0; i < p.numCols(); i++) {
            p.setColumn(i, 0, p.extractVector(false, i).
                    divide(sum.get(i)).getDDRM().getData());
        }
        return p;
    }

    /**
     * Función Derivada de la Softmax
     * <br>
     * 1{y<sup>(i)</sup> = k} − P(y<sup>(i)</sup> = k | x<sup>(i)</sup>;θ)
     *
     * @param a es la salida de la capa
     * @return a
     */
    @Override
    public SimpleMatrix derivative(SimpleMatrix a) {
        throw new UnsupportedOperationException(
                "Softmax was not supported in Hidden Layers.");
    }

    /**
     *
     * (Ycalc - YObs)<br>
     *
     * see:<br>
     *
     * https://www.youtube.com/watch?v=mlaLLQofmR8
     *
     * @param yCalc
     * @param yObs
     * @return
     */
    @Override
    public SimpleMatrix derivative(SimpleMatrix yCalc, SimpleMatrix yObs) {
        return yCalc.minus(yObs);
    }

    @Override
    public SimpleMatrix derivative(SimpleMatrix a, double b) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        return "softmax";
    }

    @Override
    public SimpleMatrix outputZ(SimpleMatrix W, SimpleMatrix a, SimpleMatrix B) {
        //in softmax B is not necesary
        SimpleMatrix aux = W.mult(a);
        return aux;
    }

}
