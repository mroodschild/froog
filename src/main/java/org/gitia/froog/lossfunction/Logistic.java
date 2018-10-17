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
