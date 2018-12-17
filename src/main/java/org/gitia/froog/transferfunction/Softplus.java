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

import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class Softplus implements TransferFunction {

    /**
     * de forma matricial: <br>
     *
     * f(z) = ln(1 + e ^ z)
     *
     * @param z
     * @return
     */
    @Override
    public SimpleMatrix output(SimpleMatrix z) {
        return z.elementExp().plus(1).elementLog();
    }
    
    @Override
    public SimpleMatrix outputZ(SimpleMatrix W, SimpleMatrix a, SimpleMatrix B) {
        return Z.output(W, a, B);
    }

    /**
     * Función Derivada de la softplus
     *
     * @param z
     * @return 1 / (1 + e^(-z))
     */
    @Override
    public SimpleMatrix derivative(SimpleMatrix z) {
        SimpleMatrix div = z.scale(-1).elementExp().plus(1);
        SimpleMatrix b = new SimpleMatrix(z.numRows(), z.numCols());
        b.fill(1);
        return b.elementDiv(div);
    }
    
    @Override
    public SimpleMatrix derivative(SimpleMatrix a, double b) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        return "softplus";
    }

    @Override
    public SimpleMatrix derivative(SimpleMatrix yCalc, SimpleMatrix yObs) {
        //calculamos la derivada del "a" de salida
        SimpleMatrix f_a = derivative(yCalc).transpose();
        //ponemos la derivada de la salida en la posición de la salida
        return yCalc.minus(yObs).elementMult(f_a);
    }

}
