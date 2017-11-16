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
package org.gitia.froog.lossfunction;

import org.apache.commons.lang3.ArrayUtils;
import org.ejml.ops.CommonOps;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class CrossEntropyLoss implements LossFunction {

    /**
     *
     * cost = -1 * ln(ak)
     *
     * @param Ycalc
     * @param Yobs
     * @return
     */
    @Override
    public double cost(SimpleMatrix Ycalc, SimpleMatrix Yobs) {
        //averiguamos q salida correspondia para calcular el ajuste
        //(el resto se hace cero ya que -1 * {y = k} * ln(ak), en las 
        // otras salidas {y != k} => 0
        int Yk = ArrayUtils.indexOf(Yobs.getMatrix().getData(), 1);
        //obtenemso la salida
        double aCalc = Ycalc.get(Yk);
        //realizamos los calculos
        return -1 * Math.log(aCalc);
    }

    /**
     * Ingresan matrices donde cada columna representa una característica de
     * salida y cada fila representa un nuevo registro, luego se implementa el
     * CrossEntropy entre las "y<sub>observadas</sub>" y las
     * "y<sub>calculadas</sub>".<br>
     *
     * <br>
     * [Y<sub>11</sub>, Y<sub>12</sub>, Y<sub>13</sub>]<br>
     * [Y<sub>21</sub>, Y<sub>22</sub>, Y<sub>23</sub>]<br>
     * [Y<sub>31</sub>, Y<sub>32</sub>, Y<sub>33</sub>]<br>
     * [..., ..., ...]<br>
     * [Y<sub>n1</sub>, Y<sub>n2</sub>, Y<sub>n3</sub>]<br>
     *
     * @param Ycalc
     * @param Yobs
     * @return J(Ɵ) = -[∑<sup>m</sup><sub>i = 1</sub> ∑<sup>1</sup><sub>K =
     * 0</sub>
     * (1.{y<sup>(i)</sup> = k} log P(y<sup>(i)</sup> = k | x<sup>(i)</sup>; Ɵ
     * )]
     */
    @Override
    public double costAll(SimpleMatrix Ycalc, SimpleMatrix Yobs) {
        SimpleMatrix y = Ycalc.elementMult(Yobs);
        SimpleMatrix sumCols = new SimpleMatrix(y.numRows(), 1);
        CommonOps.sumRows(y.getMatrix(), sumCols.getMatrix());
        return sumCols.elementLog().elementSum() * -1;

        //SimpleMatrix y = Ycalc.elementMult(Yobs);
//        double[] array = ArrayUtils.removeAllOccurences(y.getMatrix().getData(), 0);
//        y = new SimpleMatrix(1, array.length, true, array);
//        return y.elementLog().elementSum() * -1;
    }

    @Override
    public String toString() {
        return "CrossEntropy";
    }
}
