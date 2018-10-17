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
public class PRelu implements TransferFunction {

    @Override
    public SimpleMatrix output(SimpleMatrix z) {
        double parm = 3;
        SimpleMatrix p = new SimpleMatrix(z.numRows(), z.numCols());
        for (int i = 0; i < p.getNumElements(); i++) {
            p.set(i, Math.max(z.get(i), z.get(i) * parm));
        }
        return p;
    }

    @Override
    public SimpleMatrix outputZ(SimpleMatrix W, SimpleMatrix a, SimpleMatrix B) {
//        return W.mult(a).plus(B);
        SimpleMatrix aux = W.mult(a);
        for (int i = 0; i < aux.numCols(); i++) {
            aux.setColumn(i, 0, aux.extractVector(false, i).plus(B).getDDRM().getData());
        }
        return aux;
    }

    @Override
    public SimpleMatrix derivative(SimpleMatrix a) {
        double parm = 3;
        SimpleMatrix p = new SimpleMatrix(a.numRows(), a.numCols());
        for (int i = 0; i < p.getNumElements(); i++) {
            p.set(i, (a.get(i) >= 0) ? parm : -1);
        }
        return p;
    }

    @Override
    public SimpleMatrix derivative(SimpleMatrix a, double b) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        return "relu";
    }

    @Override
    public SimpleMatrix derivative(SimpleMatrix yCalc, SimpleMatrix yObs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
