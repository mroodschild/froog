/*
 * The MIT License
 *
 * Copyright 2018 Matías Rodschild <mroodschild@gmail.com>.
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
package org.gitia.froog.trainingalgorithm.updaterule;

import java.util.List;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.Feedforward;
import org.gitia.froog.layer.Layer;
import org.gitia.froog.trainingalgorithm.accelerate.Accelerate;
import org.gitia.froog.trainingalgorithm.accelerate.AccelerateRule;

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
        for (int i = 0; i < net.getLayers().size(); i++) {
            Layer layer = net.getLayers().get(i);
            SimpleMatrix W = layer.getW();
            SimpleMatrix B = layer.getB();

            if (L2_Lambda > 0) {
                W = W.scale((1 - learningRate * L2_Lambda / m));//weight decay
            }
            
            W = W.minus(dW.get(i));
            B = B.minus(dB.get(i));
            
            layer.setW(W);
            layer.setB(B);
            net.getLayers().set(i, layer);
        }
    }

    @Override
    public void setMomentum(double momentum) {

    }

    @Override
    public void setAccelerate(Accelerate accelerate) {
        this.accelerate = accelerate;
    }

    public Accelerate getAccelerate() {
        return accelerate;
    }

}
