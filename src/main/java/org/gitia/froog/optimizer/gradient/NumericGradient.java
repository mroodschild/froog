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
import org.apache.commons.lang3.ArrayUtils;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.Feedforward;
import org.gitia.froog.NeuralNetwork;
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
    public void compute(NeuralNetwork net, List<SimpleMatrix> gradW, List<SimpleMatrix> gradB, LossFunction loss, SimpleMatrix X, SimpleMatrix Y) {
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
