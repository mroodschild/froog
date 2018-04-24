/*
 * The MIT License
 *
 * Copyright 2018 Matías Roodschild <mroodschild@gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.gitia.froog.trainingalgorithm.gradient;

import java.util.List;
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.Feedforward;

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
    public void compute(Feedforward net, List<SimpleMatrix> Activations, List<SimpleMatrix> gradW, List<SimpleMatrix> gradB, SimpleMatrix X, SimpleMatrix Y) {
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
        SimpleMatrix W = net.getLayers().get(L).getW();
        SimpleMatrix g;
        for (int i = L - 1; i >= 0; i--) {
            g = net.getLayers().get(i).getFunction().derivative(A, noise);
            dZ = W.transpose().mult(dZ).elementMult(g);
            A = (i > 0) ? Activations.get(i - 1) : X;
            dW = dZ.mult(A.transpose()).divide(m);
            CommonOps_DDRM.sumRows(dZ.getDDRM(), sum.getDDRM());
            dB = sum.divide(m);
            gradW.set(i, dW);
            gradB.set(i, dB);
            W = net.getLayers().get(i).getW();
        }
    }

    public void setNoise(double noise) {
        this.noise = noise;
    }

    public double getNoise() {
        return noise;
    }

}
