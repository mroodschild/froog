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
public class Purelim implements TransferFunction {

    /**
     * 
     * @param z
     * @return 
     */
    @Override
    public SimpleMatrix output(SimpleMatrix z) {
        return z;
    }
    
    @Override
    public SimpleMatrix outputZ(SimpleMatrix W, SimpleMatrix a, SimpleMatrix B) {
        return Z.output(W, a, B);
    }

    /**
     * f'(z) = 1
     *
     * @param z
     * @return
     */
    @Override
    public SimpleMatrix derivative(SimpleMatrix z) {
        SimpleMatrix derivada = new SimpleMatrix(z.numRows(), z.numCols());
        derivada.fill(1);
        return derivada;
    }

    @Override
    public String toString() {
        return "purelim"; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SimpleMatrix derivative(SimpleMatrix a, double b) {
        return derivative(a);
    }

    @Override
    public SimpleMatrix derivative(SimpleMatrix yCalc, SimpleMatrix yObs) {
        //calculamos la derivada del "a" de salida
        SimpleMatrix f_a = derivative(yCalc);
        //ponemos la derivada de la salida en la posición de la salida
        return yCalc.minus(yObs).elementMult(f_a);
    }

}
