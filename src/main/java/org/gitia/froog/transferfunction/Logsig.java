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

import java.util.stream.IntStream;
import org.ejml.data.MatrixType;
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
//        SimpleMatrix a = z.scale(-1).elementExp().plus(1);
//        CommonOps_DDRM.divide(1, a.getDDRM());
//        return a;
//        
        SimpleMatrix m = new SimpleMatrix(z.numRows(), z.numCols(), MatrixType.DDRM);
        IntStream.range(0, m.numRows()).parallel()
                .forEach(i -> {
            int size = m.numCols();
            int idx = i * size;
            for (int j = 0; j < size; j++) {
                m.set(idx, 1.0 / (1+Math.exp(-z.get(idx++))));
            }
        });
        return m;
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
        return Z.output(W, a, B);
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
