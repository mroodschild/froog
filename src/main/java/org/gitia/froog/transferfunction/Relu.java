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

import java.util.Arrays;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.statistics.Clock;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class Relu implements TransferFunction {

    @Override
    public SimpleMatrix output(SimpleMatrix z) {
        SimpleMatrix p = new SimpleMatrix(z.numRows(), z.numCols());
        int size = z.getNumElements();
        IntStream.range(0, size).parallel()
                .forEach(i -> p.set(i, Math.max(0, z.get(i))));//tmp2[i] = Math.max(0, tmp[i]));
//        for (int i = 0; i < p.getNumElements(); i++) {
//            p.set(i, Math.max(0, z.get(i)));
//        }
        return p;
    }

    @Override
    public SimpleMatrix outputZ(SimpleMatrix W, SimpleMatrix a, SimpleMatrix B) {
        SimpleMatrix aux = W.mult(a);
        int size = aux.numCols();
        IntStream.range(0, size).parallel()
                .forEach(i -> aux.setColumn(i, 0, aux.extractVector(false, i).plus(B).getDDRM().getData()));
//        for (int i = 0; i < aux.numCols(); i++) {
//            aux.setColumn(i, 0, aux.extractVector(false, i).plus(B).getDDRM().getData());
//        }
        return aux;
    }

    @Override
    public SimpleMatrix derivative(SimpleMatrix a) {
        SimpleMatrix p = new SimpleMatrix(a.numRows(), a.numCols());
        //int size = p.getNumElements();
        int size = a.getNumElements();
        IntStream.range(0, size).parallel()
                .forEach(i -> p.set(i, (a.get(i) >= 0) ? 1 : 0));
//        for (int i = 0; i < size; i++) {
//            p.set(i, (a.get(i) >= 0) ? 1 : 0);
//        }
        return p;
    }

    @Override
    public SimpleMatrix derivative(SimpleMatrix a, double b) {
        return derivative(a).plus(b);
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
