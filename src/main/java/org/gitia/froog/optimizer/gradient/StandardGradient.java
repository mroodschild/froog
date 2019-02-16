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
package org.gitia.froog.optimizer.gradient;

import java.util.List;
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.Feedforward;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class StandardGradient implements Gradient {

    /**
     *
     * @param net
     * @param Activations
     * @param gradW
     * @param gradB
     * @param X
     * @param Y
     */
    @Override
    public void compute(Feedforward net, List<SimpleMatrix> Activations, List<SimpleMatrix> gradW, List<SimpleMatrix> gradB, SimpleMatrix X, SimpleMatrix Y) {
        int L = Activations.size() - 1;
        int m = X.numCols();
        SimpleMatrix A = Activations.get(L);//output
        SimpleMatrix dZ = A.minus(Y);
        A = Activations.get(L - 1);
        SimpleMatrix dW = dZ.mult(A.transpose()).divide(m);//dZ3*A2.T/m
        SimpleMatrix sum = new SimpleMatrix(dZ.numRows(), 1);
        CommonOps_DDRM.sumRows(dZ.getDDRM(), sum.getDDRM());
        SimpleMatrix dB = sum.divide(m);
        gradW.set(L, dW);
        gradB.set(L, dB);
        SimpleMatrix W = net.getLayers().get(L).getW();
        SimpleMatrix dA;
        for (int i = L - 1; i >= 0; i--) {
            dA = net.getLayers().get(i).getFunction().derivative(A);
            dZ = W.transpose().mult(dZ).elementMult(dA);
//            //------------------------
//            SimpleMatrix dz_aux = new SimpleMatrix(W.numCols(), dZ.numCols(), MatrixType.DDRM);
//            CommonOps_DDRM.multTransA(W.getDDRM(), dZ.getDDRM(), dz_aux.getDDRM());
//            dZ = dz_aux.elementMult(dA);
//            //dZ.printDimensions();
//            //dz_aux.printDimensions();
//            //dA.printDimensions();
//            //System.exit(0);
//            dZelementMult(dZ, dz_aux, dA);
//            //------------------------
            
            A = (i > 0) ? Activations.get(i - 1) : X;
            dW = dZ.mult(A.transpose()).divide(m);
            CommonOps_DDRM.sumRows(dZ.getDDRM(), sum.getDDRM());
            dB = sum.divide(m);
            gradW.set(i, dW);
            gradB.set(i, dB);
            W = net.getLayers().get(i).getW();
        }
    }

//    private void dZelementMult(SimpleMatrix dZ, SimpleMatrix dz_aux, SimpleMatrix dA) {
//        int cols = dZ.numCols();
//        IntStream.range(0, dZ.numRows()).parallel().forEach(i -> {
//            int idx = i * cols;
//            for (int j = 0; j < cols; j++) {
//                dZ.set(idx, dz_aux.get(idx) * dA.get(idx));
//                idx++;
//            }
//        });
//    }

}
