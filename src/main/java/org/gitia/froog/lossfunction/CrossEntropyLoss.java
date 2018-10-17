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
package org.gitia.froog.lossfunction;

import org.apache.commons.lang3.ArrayUtils;
import org.ejml.dense.row.CommonOps_DDRM;
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
        int Yk = ArrayUtils.indexOf(Yobs.getDDRM().getData(), 1);
        //obtenemso la salida
        double aCalc = Ycalc.get(Yk);
        //realizamos los calculos
        return -1 * Math.log(aCalc);
    }

    /**
     * Ingresan matrices donde cada columna representa un registro y cada fila
     * representa una característica, luego se implementa el CrossEntropy entre
     * las "y<sub>observadas</sub>" y las "y<sub>calculadas</sub>".<br>
     *
     * <br>
     * [Y<sub>11</sub>, Y<sub>12</sub>, ..., Y<sub>1m</sub>]<br>
     * [Y<sub>21</sub>, Y<sub>22</sub>, ..., Y<sub>2m</sub>]<br>
     * .
     * ..<br>
     * [Y<sub>n1</sub>, Y<sub>n2</sub>, ..., Y<sub>nm</sub>]<br>
     *
     * @param Ycalc
     * @param Yobs
     * @return J(Ɵ) = - (1/m) [∑<sup>m</sup><sub>i = 1</sub> ∑<sup>1</sup><sub>K =
     * 0</sub>
     * (1.{y<sup>(i)</sup> = k} log P(y<sup>(i)</sup> = k | x<sup>(i)</sup>; Ɵ
     * )]
     */
    @Override
    public double costAll(SimpleMatrix Ycalc, SimpleMatrix Yobs) {
        double m = Ycalc.getNumElements();
        SimpleMatrix y = Ycalc.elementMult(Yobs);
        SimpleMatrix sumRow = new SimpleMatrix(1, y.numCols());
        CommonOps_DDRM.sumCols(y.getDDRM(), sumRow.getDDRM());
        return sumRow.elementLog().elementSum() * -1/m;
        
//        SimpleMatrix y = Ycalc.elementMult(Yobs);
//        SimpleMatrix sumCols = new SimpleMatrix(y.numRows(), 1);
//        CommonOps_DDRM.sumRows(y.getMatrix(), sumCols.getMatrix());
//        return sumCols.elementLog().elementSum() * -1;
        
//        SimpleMatrix y = Ycalc.elementMult(Yobs);
//        double m = Ycalc.numCols();
//        SimpleMatrix sumRows = new SimpleMatrix(y.numCols(), 1);
//        CommonOps_DDRM.sumCols(y.getMatrix(), sumRows.getMatrix());
//        return (sumRows.elementLog().elementSum() * -1) / m;
    }

    @Override
    public String toString() {
        return "CrossEntropy";
    }
}
