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
import org.gitia.froog.statistics.Clock;

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
        SimpleMatrix A = Activations.get(L);//ouputs
        SimpleMatrix dZ = A.minus(Y);//
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
            dA = net.getLayers().get(i).getFunction().derivative(A);//0.625 seg
            //SimpleMatrix dZ1 = W.transpose().mult(dZ).elementMult(dA);
            dZ = W.transpose().mult(dZ).elementMult(dA);

            A = (i > 0) ? Activations.get(i - 1) : X;
            //dZ.mult(At);// 300x50000 50000x784
            Clock c = new Clock();
            System.out.println("Inicia el calculo de dZ*At");
            dZ.printDimensions();
            A.printDimensions();
            c.start();
            dW = dZ.mult(A.transpose()).divide(m);// -> 43.27 seg
            c.stop();
            c.printTime("Tiempo dZ*dAt");
            CommonOps_DDRM.sumRows(dZ.getDDRM(), sum.getDDRM());
            dB = sum.divide(m);
            gradW.set(i, dW);
            gradB.set(i, dB);
            W = net.getLayers().get(i).getW();
        }
    }

    private void tamano(List<SimpleMatrix> Activations) {
        double size = 0;
        for (int i = 0; i < Activations.size(); i++) {
            SimpleMatrix m = Activations.get(i);
            size += tamano(m);
        }
        System.out.println("activaciones Size: " + size + " g");
    }

    private double tamano(SimpleMatrix m) {
        double size = 0;
        return m.numCols() * m.numRows() * 8 / (1024 * 1024 * 1024);
    }

}
