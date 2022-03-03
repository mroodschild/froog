/*
 * Copyright 2022
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
import org.gitia.froog.NeuralNetwork;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class NoiseGradient implements Gradient {

    double noise = 0.05;

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
    public void compute(NeuralNetwork net, List<SimpleMatrix> Activations, List<SimpleMatrix> gradW, List<SimpleMatrix> gradB, SimpleMatrix X, SimpleMatrix Y) {
        int L = Activations.size() - 1;
        int m = X.numCols();
        SimpleMatrix A = Activations.get(L);
        SimpleMatrix dZ = A.minus(Y);
        A = Activations.get(L - 1);
        SimpleMatrix dW = dZ.mult(A.transpose()).divide(m);
        SimpleMatrix sum = new SimpleMatrix(dZ.numRows(), 1);
        CommonOps_DDRM.sumRows(dZ.getDDRM(), sum.getDDRM());
        SimpleMatrix dB = sum.divide(m);
        gradW.set(L, dW);
        gradB.set(L, dB);
        SimpleMatrix W = net.layers().get(L).getW();
        SimpleMatrix g;
        for (int i = L - 1; i >= 0; i--) {
            g = net.layers().get(i).getFunction().derivative(A, noise);
            dZ = W.transpose().mult(dZ).elementMult(g);
            A = (i > 0) ? Activations.get(i - 1) : X;
            dW = dZ.mult(A.transpose()).divide(m);
            CommonOps_DDRM.sumRows(dZ.getDDRM(), sum.getDDRM());
            dB = sum.divide(m);
            gradW.set(i, dW);
            gradB.set(i, dB);
            W = net.layers().get(i).getW();
        }
    }

    public void setNoise(double noise) {
        this.noise = noise;
    }

    public double getNoise() {
        return noise;
    }

}
