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
import org.apache.commons.lang3.ArrayUtils;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.Feedforward;
import org.gitia.froog.lossfunction.LossFunction;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class NumericGradient {

    /**
     *
     * @param net
     * @param gradW
     * @param gradB
     * @param loss
     * @param X
     * @param Y
     */
    public void compute(Feedforward net, List<SimpleMatrix> gradW, List<SimpleMatrix> gradB, LossFunction loss, SimpleMatrix X, SimpleMatrix Y) {
        double epsilon = 0.0001;
        SimpleMatrix w_orig = net.getParameters();
        SimpleMatrix J_plus = w_orig.copy();
        SimpleMatrix J_minus = w_orig.copy();
        SimpleMatrix gradApprox = w_orig.copy();
        J_plus.zero();
        J_minus.zero();
        gradApprox.zero();
        int numParameters = J_plus.getNumElements();
        for (int i = 0; i < numParameters; i++) {
            SimpleMatrix w_plus = w_orig.copy();
            w_plus.set(i, w_plus.get(i) + epsilon);
            net.setParameters(w_plus);
            SimpleMatrix A = net.output(X);
            double J = loss.costAll(A, Y);
            J_plus.set(i, J);

            SimpleMatrix w_minus = w_orig.copy();
            w_minus.set(i, w_minus.get(i) - epsilon);
            net.setParameters(w_minus);
            A = net.output(X);
            J = loss.costAll(A, Y);
            J_minus.set(i, J);
        }
        SimpleMatrix grad = J_plus.minus(J_minus).divide(2 * epsilon);
        //return grad;
        updateGrad(grad, gradW, gradB);
    }

    /**
     * @param grad
     * @param gradW
     * @param gradB
     */
    public void updateGrad(SimpleMatrix grad, List<SimpleMatrix> gradW, List<SimpleMatrix> gradB) {
        if (gradW.isEmpty() || gradB.isEmpty()) {
            System.err.println("error: gradients were not initialized");
        } else {
            int posicion = 0;
            int size;
            double[] datos = grad.getDDRM().getData();
            //cargamos los w
            for (int i = 0; i < gradW.size(); i++) {
                size = gradW.get(i).getNumElements();
                gradW.get(i).getDDRM().setData(
                        ArrayUtils.subarray(datos, posicion, posicion + size));
                posicion += size;
            }
            //cargamos los b
            for (int i = 0; i < gradB.size(); i++) {
                size = gradB.get(i).getNumElements();
                gradB.get(i).getDDRM().setData(
                        ArrayUtils.subarray(datos, posicion, posicion + size));
                posicion += size;
            }
        }
    }
}
