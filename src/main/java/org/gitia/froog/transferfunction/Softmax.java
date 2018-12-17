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
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.statistics.Clock;

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
        SimpleMatrix p = z.elementExp();
        SimpleMatrix sum = new SimpleMatrix(1, p.numCols());
        CommonOps_DDRM.sumCols(p.getMatrix(), sum.getMatrix());
        int size = p.numCols();
        for (int i = 0; i < size; i++) {
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
        return W.mult(a);
    }

}
