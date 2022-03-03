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
package org.gitia.froog.optimizer.updaterule;

import java.util.List;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.Feedforward;
import org.gitia.froog.layer.Dense;
import org.gitia.froog.optimizer.accelerate.Accelerate;
import org.gitia.froog.optimizer.accelerate.AccelerateRule;

/**
 *
 * @author Matías Rodschild <mroodschild@gmail.com>
 */
public class Update implements UpdateRule {

    Accelerate accelerate = AccelerateRule.nonAccelerate();

    public Update() {
    }

    /**
     *
     * @param gradW
     * @param gradB
     */
    @Override
    public void init(List<SimpleMatrix> gradW, List<SimpleMatrix> gradB) {
        
    }

    /**
     *
     * @param net neural network
     * @param m amount of data (it's used by L2)
     * @param L2_Lambda regularization parameter
     * @param learningRate
     * @param dW gradient W
     * @param dB gradient B
     */
    @Override
    public void updateParameters(Feedforward net, double m, double L2_Lambda, double learningRate, List<SimpleMatrix> dW, List<SimpleMatrix> dB) {
        //calculamos todos los deltas
        accelerate.grad(dW, dB, learningRate);
        for (int i = 0; i < net.layers().size(); i++) {
            Dense layer = net.layers().get(i);
            SimpleMatrix W = layer.getW();
            SimpleMatrix B = layer.getB();

            if (L2_Lambda > 0) {
                W = W.scale((1 - learningRate * L2_Lambda / m));//weight decay
            }
            
            W = W.minus(dW.get(i));
            B = B.minus(dB.get(i));
            
            layer.setW(W);
            layer.setB(B);
            net.layers().set(i, layer);
        }
    }

    @Override
    public void setAccelerate(Accelerate accelerate) {
        this.accelerate = accelerate;
    }

    public Accelerate getAccelerate() {
        return accelerate;
    }

}
