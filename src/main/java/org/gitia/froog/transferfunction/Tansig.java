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
package org.gitia.froog.transferfunction;

import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class Tansig implements TransferFunction {

    /**
     * de forma matricial: <br>
     * -1 + 2 / (1 + e^(-2 . z))
     *
     * @param z
     * @return
     */
    @Override
    public SimpleMatrix output(SimpleMatrix z) {
        SimpleMatrix div = z.scale(-2).elementExp().plus(1);
//        SimpleMatrix b = new SimpleMatrix(x.numRows(), x.numCols());
//        b.set(2);
//        return b.elementDiv(div).plus(-1);
        CommonOps_DDRM.divide(2, div.getDDRM());
        return div.minus(1);
    }

    /**
     *
     * @param W W[neuronas x entrada]
     * @param a a[entrada x m]
     * @param B B[neuronas x 1]
     * @return
     */
    @Override
    public SimpleMatrix outputZ(SimpleMatrix W, SimpleMatrix a, SimpleMatrix B) {
//        SimpleMatrix aux = W.mult(a);
//        for (int i = 0; i < aux.numCols(); i++) {
//            aux.setColumn(i, 0, aux.extractVector(false, i).plus(B).getDDRM().getData());
//        }
//        SimpleMatrix b = new SimpleMatrix(1, a.numCols());
//        b.fill(1);
//        SimpleMatrix aux = B.mult(b);
//        CommonOps_DDRM.multAdd(W.getDDRM(), a.getDDRM(), aux.getDDRM());
//        return aux;
        
        SimpleMatrix aux = W.mult(a);
        for (int i = 0; i < aux.numCols(); i++) {
            aux.setColumn(i, 0, aux.extractVector(false, i).plus(B).getDDRM().getData());
        }
        return aux;
    }

    /**
     * Función Derivada de la Tansig
     *
     * @param a
     * @return (1 - a²)
     */
    @Override
    public SimpleMatrix derivative(SimpleMatrix a) {
        return a.elementPower(2).scale(-1).plus(1);
    }

    /**
     * Función Derivada de la Tansig
     *
     * @param a
     * @param b
     * @return (1 - (1 - b) a²)
     */
    @Override
    public SimpleMatrix derivative(SimpleMatrix a, double b) {
        return a.elementPower(2).scale(-(1 - b)).plus(1);
    }

    /**
     * output derivative
     *
     * @param yCalc
     * @param yObs
     * @return
     */
    @Override
    public SimpleMatrix derivative(SimpleMatrix yCalc, SimpleMatrix yObs) {
        //calculamos la derivada del "a" de salida
//        SimpleMatrix f_a = derivative(yCalc);
//        //ponemos la derivada de la salida en la posición de la salida
//        return yCalc.minus(yObs).elementMult(f_a);
        return yCalc.minus(yObs);
    }

    @Override
    public String toString() {
        return "tansig"; //To change body of generated methods, choose Tools | Templates.
    }

}
